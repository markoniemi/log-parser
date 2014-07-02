package com.tieto.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import com.tieto.parser.converter.Converter;

/**
 * Field parses an individual field and sets the value to java class. ClassName
 * parameter provides the name of java class. This class is created by Field, if
 * it has not yet been created.
 * 
 * Attribute contains the name of the attribute that is used for getting the
 * name of the setter for the value. MethodName is the name of the setter
 * method, if the java class does not comply to java bean standard. Converter is
 * a name of the converter class, that should be used for converting the value,
 * for example when the value is Date.
 * 
 * An example of field tag in configuration:
 * 
 * <pre>
 * &lt;field attribute="time" offset="56" length="20" type="java.util.Date" converter="com.tieto.parser.DateConverter" /&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j
@Getter
@Setter
public class Field extends TextParser {
    /**
     * Attribute contains the name of the attribute that is used for getting the
     * name of the setter for the value.
     */
    @XmlAttribute
    private String attribute;
    /**
     * MethodName is the name of the setter method, if the java class does not
     * comply to java bean standard.
     */
    @XmlAttribute
    private String methodName;
    @XmlAttribute
    private int offset;
    @XmlAttribute
    private int length;
    @XmlAttribute
    private String start;
    @XmlAttribute
    private String end;
    /** Is value trimmed before trying to read it */
    @XmlAttribute
    private boolean trim;
    /** Type of the attribute where value is written. Can also use primitives. */
    @XmlAttribute
    private String type;
    /** Regexp to use for finding the string */
    @XmlAttribute
    private String searchRegExp;
    /**
     * Converter is a name of the converter class, that should be used for
     * converting the value, for example when the value is Date.
     */
    @XmlAttribute
    private String converter;

    @Override
    public void parse(ParserData parserData, String input, String className) throws ParseException {
        try {
            if (input == null) {
                return;
            }
            log.trace("attribute: {} input: {}", attribute, input);
            String valueString = parseValue(input);
            setValue(parserData, valueString, className);
        } catch (Exception e) {
            logAndThrowException(parserData.isFailOnError(), input, className, attribute, e);
        }
    }

    private void logAndThrowException(boolean failOnError, String input, String className,
            String attribute, Exception e) throws ParseException {
        String message = "Error parsing string: " + input + " to class: " + className + "." + attribute;
        log.error(message);
        if (failOnError) {
            throw new ParseException(message, e);
        }
    }

    protected void setValue(ParserData parserData, String valueString, String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException,
            SecurityException, IllegalArgumentException, NoSuchMethodException,
            InvocationTargetException {
        if (StringUtils.isBlank(valueString)) {
            log.debug("Parsed text is empty: class: {} field: {}", className, this);
            return;
        }
        log.debug("Setting value '{}' to {}.{}", valueString, className, attribute);
        // create accessor method name
        if (methodName == null) {
            createMethodName();
        }
        // create class
        if (parserData.getCurrentObject() == null) {
            parserData.setCurrentObject(Class.forName(className).newInstance());
        }
        // create value type
        Object value = null;
        Class<?> typeClass = null;
        if (type != null) {
            if (type.equals("int")) {
                value = getValue(valueString);
                typeClass = int.class;
            } else if (type.equals("long")) {
                value = getValue(valueString);
                typeClass = long.class;
            } else if (type.equals("boolean")) {
                value = getValue(valueString);
                typeClass = boolean.class;
            } else {
                typeClass = Class.forName(type);
                value = getValue(valueString);
            }
        } else {
            typeClass = Class.forName("java.lang.String");
            value = getValue(valueString);
        }
        // set value using method
        Method method = parserData.getCurrentObject().getClass().getMethod(methodName, typeClass);
        method.invoke(parserData.getCurrentObject(), value);
    }

    protected Object getValue(String valueString) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Object value = null;
        if (converter != null) {
            @SuppressWarnings("rawtypes")
            Converter converterClass = (Converter) Class.forName(converter).newInstance();
            value = converterClass.convert(valueString);
        } else {
            if (type != null) {
                if (type.equals("int")) {
                    value = Integer.parseInt(valueString);
                } else if (type.equals("long")) {
                    value = Long.parseLong(valueString);
                } else if (type.equals("boolean")) {
                    value = Boolean.parseBoolean(valueString);
                } else {
                    Class<?> typeClass = Class.forName(type);
                    Constructor<? extends Object> constructor = typeClass
                            .getConstructor(String.class);
                    value = constructor.newInstance(valueString);
                }
            } else {
                value = valueString;
            }
        }
        return value;
    }

    public String parseValue(String input) {
        // TODO instead of overloading getValueString, rename each method
        // TODO rearrange else if statements to achieve full coverage
        String value = null;
        if (searchRegExp != null) {
            Pattern pattern = Pattern.compile(searchRegExp);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                value = matcher.group();
            }
        } else if (start == null && end == null) {
            if (offset < input.length()) {
                // if length is 0, return to the end of the input string
                if (length == 0 || offset + length > input.length()) {
                    value = input.substring(offset);
                } else {
                    value = input.substring(offset, offset + length);
                }
            }
        } else if (start != null && end == null) {
            value = getValueString(input, start, offset, length);
        } else if (start == null && end != null) {
            value = getValueString(input, end);
        } else if (start != null && end != null) {
            value = getValueString(input, start, end);
        }
        if (trim) {
            value = StringUtils.trim(value);
        }
        return value;
    }

    protected String getValueString(String input, String startString, int startOffset,
            int fieldLength) {
        String value = null;
        int startIndex = input.indexOf(startString);
        if (startIndex != -1) {
            if (fieldLength == 0
                    || startIndex + startString.length() + startOffset + fieldLength >= input
                            .length()) {
                value = input.substring(startIndex + startString.length() + startOffset);
            } else {
                value = input.substring(startIndex + startString.length() + startOffset, startIndex
                        + startString.length() + startOffset + fieldLength);
            }
        }
        return value;
    }

    private String getValueString(String input, String endString) {
        String value = null;
        int endIndex = input.indexOf(endString);
        if (endIndex != -1) {
            value = input.substring(0, endIndex);
        } else {
            value = input;
        }
        return value;
    }

    private String getValueString(String input, String startString, String endString) {
        String value = null;
        int startIndex = input.indexOf(startString);
        if (startIndex != -1) {
            int endIndex = input.indexOf(endString, startIndex + startString.length());
            if (endIndex != -1) {
                value = input.substring(startIndex + startString.length(), endIndex);
            }
        }
        return value;
    }

    protected void createMethodName() {
        // TODO use some bean util
        if (!StringUtils.isBlank(attribute)) {
            String setterMethodName = "set" + attribute.substring(0, 1).toUpperCase()
                    + attribute.substring(1);
            setMethodName(setterMethodName);
        }
    }
}

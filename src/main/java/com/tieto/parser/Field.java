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
import lombok.ToString;
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
@ToString(of = { "attribute" })
public class Field extends TextParser {
    private static final String BOOLEAN = "boolean";
    private static final String LONG = "long";
    private static final String INTEGER = "int";
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
    private com.tieto.parser.Converter converter;

    @Override
    protected void parse(ParserData parserData, String input, String className) throws ParseException {
        try {
            if (input == null) {
                return;
            }
            String valueString = parseValue(input);
            setValue(parserData, valueString, className);
        } catch (Exception e) {
            logAndThrowException(parserData.isFailOnError(), input, className, attribute, e);
        }
    }

    public String parseValue(String input) {
        log.trace("attribute: {} input: {}", attribute, input);
        String value = null;
        if (searchRegExp != null) {
            value = parseUsingRegExp(input);
        } else if (end == null) {
            if (start == null) {
                // end == null && start == null
                value = parseUsingOffsetAndLength(input);
            } else {
                // end == null && start != null
                value = parseUsingStartOffsetAndLength(input, start, offset, length);
            }
        } else {
            if (start == null) {
                // end != null && start == null
                value = parseUsingEnd(input, end);
            } else {
                // end != null && start != null
                value = parseUsingStartAndEnd(input, start, end);
            }
        }
        value = trim(value);
        return value;
    }

    /**
     * Sets value to object. Creates value object if necessary. Creates and
     * object of proper type. Creates a converter if necessary and sets value to
     * value object.
     */
    protected void setValue(ParserData parserData, String valueString, String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException,
            SecurityException, IllegalArgumentException, NoSuchMethodException,
            InvocationTargetException {
        if (StringUtils.isBlank(valueString)) {
            log.warn("Parsed text is empty: class: {} field: {}", className, this);
            return;
        }
        createClass(parserData, className);
        Class<?> typeClass = createValueType();
        Object value = convertValueToObject(valueString);
        log.debug("{}: Setting value '{}' to {}.{} (of type {})", this, valueString, className, attribute, type);
        setValueToAttribute(parserData.getCurrentObject(), typeClass, value);
    }
    
    /**
     * Log error message and throw an exception if failOnError is true.
     */
    private void logAndThrowException(boolean failOnError, String input, String className,
            String attribute, Exception e) throws ParseException {
        String message = "Error parsing string: " + input + " to class: " + className + "." + attribute;
        log.error(message);
        if (failOnError) {
            throw new ParseException(message, e);
        }
    }

    /**
     * Sets value to attribute either by setter or custom method name if provided.
     */
    private void setValueToAttribute(Object currentObject, Class<?> typeClass, Object value) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        createAccessorMethodName();
        log.trace("Using setter: {}", methodName);
        Method method = currentObject.getClass().getMethod(methodName, typeClass);
        method.invoke(currentObject, value);
    }

    /**
     * Creates value object.
     */
    private Class<?> createValueType() throws ClassNotFoundException {
        if (type != null) {
            if (INTEGER.equals(type)) {
                return int.class;
            } else if (LONG.equals(type)) {
                return long.class;
            } else if (BOOLEAN.equals(type)) {
                return boolean.class;
            } else {
                return Class.forName(type);
            }
        }
        return String.class;
    }

    /**
     * Creates class if it is not yet created.
     */
    private void createClass(ParserData parserData, String className) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        if (parserData.getCurrentObject() == null) {
            parserData.setCurrentObject(Class.forName(className).newInstance());
        }
    }

    /**
     * Gets value from string, and converts it using converter if necessary.
     */
    protected Object convertValueToObject(String valueString) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SecurityException,
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        if (converter != null) {
            Converter<?> converterClass = createConverter();
            log.debug("Converting '{}' with converter {}", valueString, converter);
            return converterClass.convert(valueString);
        } else {
            return getValueFromString(valueString);
        }
    }

    /**
     * Gets value from string.
     */
    private Object getValueFromString(String valueString) throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        if (type != null) {
            if (type.equals(INTEGER)) {
                return Integer.parseInt(valueString);
            } else if (type.equals(LONG)) {
                return Long.parseLong(valueString);
            } else if (type.equals(BOOLEAN)) {
                return Boolean.parseBoolean(valueString);
            } else {
                return createCustomValueObject(valueString);
            }
        }
        return valueString;
    }

    private Object createCustomValueObject(String valueString) throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        Object value;
        Class<?> typeClass = Class.forName(type);
        Constructor<? extends Object> constructor = typeClass
                .getConstructor(String.class);
        value = constructor.newInstance(valueString);
        return value;
    }

    private Converter<?> createConverter() throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        Converter<?> converterClass = null;
        if (converter.getParameter() != null) {
            Constructor<? extends Object> constructor = Class.forName(converter.getClassName()).getConstructor(
                    String.class);
            converterClass = (Converter<?>) constructor.newInstance(converter.getParameter());
        } else {
            converterClass = (Converter<?>) Class.forName(converter.getClassName()).newInstance();
        }
        return converterClass;
    }

    private String parseUsingRegExp(String input) {
        log.debug("{} parse using regexp '{}'", this, searchRegExp);
        Pattern pattern = Pattern.compile(searchRegExp);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
    
    private String parseUsingOffsetAndLength(String input) {
        log.debug("{} parse using offset {} and length {}", this, offset, length);
        String value = null;
        if (offset < input.length()) {
            // if length is 0, return to the end of the input string
            if (length == 0 || offset + length > input.length()) {
                value = input.substring(offset);
            } else {
                value = input.substring(offset, offset + length);
            }
        }
        return value;
    }

    protected String parseUsingStartOffsetAndLength(String input, String startString, int startOffset,
            int fieldLength) {
        log.debug("{} parse using start '{}', offset {} and length {}", this, start, offset, length);
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

    private String parseUsingEnd(String input, String endString) {
        log.debug("{} parse using end '{}'", this, end);
        String value = null;
        int endIndex = input.indexOf(endString);
        if (endIndex != -1) {
            value = input.substring(0, endIndex);
        } else {
            value = input;
        }
        return value;
    }

    private String parseUsingStartAndEnd(String input, String startString, String endString) {
        log.debug("{} parse using start '{}' and end '{}'", this, start, end);
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

    private String trim(String value) {
        if (trim) {
            return StringUtils.trim(value);
        }
        return value;
    }

    protected void createAccessorMethodName() {
        if (methodName == null && !StringUtils.isBlank(attribute)) {
            String setterMethodName = "set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
            setMethodName(setterMethodName);
        }
    }
}

package com.tieto.parser.converter;

/**
 * Interface for converters. In some cases parser has to convert string to
 * classes. Parsers.xml may contain a converter attribute in field tag. In such
 * case Field class uses the converter to convert string to a class.
 * 
 * Following definition uses DateConverter to create Date class:
 * 
 * <pre>
 * &lt;field attribute="time" offset="56" length="20" type="java.util.Date" converter="com.tieto.parser.DateConverter" /&gt;
 * </pre>
 */
public interface Converter<T> {
    /**
     * Convert input to an object.
     */
    T convert(String input);
}

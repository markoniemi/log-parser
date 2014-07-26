package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Since parsers and textParsers form a hierarchy and they delegate parsing to
 * children, they need to pass data up and down the hierarchy. ParserData
 * collects this data.
 */
@Getter
@Setter
@RequiredArgsConstructor
@SuppressWarnings("PMD.UnusedPrivateField")
public class ParserData {
    /** Current java class that is being filled from input */
    private Object currentObject;
    /** The list of java classes that has already been filled from input */
    private List<Object> objects = new ArrayList<Object>();
    /** text to parse */
    private boolean failOnError;
    private String lineBreak;
}

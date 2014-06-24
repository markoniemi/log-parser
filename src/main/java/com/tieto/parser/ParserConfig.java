package com.tieto.parser;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Top level class for parser configuration. XStream cannot return a list of
 * objects, but requires a class for the top level tag.
 */
@Getter
@Setter
public class ParserConfig {
    private List<Parser> parsers;
}

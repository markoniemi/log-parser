package com.tieto.parser;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

/**
 * Top level class for parser configuration. XStream cannot return a list of
 * objects, but requires a class for the top level tag.
 */
@XmlRootElement(name="parsers")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ParserConfig {
    @XmlElement(name="parser")
    private List<Parser> parsers;
}

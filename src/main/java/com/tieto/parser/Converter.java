package com.tieto.parser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@ToString(of = { "className", "parameter" })
@SuppressWarnings("PMD.UnusedPrivateField")
public class Converter {
    @XmlAttribute
    private String className;
    @XmlAttribute
    private String parameter;
}

package com.tieto.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import lombok.Getter;
import lombok.Setter;

/**
 * Read parser configuration from XML and store parsers in parsers list. Use
 * getParser() to get a parser.
 */
@Getter
@Setter
public class ParserManager {
    private List<Parser> parsers;

    public ParserManager() {
        parsers = new ArrayList<Parser>();
    }

    public ParserManager(InputStream configInputSream) throws JAXBException {
        parsers = readParsers(configInputSream);
    }

    public ParserManager(String configFilename) throws FileNotFoundException, JAXBException {
        parsers = readParsers(new FileInputStream(configFilename));
    }

    private List<Parser> readParsers(InputStream configInputSream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ParserConfig.class);
        
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ParserConfig parserConfig = (ParserConfig) jaxbUnmarshaller.unmarshal(configInputSream);
        return parserConfig.getParsers();
    }
    
    public Parser getParser(String id) {
        if (id == null) {
            return null;
        }
        for (Parser parser : parsers) {
            if (id.equals(parser.getId())) {
                return parser;
            }
        }
        return null;
    }
}

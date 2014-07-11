package com.tieto.parser.git;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.Parser;
import com.tieto.parser.ParserManager;

public class GitLogTest {
    @Test
    public void parseGitLog() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/git-log-parsers.xml");
        Parser parser = parserManager.getParser("git");
        @SuppressWarnings("unchecked")
        List<GitCommit> list = (List<GitCommit>) parser.parse(FileUtils.readFileToString(new File("src/test/resources/git/git.log")));
        Assert.assertEquals(3, list.size());
        GitCommit gitCommit = list.get(0);
        Assert.assertEquals("ca82a6dff817ec66f44342007202690a93763949", gitCommit.getSha1());
        Assert.assertEquals("Scott Chacon", gitCommit.getAuthorName());
        Assert.assertEquals("schacon@gee-mail.com", gitCommit.getAuthorEmail());
        Assert.assertNotNull(gitCommit.getTime());
        Assert.assertEquals("changed the version number", gitCommit.getTitle());
    }
    @Test
    public void parseGitLogWithRegExp() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/git-log-parsers.xml");
        Parser parser = parserManager.getParser("git-regexp");
        @SuppressWarnings("unchecked")
        List<GitCommit> list = (List<GitCommit>) parser.parse(FileUtils.readFileToString(new File("src/test/resources/git/git.log")));
        Assert.assertEquals(3, list.size());
        GitCommit gitCommit = list.get(0);
        Assert.assertEquals("ca82a6dff817ec66f44342007202690a93763949", gitCommit.getSha1());
        Assert.assertEquals("Scott Chacon", gitCommit.getAuthorName());
        Assert.assertEquals("schacon@gee-mail.com", gitCommit.getAuthorEmail());
        Assert.assertNotNull(gitCommit.getTime());
        Assert.assertEquals("changed the version number", gitCommit.getTitle());
    }
    @Test
    public void parseGitLogWithErrors() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/git-log-parsers.xml");
        Parser parser = parserManager.getParser("git-errors");
        @SuppressWarnings("unchecked")
        List<GitCommit> list = (List<GitCommit>) parser.parse(FileUtils.readFileToString(new File("src/test/resources/git/git.log")));
        Assert.assertEquals(3, list.size());
        GitCommit gitCommit = list.get(0);
        Assert.assertNull(gitCommit.getSha1());
        Assert.assertNull(gitCommit.getAuthorName());
        Assert.assertNull(gitCommit.getAuthorEmail());
        Assert.assertNotNull(gitCommit.getTime());
        Assert.assertNull(gitCommit.getTitle());
    }
}

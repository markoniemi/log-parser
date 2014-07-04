package com.tieto.parser.git;

import java.util.Date;

import lombok.Data;

@Data
public class GitCommit {
//    private Author author;
    private String authorName;
    private String authorEmail;
    private String sha1;
    private Date time;
    private String title;
    private String message;
}

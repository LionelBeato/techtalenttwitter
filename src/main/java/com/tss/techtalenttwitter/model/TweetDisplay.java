package com.tss.techtalenttwitter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetDisplay {

    private User user;
    private String message;
    private String date;
    private List<Tag> tags;

}

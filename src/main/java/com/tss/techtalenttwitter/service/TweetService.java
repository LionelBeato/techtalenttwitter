package com.tss.techtalenttwitter.service;

import com.tss.techtalenttwitter.model.Tweet;
import com.tss.techtalenttwitter.model.User;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();
    List<Tweet> findAllByUser(User user);
    List<Tweet> findAllByUsers(List<User> users);
    List<Tweet> findAllWithTags(String tag);
    void save(Tweet tweet);
    void handleTags(Tweet tweet);
    List<Tweet> formatTweets(List<Tweet> tweets);
    void addTagLinks (List<Tweet> tweets);
    void shortLinks(List<Tweet> tweets);

}

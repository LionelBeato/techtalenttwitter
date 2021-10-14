package com.tss.techtalenttwitter.service;

import com.tss.techtalenttwitter.model.Tweet;
import com.tss.techtalenttwitter.model.TweetDisplay;
import com.tss.techtalenttwitter.model.User;

import java.util.List;

public interface TweetService {

    List<TweetDisplay> findAll();
    List<TweetDisplay> findAllByUser(User user);
    List<TweetDisplay> findAllByUsers(List<User> users);
    List<TweetDisplay> findAllWithTags(String tag);
    void save(Tweet tweet);
    void handleTags(Tweet tweet);
    List<TweetDisplay> formatTweets(List<Tweet> tweets);
    void addTagLinks (List<Tweet> tweets);
    void shortLinks(List<Tweet> tweets);

    List<TweetDisplay> formatTimestamps(List<Tweet> tweets);

}

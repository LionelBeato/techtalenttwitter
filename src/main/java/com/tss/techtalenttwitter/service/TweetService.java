package com.tss.techtalenttwitter.service;

import com.tss.techtalenttwitter.model.Tweet;
import com.tss.techtalenttwitter.model.User;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();
    List<Tweet> findAllByUser(User user);
    List<Tweet> findAllByUsers(List<User> users);
    void save(Tweet tweet);

}

package com.tss.techtalenttwitter.controller;

import com.tss.techtalenttwitter.model.Tweet;
import com.tss.techtalenttwitter.model.User;
import com.tss.techtalenttwitter.service.TweetService;
import com.tss.techtalenttwitter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private TweetService tweetService;

    public UserController(UserService userService, TweetService tweetService) {
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @GetMapping("/users/{username}")
    public String getUser(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        List<Tweet> tweets = tweetService.findAllByUser(user);

        User loggedInUser = userService.getLoggedInUser();
        List<User> following = loggedInUser.getFollowing();
        boolean isFollowing = false;

        for (User followedUser : following) {
            if (followedUser.getUsername().equals(username)) {
                isFollowing = true;
            }
        }

        boolean isSelfPage = loggedInUser.getUsername().equals(username);

        model.addAttribute("isSelfPage", isSelfPage);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("user", user);
        model.addAttribute("following", isFollowing);
        return "user";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        setTweetCounts(users, model);

        User loggedInUser = userService.getLoggedInUser();
        List<User> usersFollowing = loggedInUser.getFollowing();
        setFollowingStatus(users, usersFollowing, model);

        return "users";
    }

    private void setFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
        HashMap<String, Boolean> followingStatus = new HashMap<>();
        String username = userService.getLoggedInUser().getUsername();

        for (User user: users) {
            if(usersFollowing.contains(user)) {
                followingStatus.put(user.getUsername(), true);
            } else if (!user.getUsername().equals(username)) {
                followingStatus.put(user.getUsername(), false);
            }
        }
        model.addAttribute("followingStatus", followingStatus);
    }

    private void setTweetCounts(List<User> users, Model model) {
        HashMap<String, Integer> tweetCounts = new HashMap<>();
        for (User user : users) {
            List<Tweet> tweets = tweetService.findAllByUser(user);
            tweetCounts.put(user.getUsername(), tweets.size());
        }
        model.addAttribute("tweetCounts", tweetCounts);
    }

}

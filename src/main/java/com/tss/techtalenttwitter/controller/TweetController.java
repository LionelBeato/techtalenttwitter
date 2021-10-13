package com.tss.techtalenttwitter.controller;

import com.tss.techtalenttwitter.model.Tweet;
import com.tss.techtalenttwitter.model.User;
import com.tss.techtalenttwitter.service.TweetService;
import com.tss.techtalenttwitter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TweetController {

    private UserService userService;
    private TweetService tweetService;

    public TweetController(UserService userService, TweetService tweetService) {
        this.userService = userService;
        this.tweetService = tweetService;
    }

    @GetMapping(value = {"/tweets", "/"})
    public String getFeed(Model model) {
        List<Tweet> tweetList = tweetService.findAll();
        model.addAttribute("tweetList", tweetList);
        return "feed";
     }

     @GetMapping("/tweets/new")
    public String getTweetForm(Model model) {
        model.addAttribute("tweet", new Tweet());
        return "new-tweet";
     }

     @PostMapping("/tweets")
    public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model){
        User user = userService.getLoggedInUser();
        if(!bindingResult.hasErrors()) {
            tweet.setUser(user);
            tweetService.save(tweet);
            model.addAttribute("successMessage", "Tweet successfully created!");
            model.addAttribute("tweet", new Tweet());
        }
        return "new-tweet";
     }

    @GetMapping(value = "/tweets/{tag}")
    public String getTweetsByTag(@PathVariable String tag, Model model) {
        List<Tweet> tweets = tweetService.findAllWithTags(tag);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("tag", tag);
        return "tagged-tweets";
    }


}

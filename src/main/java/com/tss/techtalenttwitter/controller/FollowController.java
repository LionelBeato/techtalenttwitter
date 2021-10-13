package com.tss.techtalenttwitter.controller;

import com.tss.techtalenttwitter.model.User;
import com.tss.techtalenttwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FollowController {

    @Autowired
    private UserService userService;

    @PostMapping("/follow/{username}")
    public String follow(@PathVariable String username,
                         HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToFollow = userService.findByUsername(username);

        List<User> followers = userToFollow.getFollowers();
        followers.add(loggedInUser);
        userToFollow.setFollowers(followers);

        userService.save(userToFollow);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/unfollow/{username}")
    public String unfollow(@PathVariable String username,
                         HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToUnfollow = userService.findByUsername(username);

        List<User> followers = userToUnfollow.getFollowers();
        followers.remove(loggedInUser);
        userToUnfollow.setFollowers(followers);

        userService.save(userToUnfollow);
        return "redirect:" + request.getHeader("Referer");
    }

}

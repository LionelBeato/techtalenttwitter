package com.tss.techtalenttwitter.service;

import com.tss.techtalenttwitter.model.Tag;
import com.tss.techtalenttwitter.model.Tweet;
import com.tss.techtalenttwitter.model.TweetDisplay;
import com.tss.techtalenttwitter.model.User;
import com.tss.techtalenttwitter.repository.TagRepository;
import com.tss.techtalenttwitter.repository.TweetRepository;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<TweetDisplay> findAll() {
        return formatTweets(tweetRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    public List<TweetDisplay> findAllByUser(User user) {
        return formatTweets(tweetRepository.findAllByUserOrderByCreatedAtDesc(user));
    }

    @Override
    public List<TweetDisplay> findAllByUsers(List<User> users) {
        return formatTweets(tweetRepository.findAllByUserInOrderByCreatedAtDesc(users));
    }

    @Override
    public List<TweetDisplay> findAllWithTags(String tag) {
        List<Tweet> tweets = tweetRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
        return formatTweets(tweets);
    }

    @Override
    public void save(Tweet tweet) {
        handleTags(tweet);
        tweetRepository.save(tweet);
    }

    @Override
    public void handleTags(Tweet tweet) {

        List<Tag> tags = new ArrayList<Tag>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(tweet.getMessage());

        while (matcher.find()) {
            String phrase = matcher.group().substring(1).toLowerCase();
            Tag tag = tagRepository.findByPhrase(phrase);
            if (tag == null) {
                tag = new Tag();
                tag.setPhrase(phrase);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        tweet.setTags(tags);

    }

    @Override
    public List<TweetDisplay> formatTweets(List<Tweet> tweets) {
        addTagLinks(tweets);
        shortLinks(tweets);
        List<TweetDisplay> displayTweets = formatTimestamps(tweets);
        return displayTweets;
    }

    @Override
    public void addTagLinks(List<Tweet> tweets) {
        Pattern pattern = Pattern.compile("#\\w+");
        for (Tweet tweet : tweets) {
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            Set<String> tags = new HashSet<String>();
            while (matcher.find()) {
                tags.add(matcher.group());
            }
            for (String tag : tags) {
                message = message.replaceAll(tag,
                        "<a class=\"tag\" href=\"/tweets/" + tag.substring(1).toLowerCase() + "\">" + tag + "</a>");
            }
            tweet.setMessage(message);
        }
    }

    @Override
    public void shortLinks(List<Tweet> tweets) {
        Pattern pattern = Pattern.compile("https?[^ ]+");
        for (Tweet tweet : tweets) {
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String link = matcher.group();
                String shortenedLink = link;
                if (link.length() > 23) {
                    shortenedLink = link.substring(0, 20) + "...";
                    message = message.replace(link,
                            "<a class=\"tag\" href=\"" + link + "\" target=\"_blank\">" + shortenedLink + "</a>");
                }
                tweet.setMessage(message);
            }

        }
    }

    @Override
    public List<TweetDisplay> formatTimestamps(List<Tweet> tweets) {
        List<TweetDisplay> response = new ArrayList<>();
        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
        Date now = new Date();
        for (Tweet tweet : tweets) {
            TweetDisplay tweetDisplay = new TweetDisplay();
            tweetDisplay.setUser(tweet.getUser());
            tweetDisplay.setMessage(tweet.getMessage());
            tweetDisplay.setTags(tweet.getTags());
            long diffInMillies = Math.abs(now.getTime() - tweet.getCreatedAt().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            // the number 3 below is representative of the amount of days
            // so if any given tweet is older than 3 days, it'll fallback to
            // a traditional format
            if (diff > 3) {
                tweetDisplay.setDate(simpleDate.format(tweet.getCreatedAt()));
            } else {
                tweetDisplay.setDate(prettyTime.format(tweet.getCreatedAt()));
            }
            response.add(tweetDisplay);
        }
        return response;
    }
}

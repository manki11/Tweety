package com.zcorp.app.tweety.Data.twitter_feed;

import com.zcorp.app.tweety.Data.user_profile.Profile_Data;

/**
 * Created by Mankirat on 27-03-2017.
 */

public class Tweet {

    private String full_text,created_at;
    private long id;
    private int retweet_count, favorite_count;
    private Tweet retweeted_status;
    private Profile_Data user;
    private Entities entities;
    private Boolean favorited,retweeted;


    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Tweet retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return full_text;
    }

    public int getRetweeet_count() {
        return retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public Profile_Data getUser() {
        return user;
    }

    public void setText(String text) {
        this.full_text = text;
    }

    public void setRetweeet_count(int retweeet_count) {
        this.retweet_count = retweeet_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public void setUser(Profile_Data user) {
        this.user = user;
    }
}

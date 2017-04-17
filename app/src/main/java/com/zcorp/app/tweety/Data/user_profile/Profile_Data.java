package com.zcorp.app.tweety.Data.user_profile;

/**
 * Created by Mankirat on 03-04-2017.
 */

public class Profile_Data {

    private String name,screen_name,location,description, profile_banner_url,profile_image_url;
    private Boolean verified;
    long id;
    private int followers_count,friends_count;

    public long getId() {
        return id;
    }

    public String getProfileName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public String getProfile_image_url() {
        profile_image_url=profile_image_url.replaceAll("_normal","");
        return profile_image_url;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}

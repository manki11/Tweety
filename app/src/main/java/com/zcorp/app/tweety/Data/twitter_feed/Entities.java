package com.zcorp.app.tweety.Data.twitter_feed;

import java.util.ArrayList;

/**
 * Created by Mankirat on 02-04-2017.
 */

public class Entities {

    ArrayList<Media> media;
    ArrayList<Hashtag> hashtags;
    ArrayList<UserMentions> user_mentions;
    ArrayList<URLS> urls;

    public ArrayList<URLS> getUrls() {
        return urls;
    }

    public ArrayList<Hashtag> getHashtags() {
        return hashtags;
    }

    public ArrayList<UserMentions> getUser_mentions() {
        return user_mentions;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    public class Media{

        private String media_url,url;
        ArrayList<Integer> indices;


        public String getUrl() {
            return url;
        }

        public ArrayList<Integer> getIndices() {
            return indices;
        }

        private String type;
        private String id;

        public String getMedia_url() {
            return media_url;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public class Hashtag{

        private String text;
        ArrayList<Integer> indices;

        public String getText() {
            return text;
        }

        public ArrayList<Integer> getIndices() {
            return indices;
        }
    }

    public class UserMentions{
        private String screen_name, name;
        ArrayList<Integer> indices;

        public String getScreen_name() {
            return screen_name;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Integer> getIndices() {
            return indices;
        }
    }

    public class URLS{

        private String expanded_url,display_url;
        ArrayList<Integer> indices;

        public String getExpanded_url() {
            return expanded_url;
        }

        public String getDisplay_url() {
            return display_url;
        }

        public ArrayList<Integer> getIndices() {
            return indices;
        }
    }

}

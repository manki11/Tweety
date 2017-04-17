package com.zcorp.app.tweety.Network;

import com.zcorp.app.tweety.Data.search_feed.Search_list;
import com.zcorp.app.tweety.Data.twitter_feed.Tweet;
import com.zcorp.app.tweety.Data.user_profile.Profile_Data;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by manishakhattar on 19/03/17.
 */


public interface ApiInterface {

    @GET("statuses/home_timeline.json")
    @Headers("Content-Type: application/json")
    Call<ArrayList<Tweet>> getTweets(@Header("Authorization") String contentRange, @QueryMap HashMap<String,String> map);

    @GET("account/verify_credentials.json")
    @Headers("Content-Type: application/json")
    Call<Profile_Data> getProfile(@Header("Authorization") String contentRange);

    @GET("users/show.json")
    @Headers("Content-Type: application/json")
    Call<Profile_Data> getProfileforUser(@Header("Authorization") String contentRange, @QueryMap HashMap<String,String> map);


    @GET("statuses/user_timeline.json")
    @Headers("Content-Type: application/json")
    Call<ArrayList<Tweet>> getusertimeline(@Header("Authorization") String contentRange, @QueryMap HashMap<String,String> map);

    @GET("search/tweets.json")
    @Headers("Content-Type: application/json")
    Call<Search_list> getSearch(@Header("Authorization") String contentRange, @QueryMap HashMap<String,String> map);

    @POST("favorites/create.json")
    @Headers("Content-Type: application/json")
    Call<Tweet> postfavorite(@Header("Authorization") String contentRange, @Body HashMap<String,String> map);





//    @GET("posts/{id}/{comment_id}")
//    Call<Post> getPost(@Path("comment_id") int comment_id, @Path("id") int id);

//    @GET("comments")
//    Call<> getCommentsForPost(@Query("postId") int post_id, @Query("") String str);


//    @POST("")
//    Call<CoursesResponse> putCourse(@Body )


}

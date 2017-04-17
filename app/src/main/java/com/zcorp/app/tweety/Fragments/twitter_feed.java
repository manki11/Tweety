package com.zcorp.app.tweety.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zcorp.app.tweety.Activities.Profile;
import com.zcorp.app.tweety.Adapters.RVAdapter;
import com.zcorp.app.tweety.Data.twitter_feed.Tweet;
import com.zcorp.app.tweety.Data.user_profile.Profile_Data;
import com.zcorp.app.tweety.Network.ApiClient;
import com.zcorp.app.tweety.Network.ApiInterface;
import com.zcorp.app.tweety.Oauth.RequestActivity;
import com.zcorp.app.tweety.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mankirat on 29-03-2017.
 */

public class twitter_feed extends Fragment implements RVAdapter.getmoretweets,RVAdapter.favoritetweet  {

    HashMap<String,String> querymap;
    RVAdapter adapter;
    boolean first=true;
    boolean refresh=false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Dialog builder;
    LayoutInflater inflater;


//    private static final String ARG_SECTION_NUMBER = "section_number";
     ArrayList<Tweet> tweets;
     RecyclerView rv;
     TextView main_text;
     public twitter_feed(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twitter_feed, container, false);
        rv= (RecyclerView) rootView.findViewById(R.id.rv);

        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_UP)
                    hideQuickView();
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        main_text=(TextView) rootView.findViewById(R.id.main_text);
        mSwipeRefreshLayout=(SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        if(main_text!=null) {
            main_text.setText("HOME");
        }

        tweets=new ArrayList<>();

        querymap = new HashMap<>();
        querymap.put("count","100");
        querymap.put("tweet_mode","extended");

        try {
            getTweets();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return rootView;
    }


    public static twitter_feed newInstance(){
        twitter_feed fragment=new twitter_feed();
        return fragment;
    }

    public void getTweets() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {


        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/statuses/home_timeline.json","GET",querymap,null,getActivity());
        String head=oAuthClass.returnheader();

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ArrayList<Tweet>> call = apiInterface.getTweets(head,querymap);

        call.enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {

                if(response.isSuccessful()){
                    ArrayList<Tweet> twitter_feed=response.body();
                    int size=tweets.size();

                    if(first) {
                        tweets.clear();
                        first=false;
                        tweets.addAll(twitter_feed);

                        if (refresh){
                            refresh=false;
                            adapter.notifyDataSetChanged();
                        }else {
                            makeList();
                        }
                    }else {
                        tweets.addAll(twitter_feed);
                        adapter.notifyItemRangeInserted(size, tweets.size());
                    }



                    Log.e("tweet_size",tweets.size()+"");


                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e("retrofit",jObjError.getString("message"));
                    } catch (Exception e) {
                        Log.e("retrofit",e.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {
                Log.e("shitty","error6");
            }
        });
    }


    public void makeList(){

        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(tweets,getActivity());
        adapter.setgetmoretweetslistener(this);
        rv.setAdapter(adapter);
    }


    @Override
    public void getmoretweetstoshow(String max_id) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        querymap.put("max_id",max_id);
        getTweets();
    }



    void refreshItems() {

        Log.e("refresh","getting tweets");
        first=true;
        refresh=true;
        try {
            getTweets();
            Log.e("refresh","getting tweets done");
        }catch (Exception e) {
            e.printStackTrace();
        }

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        Log.e("refresh","getting tweets set");
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void favoritetweetwithid(long tweet_id) {

    }

    public void hideQuickView(){
        if(builder != null) builder.dismiss();
    }


    public void quickview(Tweet profile){
        inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.pop_up, null);

        ImageView background=(ImageView) view.findViewById(R.id.header_cover_image);
        ImageView dp=(ImageView) view.findViewById(R.id.user_profile_photo);

        TextView name=(TextView) view.findViewById(R.id.user_profile_name);
        TextView tag=(TextView) view.findViewById(R.id.user_profile_tag);

        TextView location=(TextView) view.findViewById(R.id.location);
        TextView freinds=(TextView) view.findViewById(R.id.freinds);
        TextView followers=(TextView) view.findViewById(R.id.followers);
        ImageView verified=(ImageView) view.findViewById(R.id.verified);
        TextView description=(TextView) view.findViewById(R.id.description);


        name.setText(profile.getUser().getProfileName());
        tag.setText("@"+profile.getUser().getScreen_name());
        location.setText(profile.getUser().getLocation());
        description.setText(profile.getUser().getDescription());

        if(profile.getUser().getVerified()){
            verified.setVisibility(View.VISIBLE);
        }


        freinds.setText(getnum(profile.getUser().getFriends_count())+" FOLLOWING");
        followers.setText(getnum(profile.getUser().getFollowers_count())+" FOLLOWERS");


        Picasso.with(getActivity()).load(profile.getUser().getProfile_banner_url()).into(background);
        Picasso.with(getActivity()).load(profile.getUser().getProfile_image_url()).into(dp);


        builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(view);
        builder.show();
    }

    @Override
    public void pop_up_user(String user) {
        getUserInfo(user);
    }


    public void  getUserInfo(final String user){
        HashMap<String,String> query_search=new HashMap<>();
        query_search.put("user_id",0+"");
        query_search.put("count","20");
        query_search.put("screen_name",user);

        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/users/show.json","GET",query_search,null,getActivity());
        String head=oAuthClass.returnheader();
        Log.e("getuserpop",head);

        Log.e("user_profile","getting  user profile 10");
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<Profile_Data> call = apiInterface.getProfileforUser(head,query_search);

        call.enqueue(new Callback<Profile_Data>() {
            @Override
            public void onResponse(Call<Profile_Data> call, Response<Profile_Data> response) {
                Log.e("user_profile",user);
                Profile_Data profile=response.body();

                if(profile==null){
                    Log.e("user_profile","null");
                }

                inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View view = inflater.inflate(R.layout.pop_up, null);

                ImageView background=(ImageView) view.findViewById(R.id.header_cover_image);
                ImageView dp=(ImageView) view.findViewById(R.id.user_profile_photo);

                TextView name=(TextView) view.findViewById(R.id.user_profile_name);
                TextView tag=(TextView) view.findViewById(R.id.user_profile_tag);

                TextView location=(TextView) view.findViewById(R.id.location);
                TextView freinds=(TextView) view.findViewById(R.id.freinds);
                TextView followers=(TextView) view.findViewById(R.id.followers);
                ImageView verified=(ImageView) view.findViewById(R.id.verified);
                TextView description=(TextView) view.findViewById(R.id.description);


                name.setText(profile.getProfileName());
                tag.setText("@"+profile.getScreen_name());
                location.setText(profile.getLocation());
                description.setText(profile.getDescription());

                if(profile.getVerified()){
                    verified.setVisibility(View.VISIBLE);
                }


                freinds.setText(getnum(profile.getFriends_count())+" FOLLOWING");
                followers.setText(getnum(profile.getFollowers_count())+" FOLLOWERS");


                Picasso.with(getActivity()).load(profile.getProfile_banner_url()).into(background);
                Picasso.with(getActivity()).load(profile.getProfile_image_url()).into(dp);


                builder = new Dialog(getActivity());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                builder.setContentView(view);
                builder.show();
            }

            @Override
            public void onFailure(Call<Profile_Data> call, Throwable t) {

            }
        });
    }

    public String getnum(int a){
        String b;
        float x;

        b=a+"";

        if(a>1000000){
            x=(float) a/1000000;
            String temp=x+"";
            if(a<10000000){
                temp=temp.substring(0,3);
            }
            else{
                temp=temp.substring(0,4);
            }
            b=temp+"M";
        }else if(a>1000){
            x=(float)a/1000;
            String temp=x+"";
            if(a<100000){
                if(a<10000){
                    temp=temp.substring(0,1);
                }else {
                    temp = temp.substring(0, 2);
                }
            }
            else{
                temp=temp.substring(0,3);
            }
            b=temp+"K";
        }

        return  b;
    }
}

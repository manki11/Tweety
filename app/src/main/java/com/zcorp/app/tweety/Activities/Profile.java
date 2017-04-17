package com.zcorp.app.tweety.Activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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

public class Profile extends AppCompatActivity implements RVAdapter.getmoretweets{

    Fragment fm;
    ImageView background,dp;
    TextView name,tag,location,freinds,followers;

    ArrayList<Tweet> tweets;
    HashMap<String,String> querymap;
    RecyclerView rv;

    RVAdapter adapter;
    boolean first=true;
    TextView main_text;
    TextView desciption;

    long id;
    String screen_name;
    ImageView verified;

    LayoutInflater inflater;
    Dialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            id = bundle.getLong("id", 0);
            screen_name=bundle.getString("userTag",null);
        }

        rv= (RecyclerView) findViewById(R.id.user_timeline);
        rv.setNestedScrollingEnabled(false);
        desciption=(TextView) findViewById(R.id.description);

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
        main_text=(TextView) findViewById(R.id.main_text);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        verified=(ImageView) findViewById(R.id.verified);

        background=(ImageView) findViewById(R.id.header_cover_image);
        dp=(ImageView) findViewById(R.id.user_profile_photo);

        name=(TextView) findViewById(R.id.user_profile_name);
        tag=(TextView) findViewById(R.id.user_profile_tag);

        location=(TextView) findViewById(R.id.location);
        freinds=(TextView) findViewById(R.id.freinds);
        followers=(TextView) findViewById(R.id.followers);



        tweets=new ArrayList<>();

        querymap = new HashMap<>();
        querymap.put("user_id",id+"");
        querymap.put("count","20");
        querymap.put("screen_name",screen_name);
        querymap.put("tweet_mode","extended");

        Log.e("user_id",id+"");
        Log.e("screen_name",screen_name);

        try {
            getTweets();
        } catch (Exception e) {
            e.printStackTrace();
        }


        getUserInfo();

    }


    public void getUserInfo(){
        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/users/show.json","GET",querymap,null,Profile.this);
        String head=oAuthClass.returnheader();

        Log.e("user_profile","getting  user profile 10");
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<Profile_Data> call = apiInterface.getProfileforUser(head,querymap);

        call.enqueue(new Callback<Profile_Data>() {
            @Override
            public void onResponse(Call<Profile_Data> call, Response<Profile_Data> response) {
                Log.e("user_profile","getting  user profile 20");
                Profile_Data profile=response.body();



                name.setText(profile.getProfileName());
                tag.setText("@"+profile.getScreen_name());
                location.setText(profile.getLocation());
                desciption.setText(profile.getDescription());

                freinds.setText(getnum(profile.getFriends_count())+" FOLLOWING");
                followers.setText(getnum(profile.getFollowers_count())+" FOLLOWERS");

                if(profile.getVerified()){
                    verified.setVisibility(View.VISIBLE);
                }


                Picasso.with(Profile.this).load(profile.getProfile_banner_url()).into(background);
                Picasso.with(Profile.this).load(profile.getProfile_image_url()).into(dp);



            }

            @Override
            public void onFailure(Call<Profile_Data> call, Throwable t) {

            }
        });


    }


    public void getTweets() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/statuses/user_timeline.json","GET",querymap,null,Profile.this);
        String head=oAuthClass.returnheader();

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ArrayList<Tweet>> call = apiInterface.getusertimeline(head,querymap);

        call.enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {


                if(response.isSuccessful()){
                    Log.e("profile_get","getting user tweets");

                    ArrayList<Tweet> twitter_feed=response.body();

                    int size=tweets.size();


                    if(first) {
                        tweets.clear();
                        first=false;
                        tweets.addAll(twitter_feed);
                        makeList();
                    }else {
                        tweets.addAll(twitter_feed);
                        adapter.notifyItemRangeInserted(size, tweets.size());
                    }



                    Log.e("tweet_size_profile",tweets.size()+"");


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

        LinearLayoutManager llm = new LinearLayoutManager(Profile.this);
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(tweets,Profile.this);
        rv.setAdapter(adapter);
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

    public void hideQuickView(){
        if(builder != null) builder.dismiss();
    }


    @Override
    public void getmoretweetstoshow(String max_id) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {

    }

    public void quickview(Tweet profile){
        inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.pop_up, null);

        ImageView background=(ImageView) view.findViewById(R.id.header_cover_image);
        ImageView dp=(ImageView) view.findViewById(R.id.user_profile_photo);

        TextView name=(TextView) view.findViewById(R.id.user_profile_name);
        TextView tag=(TextView) view.findViewById(R.id.user_profile_tag);

        TextView location=(TextView) view.findViewById(R.id.location);
        TextView freinds=(TextView) view.findViewById(R.id.freinds);
        TextView followers=(TextView) view.findViewById(R.id.followers);
        ImageView verified=(ImageView) view.findViewById(R.id.verified);


        name.setText(profile.getUser().getProfileName());
        tag.setText("@"+profile.getUser().getScreen_name());
        location.setText(profile.getUser().getLocation());

        if(profile.getUser().getVerified()){
            verified.setVisibility(View.VISIBLE);
        }


        freinds.setText(getnum(profile.getUser().getFriends_count())+" FOLLOWING");
        followers.setText(getnum(profile.getUser().getFollowers_count())+" FOLLOWERS");

        Log.e("background_image",profile.getUser().getProfile_banner_url());

        Picasso.with(this).load(profile.getUser().getProfile_banner_url()).into(background);
        Picasso.with(this).load(profile.getUser().getProfile_image_url()).into(dp);


        builder = new Dialog(this);
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

        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/users/show.json","GET",query_search,null,Profile.this);
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

                inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
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


                Picasso.with(Profile.this).load(profile.getProfile_banner_url()).into(background);
                Picasso.with(Profile.this).load(profile.getProfile_image_url()).into(dp);


                builder = new Dialog(Profile.this);
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
}


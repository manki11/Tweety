package com.zcorp.app.tweety.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zcorp.app.tweety.Adapters.RVAdapter;
import com.zcorp.app.tweety.Data.search_feed.Search_list;
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

public class Search extends AppCompatActivity implements RVAdapter.getmoretweets{

    ImageView back;
    HashMap<String,String> querymap;
    ArrayList<Tweet> tweets;
    Boolean first=true,refresh=false;
    RVAdapter adapter;
    RecyclerView rv;
    EditText search;
    String field;
    String hashtag;
    LayoutInflater inflater;
    Dialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            hashtag = bundle.getString("hashtag", null);
        }

        back=(ImageView) findViewById(R.id.back);

        search=(EditText) findViewById(R.id.searchbar);
        rv=(RecyclerView) findViewById(R.id.rv_search);

        tweets=new ArrayList<>();

        querymap = new HashMap<>();
        querymap.put("count","40");
        querymap.put("tweet_mode","extended");
        querymap.put("result_type","popular");

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


        if(hashtag!=null) {
            search.setText("#"+hashtag);
            field="#"+search.getText().toString();
            Log.e("search_string",field);
            hashtag="";
            tweets.clear();
            if(!first) {
                first=true;
                refresh = true;
            }
            querymap.put("q",field);
            try {
                getTweets();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && ((keyCode == KeyEvent.KEYCODE_ENTER))) {
                    // Perform action on key press
                    field=search.getText().toString();
                    Log.e("search_string",field);
                    tweets.clear();
                    if(!first) {
                        first=true;
                        refresh = true;
                    }
                    querymap.put("q",field);
                    try {
                        getTweets();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(Search.this,Twitter.class);
                startActivity(i);
            }
        });


    }



    public void getTweets() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {


        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/search/tweets.json","GET",querymap,null,this);
        String head=oAuthClass.returnheader();

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<Search_list> call = apiInterface.getSearch(head,querymap);

        call.enqueue(new Callback<Search_list>() {
            @Override
            public void onResponse(Call<Search_list> call, Response<Search_list> response) {

                if(response.isSuccessful()){
                    Search_list search=response.body();
                    ArrayList<Tweet> twitter_feed= search.getStatuses();
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
            public void onFailure(Call<Search_list> call, Throwable t) {
                Log.e("shitty","error6");
            }
        });
    }


    public void makeList(){



        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(tweets,this);
        adapter.setgetmoretweetslistener(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void getmoretweetstoshow(String max_id) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {

    }

    public void hideQuickView(){
        if(builder != null) builder.dismiss();
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

    @Override
    public void pop_up_user(String user) {
        getUserInfo(user);
    }

    public void  getUserInfo(final String user){
        HashMap<String,String> query_search=new HashMap<>();
        query_search.put("user_id",0+"");
        query_search.put("count","20");
        query_search.put("screen_name",user);

        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/users/show.json","GET",query_search,null,Search.this);
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


                Picasso.with(Search.this).load(profile.getProfile_banner_url()).into(background);
                Picasso.with(Search.this).load(profile.getProfile_image_url()).into(dp);


                builder = new Dialog(Search.this);
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

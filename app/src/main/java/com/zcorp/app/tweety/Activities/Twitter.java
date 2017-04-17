package com.zcorp.app.tweety.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zcorp.app.tweety.Adapters.PageAdapter;
import com.zcorp.app.tweety.Data.user_profile.Profile_Data;
import com.zcorp.app.tweety.Network.ApiClient;
import com.zcorp.app.tweety.Network.ApiInterface;
import com.zcorp.app.tweety.Oauth.RequestActivity;
import com.zcorp.app.tweety.R;
import com.zcorp.app.tweety.Data.twitter_feed.Tweet;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Twitter extends AppCompatActivity {

    ArrayList<Tweet> tweets;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PageAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TextView main_text;
    ImageView search;
    String[] title;
    ImageView profile_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        main_text=(TextView) findViewById(R.id.main_text);

        search=(ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(Twitter.this,Search.class);
                startActivity(i);
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        title=new String[2];
        title[0]="Home";
        title[1]="Profile";

        mSectionsPagerAdapter = new PageAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        profile_pic=(ImageView) findViewById(R.id.profile_pic);
        final TabLayout mTabLayout=(TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.timeline);
        mTabLayout.getTabAt(1).setIcon(R.drawable.no_profile);
        getUserInfo();

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                main_text.setText(title[position]);
                if(position==0) {
                    mTabLayout.getTabAt(position).setIcon(R.drawable.timeline);
                    mTabLayout.getTabAt(1).setIcon(R.drawable.no_profile);
                }else{
                    mTabLayout.getTabAt(position).setIcon(R.drawable.profile);
                    mTabLayout.getTabAt(0).setIcon(R.drawable.no_timeline);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



//        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
//
//        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getUserInfo(){

        RequestActivity oAuthClass=new RequestActivity("https://api.twitter.com/1.1/account/verify_credentials.json","GET",null,null,Twitter.this);
        String head=oAuthClass.returnheader();

        Log.e("user_profile","getting  user profile 1");
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<Profile_Data> call = apiInterface.getProfile(head);

        call.enqueue(new Callback<Profile_Data>() {
            @Override
            public void onResponse(Call<Profile_Data> call, Response<Profile_Data> response) {
                Log.e("user_profile","getting  user profile 2");
                Profile_Data profile=response.body();

                Picasso.with(Twitter.this).load(profile.getProfile_image_url()).into(profile_pic);

            }

            @Override
            public void onFailure(Call<Profile_Data> call, Throwable t) {

            }
        });



    }

}

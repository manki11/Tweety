package com.zcorp.app.tweety.Adapters;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zcorp.app.tweety.Activities.Twitter;
import com.zcorp.app.tweety.Fragments.twitter_feed;
import com.zcorp.app.tweety.Fragments.user_profile;

/**
 * Created by Mankirat on 30-03-2017.
 */

public class PageAdapter extends FragmentPagerAdapter {
    Context context;


    public PageAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("tab layout","main");
        switch (position) {
            case 0:
                Log.e("tab layout","feed");
                return twitter_feed.newInstance();
            case 1:
                Log.e("tab layout","user_profile");
                return user_profile.newInstance();
        }


        return twitter_feed.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "";
            case 1:
                return "";
        }
        return "";
    }
}

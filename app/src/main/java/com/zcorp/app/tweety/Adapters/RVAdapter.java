package com.zcorp.app.tweety.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zcorp.app.tweety.Activities.Profile;
import com.zcorp.app.tweety.Activities.Search;
import com.zcorp.app.tweety.Data.twitter_feed.Tweet;
import com.zcorp.app.tweety.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mankirat on 27-03-2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.Tweets> {

    ArrayList<Tweet> tweets;
    Context context;

    getmoretweets listener;
    favoritetweet listener_fav;
    Dialog builder;
    LayoutInflater inflater;


    public RVAdapter(ArrayList<Tweet> tweets, Context context){
        super();
        this.tweets=tweets;
        this.context=context;

    }

    public static class Tweets extends RecyclerView.ViewHolder{

        CardView cv;
        TextView userName;
        TextView userTag;
        TextView text,created, retweets_count, favorites_count,retweet_status;
        ImageView dp;
        ImageView isverified,media,retweet_status_button;
        Button retweets,favorites;

        Tweets(final View itemView){
            super(itemView);

            cv=(CardView) itemView.findViewById(R.id.cv);
            userName=(TextView) itemView.findViewById(R.id.userName);
            userTag=(TextView) itemView.findViewById(R.id.userTag);
            text=(TextView) itemView.findViewById(R.id.tweetText);
            dp=(ImageView) itemView.findViewById(R.id.dp);
            isverified=(ImageView) itemView.findViewById(R.id.isverified);
            media=(ImageView) itemView.findViewById(R.id.media);
            created=(TextView) itemView.findViewById(R.id.created);
            retweet_status_button=(ImageView) itemView.findViewById(R.id.retweet_status_button);
            retweets_count =(TextView) itemView.findViewById(R.id.retweet_count);
            favorites_count =(TextView) itemView.findViewById(R.id.favorites_count);
            retweet_status=(TextView) itemView.findViewById(R.id.retweet_status);

            retweets=(Button) itemView.findViewById(R.id.retweet);
            favorites=(Button) itemView.findViewById(R.id.favorites);



        }


    }
    @Override
    public Tweets onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.tweetcard,parent,false);
        Tweets tweets=new Tweets(v);
        return tweets;
    }


    public void setgetmoretweetslistener(getmoretweets listener)
    {
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(final Tweets holder, final int position) {
        holder.retweet_status.setVisibility(View.GONE);
        holder.retweet_status_button.setVisibility(View.GONE);
        holder.retweets.setBackgroundResource(R.drawable.no_retweet);
        holder.favorites.setBackgroundResource(R.drawable.no_favorite);

        holder.retweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener_fav.favoritetweetwithid(tweets.get(position).getId());

                if(tweets.get(position).getRetweeted()){
                    holder.retweets.setBackgroundResource(R.drawable.no_retweet);
                    tweets.get(position).setRetweeet_count(tweets.get(position).getRetweeet_count() - 1);
                    tweets.get(position).setRetweeted(false);
                }else {
                    holder.retweets.setBackgroundResource(R.drawable.retweeted);
                    tweets.get(position).setRetweeet_count(tweets.get(position).getRetweeet_count() + 1);
                    tweets.get(position).setRetweeted(true);

                }

                holder.retweets_count.setText(tweets.get(position).getRetweeet_count()+"");
            }
        });



        holder.favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener_fav.favoritetweetwithid(tweets.get(position).getId());
                if(tweets.get(position).getFavorited()){
                    holder.favorites.setBackgroundResource(R.drawable.no_favorite);
                    tweets.get(position).setFavorite_count(tweets.get(position).getFavorite_count() - 1);
                    tweets.get(position).setFavorited(false);
                }else {
                    holder.favorites.setBackgroundResource(R.drawable.favorite);
                    tweets.get(position).setFavorite_count(tweets.get(position).getFavorite_count() + 1);
                    tweets.get(position).setFavorited(true);

                }
                holder.favorites_count.setText(tweets.get(position).getFavorite_count()+"");
            }
        });

        holder.dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent();
                i.setClass(context, com.zcorp.app.tweety.Activities.Profile.class);
                if(tweets.get(position).getRetweeted_status()!=null){
                    i.putExtra("id", tweets.get(position).getRetweeted_status().getUser().getId());
                    i.putExtra("userTag", tweets.get(position).getRetweeted_status().getUser().getScreen_name());
                }else {
                    i.putExtra("id", tweets.get(position).getUser().getId());
                    i.putExtra("userTag", tweets.get(position).getUser().getScreen_name());
                }
                context.startActivity(i);

            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(context, com.zcorp.app.tweety.Activities.Profile.class);
                if(tweets.get(position).getRetweeted_status()!=null){
                    i.putExtra("id", tweets.get(position).getRetweeted_status().getUser().getId());
                    i.putExtra("userTag", tweets.get(position).getRetweeted_status().getUser().getScreen_name());
                }else {
                    i.putExtra("id", tweets.get(position).getUser().getId());
                    i.putExtra("userTag", tweets.get(position).getUser().getScreen_name());
                }
                context.startActivity(i);
            }
        });

        holder.userTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(context, com.zcorp.app.tweety.Activities.Profile.class);
                if(tweets.get(position).getRetweeted_status()!=null){
                    i.putExtra("id", tweets.get(position).getRetweeted_status().getUser().getId());
                    i.putExtra("userTag", tweets.get(position).getRetweeted_status().getUser().getScreen_name());
                }else {
                    i.putExtra("id", tweets.get(position).getUser().getId());
                    i.putExtra("userTag", tweets.get(position).getUser().getScreen_name());
                }
                context.startActivity(i);
            }
        });

        holder.userName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(tweets.get(position).getRetweeted_status()!=null) {
                    listener.quickview(tweets.get(position).getRetweeted_status());
                }else{
                    listener.quickview(tweets.get(position));
                }
                return true;
            }
        });

        holder.userTag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(tweets.get(position).getRetweeted_status()!=null) {
                    listener.quickview(tweets.get(position).getRetweeted_status());
                }else{
                    listener.quickview(tweets.get(position));
                }
                return true;
            }
        });

        holder.dp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(tweets.get(position).getRetweeted_status()!=null) {
                    listener.quickview(tweets.get(position).getRetweeted_status());
                }else{
                    listener.quickview(tweets.get(position));
                }
                return true;
            }
        });






        if(tweets.get(position).getRetweeted_status()!=null){

            holder.retweet_status_button.setVisibility(View.VISIBLE);
            holder.retweet_status.setVisibility(View.VISIBLE);
            holder.retweet_status.setText(tweets.get(position).getUser().getProfileName()+" Retweeted ");

            if(tweets.get(position).getRetweeted_status().getRetweeted()){
                holder.retweets.setBackgroundResource(R.drawable.retweeted);
            }

            if(tweets.get(position).getRetweeted_status().getFavorited()){
                holder.favorites.setBackgroundResource(R.drawable.favorite);
            }


            holder.text.setText(tweets.get(position).getRetweeted_status().getText());
            holder.userName.setText(tweets.get(position).getRetweeted_status().getUser().getProfileName());
            holder.userTag.setText("@"+tweets.get(position).getRetweeted_status().getUser().getScreen_name());
            holder.retweets_count.setText(tweets.get(position).getRetweeted_status().getRetweeet_count()+"");
            holder.favorites_count.setText(tweets.get(position).getRetweeted_status().getFavorite_count()+"");

            Date date=new Date();
            Log.e("id",position+" "+ tweets.get(position).getUser().getId());
            Log.e("id",position+" "+ tweets.get(position).getUser().getScreen_name());
            Log.e("date",date.toString());


            try {
                date=getTwitterDate(tweets.get(position).getRetweeted_status().getCreated_at());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long ttime=getDateDiff(date,new Date(),TimeUnit.MINUTES);

            String final_time=getFinalTime(ttime);
            holder.created.setText(final_time);


            holder.created.setText(final_time);


            holder.media.setVisibility(View.GONE);


            if(tweets.get(position).getRetweeted_status().getEntities().getMedia()!=null) {
                Log.e("media url",position+"media");

                if (tweets.get(position).getRetweeted_status().getEntities().getMedia().get(0).getType().equals("photo")) {
                    Log.e("media url",position+"");
                    String imgurl = tweets.get(position).getRetweeted_status().getEntities().getMedia().get(0).getMedia_url();
                    Log.e("media url",position+" "+ imgurl);
                    holder.media.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(imgurl).into(holder.media);
                    holder.media.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }


            }

            SpannableString spannableString=new SpannableString(this.tweets.get(position).getText());

            if(tweets.get(position).getEntities().getHashtags()!=null) {
                if(tweets.get(position).getEntities().getHashtags().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getHashtags().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getHashtags().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getHashtags().get(i).getIndices().get(1);

                        spannableString.setSpan(new MyClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#039BE5"));
                                                    }

                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        onClick(widget);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {

                                                        TextView tv = (TextView) widget;
                                                        Spanned s = (Spanned) tv.getText();
                                                        int start = s.getSpanStart(this);
                                                        int end = s.getSpanEnd(this);
                                                        String theWord = s.subSequence(start + 1, end).toString();

                                                        Intent i=new Intent();
                                                        i.setClass(context, Search.class);
                                                        i.putExtra("hashtag", theWord);
                                                        context.startActivity(i);
                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }
            }

            if(tweets.get(position).getEntities().getUser_mentions()!=null) {
                if(tweets.get(position).getEntities().getUser_mentions().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getUser_mentions().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getUser_mentions().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getUser_mentions().get(i).getIndices().get(1);
                        final String user=tweets.get(position).getEntities().getUser_mentions().get(i).getScreen_name();

                        spannableString.setSpan(new MyClickableSpan() {
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#D32F2F"));

                                                    }
                                                    @Override
                                                    public void onClick(View widget) {
                                                        Intent i=new Intent();
                                                        i.setClass(context, Profile.class);
                                                        i.putExtra("id", 0);
                                                        i.putExtra("userTag", user);
                                                        context.startActivity(i);
                                                    }

                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        listener.pop_up_user(user);
                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }

            }

            if(tweets.get(position).getEntities().getUrls()!=null) {
                if(tweets.get(position).getEntities().getUrls().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getUrls().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getUrls().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getUrls().get(i).getIndices().get(1);
                        final String MyURL= tweets.get(position).getEntities().getUrls().get(i).getExpanded_url();

                        spannableString.setSpan(new MyClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#00838F"));

                                                    }

                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        onClick(widget);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        TextView tv = (TextView) widget;

                                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                        alert.setTitle("");

                                                        WebView wv = new WebView(context);
                                                        wv.loadUrl(MyURL);
                                                        wv.getSettings().setJavaScriptEnabled(true);
                                                        wv.setWebViewClient(new WebViewClient() {
                                                            @Override
                                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                                view.loadUrl(url);

                                                                return true;
                                                            }
                                                        });


                                                        alert.setView(wv);
                                                        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                        alert.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setData(Uri.parse(MyURL));
                                                                context.startActivity(intent);
                                                            }
                                                        });

                                                        alert.show();

                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }

            }

            if(tweets.get(position).getEntities().getMedia()!=null) {
                if(tweets.get(position).getEntities().getMedia().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getMedia().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getMedia().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getMedia().get(i).getIndices().get(1);
                        final String MyURL= tweets.get(position).getEntities().getMedia().get(i).getMedia_url();

                        spannableString.setSpan(new MyClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#00838F"));

                                                    }
                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        onClick(widget);
                                                    }
                                                    @Override
                                                    public void onClick(View widget) {
                                                        TextView tv = (TextView) widget;

                                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                        alert.setTitle("");

                                                        WebView wv = new WebView(context);
                                                        wv.loadUrl(MyURL);
                                                        wv.getSettings().setJavaScriptEnabled(true);
                                                        wv.setWebViewClient(new WebViewClient() {
                                                            @Override
                                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                                view.loadUrl(url);

                                                                return true;
                                                            }
                                                        });


                                                        alert.setView(wv);
                                                        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                        alert.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setData(Uri.parse(MyURL));
                                                                context.startActivity(intent);
                                                            }
                                                        });

                                                        alert.show();

                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }

            }

            if(position==tweets.size()-1){
                String max_id=tweets.get(tweets.size()-1).getId()+"";
                try {
                    listener.getmoretweetstoshow(max_id);
                }  catch (Exception e) {
                    e.printStackTrace();
                }

            }

            Picasso.with(context).load(tweets.get(position).getRetweeted_status().getUser().getProfile_image_url()).into(holder.dp);

            if(tweets.get(position).getUser().getVerified()){
                holder.isverified.setImageResource(R.drawable.verified);
            }
        }else {

            holder.text.setText(tweets.get(position).getText());
            holder.userName.setText(tweets.get(position).getUser().getProfileName());
            holder.userTag.setText("@" + tweets.get(position).getUser().getScreen_name());
            holder.retweets_count.setText(tweets.get(position).getRetweeet_count() + "");
            holder.favorites_count.setText(tweets.get(position).getFavorite_count() + "");

            Date date = new Date();
            Log.e("date", date.toString());

            if(tweets.get(position).getRetweeted()){
                holder.retweets.setBackgroundResource(R.drawable.retweeted);
            }

            if(tweets.get(position).getFavorited()){
                holder.favorites.setBackgroundResource(R.drawable.favorite);
            }


            try {
                date = getTwitterDate(tweets.get(position).getCreated_at());
                Log.e("date","1 "+getTwitterDate(tweets.get(position).getCreated_at()).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long ttime=getDateDiff(date,new Date(),TimeUnit.MINUTES);

            String final_time=getFinalTime(ttime);
            holder.created.setText(final_time);

            SpannableString spannableString=new SpannableString(this.tweets.get(position).getText());

            if(tweets.get(position).getEntities().getHashtags()!=null) {
                if(tweets.get(position).getEntities().getHashtags().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getHashtags().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getHashtags().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getHashtags().get(i).getIndices().get(1);

                        spannableString.setSpan(new MyClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#039BE5"));
                                                    }

                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        onClick(widget);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        TextView tv = (TextView) widget;
                                                        Spanned s = (Spanned) tv.getText();
                                                        int start = s.getSpanStart(this);
                                                        int end = s.getSpanEnd(this);
                                                        String theWord = s.subSequence(start + 1, end).toString();

                                                        Intent i=new Intent();
                                                        i.setClass(context, Search.class);
                                                        i.putExtra("hashtag", theWord);
                                                        context.startActivity(i);
                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }

            }

            if(tweets.get(position).getEntities().getUser_mentions()!=null) {
                if(tweets.get(position).getEntities().getUser_mentions().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getUser_mentions().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getUser_mentions().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getUser_mentions().get(i).getIndices().get(1);
                        final String user=tweets.get(position).getEntities().getUser_mentions().get(i).getScreen_name();
                        spannableString.setSpan(new MyClickableSpan() {
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#D32F2F"));

                                                    }
                                                    @Override
                                                    public void onClick(View widget) {
                                                        Intent i=new Intent();
                                                        i.setClass(context, Profile.class);
                                                        i.putExtra("id", 0);
                                                        i.putExtra("userTag", user);
                                                        context.startActivity(i);
                                                    }

                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        listener.pop_up_user(user);
                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);

                }

            }

            if(tweets.get(position).getEntities().getUrls()!=null) {
                if(tweets.get(position).getEntities().getUrls().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getUrls().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getUrls().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getUrls().get(i).getIndices().get(1);
                        final String MyURL=tweets.get(position).getEntities().getUrls().get(i).getExpanded_url();

                        spannableString.setSpan(new MyClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#00838F"));

                                                    }
                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        onClick(widget);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        TextView tv = (TextView) widget;

                                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                        alert.setTitle("");

                                                        WebView wv = new WebView(context);
                                                        wv.loadUrl(MyURL);
                                                        wv.getSettings().setJavaScriptEnabled(true);
                                                        wv.setWebViewClient(new WebViewClient() {
                                                            @Override
                                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                                view.loadUrl(url);

                                                                return true;
                                                            }
                                                        });


                                                        alert.setView(wv);
                                                        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                        alert.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setData(Uri.parse(MyURL));
                                                                context.startActivity(intent);
                                                            }
                                                        });

                                                        alert.show();

                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }

            }

            if(tweets.get(position).getEntities().getMedia()!=null) {
                if(tweets.get(position).getEntities().getMedia().size()>0) {
                    Log.e("linking","hashtag");

                    for(int i=0;i<tweets.get(position).getEntities().getMedia().size();i++) {
                        int hashTagStart = tweets.get(position).getEntities().getMedia().get(i).getIndices().get(0);
                        int hashTagEnd = tweets.get(position).getEntities().getMedia().get(i).getIndices().get(1);
                        final String MyURL=tweets.get(position).getEntities().getMedia().get(i).getMedia_url();

                        spannableString.setSpan(new MyClickableSpan() {
                                                    @Override
                                                    public void updateDrawState(TextPaint ds) {
                                                        ds.setColor(Color.parseColor("#00838F"));

                                                    }
                                                    @Override
                                                    public void onLongClick(View widget) {
                                                        onClick(widget);
                                                    }

                                                    @Override
                                                    public void onClick(View widget) {
                                                        TextView tv = (TextView) widget;

                                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                        alert.setTitle("");

                                                        WebView wv = new WebView(context);
                                                        wv.loadUrl(MyURL);
                                                        wv.getSettings().setJavaScriptEnabled(true);
                                                        wv.setWebViewClient(new WebViewClient() {
                                                            @Override
                                                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                                view.loadUrl(url);

                                                                return true;
                                                            }
                                                        });


                                                        alert.setView(wv);
                                                        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                        alert.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setData(Uri.parse(MyURL));
                                                                context.startActivity(intent);
                                                            }
                                                        });

                                                        alert.show();

                                                    }
                                                },
                                hashTagStart,
                                hashTagEnd, 0);
                    }
                    holder.text.setText(spannableString);
                }

            }
            holder.media.setVisibility(View.GONE);


            if (tweets.get(position).getEntities().getMedia() != null) {
                Log.e("media url", position + "media");

                if (tweets.get(position).getEntities().getMedia().get(0).getType().equals("photo")) {
                    Log.e("media url", position + "");
                    String imgurl = tweets.get(position).getEntities().getMedia().get(0).getMedia_url();
                    Log.e("media url", position + " " + imgurl);
                    holder.media.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(imgurl).into(holder.media);
                    holder.media.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }


            }


            if (position == tweets.size() - 1) {
                String max_id = tweets.get(tweets.size() - 1).getId()+"";
                try {
                    listener.getmoretweetstoshow(max_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            Picasso.with(context).load(tweets.get(position).getUser().getProfile_image_url()).into(holder.dp);

            if (tweets.get(position).getUser().getVerified()) {
                holder.isverified.setImageResource(R.drawable.verified);
            }
        }

        if(holder.text.getLinksClickable()) {
            holder.text.setMovementMethod(LongClickLinkMovementMethod.getInstance());
        }

    }

    @Override
    public int getItemCount() {
        Log.d("lala", String.valueOf(1));
        return tweets.size();
    }


    public interface getmoretweets{
        void getmoretweetstoshow(String max_id) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException;
        void quickview(Tweet tweet);
        void pop_up_user(String user);
    }

    public interface favoritetweet{
        void favoritetweetwithid(long tweet_id);
    }

    public static Date getTwitterDate(String date) throws ParseException {

        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
        sf.setLenient(true);
        return sf.parse(date);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public String getFinalTime(long ttime){
        String time_final;

        if(ttime<60){
            time_final=ttime+"m";
        }else {
            ttime=ttime/60;

            if(ttime<24){
                time_final=ttime+"h";
            }else{
                ttime=ttime/24;

                if(ttime<30){
                    time_final=ttime+"d";
                }else{
                    ttime=ttime/30;

                    if(ttime<12){
                        time_final=ttime+"M";
                    }else{
                        ttime=ttime/12;

                        time_final=ttime+"Y";
                    }
                }
            }
        }

        return time_final;
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

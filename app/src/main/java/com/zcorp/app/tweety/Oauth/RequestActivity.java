package com.zcorp.app.tweety.Oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.zcorp.app.tweety.R;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity {

    String header;
    SharedPreferences sp;


    public RequestActivity(String url , String methodtype, HashMap<String,String>query , HashMap<String,String> body, Context applicationContext){

        OAuthClass oAuthClass= new OAuthClass();

        sp= applicationContext.getSharedPreferences("keys",Context.MODE_PRIVATE);

        String TWITTER_KEY=sp.getString("TWITTER_KEY",null);
        String TWITTER_SECRET=sp.getString("TWITTER_SECRET",null);
        String TOKEN=sp.getString("TOKEN",null);
        String TOKEN_SECRET=sp.getString("TOKEN_SECRET",null);

        Log.e("RA 1",TWITTER_KEY);
        Log.e("RA 2",TWITTER_SECRET);
        Log.e("RA 3",TOKEN);
        Log.e("RA 4",TOKEN_SECRET);


        String random=randomString();

        try {

            Log.e("request header","header");

            String time=System.currentTimeMillis()/1000+"";


            header=oAuthClass.setMethod(methodtype)
                    .setConsumersecret(TWITTER_SECRET)
                    .setTokensecret(TOKEN_SECRET)
                    .setOauth_consumer_key(TWITTER_KEY)
                    .setOauth_token(TOKEN)
                    .setOauth_signature_method("HMAC-SHA1")
                    .setOauth_version("1.0")

                    .setOauth_nonce(random)//should be a random string everytime
                    .setOauth_timestamp(time) //current epoch time
                    .setBody(body) //set to null if there is no request body
                    .setBaseurl(url)//Complete API endpoint which is being hit
                    .setQuery(query)//Query Paremeters for the request-set to null if not any
                    .getAuthheader();

            Log.e("lala",header);

            /*Now this header string will be added as a header as {Authorization: header string} to each request
            Set the 4 keys ,request & signature method , version only once while making the oAuthObject or making the first request

            This whole header calculation can be done in the API interface of retrofit while using one oAuthClass object

            Refer to the link https://dev.twitter.com/oauth/overview/authorizing-requests for more details
            */

            Log.d("lala",oAuthClass.displaySignature());
            /*This function displays the signature that is generated internally using the oAuth header and other details

            Refer to https://dev.twitter.com/oauth/overview/creating-signatures for more details
            */

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String returnheader(){
        return header;
    }

    public String randomString(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);

    }
}

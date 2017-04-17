package com.zcorp.app.tweety.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.zcorp.app.tweety.R;

import io.fabric.sdk.android.Fabric;


public class LoginActivity extends AppCompatActivity {

    SharedPreferences sp;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YOUR_TWITTER_KEY";
    private static final String TWITTER_SECRET = "YOUR_TWITTER_SECRET";


    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new com.twitter.sdk.android.Twitter(authConfig));
        setContentView(R.layout.activity_login);

        sp =getSharedPreferences("keys",MODE_PRIVATE);
        final SharedPreferences.Editor editor=sp.edit();

        editor.putString("TWITTER_KEY",TWITTER_KEY);
        editor.putString("TWITTER_SECRET",TWITTER_SECRET);

        editor.commit();



        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {


            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                TwitterAuthToken Usertoken = session.getAuthToken();
                String UserToken= Usertoken.token;
                String UserSecret= Usertoken.secret;

                Log.e("tokens",UserToken);
                Log.e("secret",UserSecret);

                editor.putString("TOKEN",UserToken);
                editor.putString("TOKEN_SECRET",UserSecret);

                editor.commit();


            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);

        Intent i=new Intent();
        i.setClass(LoginActivity.this,Twitter.class);
        startActivity(i);
    }

}

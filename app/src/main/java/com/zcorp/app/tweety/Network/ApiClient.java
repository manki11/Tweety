package com.zcorp.app.tweety.Network;

import android.util.Log;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by manishakhattar on 19/03/17.
 */

public class ApiClient {

    static ApiInterface apiInterface;

    public static ApiInterface getApiInterface(){

        Log.e("retrofit","baseurl");

        if(apiInterface == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.twitter.com/1.1/")
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().
                            serializeNulls().create()))
                    .build();

            apiInterface =retrofit.create(ApiInterface.class);;
        }
        return apiInterface;
    }


}

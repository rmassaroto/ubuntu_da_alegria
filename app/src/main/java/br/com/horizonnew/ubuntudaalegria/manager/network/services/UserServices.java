package br.com.horizonnew.ubuntudaalegria.manager.network.services;

import com.google.gson.JsonObject;

import br.com.horizonnew.ubuntudaalegria.BuildConfig;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by renan on 30/08/16.
 */
public interface UserServices {

    String RESOURCE_GET_FEED = "get_feed";

    @Headers({
            BuildConfig.HEADER_KEY_APPLICATION_KEY + ": " + BuildConfig.HEADER_VALUE_APPLICATION_KEY,
            BuildConfig.HEADER_KEY_DEV_TOKEN + ": " + BuildConfig.HEADER_VALUE_DEV_TOKEN
    })
    @POST(BuildConfig.ENDPOINT + RESOURCE_GET_FEED)
    Call<JsonObject> getUserFeed();
}

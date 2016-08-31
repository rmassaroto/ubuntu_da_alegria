package br.com.horizonnew.ubuntudaalegria.manager.network.services;

import com.google.gson.JsonObject;

import br.com.horizonnew.ubuntudaalegria.BuildConfig;
import br.com.horizonnew.ubuntudaalegria.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by renan on 30/08/16.
 */
public interface UserServices {

    String RESOURCE_SAVE_USER_DETAILS = "get_user_feed";

    @Headers({
            BuildConfig.HEADER_KEY_APPLICATION_KEY + ": " + BuildConfig.HEADER_VALUE_APPLICATION_KEY,
            BuildConfig.HEADER_KEY_DEV_TOKEN + ": " + BuildConfig.HEADER_VALUE_DEV_TOKEN
    })
    @POST(BuildConfig.ENDPOINT + RESOURCE_SAVE_USER_DETAILS)
    @FormUrlEncoded
    Call<JsonObject> getUserFeed();
}

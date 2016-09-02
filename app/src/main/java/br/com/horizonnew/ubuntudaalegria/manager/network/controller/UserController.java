package br.com.horizonnew.ubuntudaalegria.manager.network.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import br.com.horizonnew.ubuntudaalegria.manager.bus.event.feed.OnGetFeedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.feed.OnGetFeedFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.network.ServiceBuilder;
import br.com.horizonnew.ubuntudaalegria.manager.network.services.UserServices;
import br.com.horizonnew.ubuntudaalegria.manager.utils.AFSharedPreferencesUtils;
import br.com.horizonnew.ubuntudaalegria.model.Post;
import br.com.horizonnew.ubuntudaalegria.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class UserController {

    private static final String LOG_TAG = "UserController";

    public static final String PREFERENCES_KEY_LOGGED_USER = "br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController.PREFERENCES_KEY_LOGGED_USER";

    private UserController() {}

    @NonNull
    public static Call getFeed(@NonNull final User user) {
        UserServices userServices = ServiceBuilder.createService(UserServices.class);

        Call<JsonObject> call = userServices.getUserFeed();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null && response.body() != null &&
                        response.body().has("response") && response.body().get("response").isJsonObject() &&
                        response.body().get("response").getAsJsonObject().has("feed") && response.body().get("response").getAsJsonObject().get("feed").isJsonArray()) {

                    JsonArray feedArray = response.body().get("response").getAsJsonObject().get("feed").getAsJsonArray();

                    ArrayList<Post> posts = new ArrayList<>();

                    for (int index = 0; index < feedArray.size(); index++) {
                        try {
                            posts.add(new Post(feedArray.get(index).getAsJsonObject()));
                        } catch (JsonParseException e) {
                            Log.e(LOG_TAG, "onResponse: ", e);
                        }
                    }

                    UserProviderBus.getInstance().post(new OnGetFeedEvent(user, posts));
                } else {
                    UserProviderBus.getInstance().post(new OnGetFeedFailedEvent(user, null));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                UserProviderBus.getInstance().post(new OnGetFeedFailedEvent(user, t));
            }
        });

        return call;
    }

//    @Nullable
//    public static User getLoggedUser(@NonNull Context context) {
//        String userJsonString = AFSharedPreferencesUtils.getString(context, PREFERENCES_KEY_LOGGED_USER, null);
//
//        if (userJsonString != null && !userJsonString.isEmpty()) {
//            return new User(new JsonParser().parse(userJsonString));
//        } else {
//            return null;
//        }
//    }

    @Nullable
    public static User getLoggedUser(@NonNull Context context) {
//        String userJsonString = AFSharedPreferencesUtils.getString(context, PREFERENCES_KEY_LOGGED_USER, null);
//
//        if (userJsonString != null && !userJsonString.isEmpty()) {
            return new User();
//        } else {
//            return null;
//        }
    }
}

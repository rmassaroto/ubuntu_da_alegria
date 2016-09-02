package br.com.horizonnew.ubuntudaalegria.manager.network.controller;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;

import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.post.OnUploadPostEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.post.OnUploadPostFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.MediaProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.PostProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.network.ServiceBuilder;
import br.com.horizonnew.ubuntudaalegria.manager.network.services.PostServices;
import br.com.horizonnew.ubuntudaalegria.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class PostController {

    private static final String LOG_TAG = "PostController";

    public static void uploadPost(@NonNull final Post post) {
        PostServices postServices = ServiceBuilder.createService(PostServices.class);

        Call<JsonObject> call = postServices.createPost(post.toJsonObject());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject jsonObject = response.body();

                    if (jsonObject.has("response") && jsonObject.get("response").isJsonObject() &&
                            jsonObject.get("response").getAsJsonObject().has(Post.API_KEYWORD_FEED_ID) && !jsonObject.get("response").getAsJsonObject().get(Post.API_KEYWORD_FEED_ID).isJsonNull()) {

                        post.setId(jsonObject.get("response").getAsJsonObject().get(Post.API_KEYWORD_FEED_ID).getAsLong());

                        PostProviderBus.getInstance().post(new OnUploadPostEvent(post));
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "onResponse: ", e);

                    PostProviderBus.getInstance().post(new OnUploadPostFailedEvent(post, e));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure: ", t);

                PostProviderBus.getInstance().post(new OnUploadPostFailedEvent(post, t));
            }
        });
    }
}

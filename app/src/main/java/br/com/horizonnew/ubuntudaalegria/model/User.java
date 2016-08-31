package br.com.horizonnew.ubuntudaalegria.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import br.com.horizonnew.ubuntudaalegria.manager.bus.event.OnGetUserFeedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.network.ServiceBuilder;
import br.com.horizonnew.ubuntudaalegria.manager.network.services.UserServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by renan on 30/08/16.
 */
public class User {

    private long id;
    private String pictureUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Call getFeed() {
        UserServices userServices = ServiceBuilder.createService(UserServices.class);

        Call<JsonObject> call = userServices.getUserFeed();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                UserProviderBus.getInstance().post(new OnGetUserFeedEvent(new ArrayList<Post>()));
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                UserProviderBus.getInstance().post(new OnGetUserFeedEvent(new ArrayList<Post>()));
            }
        });

        return call;
    }
}

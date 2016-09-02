package br.com.horizonnew.ubuntudaalegria.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

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
public class User implements Parcelable {

    private static final String LOG_TAG = "User";

    public static final String ARG = "br.com.horizonnew.ubuntudaalegria.model.User.ARG";

    private long id;
    private String name, pictureUrl;

    public static Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User() {
        id = 1;
        name = "Douglas Faria";
        pictureUrl = "http://www.vitamin-ha.com/wp-content/uploads/2013/09/Funny-Profile-Pictures-14.jpg";
    }

    public User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        pictureUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(pictureUrl);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

                    UserProviderBus.getInstance().post(new OnGetUserFeedEvent(User.this, posts));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ArrayList<Post> posts = new ArrayList<Post>();
//                posts.add(new Post(true));
//                posts.add(new Post(false));

                UserProviderBus.getInstance().post(new OnGetUserFeedEvent(User.this, posts));
            }
        });

        return call;
    }

    @Nullable
    public static User getLoggedUser(@NonNull Context context) {
        return new User();
    }
}

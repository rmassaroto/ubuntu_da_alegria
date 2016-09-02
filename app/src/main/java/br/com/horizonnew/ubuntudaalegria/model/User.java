package br.com.horizonnew.ubuntudaalegria.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Created by renan on 30/08/16.
 */
public class User implements Parcelable {

    private static final String LOG_TAG = "User";

    public static final String ARG = "br.com.horizonnew.ubuntudaalegria.model.User.ARG";

    public static final String API_KEYWORD_USER_ID = "user_id";
    public static final String API_KEYWORD_NAME = "name";
    public static final String API_KEYWORD_PICTURE_URL = "picture_url";

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

    public User() {
        super();

        id = 1;
        name = "Douglas Faria";
        pictureUrl = "http://www.vitamin-ha.com/wp-content/uploads/2013/09/Funny-Profile-Pictures-14.jpg";
    }

    public User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        pictureUrl = in.readString();
    }

    public User(@NonNull JsonElement element) throws JsonParseException {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has(API_KEYWORD_USER_ID))
                id = jsonObject.get(API_KEYWORD_USER_ID).getAsLong();

            if (jsonObject.has(API_KEYWORD_NAME))
                name = jsonObject.get(API_KEYWORD_NAME).getAsString();

            if (jsonObject.has(API_KEYWORD_PICTURE_URL))
                pictureUrl = jsonObject.get(API_KEYWORD_PICTURE_URL).getAsString();

        } else {
            throw new RuntimeException("Json element must be a valid json object");
        }
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

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(API_KEYWORD_USER_ID, id);
        jsonObject.addProperty(API_KEYWORD_NAME, name);
        jsonObject.addProperty(API_KEYWORD_PICTURE_URL, pictureUrl);

        return jsonObject;
    }
}

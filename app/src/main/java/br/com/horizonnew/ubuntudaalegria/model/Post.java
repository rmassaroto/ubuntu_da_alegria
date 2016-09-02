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
public class Post implements Parcelable {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private int type;
    private long id;
    private boolean campaign;
    private String title, url, description, text;
    private User user;

    public static Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Post() {
        super();
    }

    public Post(@NonNull JsonElement element) throws JsonParseException {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            title = jsonObject.get("title").getAsString();
            description = jsonObject.get("description").getAsString();
            text = jsonObject.get("text").getAsString();
            url = jsonObject.get("url").getAsString();
            type = jsonObject.get("type").getAsInt();
            campaign = jsonObject.get("campaign").getAsInt() == 1;

        } else {
            throw new RuntimeException("Json element must be a valid json object");
        }
    }

    public Post(Parcel in) {
        type = in.readInt();
        id = in.readLong();
        campaign = in.readInt() == 1;
        title = in.readString();
        url = in.readString();
        description = in.readString();
        text = in.readString();

        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeLong(id);
        dest.writeInt((campaign ? 1 : 0));
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(text);

        dest.writeParcelable(user, flags);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCampaign() {
        return campaign;
    }

    public void setCampaign(boolean campaign) {
        this.campaign = campaign;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();

        return jsonObject;
    }
}

package br.com.horizonnew.ubuntudaalegria.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by renan on 30/08/16.
 */
public class Post implements Parcelable {

    private static final String LOG_TAG = "Post";

    public static final String ARG = "br.com.horizonnew.ubuntudaalegria.model.Post.ARG";

    public static final String API_KEYWORD_FEED_ID = "feed_id";
    public static final String API_KEYWORD_USER = "user";
    public static final String API_KEYWORD_TITLE = "title";
    public static final String API_KEYWORD_DESCRIPTION = "description";
    public static final String API_KEYWORD_TEXT = "text";
    public static final String API_KEYWORD_URL = "url";
    public static final String API_KEYWORD_TYPE = "type";
    public static final String API_KEYWORD_CAMPAIGN = "campaign";
    public static final String API_KEYWORD_CREATE_DATE = "create_date";
    public static final String API_KEYWORD_ACTIVE = "active";

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private long id;
    private int type;
    private boolean campaign, active;
    private String title, url, description, text;
    private Date createDate;
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

    public Post(Parcel in) {
        type = in.readInt();
        id = in.readLong();
        campaign = in.readInt() == 1;
        active = in.readInt() == 1;
        title = in.readString();
        url = in.readString();
        description = in.readString();
        text = in.readString();

        long createDate = in.readLong();
        if (createDate > 0)
            this.createDate = new Date(createDate);

        user = in.readParcelable(User.class.getClassLoader());
    }

    public Post(@NonNull JsonElement element) throws JsonParseException {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has(API_KEYWORD_FEED_ID))
                id = jsonObject.get(API_KEYWORD_FEED_ID).getAsLong();

            if (jsonObject.has(API_KEYWORD_USER))
                user = new User(jsonObject.get(API_KEYWORD_USER));

            if (jsonObject.has(API_KEYWORD_TITLE))
                title = jsonObject.get(API_KEYWORD_TITLE).getAsString();

            if (jsonObject.has(API_KEYWORD_DESCRIPTION))
                description = jsonObject.get(API_KEYWORD_DESCRIPTION).getAsString();

            if (jsonObject.has(API_KEYWORD_TEXT))
                text = jsonObject.get(API_KEYWORD_TEXT).getAsString();

            if (jsonObject.has(API_KEYWORD_URL))
                url = jsonObject.get(API_KEYWORD_URL).getAsString();

            if (jsonObject.has(API_KEYWORD_TYPE))
                type = jsonObject.get(API_KEYWORD_TYPE).getAsInt();

            if (jsonObject.has(API_KEYWORD_CAMPAIGN))
                campaign = jsonObject.get(API_KEYWORD_CAMPAIGN).getAsInt() == 1;

            if (jsonObject.has(API_KEYWORD_CREATE_DATE)) {
                try {
                    createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.get(API_KEYWORD_CAMPAIGN).getAsString());
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Post: ", e);
                }
            }

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
        dest.writeInt(type);
        dest.writeLong(id);
        dest.writeInt((campaign ? 1 : 0));
        dest.writeInt((active ? 1 : 0));
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(text);

        if (createDate != null)
            dest.writeLong(createDate.getTime());
        else
            dest.writeLong(0);

        dest.writeParcelable(user, flags);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCampaign() {
        return campaign;
    }

    public void setCampaign(boolean campaign) {
        this.campaign = campaign;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(API_KEYWORD_FEED_ID, id);
        jsonObject.addProperty(API_KEYWORD_TYPE, type);
        jsonObject.addProperty(API_KEYWORD_CAMPAIGN, campaign);
        jsonObject.addProperty(API_KEYWORD_ACTIVE, active);

        jsonObject.addProperty(API_KEYWORD_TITLE, title);
        jsonObject.addProperty(API_KEYWORD_URL, url);
        jsonObject.addProperty(API_KEYWORD_DESCRIPTION, description);
        jsonObject.addProperty(API_KEYWORD_TEXT, text);

        if (createDate != null)
            jsonObject.addProperty(API_KEYWORD_CREATE_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDate));

        jsonObject.add(API_KEYWORD_USER, user.toJsonObject());

        return jsonObject;
    }
}

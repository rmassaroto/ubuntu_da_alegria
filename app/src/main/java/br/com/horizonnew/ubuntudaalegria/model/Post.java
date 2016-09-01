package br.com.horizonnew.ubuntudaalegria.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    public Post(boolean campaign) {
        type = TYPE_IMAGE;
        id = 1;
        this.campaign = campaign;
        title = "Título do post";
        url = "http://www.ultracurioso.com.br/wp-content/uploads/2016/01/palha%C3%A7o-capa.jpg";
        description = "Descrição do post";
        text = "Texto do post";
        user = User.getLoggedUser(null);
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
}

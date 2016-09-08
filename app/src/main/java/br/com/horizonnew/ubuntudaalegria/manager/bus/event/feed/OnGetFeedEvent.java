package br.com.horizonnew.ubuntudaalegria.manager.bus.event.feed;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import br.com.horizonnew.ubuntudaalegria.model.Post;
import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class OnGetFeedEvent {

    private User user;
    private ArrayList<Post> feed;

    public OnGetFeedEvent(@Nullable User user, @NonNull ArrayList<Post> feed) {
        this.user = user;
        this.feed = feed;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    @NonNull
    public ArrayList<Post> getFeed() {
        return feed;
    }

    public void setFeed(@NonNull ArrayList<Post> feed) {
        this.feed = feed;
    }
}

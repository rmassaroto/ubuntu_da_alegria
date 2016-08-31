package br.com.horizonnew.ubuntudaalegria.manager.bus.event;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.horizonnew.ubuntudaalegria.model.Post;
import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by renan on 30/08/16.
 */
public class OnGetUserFeedEvent {
    public User user;
    public ArrayList<Post> posts;

    public OnGetUserFeedEvent(@NonNull User user, @NonNull ArrayList<Post> posts) {
        this.user = user;
        this.posts = posts;
    }
}

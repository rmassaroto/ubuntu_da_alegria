package br.com.horizonnew.ubuntudaalegria.manager.bus.event.post;

import android.support.annotation.NonNull;

import br.com.horizonnew.ubuntudaalegria.model.Post;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class OnUploadPostEvent {

    private Post localPost;

    public OnUploadPostEvent(@NonNull Post localPost) {
        this.localPost = localPost;
    }

    @NonNull
    public Post getLocalPost() {
        return localPost;
    }

    public void setLocalPost(@NonNull Post localPost) {
        this.localPost = localPost;
    }
}

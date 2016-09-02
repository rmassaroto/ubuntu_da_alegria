package br.com.horizonnew.ubuntudaalegria.manager.bus.event.post;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.horizonnew.ubuntudaalegria.model.Post;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class OnUploadPostFailedEvent {

    private Post localPost;
    private Throwable throwable;

    public OnUploadPostFailedEvent(@NonNull Post localPost, @Nullable Throwable throwable) {
        super();

        this.localPost = localPost;
        this.throwable = throwable;
    }

    @Nullable
    public Post getLocalPost() {
        return localPost;
    }

    public void setLocalPost(@Nullable Post localPost) {
        this.localPost = localPost;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}

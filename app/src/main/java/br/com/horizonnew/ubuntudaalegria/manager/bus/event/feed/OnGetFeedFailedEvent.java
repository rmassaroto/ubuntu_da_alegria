package br.com.horizonnew.ubuntudaalegria.manager.bus.event.feed;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class OnGetFeedFailedEvent {

    private User user;
    private Throwable throwable;

    public OnGetFeedFailedEvent(@NonNull User user, @Nullable Throwable throwable) {
        super();

        this.user = user;
        this.throwable = throwable;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}

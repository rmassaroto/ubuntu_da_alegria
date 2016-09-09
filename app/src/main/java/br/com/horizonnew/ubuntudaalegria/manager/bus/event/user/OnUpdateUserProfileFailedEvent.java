package br.com.horizonnew.ubuntudaalegria.manager.bus.event.user;

import android.support.annotation.NonNull;

import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by Renan Cardoso Massaroto on 09/09/16.
 */
public class OnUpdateUserProfileFailedEvent {
    private User user;

    public OnUpdateUserProfileFailedEvent(@NonNull User user) {
        this.user = user;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }
}

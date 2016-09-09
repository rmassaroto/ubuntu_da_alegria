package br.com.horizonnew.ubuntudaalegria.manager.bus.event.user;

import android.support.annotation.NonNull;

import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by Renan Cardoso Massaroto on 09/09/16.
 */
public class OnUpdateUserProfileEvent {
    private User user;

    public OnUpdateUserProfileEvent(@NonNull User user) {
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

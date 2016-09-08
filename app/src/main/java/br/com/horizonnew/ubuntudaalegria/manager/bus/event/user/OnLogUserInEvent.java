package br.com.horizonnew.ubuntudaalegria.manager.bus.event.user;

import android.support.annotation.NonNull;

import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by renan on 07/09/16.
 */
public class OnLogUserInEvent {
    private User user;

    public OnLogUserInEvent(@NonNull User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

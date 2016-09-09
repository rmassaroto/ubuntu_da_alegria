package br.com.horizonnew.ubuntudaalegria.manager.bus.event.user;

import android.support.annotation.NonNull;

import br.com.horizonnew.ubuntudaalegria.model.User;

/**
 * Created by Renan Cardoso Massaroto on 08/09/16.
 */
public class OnSignUserUpEvent {
    private User user;

    public OnSignUserUpEvent(@NonNull User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

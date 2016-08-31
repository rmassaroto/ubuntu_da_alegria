package br.com.horizonnew.ubuntudaalegria.manager.bus.provider;

import com.squareup.otto.Bus;

/**
 * Created by renan on 30/08/16.
 */
public class UserProviderBus extends Bus {

    private static UserProviderBus mInstance;

    private UserProviderBus(){}

    public static UserProviderBus getInstance() {
        if (mInstance == null)
            mInstance = new UserProviderBus();

        return mInstance;
    }
}

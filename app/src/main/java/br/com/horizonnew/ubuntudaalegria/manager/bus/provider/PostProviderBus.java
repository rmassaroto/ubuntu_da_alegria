package br.com.horizonnew.ubuntudaalegria.manager.bus.provider;

import com.squareup.otto.Bus;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class PostProviderBus extends Bus {

    private static PostProviderBus mInstance;

    private PostProviderBus(){}

    public static PostProviderBus getInstance() {
        if (mInstance == null)
            mInstance = new PostProviderBus();

        return mInstance;
    }
}

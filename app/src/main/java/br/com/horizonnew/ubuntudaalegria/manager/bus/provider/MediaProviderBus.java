package br.com.horizonnew.ubuntudaalegria.manager.bus.provider;

import com.squareup.otto.Bus;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class MediaProviderBus extends Bus {

    private static MediaProviderBus mInstance;

    private MediaProviderBus(){}

    public static MediaProviderBus getInstance() {
        if (mInstance == null)
            mInstance = new MediaProviderBus();

        return mInstance;
    }
}

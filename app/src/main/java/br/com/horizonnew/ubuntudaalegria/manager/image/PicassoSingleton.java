package br.com.horizonnew.ubuntudaalegria.manager.image;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;

/**
 * Created by renan on 31/08/16.
 */
public class PicassoSingleton {

    private static Picasso mInstance;

    private PicassoSingleton() {}

    public static Picasso getInstance(@NonNull Context context) {
        if (mInstance == null)
            mInstance = Picasso.with(context.getApplicationContext());

        mInstance.setLoggingEnabled(true);

        return mInstance;
    }
}

package br.com.horizonnew.ubuntudaalegria.manager.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import br.com.horizonnew.ubuntudaalegria.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by renan on 30/08/16.
 */
public class ServiceBuilder {
    public static final String API_BASE_URL = BuildConfig.URL_API;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).build()).build();
        return retrofit.create(serviceClass);
    }


    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

package br.com.horizonnew.ubuntudaalegria.manager.network.services;

import com.google.gson.JsonObject;

import br.com.horizonnew.ubuntudaalegria.BuildConfig;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public interface MediaServices {

    String RESOURCE_UPLOAD_FILE = "upload";

    @Headers({
            BuildConfig.HEADER_KEY_APPLICATION_KEY + ": " + BuildConfig.HEADER_VALUE_APPLICATION_KEY,
            BuildConfig.HEADER_KEY_DEV_TOKEN + ": " + BuildConfig.HEADER_VALUE_DEV_TOKEN
    })
    @Multipart
    @POST(BuildConfig.ENDPOINT + RESOURCE_UPLOAD_FILE)
    Call<JsonObject> uploadImage(
            @Part MultipartBody.Part image
    );
}

package br.com.horizonnew.ubuntudaalegria.manager.network.controller;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.MediaProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.network.ServiceBuilder;
import br.com.horizonnew.ubuntudaalegria.manager.network.services.MediaServices;
import br.com.horizonnew.ubuntudaalegria.manager.network.services.PostServices;
import br.com.horizonnew.ubuntudaalegria.manager.utils.AFImageUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class MediaController {

    private static final String LOG_TAG = "MediaController";

    private MediaController() {}

    public static void uploadImage(@NonNull final String path) {
        MediaServices mediaServices = ServiceBuilder.createService(MediaServices.class);

        File file = new File(path);

        try {
            Bitmap compressedBitmap = AFImageUtils.decodeSampledBitmapFromFile(file.getPath(), 640, 640);
            AFImageUtils.saveBitmapToFile(compressedBitmap, file);
        } catch (IOException e) {
            Log.d(LOG_TAG, "uploadPhoto: ", e);
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("arquivo", file.getName(), requestBody);

        Call<JsonObject> call = mediaServices.uploadImage(
                imageFileBody
        );
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                        JsonObject jsonObject = response.body();

                        MediaProviderBus.getInstance().post(new OnUploadImageEvent(path, jsonObject.get("url").getAsString()));
                } catch (Exception e) {
                    Log.e(LOG_TAG, "onResponse: ", e);

                    MediaProviderBus.getInstance().post(new OnUploadImageFailedEvent(path, e));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure: ", t);

                MediaProviderBus.getInstance().post(new OnUploadImageFailedEvent(path, t));
            }
        });
    }
}

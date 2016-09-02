package br.com.horizonnew.ubuntudaalegria.manager.bus.event.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class OnUploadImageFailedEvent {

    private String localUrl;
    private Throwable throwable;

    public OnUploadImageFailedEvent(@NonNull String localUrl, @Nullable Throwable throwable) {
        super();

        this.localUrl = localUrl;
        this.throwable = throwable;
    }

    @NonNull
    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(@NonNull String localUrl) {
        this.localUrl = localUrl;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}

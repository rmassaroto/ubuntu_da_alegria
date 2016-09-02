package br.com.horizonnew.ubuntudaalegria.manager.bus.event.media;

import android.support.annotation.NonNull;

/**
 * Created by Renan Cardoso Massaroto on 02/09/16.
 */
public class OnUploadImageEvent {

    private String localUrl;
    private String remoteUrl;

    public OnUploadImageEvent(@NonNull String localUrl, @NonNull String remoteUrl) {
        this.localUrl = localUrl;
        this.remoteUrl = remoteUrl;
    }

    @NonNull
    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(@NonNull String localUrl) {
        this.localUrl = localUrl;
    }

    @NonNull
    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(@NonNull String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }
}

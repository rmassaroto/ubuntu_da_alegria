package br.com.horizonnew.ubuntudaalegria.manager.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Renan Cardoso Massaroto on 28/07/16.
 */
public class AFFileUtils {

    public static final int EXTERNAL_FILES_DIR = 0;

    public static File getFile(Context context, int directory) throws IOException {
        String imageFileName = Long.toString(Calendar.getInstance().getTimeInMillis()) + ".png";
        File fileDir;

        switch (directory) {
            case EXTERNAL_FILES_DIR:
                fileDir = context.getExternalFilesDir(null);
                break;
            default:
                fileDir = context.getCacheDir();
        }

        return new File(fileDir, imageFileName);
    }

    public static boolean exists(String fileName) {
        return new File(fileName).exists();
    }
}

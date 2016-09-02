package br.com.horizonnew.ubuntudaalegria.manager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import br.com.horizonnew.ubuntudaalegria.R;

/**
 * Created by Renan Cardoso Massaroto on 06/06/16.
 */
public class PermissionsHelper {

    public static boolean getPermission(final String permission,
                                        final int rationaleRequestMessageResId,
                                        final Activity activity, final int requestCode) {

        if (ContextCompat.checkSelfPermission(activity, permission) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle(R.string.dialog_title_permission_denied)
                        .setMessage(rationaleRequestMessageResId)
                        .setNegativeButton(R.string.button_im_sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (activity instanceof AppCompatActivity) {
                                    ((AppCompatActivity) activity).onRequestPermissionsResult(requestCode, new String[]{permission}, new int[]{PackageManager.PERMISSION_DENIED});
                                }
                            }
                        })
                        .setPositiveButton(R.string.button_try_again, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                            }
                        });

                builder.create().show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }

            return false;
        } else {
            return true;
        }
    }

    public static boolean hasPermission(String permission, Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
}

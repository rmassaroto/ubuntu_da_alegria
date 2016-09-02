package br.com.horizonnew.ubuntudaalegria.manager.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.horizonnew.ubuntudaalegria.R;

/**
 * Created by Renan Cardoso Massaroto on 28/07/16.
 */
public class AFImageProvider {

    public static final String TAG = "AFImageProvider";

    public static final String IMAGE_PATH = "br.com.appfactory.afplatform.afutils.image.AFImageProvider.IMAGE_PATH";

    public static final int REQUEST_GET_IMAGE = 0;
    private static final int REQUEST_PERMISSION_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 3;

    public static final int RESULT_OK = 0;
    public static final int RESULT_NOK = 1;
    public static final int RESULT_CANCEL = 2;

    private Activity mActivity;
    private Fragment mFragment;
    private String mTempPicturePath;

    public AFImageProvider(@NonNull AppCompatActivity activity) {
        mActivity = activity;
    }

    public AFImageProvider(@NonNull Fragment fragment) {
        mFragment = fragment;
    }

    public void displayChoosePictureSourceDialog() {
        if (mActivity != null) {
            displayChoosePictureSourceDialogActivity();
        } else {
            displayChoosePictureSourceDialogFragment();
        }
    }

    private void displayChoosePictureSourceDialogActivity() {
        Context context;

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setItems(
                mActivity.getResources().getStringArray(R.array.picture_sources),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (checkCameraPermissionActivity()) {
                                    takePictureActivity();
                                }
                                break;
                            case 1:
                                getPictureFromGalleryActivity();
                                break;
                        }
                    }
                }
        );

        builder.create().show();
    }

    private void displayChoosePictureSourceDialogFragment() {
        Context context;

        AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getContext());
        builder.setItems(
                mFragment.getResources().getStringArray(R.array.picture_sources),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (checkCameraPermissionFragment()) {
                                    takePictureFragment();
                                }
                                break;
                            case 1:
                                getPictureFromGalleryFragment();
                                break;
                        }
                    }
                }
        );

        builder.create().show();
    }

    private boolean checkCameraPermissionActivity() {
        if (!PermissionsHelper.hasPermission(Manifest.permission.CAMERA, mActivity)) {
            ActivityCompat.requestPermissions(
                    mActivity,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA
            );

            return false;
        } else {
            return true;
        }
    }

    private boolean checkCameraPermissionFragment() {
        if (!PermissionsHelper.hasPermission(Manifest.permission.CAMERA, mFragment.getActivity())) {
            mFragment.requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA
            );

            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mActivity != null) {
                        takePictureActivity();
                    } else {
                        takePictureFragment();
                    }
                }

                break;
        }
    }

    private void takePictureActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {

            try {
                File pictureFile = AFFileUtils.getFile(mActivity, AFFileUtils.EXTERNAL_FILES_DIR);

                if (pictureFile != null) {
                    mTempPicturePath = pictureFile.getAbsolutePath();

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
                    mActivity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                //TODO: Handle error
            }

        } else {
            // The device cannot take pictures
            //TODO: Handle situation
        }
    }

    private void takePictureFragment() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (intent.resolveActivity(mFragment.getContext().getPackageManager()) != null) {

            try {
                File pictureFile = AFFileUtils.getFile(mFragment.getContext(), AFFileUtils.EXTERNAL_FILES_DIR);

                if (pictureFile != null) {
                    mTempPicturePath = pictureFile.getAbsolutePath();

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
                    mFragment.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                //TODO: Handle error
            }

        } else {
            // The device cannot take pictures
            mFragment.onActivityResult(REQUEST_GET_IMAGE, RESULT_NOK, null);
        }
    }

    private void getPictureFromGalleryActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivity.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    private void getPictureFromGalleryFragment() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mFragment.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mActivity != null) {
            onActivityResultActivity(requestCode, resultCode, data);
        } else {
            onActivityResultFragment(requestCode, resultCode, data);
        }
    }

    private void onActivityResultActivity(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode != Activity.RESULT_OK) {
                    deleteTempFileActivity();
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        ParcelFileDescriptor parcelFileDescriptor =
                                mActivity.getContentResolver().openFileDescriptor(data.getData(), "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                            parcelFileDescriptor.close();

                            File file = AFFileUtils.getFile(mActivity, AFFileUtils.EXTERNAL_FILES_DIR);

                            if (file != null) {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.close();

                                mTempPicturePath = file.getAbsolutePath();
                            } else {
                                deleteTempFileActivity();
                            }
                        } catch (OutOfMemoryError outOfMemoryError) {
                            Log.e(TAG, "onActivityResult: ", outOfMemoryError);
                            deleteTempFileActivity();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onActivityResult: ", e);
                        deleteTempFileActivity();
                    }
                } else {
                    deleteTempFileActivity();
                }
        }

        if (mTempPicturePath != null) {
            Intent intent = new Intent();
            intent.putExtra(IMAGE_PATH, mTempPicturePath);

            try {
                Method method = mActivity.getClass().getDeclaredMethod("onActivityResult", int.class, int.class, Intent.class);
                method.setAccessible(true);
                method.invoke(mActivity, REQUEST_GET_IMAGE, RESULT_OK, intent);

//                mActivity.onActivityResult(RESULT_OK, intent);
            } catch (Exception e) {
                Log.e(TAG, "onActivityResultActivity: ", e);
            }
        } else {
            try {
                Method method = mActivity.getClass().getDeclaredMethod("onActivityResult", int.class, int.class, Intent.class);
                method.setAccessible(true);
                method.invoke(mActivity, REQUEST_GET_IMAGE, RESULT_NOK, null);

//                mActivity.onActivityResult(RESULT_OK, intent);
            } catch (Exception e) {
                Log.e(TAG, "onActivityResultActivity: ", e);
            }

//            mActivity.setResult(RESULT_NOK, null);
        }
    }

    private void onActivityResultFragment(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode != Activity.RESULT_OK) {
                    deleteTempFileFragment();
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        ParcelFileDescriptor parcelFileDescriptor =
                                mFragment.getContext().getContentResolver().openFileDescriptor(data.getData(), "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                            parcelFileDescriptor.close();

                            File file = AFFileUtils.getFile(mFragment.getContext(), AFFileUtils.EXTERNAL_FILES_DIR);

                            if (file != null) {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.close();

                                mTempPicturePath = file.getAbsolutePath();
                            } else {
                                deleteTempFileFragment();
                            }
                        } catch (OutOfMemoryError outOfMemoryError) {
                            Log.e(TAG, "onActivityResult: ", outOfMemoryError);
                            deleteTempFileFragment();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onActivityResult: ", e);
                        deleteTempFileFragment();
                    }
                } else {
                    deleteTempFileFragment();
                }
        }

        if (mTempPicturePath != null) {
            Intent intent = new Intent();
            intent.putExtra(IMAGE_PATH, mTempPicturePath);

            mFragment.onActivityResult(REQUEST_GET_IMAGE, RESULT_OK, intent);
        } else {
            mFragment.onActivityResult(REQUEST_GET_IMAGE, RESULT_NOK, null);
        }
    }

    private void deleteTempFileActivity() {
        try {
            File file = AFFileUtils.getFile(mActivity, AFFileUtils.EXTERNAL_FILES_DIR);

            if (!file.delete()) {
                Log.d(TAG, "deleteTempFile: Could not delete temporary file at path '" + mTempPicturePath + "'");
            }
        } catch (IOException e) {
            Log.e(TAG, "deleteTempFile: Could not delete temporary file at path '" + mTempPicturePath + "' due to error:\n", e);
        }

        mTempPicturePath = null;
    }

    private void deleteTempFileFragment() {
        try {
            File file = AFFileUtils.getFile(mFragment.getContext(), AFFileUtils.EXTERNAL_FILES_DIR);

            if (!file.delete()) {
                Log.d(TAG, "deleteTempFile: Could not delete temporary file at path '" + mTempPicturePath + "'");
            }
        } catch (IOException e) {
            Log.e(TAG, "deleteTempFile: Could not delete temporary file at path '" + mTempPicturePath + "' due to error:\n", e);
        }

        mTempPicturePath = null;
    }
}

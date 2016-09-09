package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnUpdateUserProfileEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnUpdateUserProfileFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.MediaProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.PostProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.image.PicassoSingleton;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.MediaController;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController;
import br.com.horizonnew.ubuntudaalegria.manager.utils.AFImageProvider;
import br.com.horizonnew.ubuntudaalegria.model.User;

public class ProfileActivity extends BaseActivity {

    private static final String LOG_TAG = "ProfileActivity";

    public static final String ARG_IMAGE_PATH = "br.com.horizonnew.ubuntudaalegria.view.activity.ProfileActivity.ARG_IMAGE_PATH";

    private ImageView mProfileImageView;
    private EditText mNameEditText, mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;

    private AFImageProvider mImageProvider;

    private User mUser;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mProfileImageView = (ImageView) findViewById(R.id.activity_profile_image_view);

        mNameEditText = (EditText) findViewById(R.id.activity_profile_name_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.activity_profile_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.activity_profile_password_edit_text);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.activity_profile_confirm_password_text_view);

        mUser = UserController.getLoggedUser(this);

        if (mUser != null) {
            if (mUser.getPictureUrl() != null && !mUser.getPictureUrl().trim().isEmpty()) {
                PicassoSingleton.getInstance(this)
                        .load(mUser.getPictureUrl())
                        .fit()
                        .centerCrop()
                        .into(mProfileImageView);
            }

            mNameEditText.setText(mUser.getName());
            mEmailEditText.setText(mUser.getEmail());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            MediaProviderBus.getInstance().register(this);
        } catch (Exception e) {
            //Do nothing
        }

        try {
            UserProviderBus.getInstance().register(this);
        } catch (Exception e) {
            //Do nothing
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            MediaProviderBus.getInstance().unregister(this);
        } catch (Exception e) {
            //Do nothing
        }

        try {
            UserProviderBus.getInstance().unregister(this);
        } catch (Exception e) {
            //Do nothing
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_picture) {
            addPicture();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_IMAGE_PATH, mImagePath);
    }

    private void addPicture() {
        mImageProvider = new AFImageProvider(this);
        mImageProvider.displayChoosePictureSourceDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AFImageProvider.REQUEST_GET_IMAGE && resultCode == AFImageProvider.RESULT_OK) {

            CropImage.activity(Uri.fromFile(new File(data.getStringExtra(AFImageProvider.IMAGE_PATH))))
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .start(this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                mImagePath = resultUri.getPath();
                PicassoSingleton.getInstance(this)
                        .load("file://" + mImagePath)
                        .fit()
                        .centerCrop()
                        .error(R.drawable.image_logo)
                        .into(mProfileImageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

                Log.e(LOG_TAG, "onActivityResult: ", error);
            }
        } else if (requestCode != AFImageProvider.REQUEST_GET_IMAGE) {
            mImageProvider.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mImageProvider.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean validateFields() {
        boolean result = true;

        if (mNameEditText.getText().toString().trim().isEmpty()) {
            mNameEditText.setError(getString(R.string.error_field_cannot_be_empty));
            result = false;
        }

        if (mEmailEditText.getText().toString().trim().isEmpty()) {
            mEmailEditText.setError(getString(R.string.error_field_cannot_be_empty));
            result = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString().trim()).matches()) {
            result = false;

            mEmailEditText.setError("Este e-mail não é um e-mail valido!");
        }

        return result;
    }

    private void updateProfile() {
        if (validateFields()) {
            if (mImagePath != null)
                uploadPhoto();
            else
                updateUser(null);
        }
    }

    private void uploadPhoto() {
        final User loggedUser = UserController.getLoggedUser(this);

        if (loggedUser != null) {
            showProgressDialog(
                    R.string.dialog_title_wait,
                    R.string.message_uploading_image_please_wait
            );

            MediaController.uploadImage(mImagePath);
        }
    }

    @Subscribe
    public void onUploadPhotoFailed(OnUploadImageFailedEvent event) {
        if (event.getLocalUrl().equals(mImagePath)) {
            dismissDialogs();

            showAlertDialog(
                    R.string.dialog_title_error,
                    R.string.error_message_could_not_upload_image_want_to_try_again,
                    R.string.button_cancel, null,
                    R.string.button_try_again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uploadPhoto();
                        }
                    }
            );
        }
    }

    @Subscribe
    public void onUploadPhoto(OnUploadImageEvent event) {
        if (event.getLocalUrl().equals(mImagePath)) {
            updateUser(event.getRemoteUrl());
        }
    }

    private void updateUser(@Nullable String url) {
        showProgressDialog(
                R.string.dialog_title_wait,
                R.string.message_updating_user
        );

        if (url != null) {
            mUser.setPictureUrl(url);
        }

        UserController.updateUserProfile(this, mUser);
    }

    @Subscribe
    public void onUpdateUserProfileFailed(OnUpdateUserProfileFailedEvent event) {
        if (event.getUser().getId() == mUser.getId()) {
            dismissDialogs();

            showAlertDialog(
                    R.string.dialog_title_error,
                    R.string.error_message_could_not_update_user_profile_want_to_try_again,
                    R.string.button_cancel, null,
                    R.string.button_try_again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateProfile();
                        }
                    }
            );
        }
    }

    @Subscribe
    public void onUpdateUserProfile(OnUpdateUserProfileEvent event) {
        if (event.getUser().getId() == mUser.getId()) {
            Toast.makeText(this, R.string.message_user_profile_successfully_updated, Toast.LENGTH_LONG).show();

            finish();
        }
    }
}

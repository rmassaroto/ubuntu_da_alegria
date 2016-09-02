package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.feed.OnRefreshFeedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.media.OnUploadImageFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.post.OnUploadPostEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.post.OnUploadPostFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.MediaProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.PostProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.image.PicassoSingleton;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.MediaController;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.PostController;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController;
import br.com.horizonnew.ubuntudaalegria.manager.utils.AFImageProvider;
import br.com.horizonnew.ubuntudaalegria.model.Post;
import br.com.horizonnew.ubuntudaalegria.model.User;

public class CreatePostActivity extends BaseActivity {

    private static final String LOG_TAG = "CreatePostActivity";

    public static final String ARG_TYPE = "br.com.horizonnew.ubuntudaalegria.view.activity.CreatePostActivity.ARG_TYPE";
    public static final String ARG_IMAGE_PATH = "br.com.horizonnew.ubuntudaalegria.view.activity.CreatePostActivity.ARG_IMAGE_PATH";

    private NestedScrollView mScrollView;
    private ImageView mPhotoImageView;
    private EditText mYoutubeUrl, mTitleEditText, mTextEditText;
    private Switch mCampaignSwitch;

    private FloatingActionButton mFab;

    private AFImageProvider mImageProvider;
    private int mType;

    private String mImagePath;
    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mType = savedInstanceState.getInt(ARG_TYPE, Post.TYPE_TEXT);
            mImagePath = savedInstanceState.getString(ARG_IMAGE_PATH, null);
        } else {
            if (getIntent().hasExtra(ARG_TYPE)) {
                mType = getIntent().getIntExtra(ARG_TYPE, Post.TYPE_TEXT);
            } else {
                throw new RuntimeException("Could not find type in intent extras");
            }
        }

        mScrollView = (NestedScrollView) findViewById(R.id.activity_create_post_scroll_view);

        mYoutubeUrl = (EditText) findViewById(R.id.activity_create_post_url_edit_text);
        mPhotoImageView = (ImageView) findViewById(R.id.activity_create_post_image_view);
        mTitleEditText = (EditText) findViewById(R.id.activity_create_post_title_edit_text);
        mTextEditText = (EditText) findViewById(R.id.activity_create_post_text_edit_text);
        mCampaignSwitch = (Switch) findViewById(R.id.activity_create_post_campaign_switch);

        switch (mType) {
            case Post.TYPE_TEXT:
                mYoutubeUrl.setVisibility(View.GONE);
                mPhotoImageView.setVisibility(View.GONE);
                break;
            case Post.TYPE_IMAGE:
                mYoutubeUrl.setVisibility(View.GONE);
                break;
            case Post.TYPE_VIDEO:
                mPhotoImageView.setVisibility(View.GONE);
                break;
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });
        mFab.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY > oldScrollY) {
                            mFab.hide();
                        } else {
                            mFab.show();
                        }
                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mType == Post.TYPE_IMAGE) {
            getMenuInflater().inflate(R.menu.menu_create_post, menu);

            return true;
        }

        return super.onCreateOptionsMenu(menu);
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
            PostProviderBus.getInstance().register(this);
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
            PostProviderBus.getInstance().unregister(this);
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
        outState.putInt(ARG_TYPE, mType);
        outState.putString(ARG_TYPE, mImagePath);
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
                        .error(R.mipmap.ic_launcher)
                        .into(mPhotoImageView);

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

        switch (mType) {
            case Post.TYPE_IMAGE:
                if (mImagePath != null && mImagePath.trim().isEmpty()) {
                    result = false;
                } else if (mImagePath == null) {
                    result = false;
                }

                break;
        }

        if (mTitleEditText.getText().toString().trim().isEmpty()) {
            mTitleEditText.setError(getString(R.string.error_field_cannot_be_empty));
            result = false;
        }

        if (mTextEditText.getText().toString().trim().isEmpty()) {
            mTextEditText.setError(getString(R.string.error_field_cannot_be_empty));
            result = false;
        }

        return result;
    }

    private void createPost() {
        if (validateFields()) {
            if (mType == Post.TYPE_IMAGE) {
                uploadPhoto();
            } else if (mType == Post.TYPE_VIDEO) {
                uploadPost(mYoutubeUrl.getText().toString().trim());
            }
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
            uploadPost(event.getRemoteUrl());
        }
    }

    private void uploadPost(@Nullable String url) {
        showProgressDialog(
                R.string.dialog_title_wait,
                R.string.message_uploading_post
        );

        mPost = new Post();

        switch (mType) {
            case Post.TYPE_VIDEO:
                mPost.setUrl(mYoutubeUrl.getText().toString().trim());
                break;
            case Post.TYPE_IMAGE:
                mPost.setUrl(url);
                break;
            case Post.TYPE_TEXT:
        }

        mPost.setType(mType);

        mPost.setTitle(mTitleEditText.getText().toString().trim());
        mPost.setText(mTextEditText.getText().toString().trim());
        mPost.setCampaign(mCampaignSwitch.isChecked());
        mPost.setActive(true);

        //TODO: Replace for the real user
        mPost.setUser(new User());

        PostController.uploadPost(mPost);
    }

    @Subscribe
    public void onUploadPostFailed(OnUploadPostFailedEvent event) {
        if (event.getLocalPost() == mPost) {
            dismissDialogs();

            showAlertDialog(
                    R.string.dialog_title_error,
                    R.string.error_message_could_not_upload_post_want_to_try_again,
                    R.string.button_cancel, null,
                    R.string.button_try_again, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uploadPost(mImagePath);
                        }
                    }
            );
        }
    }

    @Subscribe
    public void onUploadPost(OnUploadPostEvent event) {
        if (event.getLocalPost() == mPost) {
            Toast.makeText(this, R.string.message_post_successfully_created, Toast.LENGTH_LONG).show();

            UserProviderBus.getInstance().post(new OnRefreshFeedEvent());
            finish();
        }
    }
}
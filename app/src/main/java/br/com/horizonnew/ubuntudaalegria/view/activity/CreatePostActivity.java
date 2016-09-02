package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.image.PicassoSingleton;
import br.com.horizonnew.ubuntudaalegria.manager.network.ServiceBuilder;
import br.com.horizonnew.ubuntudaalegria.manager.network.services.PostServices;
import br.com.horizonnew.ubuntudaalegria.manager.utils.AFImageProvider;
import br.com.horizonnew.ubuntudaalegria.manager.utils.AFImageUtils;
import br.com.horizonnew.ubuntudaalegria.model.Post;
import br.com.horizonnew.ubuntudaalegria.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends BaseActivity {

    private static final String LOG_TAG = "CreatePostActivity";

    public static final String ARG_TYPE = "br.com.horizonnew.ubuntudaalegria.view.activity.CreatePostActivity.ARG_TYPE";
    public static final String ARG_IMAGE_PATH = "br.com.horizonnew.ubuntudaalegria.view.activity.CreatePostActivity.ARG_IMAGE_PATH";

    private ImageView mPhotoImageView;
    private EditText mYoutubeUrl, mTitleEditText, mTextEditText;
    private Switch mCampaignSwitch;

    private AFImageProvider mImageProvider;
    private int mType;

    private String mImagePath;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
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
            }
        }
    }

    private void uploadPhoto() {
        final User loggedUser = User.getLoggedUser(this);

        if (loggedUser != null) {
            final PostServices postServices = ServiceBuilder.createService(PostServices.class);

            File file = new File(mImagePath);

            try {
                Bitmap compressedBitmap = AFImageUtils.decodeSampledBitmapFromFile(file.getPath(), 640, 640);
                AFImageUtils.saveBitmapToFile(compressedBitmap, file);
            } catch (IOException e) {
                Log.d(LOG_TAG, "uploadPhoto: ", e);
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("arquivo", file.getName(), requestBody);

            Call<JsonObject> call = postServices.uploadImage(
                    imageFileBody
            );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response != null && response.body() != null) {
                        JsonObject jsonObject = response.body();

                        if (jsonObject.has("url")) {
                            uploadPostJson(postServices, jsonObject.get("url").getAsString());
                        } else {
                            //TODO: handle this
                        }
                    } else {
                        //TODO: handle this
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    //TODO: Handle error
                    Log.e(LOG_TAG, "onFailure: ", t);
                }
            });
        }
    }

    private void uploadPostJson(PostServices postServices, String url) {
        Post post = new Post();
        switch (mType) {
            case Post.TYPE_VIDEO:
                post.setUrl(mYoutubeUrl.getText().toString().trim());
                post.setType(Post.TYPE_VIDEO);
                break;
            case Post.TYPE_IMAGE:
                post.setUrl(url);
                post.setType(Post.TYPE_IMAGE);
                break;
        }

        post.setTitle(mTitleEditText.getText().toString().trim());
        post.setText(mTextEditText.getText().toString().trim());
        post.setCampaign(mCampaignSwitch.isChecked());

//        Call<JsonObject> call = postServices.createPost(post.to);
    }
}
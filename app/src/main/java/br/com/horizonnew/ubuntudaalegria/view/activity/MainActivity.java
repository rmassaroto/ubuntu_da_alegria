package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.OnGetUserFeedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.model.User;
import br.com.horizonnew.ubuntudaalegria.view.fragment.FeedListFragment;

public class MainActivity extends BaseActivity {

    private static final String LOG_TAG = "MainActivity";

    private static final int REQUEST_CREATE_POST = 0;

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loggedUser = User.getLoggedUser(this);

            if (loggedUser != null) {
                FeedListFragment fragment = FeedListFragment.newInstance(loggedUser);
                showFragment(fragment, FeedListFragment.TAG, false);
            } else {
                //TODO: Handle this
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePostDialog();
            }
        });
    }

    private void showFragment(@NonNull Fragment fragment, @Nullable String fragmentTag,
                              boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_container, fragment, fragmentTag);

        if (addToBackStack)
            transaction.addToBackStack(fragmentTag);

        transaction.commit();
    }

    private void showCreatePostDialog() {
        showAlertDialog(
                R.string.dialog_title_post_type,
                R.array.activity_main_post_types,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showCreatePost(which);
                    }
                }
        );
    }

    private void showCreatePost(int type) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(CreatePostActivity.ARG_TYPE, type);
        startActivityForResult(intent, REQUEST_CREATE_POST);
    }
}

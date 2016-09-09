package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnUpdateUserProfileEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.MediaProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.image.PicassoSingleton;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController;
import br.com.horizonnew.ubuntudaalegria.model.User;
import br.com.horizonnew.ubuntudaalegria.view.fragment.FeedListFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = "MainActivity";

    private static final int REQUEST_CREATE_POST = 0;

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;

    private ImageView mProfileImageView;
    private TextView mUserNameTextView, mUserEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProfileImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image);
        mUserNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_1);
        mUserEmailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_2);

        if (savedInstanceState == null) {
            FeedListFragment fragment = FeedListFragment.newInstance(UserController.getLoggedUser(this));
            showFragment(fragment, FeedListFragment.TAG, false);
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePostDialog();
            }
        });
        mFab.hide();

        updateNavigationDrawer();

        try {
            UserProviderBus.getInstance().register(this);
        } catch (Exception e) {
            //Do nothing
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            UserProviderBus.getInstance().unregister(this);
        } catch (Exception e) {
            //Do nothing
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void updateNavigationDrawer() {
        User user = UserController.getLoggedUser(this);
        if (user != null) {
            if (user.getPictureUrl() != null && !user.getPictureUrl().trim().isEmpty() && !user.getPictureUrl().equals("null")) {
                PicassoSingleton.getInstance(this)
                        .load(user.getPictureUrl())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.image_logo)
                        .into(mProfileImageView);
            }

            mUserNameTextView.setText(user.getName());
            mUserEmailTextView.setText(user.getEmail());

            switch (user.getGroup()) {
                case 0:
                    mFab.show();
                    break;
                case 1:
                    break;
                case 2:
                    mFab.show();
                    break;
            }
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            UserController.logUserOut(this);
            updateNavigationDrawer();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        showCreatePost(which + 1);
                    }
                }
        );
    }

    private void showCreatePost(int type) {
        Intent intent = new Intent(this, CreatePostActivity.class);
        intent.putExtra(CreatePostActivity.ARG_TYPE, type);
        intent.putExtra(CreatePostActivity.ARG_MODE, CreatePostActivity.MODE_CREATE_POST);
        startActivityForResult(intent, REQUEST_CREATE_POST);
    }

    @Subscribe
    public void onUpdateUserProfile(OnUpdateUserProfileEvent event) {
        updateNavigationDrawer();
    }
}

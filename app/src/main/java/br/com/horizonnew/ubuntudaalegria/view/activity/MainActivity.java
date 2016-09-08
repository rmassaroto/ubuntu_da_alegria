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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController;
import br.com.horizonnew.ubuntudaalegria.model.User;
import br.com.horizonnew.ubuntudaalegria.view.dialog.ContactFragmentDialog;
import br.com.horizonnew.ubuntudaalegria.view.fragment.FeedListFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String LOG_TAG = "MainActivity";

    private static final int REQUEST_CREATE_POST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FeedListFragment fragment = FeedListFragment.newInstance(UserController.getLoggedUser(this));
            showFragment(fragment, FeedListFragment.TAG, false);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePostDialog();
            }
        });
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

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

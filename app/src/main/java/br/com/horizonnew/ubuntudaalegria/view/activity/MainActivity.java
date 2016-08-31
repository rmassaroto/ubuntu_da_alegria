package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.view.fragment.FeedListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FeedListFragment fragment = FeedListFragment.newInstance();
            showFragment(fragment, FeedListFragment.TAG, false);
        }
    }

    private void showFragment(@NonNull Fragment fragment, @Nullable String fragmentTag,
                              boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_container, fragment, fragmentTag);

        if (addToBackStack)
            transaction.addToBackStack(fragmentTag);

        transaction.commit();
    }
}

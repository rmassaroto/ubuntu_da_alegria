package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                proceedWithAppLaunch();
            }
        }, SPLASH_DURATION);
    }

    private void proceedWithAppLaunch() {
        Intent intent;

        if (UserController.getLoggedUser(this) == null) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}

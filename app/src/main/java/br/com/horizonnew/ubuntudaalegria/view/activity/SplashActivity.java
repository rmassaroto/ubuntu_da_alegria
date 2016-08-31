package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.horizonnew.ubuntudaalegria.R;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showHome();
            }
        }, SPLASH_DURATION);
    }

    private void showHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

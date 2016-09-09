package br.com.horizonnew.ubuntudaalegria.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import br.com.horizonnew.ubuntudaalegria.R;
import br.com.horizonnew.ubuntudaalegria.manager.base.BaseActivity;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnLogUserInEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnLogUserInFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnSignUserUpEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.event.user.OnSignUserUpFailedEvent;
import br.com.horizonnew.ubuntudaalegria.manager.bus.provider.UserProviderBus;
import br.com.horizonnew.ubuntudaalegria.manager.network.controller.UserController;

public class LoginActivity extends BaseActivity {

    private static final String ARG_MODE = "br.com.horizonnew.ubuntudaalegria.view.activity.LoginActivity.ARG_MODE";

    private static final int MODE_LOGIN = 0;
    private static final int MODE_SIGN_UP = 1;

    private RelativeLayout mLoginRelativeLayout, mSignUpRelativeLayout;

    private EditText mEmailEditText, mPasswordEditText, mNameEditText;
    private Button mLoginButton, mGuestButton, mSignUpButton;
    private TextView mSignUpTextView;

    private int mMode = MODE_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginRelativeLayout = (RelativeLayout) findViewById(R.id.activity_login_login_relative_layout);
        mSignUpRelativeLayout = (RelativeLayout) findViewById(R.id.activity_login_sign_up_relative_layout);

        mEmailEditText = (EditText) findViewById(R.id.activity_login_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.activity_login_password_edit_text);
        mNameEditText = (EditText) findViewById(R.id.activity_login_name_edit_text);

        mLoginButton = (Button) findViewById(R.id.activity_login_log_in_button);
        mGuestButton = (Button) findViewById(R.id.activity_login_guest_button);
        mSignUpButton = (Button) findViewById(R.id.activity_login_sign_up_button);

        mSignUpTextView = (TextView) findViewById(R.id.activity_login_sign_up_text_view);

        setListeners();
        prepareActivityState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        UserProviderBus.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        UserProviderBus.getInstance().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mMode == MODE_LOGIN) {
            super.onBackPressed();
        } else {
            toggleMode();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_MODE, mMode);
    }

    private void setListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserIn();
            }
        });

        mGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestLogIn();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUserUp();
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUp();
            }
        });
    }

    private void prepareActivityState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMode = savedInstanceState.getInt(ARG_MODE, MODE_LOGIN);
        }

        switch (mMode) {
            case MODE_LOGIN:
                showLoginButtons();
                break;

            case MODE_SIGN_UP:
                showSignUpButtons();
                break;
        }
    }

    private void toggleMode() {
        switch (mMode) {
            case MODE_LOGIN:
                mMode = MODE_SIGN_UP;

                showSignUpButtons();

                break;
            case MODE_SIGN_UP:
                mMode = MODE_LOGIN;

                showLoginButtons();

                break;
        }
    }

    private void showLoginButtons() {
        mLoginRelativeLayout.setVisibility(View.VISIBLE);
        mSignUpRelativeLayout.setVisibility(View.GONE);
    }

    private void showSignUpButtons() {
        mLoginRelativeLayout.setVisibility(View.GONE);
        mSignUpRelativeLayout.setVisibility(View.VISIBLE);
    }

    private boolean areFieldsValid() {
        boolean result = true;

        String email = mEmailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            result = false;

            mEmailEditText.setError("Este campo não pode estar vazio!");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = false;

            mEmailEditText.setError("Este e-mail não é um e-mail valido!");
        }

        String password = mPasswordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            result = false;

            mPasswordEditText.setError("Este campo não pode estar vazio!");
        } else if (password.length() < 6) {
            result = false;

            mPasswordEditText.setError("A senha deve ter ao menos 6 caracteres!");
        }

        if (mMode == MODE_SIGN_UP) {
            String name = mNameEditText.getText().toString().trim();
            if (name.isEmpty()) {
                result = false;

                mNameEditText.setError("Este campo não pode estar vazio!");
            }
        }

        return result;
    }

    private void logUserIn() {
        if (areFieldsValid()) {
            showProgressDialog(
                    R.string.dialog_title_wait,
                    R.string.message_login_doing_log_user_in_please_wait
            );

            UserController.logUserIn(
                    this,
                    mEmailEditText.getText().toString(),
                    mPasswordEditText.getText().toString()
            );
        }
    }

    @Subscribe
    public void onLogUserIn(OnLogUserInEvent event) {
        dismissDialogs();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onLogUserInFailed(OnLogUserInFailedEvent event) {
        dismissDialogs();

        showAlertDialog(
                R.string.dialog_title_error,
                R.string.error_message_could_not_log_user_in_want_to_try_again,
                R.string.button_cancel, null,
                R.string.button_try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logUserIn();
                    }
                }
        );
    }

    private void guestLogIn() {
        //TODO: Log guest in
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void signUserUp() {
        if (areFieldsValid()) {
            showProgressDialog(
                    R.string.dialog_title_wait,
                    R.string.message_signing_user_up_please_wait
            );

            UserController.signUserUp(
                    this,
                    mEmailEditText.getText().toString(),
                    mPasswordEditText.getText().toString(),
                    mNameEditText.getText().toString()
            );
        }
    }

    @Subscribe
    public void onSignUserUp(OnSignUserUpEvent event) {
        dismissDialogs();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onSignUserUpFailed(OnSignUserUpFailedEvent event) {
        dismissDialogs();

        showAlertDialog(
                R.string.dialog_title_error,
                R.string.error_message_could_not_sign_user_up_want_to_try_again,
                R.string.button_cancel, null,
                R.string.button_try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signUserUp();
                    }
                }
        );
    }

    private void showSignUp() {
        toggleMode();
    }
}

package br.com.horizonnew.ubuntudaalegria.manager.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by renan on 31/08/16.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    protected void dismissDialogs() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mAlertDialog = null;

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = null;
    }

    protected void showAlertDialog(@StringRes int titleId, @ArrayRes int itemsId,
                                   @Nullable DialogInterface.OnClickListener itemsListener) {
        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setItems(itemsId, itemsListener)
                .create();

        mAlertDialog.show();
    }

    protected void showAlertDialog(@StringRes int titleId, @StringRes int messageId,
                                   @StringRes int positiveButtonTitleId, @Nullable DialogInterface.OnClickListener positiveListener) {
        dismissDialogs();

        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setMessage(getString(messageId))
                .setPositiveButton(positiveButtonTitleId, positiveListener)
                .create();

        mAlertDialog.show();
    }

    protected void showAlertDialog(@StringRes int titleId, @StringRes int messageId,
                                   @StringRes int negativeButtonTitleId, @Nullable DialogInterface.OnClickListener negativeListener,
                                   @StringRes int positiveButtonTitleId, @Nullable DialogInterface.OnClickListener positiveListener) {
        dismissDialogs();

        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setMessage(getString(messageId))
                .setNegativeButton(negativeButtonTitleId, negativeListener)
                .setPositiveButton(positiveButtonTitleId, positiveListener)
                .create();

        mAlertDialog.show();
    }

    protected void showProgressDialog(@StringRes int titleId, @StringRes int messageId) {
        dismissDialogs();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(titleId);
        mProgressDialog.setMessage(getString(messageId));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);

        mProgressDialog.show();
    }
}

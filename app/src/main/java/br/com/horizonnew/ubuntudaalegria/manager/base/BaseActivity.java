package br.com.horizonnew.ubuntudaalegria.manager.base;

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

    private AlertDialog mAlertDialog;

    protected void dismissDialogs() {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();

        mAlertDialog = null;
    }

    protected void showAlertDialog(@StringRes int titleId, @ArrayRes int itemsId,
                                   @Nullable DialogInterface.OnClickListener itemsListener) {
        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setItems(itemsId, itemsListener)
                .create();

        mAlertDialog.show();
    }
}

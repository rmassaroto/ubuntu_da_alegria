package br.com.horizonnew.ubuntudaalegria.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import br.com.horizonnew.ubuntudaalegria.R;

/**
 * Created by renan on 07/09/16.
 */
public class ContactFragmentDialog extends DialogFragment {

    private EditText mEmailEditText, mCommentEditText;

    private ContactFragmentDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_contact, null);

        mEmailEditText = (EditText) view.findViewById(R.id.dialog_email_edit_text);
        mCommentEditText = (EditText) view.findViewById(R.id.dialog_comment_edit_text);

        builder.setView(view)
                .setTitle(R.string.dialog_title_contact)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null)
                            mListener.onPositiveButtonClick(
                                    mEmailEditText.getText().toString(),
                                    mCommentEditText.getText().toString()
                            );
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null)
                            mListener.onNegativeButtonClick();
                    }
                });
        return builder.create();
    }

    public ContactFragmentDialogListener getListener() {
        return mListener;
    }

    public void setListener(ContactFragmentDialogListener listener) {
        this.mListener = listener;
    }

    public interface ContactFragmentDialogListener {
        void onNegativeButtonClick();
        void onPositiveButtonClick(String email, String comment);
    }
}

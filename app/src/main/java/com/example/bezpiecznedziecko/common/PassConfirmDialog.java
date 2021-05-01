package com.example.bezpiecznedziecko.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.parentChildEdit;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PassConfirmDialog extends AppCompatDialogFragment {
    private TextInputEditText mCurrentPassword;
    private TextInputLayout passwordInputLayout;
    private PassConfirmDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.parent_child_edit_confirm,null);

        mCurrentPassword = (TextInputEditText) mView.findViewById(R.id.child_edit_confirm_currentPassword);
        passwordInputLayout  = (TextInputLayout) mView.findViewById(R.id.child_edit_confirm_currentPasswordLayout);

        mCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBuilder.setView(mView);
        AlertDialog confirmDialog = mBuilder.create();
        confirmDialog.setCanceledOnTouchOutside(false);

        Button mConfirmButton = (Button) mView.findViewById(R.id.child_edit_confirm_confirmButton);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = mCurrentPassword.getText().toString();
                listener.confirmCorrectPassword(confirmDialog, passwordInputLayout, enteredPassword);
            }
        });
        Button mCancelButton = (Button) mView.findViewById(R.id.child_edit_confirm_cancelButton);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });

        return confirmDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PassConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement PassConfirmDialogListener");
        }
    }

    public interface PassConfirmDialogListener
    {
        void confirmCorrectPassword(AlertDialog confirmDialog, TextInputLayout passwordInputLayout, String enteredPlainPassword);
    }
}

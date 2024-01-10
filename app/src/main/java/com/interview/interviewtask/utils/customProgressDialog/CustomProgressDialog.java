package com.interview.interviewtask.utils.customProgressDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.interview.interviewtask.R;


public class CustomProgressDialog extends Dialog {

    public ProgressBar progressBar;

    public CustomProgressDialog(@NonNull Context context) {
        super(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        progressBar = dialogView.findViewById(R.id.progressBar);


        setContentView(dialogView);
        setCancelable(false);

    }



    public void showProgressDialog() {
        if (!isShowing()) {
            show();
        }
    }

    public void hideProgressDialog() {
        if (isShowing()) {
            dismiss();
        }
    }
}

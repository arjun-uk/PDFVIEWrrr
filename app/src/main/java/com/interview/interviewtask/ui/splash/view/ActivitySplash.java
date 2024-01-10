package com.interview.interviewtask.ui.splash.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.interview.interviewtask.R;
import com.interview.interviewtask.databinding.ActivitySplashBinding;
import com.interview.interviewtask.ui.home.view.ActivityHome;

public class ActivitySplash extends AppCompatActivity {

    public Context context;
    public ActivitySplashBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = ActivitySplash.this;
        new Handler().postDelayed(() -> startActivity(new Intent(context, ActivityHome.class)),2000);

    }
}
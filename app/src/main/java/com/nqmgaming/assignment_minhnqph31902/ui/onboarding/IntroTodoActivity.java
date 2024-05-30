package com.nqmgaming.assignment_minhnqph31902.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nqmgaming.assignment_minhnqph31902.R;

public class IntroTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_todo);

        // Set delay 3s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Start new activity
                Intent intent = new Intent(IntroTodoActivity.this, WelcomeActivity.class);
                startActivity(intent);

                // Finish current activity
                finish();

            }
        }, 3000);
    }
}

package com.nqmgaming.assignment_minhnqph31902.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.ui.authentication.LoginActivity;
import com.nqmgaming.assignment_minhnqph31902.ui.authentication.RegisterActivity;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

public class GetStartActivity extends AppCompatActivity {

    //declare variables
    Button btnLogin, btnRegister;
    CheckBox cbAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check login state
        UserPreferences userPreferences = new UserPreferences(this);

        //if login, go to MainActivity
        if (userPreferences.isLogin()) {

            startActivity(new Intent(GetStartActivity.this, MainActivity.class));
            finish();
            return;

        }
        setContentView(R.layout.activity_get_start);

        //mapping variables with view
        btnLogin = findViewById(R.id.btnLoginGetStart);
        btnRegister = findViewById(R.id.btnSignUpGetStart);

        cbAgree = findViewById(R.id.checkBoxAgree);


        //set event click for buttons
        btnLogin.setOnClickListener(v -> {

            cbAgree.setChecked(true);
            Intent intent = new Intent(GetStartActivity.this, LoginActivity.class);
            startActivity(intent);

        });

        //set event click for buttons
        btnRegister.setOnClickListener(v -> {

            //set checkbox agree to true
            cbAgree.setChecked(true);
            Intent intent = new Intent(GetStartActivity.this, RegisterActivity.class);
            startActivity(intent);

        });

    }
}
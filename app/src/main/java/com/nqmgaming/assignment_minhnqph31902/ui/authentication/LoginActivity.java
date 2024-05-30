package com.nqmgaming.assignment_minhnqph31902.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.dao.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.ui.onboarding.GetStartActivity;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

import io.github.cutelibs.cutedialog.CuteDialog;

public class LoginActivity extends AppCompatActivity {

    //declare variables
    LinearLayout btnLoginApple, btnLoginFacebook, btnLoginGoogle;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtForgotPassword, txtRegister;
    ImageView imgLogo;
    ImageButton btnBack;
    CheckBox cbRemember;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if user is logged in
        userPreferences = new UserPreferences(this);

        //if user is logged in, go to MainActivity
        if (userPreferences.isLogin()) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;

        }

        setContentView(R.layout.activity_login);

        //mapping variables with view
        btnLoginApple = findViewById(R.id.btnApple);
        btnLoginFacebook = findViewById(R.id.btnFacebook);
        btnLoginGoogle = findViewById(R.id.btnGoogle);

        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);

        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtRegister);

        btnLogin = findViewById(R.id.btnLogin);

        imgLogo = findViewById(R.id.imgLogoApp);

        btnBack = findViewById(R.id.btnBack);

        cbRemember = findViewById(R.id.cbRememberMe);

        cbRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // set background color to green
                cbRemember.setButtonTintList(getResources().getColorStateList(R.color.active));
            } else {
                // set background color to red
                cbRemember.setButtonTintList(getResources().getColorStateList(R.color.inactive));
            }
        });

        //set logo for app
        Glide.with(this)
                .load(R.drawable.minh)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .transform(new CircleCrop())
                .into(imgLogo);

        //set event click for buttons and textview
        Intent intent = getIntent();
        String emailAccount = intent.getStringExtra("emailDTO");
        String passwordAccount = intent.getStringExtra("passwordDTO");

        if (emailAccount != null && passwordAccount != null) {
            // Check if emailAccount and passwordAccount are not null before setting the text
            edtEmail.setText(emailAccount);

            edtPassword.setText(passwordAccount);
        }


        //set event click for txtRegister
        txtRegister.setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();

        });

        //set event click for txtForgotPassword
        txtForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        //set event click for btnBack
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, GetStartActivity.class));
            finish();
        });

        //set event click for btnLogin
        btnLogin.setOnClickListener(v -> {

            //get email and password from edittext
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            //check if email and password is empty
            try {

                //check if email is empty
                if (email.isEmpty()) {
                    edtEmail.setError("Email is required!");
                    edtEmail.requestFocus();
                    return;
                }

                //check if the email is valid
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.setError("Please provide a valid email!");
                    edtEmail.requestFocus();
                    return;
                }

                //check if email exists
                UserDAO userDAO = new UserDAO(this);
                if (!userDAO.checkEmailExist(email)) {
                    edtEmail.setError("Email does not exist!");
                    edtEmail.requestFocus();
                    return;
                }

                //check if password is empty
                if (password.isEmpty()) {
                    edtPassword.setError("Password is required!");
                    edtPassword.requestFocus();
                    return;
                }

                //check if password is correct
                if (!userDAO.checkAccount(email, password)) {
                    edtPassword.setError("Incorrect password!");
                    edtPassword.requestFocus();
                    return;
                }

                //set login status
                if (cbRemember.isChecked()) {
                    userPreferences.setLogin(true);
                    userPreferences.setIdUser(userDAO.getIdByEmail(email));
                    new CuteDialog.withAnimation(LoginActivity.this)
                            .setAnimation(R.raw.done)
                            .setTitle("Success!")
                            .setDescription("Login successfully!")
                            .setPositiveButtonText("Ok", v1 -> {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            })
                            .hideNegativeButton(true)
                            .show();
                } else {
                    new CuteDialog.withAnimation(LoginActivity.this)
                            .setAnimation(R.raw.done)
                            .hideCloseIcon(true)
                            .setPositiveButtonColor(R.color.active)
                            .setTitle("Success!")
                            .setDescription("Login successfully!")
                            .setPositiveButtonText("Ok", v12 -> {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            })
                            .hideNegativeButton(true)
                            .show();
                }

                //go to MainActivity


            } catch (Exception e) {

                e.printStackTrace();

            }
        });

    }

}
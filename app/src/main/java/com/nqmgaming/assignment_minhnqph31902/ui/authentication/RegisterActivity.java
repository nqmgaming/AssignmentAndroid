package com.nqmgaming.assignment_minhnqph31902.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nqmgaming.assignment_minhnqph31902.dao.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

import io.github.cutelibs.cutedialog.CuteDialog;

public class RegisterActivity extends AppCompatActivity {

    //declare variables
    ImageView imgLogoRegister;
    LinearLayout btnGoogleRegister, btnFacebookRegister, btnAppleRegister;
    EditText edtFirstNameRegister, edtLastNameRegister, edtEmailRegister, edtPasswordRegister, edtConfirmPasswordRegister, edtUsernameRegister;
    Button btnRegisterRegister;
    TextView txtLoginRegister;
    ImageButton imgBackRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreferences userPreferences = new UserPreferences(this);

        //if login, go to MainActivity
        if (userPreferences.isLogin()) {

            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
            return;

        }
        setContentView(R.layout.activity_register);

        //mapping variables with view
        imgLogoRegister = findViewById(R.id.imgLogoAppRegister);

        btnGoogleRegister = findViewById(R.id.btnGoogleRegister);
        btnFacebookRegister = findViewById(R.id.btnFacebookRegister);
        btnAppleRegister = findViewById(R.id.btnAppleRegister);

        edtFirstNameRegister = findViewById(R.id.edtFirstNameRegister);
        edtLastNameRegister = findViewById(R.id.edtLastNameRegister);
        edtUsernameRegister = findViewById(R.id.edtUsernameRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtConfirmPasswordRegister = findViewById(R.id.edtPasswordConfirmRegister);

        btnRegisterRegister = findViewById(R.id.btnRegisterRegister);

        txtLoginRegister = findViewById(R.id.txtLoginRegister);

        imgBackRegister = findViewById(R.id.btnBackRegister);

        //load image from drawable
        Glide.with(this)
                .load(R.drawable.minh)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .transform(new CircleCrop())
                .into(imgLogoRegister);

        imgBackRegister.setOnClickListener(v -> finish());

        txtLoginRegister.setOnClickListener(v -> {

            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();

        });

        btnRegisterRegister.setOnClickListener(v -> {

            //get data from edit text
            String firstName = edtFirstNameRegister.getText().toString().trim();
            String lastName = edtLastNameRegister.getText().toString().trim();
            String username = edtUsernameRegister.getText().toString().trim();
            String email = edtEmailRegister.getText().toString().trim();
            String password = edtPasswordRegister.getText().toString().trim();
            String confirmPassword = edtConfirmPasswordRegister.getText().toString().trim();

            try {

                //check if first name is empty
                if (firstName.isEmpty()) {
                    edtFirstNameRegister.setError("First name is required!");
                    edtFirstNameRegister.requestFocus();
                    return;
                }

                //check if first name is valid
                if (!firstName.matches("[a-zA-Z0-9._-]{3,}")) {
                    edtFirstNameRegister.setError("First name is not valid!");
                    edtFirstNameRegister.requestFocus();
                    return;
                }

                //check if last name is empty
                if (lastName.isEmpty()) {
                    edtLastNameRegister.setError("Last name is required!");
                    edtLastNameRegister.requestFocus();
                    return;
                }

                //check if last name is valid
                if (!lastName.matches("[a-zA-Z0-9._-]{3,}")) {
                    edtLastNameRegister.setError("Last name is not valid!");
                    edtLastNameRegister.requestFocus();
                    return;
                }

                //check if username is empty
                if (username.isEmpty()) {
                    edtUsernameRegister.setError("Username is required!");
                    edtUsernameRegister.requestFocus();
                    return;
                }

                //check if username is already exist
                UserDAO userDAO = new UserDAO(this);
                if (userDAO.checkUserExist(username)) {
                    edtUsernameRegister.setError("Username already exists!");
                    edtUsernameRegister.requestFocus();
                    return;
                }

                //check if username is valid
                if (!username.matches("[a-zA-Z0-9._-]{3,}")) {
                    edtUsernameRegister.setError("Username is not valid!");
                    edtUsernameRegister.requestFocus();
                    return;
                }

                //check if email is empty
                if (email.isEmpty()) {
                    edtEmailRegister.setError("Email is required!");
                    edtEmailRegister.requestFocus();
                    return;
                }

                //check if email is valid
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmailRegister.setError("Email is not valid!");
                    edtEmailRegister.requestFocus();
                    return;
                }

                //check if email is already exist
                if (userDAO.checkEmailExist(email)) {
                    edtEmailRegister.setError("Email already exists!");
                    edtEmailRegister.requestFocus();
                    return;
                }

                //check if password is empty
                if (password.isEmpty()) {
                    edtPasswordRegister.setError("Password is required!");
                    edtPasswordRegister.requestFocus();
                    return;
                }

                //check if confirm password is empty
                if (confirmPassword.isEmpty()) {
                    edtConfirmPasswordRegister.setError("Confirm password is required!");
                    edtConfirmPasswordRegister.requestFocus();
                    return;
                }

                //check if password and confirm password match
                if (!password.equals(confirmPassword)) {
                    edtConfirmPasswordRegister.setError("Confirm password does not match!");
                    edtConfirmPasswordRegister.requestFocus();
                    return;
                }

                //check if password is strong enough
                if (password.length() < 8) {
                    edtPasswordRegister.setError("Password must be at least 8 characters!");
                    edtPasswordRegister.requestFocus();
                    return;
                }


            } catch (Exception e) {

                e.printStackTrace();
            }

            //if all data is valid, create new user

            //create new user
            UserDTO user = new UserDTO(username, email, password, firstName, lastName);

            //insert new user to database
            UserDAO userDAO = new UserDAO(this);
            long result = userDAO.insertUser(user);

            //check if insert success
            if (result > 0) {

                new CuteDialog.withAnimation(RegisterActivity.this)
                        .setAnimation(R.raw.done)
                        .setTitle("Success!")
                        .hideCloseIcon(true)
                        .setPositiveButtonColor(R.color.active)
                        .setDescription("Login successfully!")
                        .setPositiveButtonText("Ok", v1 -> {

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();

                        })
                        .hideNegativeButton(true)
                        .show();

            } else {

                //if insert failed, show error message
                edtUsernameRegister.setError("Register failed!");
                edtUsernameRegister.requestFocus();

            }
        });

    }

}
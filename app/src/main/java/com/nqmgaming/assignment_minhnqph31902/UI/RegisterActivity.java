package com.nqmgaming.assignment_minhnqph31902.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nqmgaming.assignment_minhnqph31902.DAO.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.DTO.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.R;

public class RegisterActivity extends AppCompatActivity {
    ImageView imgLogoRegister;
    LinearLayout btnGoogleRegister, btnFacebookRegister, btnAppleRegister;
    EditText edtFirstNameRegister, edtLastNameRegister, edtEmailRegister, edtPasswordRegister, edtConfirmPasswordRegister, edtUsernameRegister;
    Button btnRegisterRegister;
    TextView txtLoginRegister;
    ImageButton imgBackRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        Glide.with(this)
                .load(R.drawable.minh)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .transform(new CircleCrop())
                .into(imgLogoRegister);

        imgBackRegister.setOnClickListener(v -> {
            finish();
        });

        txtLoginRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btnRegisterRegister.setOnClickListener(v -> {
            String firstName = edtFirstNameRegister.getText().toString().trim();
            String lastName = edtLastNameRegister.getText().toString().trim();
            String username = edtUsernameRegister.getText().toString().trim();
            String email = edtEmailRegister.getText().toString().trim();
            String password = edtPasswordRegister.getText().toString().trim();
            String confirmPassword = edtConfirmPasswordRegister.getText().toString().trim();

            try {
                // Kiểm tra các trường dữ liệu có được nhập đầy đủ hay không
                if (firstName.isEmpty()) {
                    edtFirstNameRegister.setError("First name is required!");
                    edtFirstNameRegister.requestFocus();
                    return;
                }
                if (lastName.isEmpty()) {
                    edtLastNameRegister.setError("Last name is required!");
                    edtLastNameRegister.requestFocus();
                    return;
                }
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
                if (email.isEmpty()) {
                    edtEmailRegister.setError("Email is required!");
                    edtEmailRegister.requestFocus();
                    return;
                }
                //check if email is already exist
                if (userDAO.checkEmailExist(email)) {
                    edtEmailRegister.setError("Email already exists!");
                    edtEmailRegister.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    edtPasswordRegister.setError("Password is required!");
                    edtPasswordRegister.requestFocus();
                    return;
                }
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

                //check if email is valid
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmailRegister.setError("Email is not valid!");
                    edtEmailRegister.requestFocus();
                    return;
                }

                //check if username is valid
                if (!username.matches("[a-zA-Z0-9._-]{3,}")) {
                    edtUsernameRegister.setError("Username is not valid!");
                    edtUsernameRegister.requestFocus();
                    return;
                }

                //check if first name is valid
                if (!firstName.matches("[a-zA-Z0-9._-]{3,}")) {
                    edtFirstNameRegister.setError("First name is not valid!");
                    edtFirstNameRegister.requestFocus();
                    return;
                }

                //check if last name is valid
                if (!lastName.matches("[a-zA-Z0-9._-]{3,}")) {
                    edtLastNameRegister.setError("Last name is not valid!");
                    edtLastNameRegister.requestFocus();
                    return;
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            //if all data is valid, create new user
            UserDTO user = new UserDTO(username, email, password, firstName, lastName);
            UserDAO userDAO = new UserDAO(this);
            long result = userDAO.insertUser(user);
            if (result > 0) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                edtUsernameRegister.setError("Register failed!");
                edtUsernameRegister.requestFocus();
            }
        });

    }

}
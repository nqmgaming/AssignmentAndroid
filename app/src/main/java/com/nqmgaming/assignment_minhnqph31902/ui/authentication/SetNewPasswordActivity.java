package com.nqmgaming.assignment_minhnqph31902.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nqmgaming.assignment_minhnqph31902.dao.UserDAO;
import com.nqmgaming.assignment_minhnqph31902.dto.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

import io.github.cutelibs.cutedialog.CuteDialog;

public class SetNewPasswordActivity extends AppCompatActivity {

    //declare variables
    ImageButton imgBackSetNewPassword;
    Button btnSetNewPassword;
    TextView tvLoginHere, tvRegisterHere, EMail;
    EditText edtNewPassword, edtConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreferences userPreferences = new UserPreferences(this);

        //if login, go to MainActivity
        if (userPreferences.isLogin()) {

            startActivity(new Intent(SetNewPasswordActivity.this, MainActivity.class));
            finish();
            return;

        }
        setContentView(R.layout.activity_set_new_password);

        //mapping variables with view
        imgBackSetNewPassword = findViewById(R.id.btnBackForgotPasswordTow);

        btnSetNewPassword = findViewById(R.id.btnResetPasswordFinal);

        tvLoginHere = findViewById(R.id.tvBackToLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        EMail = findViewById(R.id.Email);

        edtNewPassword = findViewById(R.id.edtEmailForgotPassword);
        edtConfirmNewPassword = findViewById(R.id.edtEmailForgotPasswordConfirm);

        //set event click for buttons and textview

        //setOnCLickListener for imgBackSetNewPassword
        imgBackSetNewPassword.setOnClickListener(v -> {
            startActivity(new Intent(SetNewPasswordActivity.this, LoginActivity.class));
            finish();
        });

        //setOnCLickListener for tvLoginHere
        tvLoginHere.setOnClickListener(v -> {
            startActivity(new Intent(SetNewPasswordActivity.this, LoginActivity.class));
            finish();
        });

        //setOnCLickListener for tvRegisterHere
        tvRegisterHere.setOnClickListener(v -> {
            startActivity(new Intent(SetNewPasswordActivity.this, RegisterActivity.class));
            finish();
        });

        //Get data from ForgotPasswordActivity
        Intent intent = getIntent();

        String idDTOString = intent.getStringExtra("idDTO");
        String username = intent.getStringExtra("userDTO");
        String email = intent.getStringExtra("emailDTO");
        String password = intent.getStringExtra("passwordDTO");
        String firstName = intent.getStringExtra("firstNameDTO");
        String lastName = intent.getStringExtra("lastNameDTO");

        EMail.setText(email);
        //setOnCLickListener for btnSetNewPassword
        btnSetNewPassword.setOnClickListener(v -> {

            //get data from edtNewPassword and edtConfirmNewPassword
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();

            //check data
            UserDAO userDAO = new UserDAO(SetNewPasswordActivity.this);
            //check if newPassword is empty
            if (newPassword.isEmpty()) {
                edtNewPassword.setError("New password is required!");
                edtNewPassword.requestFocus();
                return;
            }

            if (confirmNewPassword.isEmpty()) {
                edtConfirmNewPassword.setError("Confirm new password is required!");
                edtConfirmNewPassword.requestFocus();
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                edtConfirmNewPassword.setError("Confirm new password does not match!");
                edtConfirmNewPassword.requestFocus();
                return;
            }


            //check if newPassword is the same as old password
            if (newPassword.equals(password)) {
                edtNewPassword.setError("Please enter a different password!");
                edtNewPassword.requestFocus();
                return;
            }

            //update new password to database
            int idDTO;
            try {
                assert idDTOString != null;
                idDTO = Integer.parseInt(idDTOString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }

            UserDTO userDTO = new UserDTO(idDTO, username, email, newPassword, firstName, lastName);

            //check if update successfully
            int result = userDAO.updateUser(userDTO);
            if (result > 0) {
                new CuteDialog.withAnimation(SetNewPasswordActivity.this)
                        .setAnimation(R.raw.done)
                        .setTitle("Success!")
                        .setDescription("Your password has been reset successfully!")
                        .setPositiveButtonText("Ok", v1 -> {

                            Intent intent1 = new Intent(SetNewPasswordActivity.this, LoginActivity.class);
                            intent1.putExtra("emailDTO", email);
                            intent1.putExtra("passwordDTO", newPassword);
                            intent1.putExtra("ok", "ok");
                            startActivity(intent1);

                        })
                        .hideNegativeButton(true)
                        .show();

            } else {
                Toast.makeText(SetNewPasswordActivity.this, "Reset password failed!" + result, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
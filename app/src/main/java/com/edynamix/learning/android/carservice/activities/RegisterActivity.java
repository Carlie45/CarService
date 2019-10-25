package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.dialogs.ErrorDialog;
import com.edynamix.learning.android.carservice.exceptions.IllegalCredentialsException;
import com.edynamix.learning.android.carservice.exceptions.RegistrationFailedException;
import com.edynamix.learning.android.carservice.models.User;
import com.edynamix.learning.android.carservice.storages.UsersStorage;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.edynamix.learning.android.carservice.utils.MessageDigestHelper;
import com.edynamix.learning.android.carservice.utils.RegisterDataValidator;

import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText editTextRegisterEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        final EditText editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        final EditText editTextRegisterConfirmPassword = (EditText) findViewById(R.id.editTextRegisterConfirmPassword);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);

        // Register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String emailTextFromInput = null;
                    if (editTextRegisterEmail.getText() != null) {
                        emailTextFromInput = editTextRegisterEmail.getText().toString();
                    }

                    String passwordTextFromInput = null;
                    if (editTextRegisterPassword.getText() != null) {
                        passwordTextFromInput = editTextRegisterPassword.getText().toString();
                    }

                    String confirmPasswordTextFromInput = null;
                    if (editTextRegisterConfirmPassword.getText() != null) {
                        confirmPasswordTextFromInput = editTextRegisterConfirmPassword.getText().toString();
                    }

                    checkCredentials(emailTextFromInput, passwordTextFromInput, confirmPasswordTextFromInput);

                    registerUser(emailTextFromInput, passwordTextFromInput);

                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register_user_created), Toast.LENGTH_LONG).show();

                    RegisterActivity.this.finish();
                } catch (IllegalCredentialsException | RegistrationFailedException e) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    new ErrorDialog().createDialog(alertDialogBuilder, e.getMessage());
                }
            }
        });
    }

    private void checkCredentials(
            String emailTextFromInput,
            String passwordTextFromInput,
            String confirmPasswordTextFromInput)
            throws IllegalCredentialsException, RegistrationFailedException{

        if (emailTextFromInput == null || emailTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_enter_email));
        }

        RegisterDataValidator validator = new RegisterDataValidator(RegisterActivity.this);
        validator.validateEmail(emailTextFromInput);

        if (passwordTextFromInput == null || passwordTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_enter_password));
        }
        validator.validatePassword(passwordTextFromInput);

        if (confirmPasswordTextFromInput == null || confirmPasswordTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_confirm_password));
        }

       validator.validatePasswordAndConfirmPasswordMatch(passwordTextFromInput, confirmPasswordTextFromInput);
    }

    private void registerUser(String email, String password) throws RegistrationFailedException {
        String hashedPassword = null;
        try {
            hashedPassword = new String(MessageDigestHelper.getHashForString(password));

            User registeredUser = new User(email, hashedPassword);
            UsersStorage usersStorage = new UsersStorage(RegisterActivity.this);
            usersStorage.addUser(registeredUser);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RegistrationFailedException(getResources().getString(R.string.register_registration_failed)
                    + " Cause: " + nsae.getMessage());
        }
    }
}

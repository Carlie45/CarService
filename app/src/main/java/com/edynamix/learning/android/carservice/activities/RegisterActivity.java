package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.dialogs.ErrorDialog;
import com.edynamix.learning.android.carservice.exceptions.EmailAlreadyRegisteredException;
import com.edynamix.learning.android.carservice.exceptions.IllegalCredentialsException;
import com.edynamix.learning.android.carservice.exceptions.NotMatchingCredentialsException;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_register);

        final EditText editTextRegisterEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        final EditText editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        final EditText editTextRegisterConfirmPassword = (EditText) findViewById(R.id.editTextRegisterConfirmPassword);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Editable emailTextFromInput = editTextRegisterEmail.getText();
                    Editable passwordTextFromInput = editTextRegisterPassword.getText();
                    Editable confirmPasswordTextFromInput = editTextRegisterConfirmPassword.getText();

                    checkCredentials(emailTextFromInput, passwordTextFromInput, confirmPasswordTextFromInput);
                    // We have already verified the entered credentials, so we can use them as strings.
                    String email = emailTextFromInput.toString();
                    String password = passwordTextFromInput.toString();

                    registerUser(email, password);

                    RegisterActivity.this.finish();
                } catch (IllegalCredentialsException | EmailAlreadyRegisteredException | NotMatchingCredentialsException e) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    new ErrorDialog().createDialog(alertDialogBuilder, e.getMessage());
                } catch (RegistrationFailedException rfe) {
                    Log.e(getResources().getString(R.string.app_name), rfe.getMessage());
                }
            }
        });
    }

    private void checkCredentials(
            Editable emailTextFromInput,
            Editable passwordTextFromInput,
            Editable confirmPasswordTextFromInput)
            throws IllegalCredentialsException, EmailAlreadyRegisteredException, NotMatchingCredentialsException {

        if (emailTextFromInput == null || emailTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_enter_email));
        }

        RegisterDataValidator validator = new RegisterDataValidator(RegisterActivity.this);
        validator.validateEmail(emailTextFromInput.toString());

        if (passwordTextFromInput == null || passwordTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_enter_password));
        }
        validator.validatePassword(passwordTextFromInput.toString());

        if (confirmPasswordTextFromInput == null || confirmPasswordTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_confirm_password));
        }

       validator.validatePasswordAndConfirmPasswordMatch(passwordTextFromInput.toString(), confirmPasswordTextFromInput.toString());
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

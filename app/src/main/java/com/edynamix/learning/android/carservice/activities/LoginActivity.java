package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.dialogs.ErrorDialog;
import com.edynamix.learning.android.carservice.exceptions.IllegalCredentialsException;
import com.edynamix.learning.android.carservice.exceptions.LoginFailedException;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.edynamix.learning.android.carservice.utils.LoginDataValidator;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login);

        final EditText editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        final EditText editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);

        // Login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Editable emailTextFromInput = editTextLoginEmail.getText();
                    Editable passwordTextFromInput = editTextLoginPassword.getText();

                    checkCredentials(emailTextFromInput, passwordTextFromInput);

                    saveLoggedInUserToSharedPreferences(emailTextFromInput.toString());

                    Intent navigateToListCarsActivity = new Intent(LoginActivity.this, ListCarsActivity.class);
                    startActivity(navigateToListCarsActivity);
                } catch (LoginFailedException lfe) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    new ErrorDialog().createDialog(alertDialogBuilder, lfe.getMessage());
                }
            }
        });

        // Set link for registration
        final TextView textViewLoginNewHere = (TextView) findViewById(R.id.textViewLoginNewHere);
        textViewLoginNewHere.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textViewLoginNewHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(navigateToRegisterActivity);
            }
        });
    }

    private void checkCredentials(
            Editable emailTextFromInput,
            Editable passwordTextFromInput)
            throws LoginFailedException {

        if (emailTextFromInput == null || emailTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_enter_email));
        }

        if (passwordTextFromInput == null || passwordTextFromInput.equals(Constants.EMPTY_VALUE)) {
            throw new IllegalCredentialsException(getResources().getString(R.string.register_please_enter_password));
        }

        String email = emailTextFromInput.toString();
        String password = passwordTextFromInput.toString();

        LoginDataValidator validator = new LoginDataValidator(LoginActivity.this);
        validator.validateCredentials(email, password);
    }

    private void saveLoggedInUserToSharedPreferences(String email) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_LOGGED_IN_USER, email);
        editor.commit();
    }
}

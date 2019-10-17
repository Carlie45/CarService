package com.edynamix.learning.android.carservice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.exceptions.EmailAlreadyRegisteredException;
import com.edynamix.learning.android.carservice.exceptions.IllegalCredentialsException;
import com.edynamix.learning.android.carservice.exceptions.NotMatchingCredentialsException;
import com.edynamix.learning.android.carservice.storages.UsersStorage;

import java.util.regex.Pattern;

public class RegisterDataValidator {

    private Context context;
    private SharedPreferences sharedPreferences;
    private Resources appResources;

    // Regex according to OWASP Validation Regex Repository.
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public RegisterDataValidator(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        appResources = App.getRes();
    }

    /**
     * The provided e-mail is validated according to the OWASP Validation Regex Repository.
     * The email will be rejected if it is too long or if it is already registered.
     * See Constants.MAX_EMAIL_LENGTH.
     * @param email - required to be not null
     */
    public void validateEmail(String email) throws IllegalCredentialsException, EmailAlreadyRegisteredException {
        if (email.length() > Constants.MAX_EMAIL_LENGTH) {
            throw new IllegalCredentialsException(appResources.getString(R.string.register_email_too_long)
                    + " " + Constants.MAX_EMAIL_LENGTH);
        }

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        boolean matchesRegex = pattern.matcher(email).matches();
        if (!matchesRegex) {
            throw new IllegalCredentialsException(appResources.getString(R.string.register_email_not_valid));
        }

        UsersStorage usersStorage = new UsersStorage(context);
        boolean userAlreadyExists =  usersStorage.hasUserWithEmail(email);
        if (userAlreadyExists) {
            throw new EmailAlreadyRegisteredException(appResources.getString(R.string.register_email_already_registered));
        }
    }

    /**
     * This method checks if the password is at least 10 characters long, contains at least 1 letter, at least 1 digit and
     * at least 1 special character. The password will be rejected if it is too long. See Constants.MAX_PASSWORD_LENGTH.
     * @param password - required to be not null
     */
    public void validatePassword(String password) throws IllegalCredentialsException {
        if (password.length() < 10) {
             throw new IllegalCredentialsException(
                     appResources.getString(R.string.register_password_must_be_at_least_ten_characters_long));
        }

        if (password.length() > Constants.MAX_PASSWORD_LENGTH) {
            throw new IllegalCredentialsException(
                    appResources.getString(R.string.register_password_is_too_long) + " " + Constants.MAX_PASSWORD_LENGTH);
        }

        boolean containsDigit = false;
        boolean containsLetter = false;
        boolean containsSpecialCharacters = false;
        for (int currentCharIndex = 0; currentCharIndex < password.length(); currentCharIndex++) {
            if (containsDigit && containsLetter && containsSpecialCharacters) {
                // The password meets all the requirements. Nothing more to check.
                return;
            }

            char currentChar = password.charAt(currentCharIndex);
            if (!containsDigit) {
                containsDigit = CharacterMatcher.isADigit(currentChar);
            }
            if (!containsLetter) {
                containsLetter = CharacterMatcher.isALetter(currentChar);
            }
            if (!containsSpecialCharacters) {
                containsSpecialCharacters = CharacterMatcher.isASpecialCharacter(currentChar);
            }
        }

        StringBuilder errorMessage = new StringBuilder();
        if (!containsDigit) {
            errorMessage.append(appResources.getString(R.string.register_password_does_not_contain_digit));
        }
        if (!containsLetter) {
            errorMessage.append(appResources.getString(R.string.register_password_does_not_contain_letter));
        }
        if (!containsSpecialCharacters) {
            errorMessage.append(appResources.getString(R.string.register_password_does_not_contain_spec_char));
        }

        if (errorMessage.length() != 0) {
            throw new IllegalCredentialsException(errorMessage.toString());
        }
    }

    public void validatePasswordAndConfirmPasswordMatch(String password, String confirmPassword)
            throws NotMatchingCredentialsException {

        if (!password.equals(confirmPassword)) {
            throw new NotMatchingCredentialsException(appResources.getString(R.string.register_passwords_do_not_match));
        }
    }
}

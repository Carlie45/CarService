package com.edynamix.learning.android.carservice.utils;

import android.content.Context;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.exceptions.LoginFailedException;
import com.edynamix.learning.android.carservice.exceptions.NoSuchUserException;
import com.edynamix.learning.android.carservice.models.User;
import com.edynamix.learning.android.carservice.storages.UsersStorage;

import java.security.NoSuchAlgorithmException;

public class LoginDataValidator {

    private Context context;

    public LoginDataValidator(Context context) {
        this.context = context;
    }

    public void validateCredentials(String email, String password) throws LoginFailedException {
        UsersStorage usersStorage = new UsersStorage(context);
        User foundUser = usersStorage.getUserForEmail(email);

        if (foundUser == null) {
            throw new NoSuchUserException(App.getRes().getString(R.string.login_no_such_user));
        }

        try {
            String hashedPassword = new String(MessageDigestHelper.getHashForString(password));
            if (!hashedPassword.equals(foundUser.passwordHash)) {
                throw new LoginFailedException(App.getRes().getString(R.string.login_invalid_password));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new LoginFailedException(App.getRes().getString(R.string.login_login_failed));
        }
    }
}

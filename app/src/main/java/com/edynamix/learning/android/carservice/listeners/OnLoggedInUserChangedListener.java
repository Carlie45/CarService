package com.edynamix.learning.android.carservice.listeners;

import android.content.SharedPreferences;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.utils.Constants;

public class OnLoggedInUserChangedListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView textViewToolbarLoggedInEmail;

    public OnLoggedInUserChangedListener(TextView textViewToolbarLoggedInEmail) {
        this.textViewToolbarLoggedInEmail = textViewToolbarLoggedInEmail;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.SHARED_PREFERENCES_LOGGED_IN_USER)) {
            String sharedPrefsEmail = sharedPreferences.getString(Constants.SHARED_PREFERENCES_LOGGED_IN_USER, Constants.EMPTY_VALUE);
            textViewToolbarLoggedInEmail.setText(sharedPrefsEmail);
        }
    }
}

package com.edynamix.learning.android.carservice.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.edynamix.learning.android.carservice.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for logged in user.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedInUser = sharedPreferences.getString(Constants.SHARED_PREFERENCES_LOGGED_IN_USER, null);
        Intent intentToNextActivity;
        if (loggedInUser == null) {
            intentToNextActivity = new Intent(this, LoginActivity.class);
        } else {
            intentToNextActivity = new Intent(this, ListCarsActivity.class);
        }
        startActivity(intentToNextActivity);
    }
}

package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.CarOwner;
import com.edynamix.learning.android.carservice.models.CarOwnerBuilder;
import com.edynamix.learning.android.carservice.storages.CarOwnersStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

public class AddCarOwnerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_owner);

        // Toolbar back button
        Button buttonToolbarBack = (Button) findViewById(R.id.buttonToolbarBack);
        buttonToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Toolbar logged in user email
        TextView textViewToolbarLoggedInEmail = (TextView) findViewById(R.id.textViewToolbarLoggedInEmail);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sharedPrefsEmail = sharedPreferences.getString(Constants.SHARED_PREFERENCES_LOGGED_IN_USER, Constants.EMPTY_VALUE);
        textViewToolbarLoggedInEmail.setText(sharedPrefsEmail);

        // Toolbar logout button
        Button buttonToolbarLogout = (Button) findViewById(R.id.buttonToolbarLogout);
        buttonToolbarLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLoggedInUser();
                Intent navigateToLoginActivity = new Intent(AddCarOwnerActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // Edit texts for car owner properties
        final EditText editTextAddCarOwnerFirstName = (EditText) findViewById(R.id.editTextAddCarOwnerFirstName);
        final EditText editTextAddCarOwnerLastName = (EditText) findViewById(R.id.editTextAddCarOwnerLastName);
        final EditText editTextAddCarOwnerAddress = (EditText) findViewById(R.id.editTextAddCarOwnerAddress);
        final EditText editTextAddCarOwnerPhone = (EditText) findViewById(R.id.editTextAddCarOwnerPhone);
        final EditText editTextAddCarOwnerEmail = (EditText) findViewById(R.id.editTextAddCarOwnerEmail);

        // Button to add new owner
        Button buttonAddCarOwnerCreate = (Button) findViewById(R.id.buttonAddCarOwnerCreate);
        buttonAddCarOwnerCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = null;
                if (editTextAddCarOwnerFirstName.getText() != null) {
                    firstName = editTextAddCarOwnerFirstName.getText().toString();
                }

                String lastName = null;
                if (editTextAddCarOwnerLastName.getText() != null) {
                    lastName = editTextAddCarOwnerLastName.getText().toString();
                }

                String address = null;
                if (editTextAddCarOwnerAddress.getText() != null) {
                    address = editTextAddCarOwnerAddress.getText().toString();
                }

                String phone = null;
                if (editTextAddCarOwnerPhone.getText() != null) {
                    phone = editTextAddCarOwnerPhone.getText().toString();
                }

                String email = null;
                if (editTextAddCarOwnerEmail.getText() != null) {
                    email = editTextAddCarOwnerEmail.getText().toString();
                }

                // Persist data to storage
                CarOwnersStorage carOwnersStorage = new CarOwnersStorage(AddCarOwnerActivity.this);
                CarOwner newCarOwner = new CarOwnerBuilder()
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setAddress(address)
                        .setPhone(phone)
                        .setEmail(email)
                        .setId(carOwnersStorage.getNextCarOwnerId())
                        .build();

                carOwnersStorage.addCarOwner(newCarOwner);
                Toast.makeText(AddCarOwnerActivity.this, App.getRes().getString(R.string.add_car_car_owner_saved_successfully_message), Toast.LENGTH_LONG).show();

                // Return to previous activity.
                finish();
            }
        });
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }
}

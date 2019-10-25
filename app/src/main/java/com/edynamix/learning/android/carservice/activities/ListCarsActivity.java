package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.edynamix.learning.android.carservice.views.CarDetailsView;

import java.util.List;

public class ListCarsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cars);

        // Toolbar back button
        Button buttonToolbarBack = (Button) findViewById(R.id.buttonToolbarBack);
        buttonToolbarBack.setVisibility(View.GONE);

        // Toolbar logged in user
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
                Intent navigateToLoginActivity = new Intent(ListCarsActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // Button add car
        Button buttonListCarsAddCar = (Button) findViewById(R.id.buttonListCarsAdd);
        buttonListCarsAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToAddCarActivity = new Intent(ListCarsActivity.this, AddCarActivity.class);
                startActivity(navigateToAddCarActivity);
            }
        });

        // Button add car owner
        Button buttonListCarsAddCarOwner = (Button) findViewById(R.id.buttonListCarsAddCarOwner);
        buttonListCarsAddCarOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToAddCarOwnerActivity = new Intent(ListCarsActivity.this, AddCarOwnerActivity.class);
                startActivity(navigateToAddCarOwnerActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout linearLayoutListCars = (LinearLayout) findViewById(R.id.linearLayoutListCars);
        linearLayoutListCars.removeAllViews();
        CarsStorage carsStorage = new CarsStorage(ListCarsActivity.this);
        List<Car> cars = carsStorage.getCars();
        for (Car car : cars) {
            LinearLayout carDetailsView = new CarDetailsView(ListCarsActivity.this, car);
            linearLayoutListCars.addView(carDetailsView);
        }
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }
}

package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.OnLoggedInUserChangedListener;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.dialogs.ErrorDialog;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.CarBuilder;
import com.edynamix.learning.android.carservice.models.CarOwner;
import com.edynamix.learning.android.carservice.storages.CarOwnersStorage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddCarActivity extends Activity {

    private int yearOfManufacture;
    private int carOwnerId;
    private List<CarOwner> carOwners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_add_car);

        // Toolbar back button
        Button buttonToolbarBack = (Button) findViewById(R.id.buttonToolbarBack);
        buttonToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToListCarsActivity = new Intent(AddCarActivity.this, ListCarsActivity.class);
                startActivity(navigateToListCarsActivity);
            }
        });

        // Toolbar logged in user email
        TextView textViewToolbarLoggedInEmail = (TextView) findViewById(R.id.textViewToolbarLoggedInEmail);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sharedPrefsEmail = sharedPreferences.getString(Constants.SHARED_PREFERENCES_LOGGED_IN_USER, Constants.EMPTY_VALUE);
        textViewToolbarLoggedInEmail.setText(sharedPrefsEmail);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new OnLoggedInUserChangedListener(textViewToolbarLoggedInEmail));

        // Toolbar logout button
        Button buttonToolbarLogout = (Button) findViewById(R.id.buttonToolbarLogout);
        buttonToolbarLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLoggedInUser();
                Intent navigateToLoginActivity = new Intent(AddCarActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // Edit texts for car properties
        final EditText editTextAddCarBrand = (EditText) findViewById(R.id.editTextAddCarBrand);
        final EditText editTextAddCarModel = (EditText) findViewById(R.id.editTextAddCarModel);
        final EditText editTextAddCarColour = (EditText) findViewById(R.id.editTextAddCarColour);
        final EditText editTextAddCarDoorsCount = (EditText) findViewById(R.id.editTextAddCarDoorsCount);

        // Year of manufacture
        final TextView textViewAddCarShowSelectedYearOfManufacture = (TextView) findViewById(R.id.textViewAddCarShowSelectedYearOfManufacture);
        displayCurrentYear(textViewAddCarShowSelectedYearOfManufacture);

        // Buton select year of manufacture
        Button buttonAddCarSelectYearOfManufacture = (Button) findViewById(R.id.buttonAddCarSelectYearOfManufacture);
        buttonAddCarSelectYearOfManufacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog for year picker
                final Dialog dialogYearPicker = new Dialog(AddCarActivity.this);
                dialogYearPicker.setContentView(R.layout.dialog_year_picker);
                dialogYearPicker.setTitle(App.getRes().getString(R.string.add_car_select_year_of_manufacture));

                // Number picker for year of manufacture in dialog
                final NumberPicker numberPickerYear = (NumberPicker) dialogYearPicker.findViewById(R.id.numberPickerYear);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                numberPickerYear.setMaxValue(year);
                numberPickerYear.setMinValue(year - 50);
                numberPickerYear.setValue(year);
                numberPickerYear.setWrapSelectorWheel(false);
                numberPickerYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        yearOfManufacture = newVal;
                        displaySelectedYear(textViewAddCarShowSelectedYearOfManufacture, String.valueOf(newVal));
                    }
                });

                // Dialog buttons
                Button buttonYearPickerSelect = (Button) dialogYearPicker.findViewById(R.id.buttonYearPickerDialogSelect);
                Button buttonYearPickerCancel = (Button) dialogYearPicker.findViewById(R.id.buttonYearPickerDialogCancel);
                View.OnClickListener onClickDismissDialog = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogYearPicker.dismiss();
                    }
                };

                buttonYearPickerSelect.setOnClickListener(onClickDismissDialog);
                buttonYearPickerCancel.setOnClickListener(onClickDismissDialog);
                dialogYearPicker.show();
            }
        });

        // Button to add new car owner
        Button buttonAddCarAddCarOwner = (Button) findViewById(R.id.buttonAddCarAddCarOwner);
        buttonAddCarAddCarOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToAddCarOwnerActivity = new Intent(AddCarActivity.this, AddCarOwnerActivity.class);
                startActivity(navigateToAddCarOwnerActivity);
            }
        });

        // Create new car button
        Button buttonAddCarCreateNewCar = (Button) findViewById(R.id.buttonAddCarCreateNewCar);
        buttonAddCarCreateNewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = editTextAddCarBrand.getText() != null ? editTextAddCarBrand.getText().toString() : null;
                String model = editTextAddCarModel.getText() != null ? editTextAddCarModel.getText().toString() : null;
                String colour = editTextAddCarColour.getText() != null ? editTextAddCarColour.getText().toString() : null;
                int doorsCount = editTextAddCarDoorsCount.getText() != null && editTextAddCarDoorsCount.getText().length() > 0 ?
                        Integer.parseInt(editTextAddCarDoorsCount.getText().toString()) : 0;

                CarOwnersStorage carOwnersStorage = new CarOwnersStorage(AddCarActivity.this);
                boolean carOwnerExistsInStorage = carOwnersStorage.isCarOwnerWithIdInTheStorage(carOwnerId);
                if (!carOwnerExistsInStorage) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddCarActivity.this);
                    new ErrorDialog().createDialog(alertDialogBuilder, getResources().getString(R.string.add_car_enter_car_owner));
                } else {
                    CarsStorage carsStorage = new CarsStorage(AddCarActivity.this);
                    String addedByUser = ((TextView) findViewById(R.id.textViewToolbarLoggedInEmail)).getText().toString();
                    // Create car and save to storage
                    Car newCar = new CarBuilder()
                            .setId(carsStorage.getNextCarId())
                            .setBrand(brand)
                            .setModel(model)
                            .setColour(colour)
                            .setDoorsCount(doorsCount)
                            .setYearOfManufacture(yearOfManufacture)
                            .setCarOwnerId(carOwnerId)
                            .setAddedByUser(addedByUser)
                            .build();

                    carsStorage.addCar(newCar);
                    Toast.makeText(AddCarActivity.this, App.getRes().getString(
                            R.string.add_car_car_saved_successfully_message), Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CarOwnersStorage carOwnersStorage = new CarOwnersStorage(AddCarActivity.this);
        carOwners = carOwnersStorage.getCarOwners();

        // Spinner for car owner
        Spinner spinnerAddCarSelectCarOwner = (Spinner) findViewById(R.id.spinnerAddCarSelectCarOwner);
        final List<String> carOwnersNames = new ArrayList<>();
        for (CarOwner carOwner : carOwners) {
            carOwnersNames.add(carOwner.firstName + " " + carOwner.lastName);
        }
        String[] carOwnersNamesArr = new String[carOwnersNames.size()];
        carOwnersNamesArr = carOwnersNames.toArray(carOwnersNamesArr);

        ArrayAdapter<String> carOwnersAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_row, carOwnersNamesArr);
        spinnerAddCarSelectCarOwner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carOwnerId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAddCarSelectCarOwner.setAdapter(carOwnersAdapter);
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }

    private void displayCurrentYear(TextView textView) {
        StringBuilder formattedDate = new StringBuilder();
        formattedDate.append(App.getRes().getString(R.string.add_car_selected_year) + ": ");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        formattedDate.append(year);

        yearOfManufacture = year;
        textView.setText(formattedDate.toString());
    }

    private void displaySelectedYear(TextView textView, String dateAsString) {
        StringBuilder formattedDate = new StringBuilder();
        formattedDate.append(App.getRes().getString(R.string.add_car_selected_year));
        formattedDate.append(": ");
        formattedDate.append(dateAsString);

        textView.setText(formattedDate.toString());
    }
}

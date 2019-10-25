package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.dialogs.ConfirmDialog;
import com.edynamix.learning.android.carservice.dialogs.ErrorDialog;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.storages.DamagesStorage;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.edynamix.learning.android.carservice.views.DamageDetailsView;

import java.io.File;
import java.util.ArrayList;

public class ListDamagesActivity extends Activity {

    private int carId;
    private Car car;
    private CarsStorage carsStorage;
    private LinearLayout linearLayoutListDamages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_damages);

        carId = getIntent().getExtras().getInt(Constants.EXTRA_CAR_ID);
        carsStorage = new CarsStorage(ListDamagesActivity.this);

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
                Intent navigateToLoginActivity = new Intent(ListDamagesActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // Button add new damage
        Button buttonListDamagesAdd = (Button) findViewById(R.id.buttonListDamagesAdd);
        buttonListDamagesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToAddDamageActivity = new Intent(ListDamagesActivity.this, AddDamageActivity.class);
                navigateToAddDamageActivity.putExtra(Constants.EXTRA_CAR_ID, carId);
                startActivity(navigateToAddDamageActivity);
            }
        });

        // Button remove all damages.
        Button buttonListDamagesRemoveAll = (Button) findViewById(R.id.buttonListDamagesRemoveAll);
        buttonListDamagesRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car = carsStorage.getCarWithId(carId);
                // If the car does not exists, show an error dialog. Nothing more to do.
                if (car == null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListDamagesActivity.this);
                    new ErrorDialog().createDialog(alertDialogBuilder, getResources().getString(R.string.list_damages_invalid_car));
                    return;
                } else {
                    if (car.damageIdsList != null) {
                        for (Integer damageId : car.damageIdsList) {
                            deleteDamageWithId(damageId);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayoutListDamages = (LinearLayout) findViewById(R.id.linearLayoutListDamages);
        linearLayoutListDamages.removeAllViews();

        carsStorage = new CarsStorage(ListDamagesActivity.this);
        car = carsStorage.getCarWithId(carId);
        // If the car does not exists, show an error dialog. Nothing more to do.
        if (car == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListDamagesActivity.this);
            new ErrorDialog().createDialog(alertDialogBuilder, getResources().getString(R.string.list_damages_invalid_car));
            return;
        }
        DamagesStorage damagesStorage = new DamagesStorage(ListDamagesActivity.this);
        Damage damage;

        if (car.damageIdsList == null) {
            car.damageIdsList = new ArrayList<>();
        }

        TextView textViewListDamagesNoImages = (TextView) findViewById(R.id.textViewListDamagesNoImages);
        HorizontalScrollView horizontalScrollViewListDamagesGallery =
                (HorizontalScrollView) findViewById(R.id.horizontalScrollViewListDamagesGallery);
        if (car.damageIdsList.size() == 0) {
            textViewListDamagesNoImages.setVisibility(View.VISIBLE);
            horizontalScrollViewListDamagesGallery.setVisibility(View.GONE);
        } else {
            textViewListDamagesNoImages.setVisibility(View.GONE);
            horizontalScrollViewListDamagesGallery.setVisibility(View.VISIBLE);

            for (final Integer damageId : car.damageIdsList) {
                damage = damagesStorage.getDamageWithId(damageId);
                if (damage == null) {
                    continue;
                }
                DamageDetailsView damageDetailsView = new DamageDetailsView(ListDamagesActivity.this, damage);
                damageDetailsView.setTag(damage.id);
                damageDetailsView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Display dialog to confirm damage deletion
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListDamagesActivity.this);
                        ConfirmDialog confirmDialog = new ConfirmDialog();
                        final AlertDialog alertDialog =
                                confirmDialog.createDialog(alertDialogBuilder, getResources().getString(R.string.confirm_dialog_message));

                        // Set listener for confirm button pressed.
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteDamageWithId(damageId);
                                alertDialog.dismiss();
                            }
                        });
                        return true;
                    }
                });
                linearLayoutListDamages.addView(damageDetailsView);
            }
        }
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }

    private void deleteDamageWithId(int damageId) {
        carsStorage = new CarsStorage(ListDamagesActivity.this);
        car = carsStorage.getCarWithId(carId);
        // If the car does not exists, show an error dialog. Nothing more to do.
        if (car == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListDamagesActivity.this);
            new ErrorDialog().createDialog(alertDialogBuilder, getResources().getString(R.string.list_damages_invalid_car));
            return;
        }

        DamagesStorage damagesStorage = new DamagesStorage(ListDamagesActivity.this);
        Damage damage = damagesStorage.getDamageWithId(damageId);
        if (damage == null) {
            // Nothing to delete.
            return;
        }

        File fileToDelete = new File(damage.imageSource);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        removeLinearLayoutForDamage(damageId);
        damagesStorage.deleteDamageWithId(damageId);
        if (car.damageIdsList != null) {
            car.damageIdsList.remove((Integer) damage.id);
        }

        carsStorage.updateCar(car.id, car);
    }

    private void removeLinearLayoutForDamage(int damageId) {
        linearLayoutListDamages = (LinearLayout) findViewById(R.id.linearLayoutListDamages);

        for (int i = 0; i < linearLayoutListDamages.getChildCount(); i++) {
            View view = linearLayoutListDamages.getChildAt(i);
            if (view instanceof LinearLayout) {
                Object obj = view.getTag();
                if (obj != null) {
                    int tag = (int) obj;
                    if (tag == damageId) {
                        view.setVisibility(View.GONE);
                        view.invalidate();
                        linearLayoutListDamages.removeView(view);
                        return;
                    }
                }
            }
        }
    }
}

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
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.storages.DamagesStorage;
import com.edynamix.learning.android.carservice.utils.Constants;
import com.edynamix.learning.android.carservice.views.DamageDetailsView;

import java.io.File;

public class ListDamagesActivity extends Activity {

    private int carId;
    private LinearLayout linearLayoutListDamages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_list_damages);

        carId = getIntent().getExtras().getInt(Constants.EXTRA_CAR_ID);
        final CarsStorage carsStorage = new CarsStorage(ListDamagesActivity.this);
        Car car = carsStorage.getCarWithId(carId);

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
                DamagesStorage damagesStorage = new DamagesStorage(ListDamagesActivity.this);
                Car car = carsStorage.getCarWithId(carId);
                for (Integer damageId : car.damageIdsList) {
                    Damage damage = damagesStorage.getDamageWithId(damageId);
                    String photoSrc = damage.imageSource;
                    File fileToDelete = new File(photoSrc);
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }

                    removeLinearLayoutForDamage(damageId);
                    damagesStorage.deleteDamageWithId(damageId);
                }
                car.damageIdsList.clear();
                carsStorage.updateCar(car.id, car);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout linearLayoutListDamages = (LinearLayout) findViewById(R.id.linearLayoutListDamages);
        linearLayoutListDamages.removeAllViews();
        CarsStorage carsStorage = new CarsStorage(ListDamagesActivity.this);
        Car car = carsStorage.getCarWithId(carId);
        DamagesStorage damagesStorage = new DamagesStorage(ListDamagesActivity.this);
        Damage damage;
        for (Integer damageId : car.damageIdsList) {
            damage = damagesStorage.getDamageWithId(damageId);
            DamageDetailsView damageDetailsView = new DamageDetailsView(ListDamagesActivity.this, damage);
            damageDetailsView.setTag(damage.id);
            damageDetailsView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeLinearLayoutForDamage((int) v.getTag());
                    return true;
                }
            });
            linearLayoutListDamages.addView(damageDetailsView);
        }
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
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

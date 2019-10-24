package com.edynamix.learning.android.carservice.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.storages.DamagesStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

import java.io.File;

public class AddDamageActivity extends Activity {

    private String imageSource;
    private int carId;
    private Car car;

    private RelativeLayout relativeLayoutAddDamageContainer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_add_damage);

        carId = getIntent().getExtras().getInt(Constants.EXTRA_CAR_ID);
        final CarsStorage carsStorage = new CarsStorage(AddDamageActivity.this);
        car = carsStorage.getCarWithId(carId);

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
                Intent navigateToLoginActivity = new Intent(AddDamageActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // Initialize car template.
        relativeLayoutAddDamageContainer = (RelativeLayout) findViewById(R.id.relativeLayoutAddDamageContainer);

        // Set a car template to mark the damages.
        final ImageView imageViewAddDamageCarTemplate = (ImageView) findViewById(R.id.imageViewAddDamageCarTemplate);
        imageViewAddDamageCarTemplate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Get the clicked positions.
                    final float xPos = event.getX();
                    final float yPos = event.getY();

                    // Create image view with the X marker where touched.
//                    ImageView imageViewRedXForDamage = new ImageView(AddDamageActivity.this);
//                    int toolbarHeight = 2 * (int) getResources().getDimension(R.dimen.toolbar_height);
//                    imageViewRedXForDamage.setX(xPos);
//                    imageViewRedXForDamage.setY(yPos + toolbarHeight);
//                    int width = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
//                    int height = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
//                    imageViewRedXForDamage.setLayoutParams(layoutParams);
//                    imageViewRedXForDamage.setImageResource(R.drawable.img_red_x);
//
//                    // Add the image on the screen.
//                    relativeLayoutAddDamageContainer.addView(imageViewRedXForDamage);
//
//                    // Add the image view to the list.
//                    imageViewListForDamageXMarks.add(imageViewRedXForDamage);

                    // Go to the add details activity.
                    Intent navigateToDamageDetailsActivity =
                            new Intent(AddDamageActivity.this, AddDamageDetailsActivity.class);

                    Bundle extras = new Bundle();
                    extras.putFloat(Constants.EXTRA_DAMAGE_X_POS, xPos);
                    extras.putFloat(Constants.EXTRA_DAMAGE_Y_POS, yPos);
                    extras.putInt(Constants.EXTRA_CAR_ID, carId);
                    navigateToDamageDetailsActivity.putExtras(extras);
                    startActivity(navigateToDamageDetailsActivity);
                }
                return false;
            }
        });

        // Button complete.
        Button buttonAddDamageCompleteChanges = (Button) findViewById(R.id.buttonAddDamageCompleteChanges);
        buttonAddDamageCompleteChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Button remove all damages.
        Button buttonAddDamageRemoveAllDamages = (Button) findViewById(R.id.buttonAddDamageRemoveAllDamages);
        buttonAddDamageRemoveAllDamages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DamagesStorage damagesStorage = new DamagesStorage(AddDamageActivity.this);
                for (Integer damageId : car.damageIdsList) {
                    Damage damage = damagesStorage.getDamageWithId(damageId);
                    String photoSrc = damage.imageSource;
                    File fileToDelete = new File(photoSrc);
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                    removeImageViewForDamage(damage.id);
                    damagesStorage.deleteDamageWithId(damageId);
                }
                car.damageIdsList.clear();
                carsStorage.updateCar(car.id, car);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0 && resultCode == RESULT_OK) {
//            File imgFile = new File(data.getExtras().getString(MediaStore.EXTRA_OUTPUT));
//            if(imgFile.exists()) {
//                imageSource = imgFile.getPath();
//                Toast.makeText(AddDamageActivity.this, "The image was saved successfully.", Toast.LENGTH_SHORT);
//            }
//        }
//        Intent navigateToAddDamageActivity = new Intent(AddDamageActivity.this, AddDamageActivity.class);
//        navigateToAddDamageActivity.putExtra(Constants.EXTRA_CAR_ID, carId);
//        startActivity(navigateToAddDamageActivity);
//
//    }

    @Override
    protected void onStart() {
        super.onStart();
        DamagesStorage damagesStorage = new DamagesStorage(AddDamageActivity.this);
        // Place existing damages over the car template.
        for (Integer damageId : car.damageIdsList) {
            Damage damage = damagesStorage.getDamageWithId(damageId);
            ImageView imageViewRedXForDamage = createXMarkForDamageLocation(damage);
            // Add the image on the screen.
            relativeLayoutAddDamageContainer.addView(imageViewRedXForDamage);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(Constants.EXTRA_CAR_ID, carId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get(Constants.EXTRA_CAR_ID) != null) {
            carId = (int) savedInstanceState.get(Constants.EXTRA_CAR_ID);
        }
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }

    private ImageView createXMarkForDamageLocation(Damage damage) {
        ImageView imageViewRedXForDamage = new ImageView(AddDamageActivity.this);
        int toolbarHeight = 2 * (int) getResources().getDimension(R.dimen.toolbar_height);
        int margin = (int) getResources().getDimension(R.dimen.half_normal_margin);

        imageViewRedXForDamage.setX(damage.xPositionOnCarTemplate);
        imageViewRedXForDamage.setY(damage.yPositionOnCarTemplate + toolbarHeight + margin);

        int width = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
        int height = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        imageViewRedXForDamage.setLayoutParams(layoutParams);
        imageViewRedXForDamage.setTag(damage.id);

        imageViewRedXForDamage.setImageResource(R.drawable.img_red_x);

        return imageViewRedXForDamage;
    }

    private void removeImageViewForDamage(int damageId) {
        for (int i = 0; i < relativeLayoutAddDamageContainer.getChildCount(); i++) {
            View view = relativeLayoutAddDamageContainer.getChildAt(i);
            if (view instanceof ImageView) {
                Object obj = view.getTag();
                if (obj != null) {
                    int tag = (int) obj;
                    if (tag == damageId) {
                        relativeLayoutAddDamageContainer.removeView(view);
                        return;
                    }
                }
            }
        }
    }
}

package com.edynamix.learning.android.carservice.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddDamageActivity extends Activity {

    private String imageSource;
    private Dialog dialogDamageDescription;
    private long carId;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_add_damage);

        carId = getIntent().getExtras().getLong(Constants.EXTRA_CAR_ID);
        CarsStorage carsStorage = new CarsStorage(AddDamageActivity.this);
        final Car car = carsStorage.getCarWithId(carId);

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

        // Button remove all damages.
        Button buttonAddDamageRemoveAllDamages = (Button) findViewById(R.id.buttonAddDamageRemoveAllDamages);
        buttonAddDamageRemoveAllDamages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Damage damage : car.damageList) {
                    String photoSrc = damage.imageSource;
                    File fileToDelete = new File(photoSrc);
                    fileToDelete.delete();
                }
                car.damageList.clear();
            }
        });

        // Initialize car template.
        final RelativeLayout relativeLayoutAddDamageContainer = (RelativeLayout) findViewById(R.id.relativeLayoutAddDamageContainer);

        // Place existing damages over the car template.
        for (Damage damage : car.damageList) {
            ImageView imageViewRedXForDamage = new ImageView(AddDamageActivity.this);
            int toolbarHeight = 2 * (int) getResources().getDimension(R.dimen.toolbar_height);
            imageViewRedXForDamage.setX(damage.xPositionOnCarTemplate);
            imageViewRedXForDamage.setY(damage.yPositionOnCarTemplate + toolbarHeight);
            int width = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
            int height = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            imageViewRedXForDamage.setLayoutParams(layoutParams);
            imageViewRedXForDamage.setImageResource(R.drawable.img_red_x);

            // Add the image on the screen.
            relativeLayoutAddDamageContainer.addView(imageViewRedXForDamage);
        }

        // Dialog to add damage description.
//        dialogDamageDescription = new Dialog(AddDamageActivity.this);
//        dialogDamageDescription.setContentView(R.layout.dialog_damage_description);
//        dialogDamageDescription.setTitle(App.getRes().getString(R.string.dialog_damage_description_title));
//        dialogDamageDescription.show();

        // Description for the damage.
//        final EditText editTextDialogDamageDescription =
//                (EditText) dialogDamageDescription.findViewById(R.id.editTextDialogDamageDescription);

        // Take photo button.
//        final Button buttonDialogDamageDescriptionTakePhoto =
//                (Button) dialogDamageDescription.findViewById(R.id.buttonDialogDamageDescriptionTakePhoto);
//        buttonDialogDamageDescriptionTakePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
//                String pictureName;
//                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                File fileToStoreThePicture = null;
//                try {
//                    pictureName = "damage" + "_" + carId + "_" + Damage.counter;
//                    fileToStoreThePicture = File.createTempFile(pictureName,  ".png", storageDir);
//                    Uri photoURI = FileProvider.getUriForFile(AddDamageActivity.this,
//                            "com.edynamix.learning.android.carservice.fileprovider",
//                            fileToStoreThePicture);
//
//                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(openCameraIntent, 0);
//                    buttonDialogDamageDescriptionTakePhoto.setVisibility(View.GONE);
//                } catch (IOException e) {
//                    // TODO: make an error dialog
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // Cancel button.
//        Button buttonDialogDamageDescriptionCancel =
//                (Button) dialogDamageDescription.findViewById(R.id.buttonDialogDamageDescriptionCancel);
//        buttonDialogDamageDescriptionCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogDamageDescription.dismiss();
//            }
//        });

        final ImageView imageViewAddDamageCarTemplate = (ImageView) findViewById(R.id.imageViewAddDamageCarTemplate);
        imageViewAddDamageCarTemplate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Get the clicked positions.
                    final float xPos = event.getX();
                    final float yPos = event.getY();

                    // Add button.
//                    Button buttonDialogDamageDescriptionAdd =
//                            (Button) dialogDamageDescription.findViewById(R.id.buttonDialogDamageDescriptionAdd);
//                    buttonDialogDamageDescriptionAdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            final String damageDescription = editTextDialogDamageDescription.getText() != null
//                                    ? editTextDialogDamageDescription.getText().toString()
//                                    : Constants.EMPTY_VALUE;
//                            Damage damage = new Damage(imageSource, damageDescription, xPos, yPos);
//                            List<Damage> carDamages = car.damageList;
//                            carDamages.add(damage);
//                            CarsStorage carsStorage = new CarsStorage(AddDamageActivity.this);
//                            carsStorage.updateCar((int) car.id, car);
//                            dialogDamageDescription.dismiss();

                            // Create image view with the X marker where touched.
                            ImageView imageViewRedXForDamage = new ImageView(AddDamageActivity.this);
                            int toolbarHeight = 2 * (int) getResources().getDimension(R.dimen.toolbar_height);
                            imageViewRedXForDamage.setX(xPos);
                            imageViewRedXForDamage.setY(yPos + toolbarHeight);
                            int width = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
                            int height = (int) getResources().getDimension(R.dimen.add_damage_x_dimens);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                            imageViewRedXForDamage.setLayoutParams(layoutParams);
                            imageViewRedXForDamage.setImageResource(R.drawable.img_red_x);

                            // Add the image on the screen.
                            relativeLayoutAddDamageContainer.addView(imageViewRedXForDamage);

                            Intent navigateToDamageDetailsActivity =
                                    new Intent(AddDamageActivity.this, AddDamageDetailsActivity.class);
                            navigateToDamageDetailsActivity.putExtra(Constants.EXTRA_CAR_ID, carId);
                            startActivity(navigateToDamageDetailsActivity);
//                        }
//                    });
//
//                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            File imgFile = new File(data.getExtras().getString(MediaStore.EXTRA_OUTPUT));
            if(imgFile.exists()) {
                imageSource = imgFile.getPath();
                Toast.makeText(AddDamageActivity.this, "The image was saved successfully.", Toast.LENGTH_SHORT);
            }
        }
        Intent navigateToAddDamageActivity = new Intent(AddDamageActivity.this, AddDamageActivity.class);
        navigateToAddDamageActivity.putExtra(Constants.EXTRA_CAR_ID, carId);
        startActivity(navigateToAddDamageActivity);

    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }
}

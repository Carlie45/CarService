package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.dialogs.ErrorDialog;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.storages.DamagesStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddDamageDetailsActivity extends Activity {

    private int carId;
    private float xPos;
    private float yPos;

    private File fileToStoreThePicture;
    private Uri photoUri;

    private Button buttonAddDamageDetailsTakePhoto;
    private ImageView imageViewAddDamageDetailsPhoto;

    private boolean isPictureTaken = false;

    private static final int REQUEST_IMAGE_CAPTURE = 158;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_add_damage_details);

        carId = getIntent().getExtras().getInt(Constants.EXTRA_CAR_ID);
        xPos = getIntent().getExtras().getFloat(Constants.EXTRA_DAMAGE_X_POS);
        yPos = getIntent().getExtras().getFloat(Constants.EXTRA_DAMAGE_Y_POS);

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
                Intent navigateToLoginActivity = new Intent(AddDamageDetailsActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // EditText damage description.
        final EditText editTextAddDamageDetailsDescription = (EditText) findViewById(R.id.editTextAddDamageDetailsDescription);

        // Button to take photo of the damage.
        buttonAddDamageDetailsTakePhoto = (Button) findViewById(R.id.buttonAddDamageDetailsTakePhoto);
        buttonAddDamageDetailsTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        // Set placeholder for photo for the damage.
        imageViewAddDamageDetailsPhoto = (ImageView) findViewById(R.id.imageViewAddDamageDetailsPhoto);
        imageViewAddDamageDetailsPhoto.setImageDrawable(getResources().getDrawable(R.drawable.img_placeholder));
        // Set this in order to see the changes when we set the taken phone to this image view.
        imageViewAddDamageDetailsPhoto.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Button to discard the changes.
        Button buttonAddDamageDetailsCancel = (Button) findViewById(R.id.buttonAddDamageDetailsCancel);
        buttonAddDamageDetailsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Button to add the damage.
        Button buttonAddDamageDetailsAdd = (Button) findViewById(R.id.buttonAddDamageDetailsAdd);
        buttonAddDamageDetailsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarsStorage carsStorage = new CarsStorage(AddDamageDetailsActivity.this);
                Car car = carsStorage.getCarWithId(carId);

                Editable editTextAddDamageDetailsDescriptionText = editTextAddDamageDetailsDescription.getText();
                String damageDescription = editTextAddDamageDetailsDescriptionText != null ?
                        editTextAddDamageDetailsDescriptionText.toString() : Constants.EMPTY_VALUE;

                String imageSource = Constants.EMPTY_VALUE;
                if (fileToStoreThePicture != null) {
                    imageSource = fileToStoreThePicture.getAbsolutePath();
                }
                DamagesStorage damagesStorage = new DamagesStorage(AddDamageDetailsActivity.this);
                Damage damage = new Damage(damagesStorage.getNextDamageId(), imageSource, damageDescription, xPos, yPos);
                damagesStorage.addDamage(damage);
                car.damageIdsList.add(damage.id);
                carsStorage.updateCar(car.id, car);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_CANCELED) {
                isPictureTaken = false;
                fileToStoreThePicture = null;
                return;
            } else if (resultCode == RESULT_OK) {
                try {
                    photoUri = data.getData();

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 8;
                    InputStream input = getContentResolver().openInputStream(photoUri);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                    input.close();

                    FileOutputStream out = new FileOutputStream(fileToStoreThePicture);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                    out.flush();
                    out.close();

                    // ImageView to display the taken photo.
                    imageViewAddDamageDetailsPhoto = (ImageView) findViewById(R.id.imageViewAddDamageDetailsPhoto);
                    imageViewAddDamageDetailsPhoto.setImageBitmap(imageBitmap);

                    // Remove the take photo button as it is not needed anymore.
                    buttonAddDamageDetailsTakePhoto.setVisibility(View.GONE);
                } catch (IOException ioe) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddDamageDetailsActivity.this);
                    new ErrorDialog().createDialog(alertDialogBuilder,
                            "An error occurred while processing the photo. Cause: " + ioe.getMessage());
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(Constants.EXTRA_CAR_ID, carId);
        savedInstanceState.putFloat(Constants.EXTRA_DAMAGE_X_POS, xPos);
        savedInstanceState.putFloat(Constants.EXTRA_DAMAGE_Y_POS, yPos);
        if (isPictureTaken) {
            savedInstanceState.putString(Constants.EXTRA_PHOTO_FILE_PATH, fileToStoreThePicture.getAbsolutePath());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get(Constants.EXTRA_CAR_ID) != null) {
            carId = (int) savedInstanceState.get(Constants.EXTRA_CAR_ID);
        }
        if (savedInstanceState.get(Constants.EXTRA_DAMAGE_X_POS) != null) {
            xPos = (float) savedInstanceState.get(Constants.EXTRA_DAMAGE_X_POS);
        }
        if (savedInstanceState.get(Constants.EXTRA_DAMAGE_Y_POS) != null) {
            yPos = (float) savedInstanceState.get(Constants.EXTRA_DAMAGE_Y_POS);
        }
        if (savedInstanceState.get(Constants.EXTRA_PHOTO_FILE_PATH) != null) {
            fileToStoreThePicture = new File(savedInstanceState.get(Constants.EXTRA_PHOTO_FILE_PATH).toString());
        }
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }

    private void takePicture() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
            fileToStoreThePicture = createImageFile();
            if (fileToStoreThePicture != null) {
                isPictureTaken = true;
                AddDamageDetailsActivity.this.startActivityForResult(openCameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(AddDamageDetailsActivity.this,
                    "There is no available camera to perform this action.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() {
        DamagesStorage damagesStorage = new DamagesStorage(AddDamageDetailsActivity.this);
        String pictureName = "damage" + "_" + carId + "_" + damagesStorage.getNextDamageId() + ".png";

        File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/");
        File file = new File(imagePath, pictureName);

        return file;
    }
}

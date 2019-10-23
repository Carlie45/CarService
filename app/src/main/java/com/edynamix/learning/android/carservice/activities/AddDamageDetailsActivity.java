package com.edynamix.learning.android.carservice.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarsStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class AddDamageDetailsActivity extends Activity {

    private long carId;
    private String imageSource;
    private Bitmap imageBitmap;
    private File fileToStoreThePicture;
    private Uri photoURI;

    private Button buttonAddDamageDetailsTakePhoto;
    private ImageView imageViewAddDamageDetailsPhoto;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_add_damage_details);

        carId = getIntent().getExtras().getLong(Constants.EXTRA_CAR_ID);
        CarsStorage carsStorage = new CarsStorage(AddDamageDetailsActivity.this);
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
                Intent navigateToLoginActivity = new Intent(AddDamageDetailsActivity.this, LoginActivity.class);
                startActivity(navigateToLoginActivity);
            }
        });

        // EditText damage description.
        EditText editTextAddDamageDetailsDescription = (EditText) findViewById(R.id.editTextAddDamageDetailsDescription);

        // Button to take photo of the damage.
        buttonAddDamageDetailsTakePhoto = (Button) findViewById(R.id.buttonAddDamageDetailsTakePhoto);
        buttonAddDamageDetailsTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        imageViewAddDamageDetailsPhoto = (ImageView) findViewById(R.id.imageViewAddDamageDetailsPhoto);
        imageViewAddDamageDetailsPhoto.setImageDrawable(getResources().getDrawable(R.drawable.img_placeholder));
        imageViewAddDamageDetailsPhoto.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void takePicture() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                fileToStoreThePicture = createImageFile();
                if (fileToStoreThePicture != null) {
//                    Uri photoURI = FileProvider.getUriForFile(AddDamageDetailsActivity.this,
//                          "com.edynamix.learning.android.carservice.fileprovider",
//                           fileToStoreThePicture);
//                    photoURI = Uri.fromFile(fileToStoreThePicture.getAbsoluteFile());
//                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    AddDamageDetailsActivity.this.startActivityForResult(openCameraIntent, 158);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(AddDamageDetailsActivity.this,
                    "There is no available camera to perform this action.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 158 && resultCode == RESULT_OK) {
            try {
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                Bitmap bitmap = BitmapFactory.decodeFile(imageSource, bmOptions);
//                Uri uri = (Uri) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
//                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Uri uri = data.getData();
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                // ImageView to display the taken photo.
                imageViewAddDamageDetailsPhoto = (ImageView) findViewById(R.id.imageViewAddDamageDetailsPhoto);
                imageViewAddDamageDetailsPhoto.setImageBitmap(image);
//                imageViewAddDamageDetailsPhoto.invalidate();
                buttonAddDamageDetailsTakePhoto.setVisibility(View.INVISIBLE);
            } catch (Exception e) {

            }
        }
    }

    private void removeLoggedInUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.SHARED_PREFERENCES_LOGGED_IN_USER);
        editor.commit();
    }

    private File createImageFile() throws IOException {
        String pictureName = "damage" + "_" + carId + "_" + Damage.counter + ".png";
//        File storageDir = getFilesDir();
//        File file = File.createTempFile(
//                pictureName,
//                ".png",
//                storageDir
//        );

        File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/");
        File file = new File(imagePath, pictureName);

        imageSource = file.getAbsolutePath();

        return file;
    }
}

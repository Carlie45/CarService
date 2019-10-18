package com.edynamix.learning.android.carservice.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.CarOwner;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarOwnersStorage;

import java.util.List;

public class CarDetailsView extends LinearLayout {

    public CarDetailsView(Context context, Car car) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_car_details, this, true);

        TextView textViewCarDetailsBrandAndModel = (TextView) findViewById(R.id.textViewCarDetailsBrandAndModel);
        textViewCarDetailsBrandAndModel.setText(getResources().getString(R.string.car_details_brand_and_model) + ": " + car.brand + " " + car.model);

        TextView textViewCarDetailsColour = (TextView) findViewById(R.id.textViewCarDetailsColour);
        textViewCarDetailsColour.setText(getResources().getString(R.string.car_details_colour) + ": " + car.colour);

        TextView textViewCarDetailsDoorsCount = (TextView) findViewById(R.id.textViewCarDetailsDoorsCount);
        textViewCarDetailsDoorsCount.setText(getResources().getString(R.string.car_details_doors_count) + ": " + car.doorsCount);

        TextView textViewCarDetailsYearOfManufacture = (TextView) findViewById(R.id.textViewCarDetailsYearOfManufacture);
        textViewCarDetailsYearOfManufacture.setText(getResources().getString(R.string.car_details_year_of_manufacture) + ": " + car.yearOfManufacture);

        CarOwnersStorage carOwnersStorage = new CarOwnersStorage(context);
        List<CarOwner> carOwners = carOwnersStorage.getCarOwners();
        String carOwnerName = "";
        for (CarOwner carOwner : carOwners) {
            if (carOwner.id == car.carOwnerId) {
                carOwnerName = carOwner.firstName + " " + carOwner.lastName;
                break;
            }
        }
        TextView textViewCarDetailsCarOwner = (TextView) findViewById(R.id.textViewCarDetailsCarOwner);
        textViewCarDetailsCarOwner.setText(getResources().getString(R.string.car_details_car_owner) + ": " + carOwnerName);

        TextView textViewCarDetailsAddedByUser = (TextView) findViewById(R.id.textViewCarDetailsAddedByUser);
        textViewCarDetailsAddedByUser.setText(getResources().getString(R.string.car_details_added_by_user) + ": " + car.addedByUser);

        LinearLayout linearLayoutCarDetailsDamageGallery = (LinearLayout) findViewById(R.id.linearLayoutCarDetailsDamageGallery);
        if (car.damageList != null) {
            for (Damage damage : car.damageList) {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(R.drawable.ic_car);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                int width = (int) (40 * context.getResources().getDisplayMetrics().density);
                int height = (int) (40 * context.getResources().getDisplayMetrics().density);
//                BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(R.drawable.ic_car);
//                int drawableHeight = bd.getBitmap().getHeight();
//                int drawableWidth = bd.getBitmap().getWidth();
//                double scaleX = 1;
//                if (drawableWidth > width) {
//                    scaleX = width / drawableWidth;
//                }
//                double scaleY = 1;
//                if (drawableWidth > width) {
//                    scaleY = height / drawableHeight;
//                }
//
//                imageView.setScaleX((float) scaleX);
//                imageView.setScaleY((float) scaleY);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
                params.setMargins(10,10,10,10);
                imageView.setLayoutParams(params);
                linearLayoutCarDetailsDamageGallery.addView(imageView);
            }
        }
    }
}

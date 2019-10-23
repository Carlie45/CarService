package com.edynamix.learning.android.carservice.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.activities.ListDamagesActivity;
import com.edynamix.learning.android.carservice.models.Car;
import com.edynamix.learning.android.carservice.models.CarOwner;
import com.edynamix.learning.android.carservice.models.Damage;
import com.edynamix.learning.android.carservice.storages.CarOwnersStorage;
import com.edynamix.learning.android.carservice.storages.DamagesStorage;
import com.edynamix.learning.android.carservice.utils.Constants;

import java.util.List;

public class CarDetailsView extends LinearLayout {

    public CarDetailsView(final Context context, final Car car) {
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
        DamagesStorage damagesStorage = new DamagesStorage(context);
        for (Integer damageId : car.damageIdsList) {
            Damage damage = damagesStorage.getDamageWithId(damageId);
            ImageView imageView = new ImageView(context);
            if (damage != null && damage.imageSource != null && damage.imageSource.length() != 0) {
                Drawable drawablePhoto = Drawable.createFromPath(damage.imageSource);
                imageView.setImageDrawable(drawablePhoto);
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_placeholder));
            }

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

        LinearLayout linearLayoutCarDetailsDamagesContainer = (LinearLayout) findViewById(R.id.linearLayoutCarDetailsDamagesContainer);
        Button buttonCarDetailsCheckAllDamages = new Button(context);
        buttonCarDetailsCheckAllDamages.setTag(car.id);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        buttonCarDetailsCheckAllDamages.setLayoutParams(layoutParams);
        buttonCarDetailsCheckAllDamages.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
        buttonCarDetailsCheckAllDamages.setText(R.string.car_details_check_all_damages);
        buttonCarDetailsCheckAllDamages.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToListDamagesActivity = new Intent(context, ListDamagesActivity.class);
                navigateToListDamagesActivity.putExtra(Constants.EXTRA_CAR_ID, (int) v.getTag()); // the tag is the car id
                context.startActivity(navigateToListDamagesActivity);
            }
        });

        linearLayoutCarDetailsDamagesContainer.addView(buttonCarDetailsCheckAllDamages);

    }
}

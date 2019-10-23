package com.edynamix.learning.android.carservice.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.models.Damage;

public class DamageDetailsView extends LinearLayout {

    public DamageDetailsView(Context context, Damage damage) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_damage_details, this, true);

        ImageView imageViewDamageDetails = (ImageView) findViewById(R.id.imageViewDamageDetails);
        Drawable drawableDamageImage;
        if (damage.imageSource != null && damage.imageSource.length() != 0) {
            drawableDamageImage = Drawable.createFromPath(damage.imageSource);
        } else {
            drawableDamageImage = getResources().getDrawable(R.drawable.img_placeholder);

        }
        imageViewDamageDetails.setImageDrawable(drawableDamageImage);

        TextView textViewDamageDetails = (TextView) findViewById(R.id.textViewDamageDetails);
        textViewDamageDetails.setText(damage.description);
    }
}

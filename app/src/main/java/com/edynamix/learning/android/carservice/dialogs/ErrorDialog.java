package com.edynamix.learning.android.carservice.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.utils.Constants;

public class ErrorDialog {

    private final int NEUTRAL_BUTTON_BACKGROUND_COLOUR_GREY = App.getRes().getColor(R.color.colorLightGrey);
    private final int NEUTRAL_BUTTON_TEXT_COLOUR_BLACK = App.getRes().getColor(R.color.colorBlack);

    public void createDialog(AlertDialog.Builder alertDialogBuilder, String errorMessageToDisplay) {
        alertDialogBuilder.setTitle(Constants.ERROR_TITLE);
        alertDialogBuilder.setMessage(errorMessageToDisplay);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setNegativeButton(
                Constants.CLOSE,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        neutralButton.setBackgroundColor(NEUTRAL_BUTTON_BACKGROUND_COLOUR_GREY);
        neutralButton.setTextColor(NEUTRAL_BUTTON_TEXT_COLOUR_BLACK);
    }

}

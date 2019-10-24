package com.edynamix.learning.android.carservice.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.utils.Constants;

public class ErrorDialog {

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
        neutralButton.setBackgroundColor(App.getRes().getColor(R.color.colorLightGrey));
        neutralButton.setTextColor(App.getRes().getColor(R.color.colorBlack));
    }
}

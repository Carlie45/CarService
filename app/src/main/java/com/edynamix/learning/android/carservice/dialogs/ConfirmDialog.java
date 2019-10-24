package com.edynamix.learning.android.carservice.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.utils.Constants;

public class ConfirmDialog {

    public AlertDialog createDialog(AlertDialog.Builder alertDialogBuilder, String messageToDisplay) {
        alertDialogBuilder.setTitle(App.getRes().getString(R.string.confirm_dialog_title));
        alertDialogBuilder.setMessage(messageToDisplay);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setNegativeButton(
                Constants.CLOSE,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        alertDialogBuilder.setPositiveButton(
                Constants.CONFIRM,
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

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackgroundColor(App.getRes().getColor(R.color.colorTeal));
        positiveButton.setTextColor(App.getRes().getColor(R.color.colorBlack));

        return alertDialog;
    }
}

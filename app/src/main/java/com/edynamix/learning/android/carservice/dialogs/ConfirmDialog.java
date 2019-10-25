package com.edynamix.learning.android.carservice.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.edynamix.learning.android.carservice.App;
import com.edynamix.learning.android.carservice.R;
import com.edynamix.learning.android.carservice.utils.Constants;

public class ConfirmDialog {

    public AlertDialog createDialog(AlertDialog.Builder alertDialogBuilder, String messageToDisplay) {
        alertDialogBuilder.setTitle(App.getRes().getString(R.string.confirm_dialog_title));
        alertDialogBuilder.setMessage(messageToDisplay);
        alertDialogBuilder.setCancelable(true);

        DialogInterface.OnClickListener onClickDismissDialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        alertDialogBuilder.setNegativeButton(Constants.CLOSE, onClickDismissDialog);
        alertDialogBuilder.setPositiveButton(Constants.CONFIRM, onClickDismissDialog);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        neutralButton.setBackgroundColor(App.getRes().getColor(R.color.colorLightGrey));
        neutralButton.setTextColor(App.getRes().getColor(R.color.colorBlack));
        neutralButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setBackgroundColor(App.getRes().getColor(R.color.colorTeal));
        positiveButton.setTextColor(App.getRes().getColor(R.color.colorBlack));
        positiveButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        return alertDialog;
    }
}

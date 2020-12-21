package com.cognifygroup.vgold.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cognifygroup.vgold.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogs {

    private ProgressDialog mDialog;
    public static AlertDialogs mInstance;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private String mAddress;
    List<Address> addresses;

    public static AlertDialogs getInstance() {
        if (mInstance == null) {
            mInstance = new AlertDialogs();
        }
        return mInstance;
    }

    public void onShowProgressDialog(Activity activity, boolean isShow) {

        try {
            if (isShow) {
                mDialog = ProgressDialog.show(activity, "", "Loading...", true);
                mDialog.show();
            } else {
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public void onShowToastNotification(Activity activity, String msg) {
        Toast ltoast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) ltoast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(16);
        ltoast.show();
    }

    public void onHideKeyBoard(Activity mActivity) {
        final InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
    }

    //Alert dialog for call admin
    public void BuyerAlert(final Activity activity) {

    }


    public static void alertDialogOk(Context context, String title, String message,
                                     String button, final int resultCode, boolean setCancel,
                                     final AlertDialogOkListener alertDialogOkListener) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.customDialogue);
        alert.setCancelable(setCancel);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });

        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialogTheme;
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialogOkListener != null) {
                    alertDialogOkListener.onDialogOk(resultCode);
                    alertDialog.dismiss();
                }

            }
        });
    }
}
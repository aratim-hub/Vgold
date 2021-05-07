package com.cognifygroup.vgold.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;


/**
 * Created by my-pc on 20/5/19.
 */

public class MySMSBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("InReceive:", intent.getAction());

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Status status = (Status) bundle.get(SmsRetriever.EXTRA_STATUS);
            Log.d("message", String.valueOf(bundle));
            Log.d("status", String.valueOf(status));
            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    String msg[]=message.split(":");
                    String m=msg[1];
                    String msg_arr[]=m.split("\n");
                    String otp=msg_arr[0].trim();

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",otp);
                    Log.d("message",msg[1]);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);

                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    Log.d("message","TimedOut");

                    break;
            }
        }
    }


}



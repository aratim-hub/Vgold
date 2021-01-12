package com.cognifygroup.vgold;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Gpay extends AppCompatActivity {
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpay);


        Button pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "ashishsatpute27@okhdfcbank")
                                .appendQueryParameter("pn", "Adity")
                                 .appendQueryParameter("tr", "6586966")
                                .appendQueryParameter("tn", "testing")
                                .appendQueryParameter("am", "1")
                                .appendQueryParameter("cu", "INR")
                               *//* .appendQueryParameter("url", "your-transaction-url")*//*
                                .build();*/

                Uri uri = Uri.parse("upi://pay").buildUpon()
                        .appendQueryParameter("pa", "oeh6866@axisbank")
                        .appendQueryParameter("pn", "adity")
                        .appendQueryParameter("mc", "")
                        //.appendQueryParameter("tid", "02125412")
                        .appendQueryParameter("tr", "779898779784")
                        .appendQueryParameter("tn", "note")
                        .appendQueryParameter("am", "1.00")
                        .appendQueryParameter("cu", "INR")
                        //.appendQueryParameter("refUrl", "blueapp")
                        .build();


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data.getStringExtra("Status"));
        }
    }
}
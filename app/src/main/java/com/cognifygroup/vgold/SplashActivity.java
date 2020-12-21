package com.cognifygroup.vgold;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cognifygroup.vgold.utils.AlertDialogs;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        notificationPayload();

       /* ImageView click =findViewById(R.id.click);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationPayload();
            }
        });*/
    }

    private void notificationPayload() {

        if (getIntent().getExtras() != null) {
            String payload = String.valueOf(getIntent().getExtras().get("payload"));
//            Log.d("payload", payload);

            if (payload != null && !TextUtils.isEmpty(payload) && !payload.equalsIgnoreCase("null")) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(payload)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(payload)));
                }

                finish();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Intent lIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(lIntent);
                            finish();
                        }
                    }
                }, 2000);
            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent lIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(lIntent);
                        finish();
                    }
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        notificationPayload();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    Intent lIntent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(lIntent);
//                    finish();
//                }
//            }
//        }, 2000);
    }
}
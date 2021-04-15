package com.cognifygroup.vgold.firebasenotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.cognifygroup.vgold.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManager notificationManager;
    private String ADMIN_CHANNEL_ID = "vgoldNotify";
//    private SharePreferenceUtils sharePreferenceUtils;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
//        Log.d("FCM token", token);
//         sendRegistrationToServer(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().size() > 0) {
//            Log.d("TAG", remoteMessage.getData().get("message"));



//                String payload = remoteMessage.getData().get("payload");
//                Log.d("payload", "Message data payload: " + payload);

                /*if (payload != null && !TextUtils.isEmpty(payload)) {
                    try {
                        if (!TextUtils.isEmpty(payload)) {
                            JSONObject jobj = new JSONObject(payload);

                            String startDate = jobj.getString("start_date");
                            String endDate = jobj.getString("end_date");

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, 0);
                            try {
                                Date currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(sdf.format(cal.getTime()));
                                Date startDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(startDate);

                                if (startDateTime != null && currentDateTime != null && !(currentDateTime.getTime() - startDateTime.getTime() >= 60 * 60 * 1000)) {
                                    sharePreferenceUtils.putString("payload", payload);

                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/


                /*sharePreferenceUtils.putString("payload", payload);

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);*/

                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                //Setting up Notification channels for android O and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setupNotificationChannels();
                }
                int notificationId = new Random().nextInt(60000);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        } /*else {
//            Log.d("TAG", "NO Data Found");
        }*/

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannels() {
//        String adminChannelName = getString(R.string.notifications_admin_channel_name);
//        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel notificationChannel = new NotificationChannel(ADMIN_CHANNEL_ID, "VGOLD", NotificationManager.IMPORTANCE_HIGH);
//        adminChannel.description = adminChannelDescription
//        notificationChannel.lightColor = Color.RED;
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);
    }
}

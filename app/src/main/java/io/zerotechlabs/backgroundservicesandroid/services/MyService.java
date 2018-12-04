package io.zerotechlabs.backgroundservicesandroid.services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

import io.zerotechlabs.backgroundservicesandroid.MainActivity;
import io.zerotechlabs.backgroundservicesandroid.R;

public class MyService extends IntentService {

    // Key for the string that's delivered in the action's intent.
    public static final String KEY_TEXT_REPLY = "key_text_reply";

    @TargetApi(20)
    public RemoteInput buildRemoteInput(){
        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        return remoteInput;
    }

    public PendingIntent getRemotePendingIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        // Build a PendingIntent for the reply action to trigger.
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        121,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        return replyPendingIntent;
    }


    public NotificationCompat.Action buildNotificationRemoteInputAction(){
        // Create the reply action and add the remote input.
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.whatsapp,
                        "Reply", getRemotePendingIntent())
                        .addRemoteInput(buildRemoteInput())
                        .build();
        return action;
    }


    public MyService() {
        super("myservice");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("tag", "on handle intent called");

        String message = intent.getStringExtra("key1");
        Toast.makeText(this, "We have started the Intent Service", Toast.LENGTH_SHORT).show();

        showNotification(message);


    }

    private void showNotification(String messageNotification) {
        // Configure the channel
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationAboveOreo(messageNotification);
        } else {
            showNotificationBelowOreo(messageNotification);
        }

    }

    @TargetApi(26)
    private void showNotificationAboveOreo(String messageNotification) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("myChannelId",
                "My Channel", importance);
        channel.setDescription("Reminders");
        // Register the channel with the notifications manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);


        NotificationCompat.Builder mBuilder =
                // Builder class for devices targeting API 26+ requires a channel ID
                new NotificationCompat.Builder(this, "myChannelId")
                        .setSmallIcon(R.drawable.whatsapp)
                        .setContentTitle("Whatsapp Clone")
                        .setContentText(messageNotification)
                        .setContentIntent(getPendingIntent())
                        /*.addAction(R.drawable.whatsapp, "OK",
                                getPendingIntent())*/
                        .addAction(buildNotificationRemoteInputAction())
                        .setAutoCancel(true);


        // mId allows you to update the notification later on.
        mNotificationManager.notify(101, mBuilder.build());


    }

    private void showNotificationBelowOreo(String messageNotification) {

        NotificationCompat.Builder mBuilder =
                // this Builder class is deprecated
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.whatsapp)
                        .setContentTitle("Whatsapp Clone")
                        .setContentText(messageNotification)

                        .setContentIntent(getPendingIntent())
                        /*.addAction(R.drawable.whatsapp, "OK",
                                getPendingIntent())*/
                        .addAction(buildNotificationRemoteInputAction())
                        .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(101, mBuilder.build());
    }


    private PendingIntent getPendingIntent() {
        // First let's define the intent to trigger when notification is selected
// Start out by creating a normal intent (in this case to open an activity)
        Intent intent = new Intent(this, MainActivity.class);
// Next, let's turn this into a PendingIntent using
//   public static PendingIntent getActivity(Context context, int requestCode,
//       Intent intent, int flags)
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this,
                requestID, intent, flags);

        return pIntent;
    }

}

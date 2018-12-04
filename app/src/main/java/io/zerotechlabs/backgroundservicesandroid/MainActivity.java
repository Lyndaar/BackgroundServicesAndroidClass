package io.zerotechlabs.backgroundservicesandroid;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.zerotechlabs.backgroundservicesandroid.services.MyService;

import static io.zerotechlabs.backgroundservicesandroid.services.MyService.KEY_TEXT_REPLY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMessageText(getIntent());

    }

    public void startIntentService(View view) {
        EditText editText = findViewById(R.id.edittext);

        if(!editText.getText().toString().isEmpty()){
            Log.e("tag", "clicked");
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra("key1", editText.getText().toString());
            startService(intent);

            /*if (android.os.Build.VERSION.SDK_INT >= 24) {

                // Setup a DirectReply IntentService
                Intent directReplyIntent = new Intent(this, MyService.class);

                // pass the notification ID -- it should be a UUID
                directReplyIntent.putExtra(KEY_NOTIFY_ID, 82);

                // only handle one pending intent at a time
                int flags = FLAG_CANCEL_CURRENT;

                PendingIntent directReplyPendingIntent = PendingIntent.getService(this, 0,
                        directReplyIntent, flags);

            }*/
        }

    }

    private CharSequence getMessageText(Intent intent) {
        if(intent == null) return null;

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String message = remoteInput.getCharSequence(KEY_TEXT_REPLY).toString();


            Toast.makeText(this, message, Toast.LENGTH_LONG).show();


            return message;
        }
        return null;
    }
}

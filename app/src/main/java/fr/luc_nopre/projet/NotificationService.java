package fr.luc_nopre.projet;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    private static int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "chanel 1";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        // regarde tout le items
        /**
        ArrayList<TodoItem> items = TodoDbHelper.getItems(this);

        for (TodoItem i :items) {
            LocalDateTime date = i.getDate();
            Log.i("date ezmrjfzmznerfozenr", ""+date);
        }
        startTimer();
**/
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    final Handler handler = new Handler();


    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        //timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        timer.schedule(timerTask, 5000,20000);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void showNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_icon_foreground)
                .setContentTitle("Titre")
                .setContentText("Contenu")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, notifBuilder.build());
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        showNotification();
                    }
                });
            }
        };
    }
}
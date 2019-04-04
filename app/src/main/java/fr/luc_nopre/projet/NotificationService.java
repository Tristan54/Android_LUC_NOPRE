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

    private Timer timer;
    private TimerTask timerTask;
    private String TAG = "Timers";
    private Intent intent;
    private static int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "chanel 1";
    private ArrayList<TodoItem> items;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        this.intent = intent;
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        //stoptimertask();
        super.onDestroy();


    }

    final Handler handler = new Handler();


    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask,5000, 6*60*60*1000); // relance une notification toute les 6 heures

    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void showNotification(String titre ,String contenu) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_icon_foreground)
                .setContentTitle(titre)
                .setContentText(contenu)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, notifBuilder.build());
        //NOTIFICATION_ID++;

    }

    public void initializeTimerTask() {


        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {

                        boolean expire =false;
                        items = (ArrayList<TodoItem>) intent.getSerializableExtra("items");
                        LocalDateTime dateAc = LocalDateTime.now();
                        if(items != null || !items.isEmpty()){
                            for (TodoItem i :items) {
                                LocalDateTime date = i.getDate();
                                if(date.isBefore(dateAc)){
                                    expire = true;
                                }
                            }

                            if(expire){
                                Log.i("notifs","notification pour rapel");
                                showNotification("Urgent","Un item de votre liste est expirer !");
                            }else{
                                showNotification("Liste","Vous pouvez consulter vos activités ici !");
                            }
                            expire =false;
                        }
                    }
                });
            }
        };
    }
}
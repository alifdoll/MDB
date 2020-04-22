package com.alif.submission.mdb.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.alif.submission.mdb.MainActivity;
import com.alif.submission.mdb.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.alif.submission.mdb.BuildConfig.API_KEY;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String EXTRA_TYPE = "type";
    public static final String TYPE_DAILY = "type_daily";
    public static final String TYPE_RELEASE = "type_release";
    private static final int ID_DAILY = 1000;
    private static final int ID_RELEASE = 1001;


    private Context context;

    public AlarmReceiver(Context context){
        this.context = context;
    }

    public AlarmReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MDB_DEBUG", "BROADCAST RECEIVED");
        String type = intent.getStringExtra(EXTRA_TYPE);
        Log.d("MDB_DEBUG", "EXTRA RECEIVED : " + type);
        if (type.equalsIgnoreCase(TYPE_DAILY)) {
            dailyNotification(context);
        }else {
            getReleaseToday(context);
        }

    }

    private Calendar getCalendarTime(String type){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, type.equalsIgnoreCase(TYPE_DAILY) ? 7 : 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

//        if (calendar.before(Calendar.getInstance())){
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
        return calendar;
    }

    public void setDailyNotif(Context context){
        Log.d("MDB_DEBUG", "BROADCAST SENT");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, TYPE_DAILY);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    getCalendarTime(TYPE_DAILY).getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String now = dateFormat.format(date);
        Log.d("MDB_DEBUG", String.valueOf(getCalendarTime(TYPE_DAILY).getTimeInMillis()));
        Log.d("MDB_DEBUG", String.valueOf(System.currentTimeMillis()));
    }

    public void setReleaseToday(Context context){
        Log.d("MDB_DEBUG", "BROADCAST SENT");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, TYPE_RELEASE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, getCalendarTime(TYPE_RELEASE).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void dailyNotification(Context context){
        int NOTIFICATION_ID = 1;
        String CHANNEL_ID = "channel_1";
        String CHANNEL_NAME = "daily_channel";

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_active_black_24dp))
                .setContentTitle(context.getResources().getString(R.string.daily_reminder))
                .setContentText(context.getResources().getString(R.string.daily_reminder_text))
                .setSubText(context.getResources().getString(R.string.daily_reminder_sub_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null){
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private void releaseTodayNotification(Context context, String title, String desc, int id){
        String CHANNEL_ID = "CHANNEL_2";
        String CHANNEL_NAME = "release today channel";

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_active_black_24dp))
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
    }

    private void getReleaseToday(final Context context){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String now = dateFormat.format(date);
        String dateCoba = "2019-01-31";

        String  URL = "https://api.themoviedb.org/3/discover/movie?api_key="
                + API_KEY
                + "&primary_release_date.gte="
                + now
                + "&primary_release_date.lte="
                + now;

        (new AsyncHttpClient()).get(URL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    //ArrayList<MovieItem> movieItems = new ArrayList<>();
                    int id = 2;
                    for (int i = 0; i < list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        String title =  movie.getString("title");
                        String desc = title + " " + context.getString(R.string.release_today_message);
                        releaseTodayNotification(context, title, desc, i);
                        id++;
                    }
                }catch (Exception e){
                    Log.d("MDB_DEBUG", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int req_code = type.equalsIgnoreCase(TYPE_DAILY) ? ID_DAILY : ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, req_code, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}

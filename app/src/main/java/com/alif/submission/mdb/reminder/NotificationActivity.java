package com.alif.submission.mdb.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.alif.submission.mdb.R;

public class NotificationActivity extends AppCompatActivity {

    SwitchCompat daily_reminder, release_today;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEdit;

    private AlarmReceiver alarmReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        daily_reminder = findViewById(R.id.daily_reminder_switch);
        release_today = findViewById(R.id.release_today_switch);

        alarmReceiver = new AlarmReceiver();
        sharedPreferences = getSharedPreferences("reminder", Context.MODE_PRIVATE);
        switchChanged();
        setSharedPreferences();
    }

    private void setSharedPreferences() {

        boolean daily = sharedPreferences.getBoolean("daily", false);
        boolean release = sharedPreferences.getBoolean("release_today",false);

        daily_reminder.setChecked(daily);
        release_today.setChecked(release);
    }

    private void switchChanged() {
        daily_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferencesEdit = sharedPreferences.edit();
                sharedPreferencesEdit.putBoolean("daily", isChecked);
                sharedPreferencesEdit.apply();
                if (isChecked) {
                    alarmReceiver.setDailyNotif(NotificationActivity.this);
                    Toast.makeText(NotificationActivity.this, "Daily Notif Activated", Toast.LENGTH_SHORT).show();
                }
                else {
                    alarmReceiver.cancelAlarm(NotificationActivity.this, AlarmReceiver.TYPE_DAILY);
                    Toast.makeText(NotificationActivity.this, "Daily Notif Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        release_today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferencesEdit = sharedPreferences.edit();
                sharedPreferencesEdit.putBoolean("release_today", isChecked);
                sharedPreferencesEdit.apply();
                if (isChecked) {
                    alarmReceiver.setReleaseToday(NotificationActivity.this);
                    Toast.makeText(NotificationActivity.this, "Release Today Activated", Toast.LENGTH_SHORT).show();
                }
                else {
                    alarmReceiver.cancelAlarm(NotificationActivity.this, AlarmReceiver.TYPE_RELEASE);
                    Toast.makeText(NotificationActivity.this, "Release Today deactivated", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

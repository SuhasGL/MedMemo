package com.example.medmemo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm extends AppCompatActivity implements View.OnClickListener {
    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        findViewById(R.id.set_Btn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText editText = findViewById(R.id.editText);
        EditText doseText = findViewById(R.id.dosage);
        TimePicker timePicker = findViewById(R.id.timePicker);

        Intent intent = new Intent(Alarm.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("todo", editText.getText().toString());
        intent.putExtra("doses", doseText.getText().toString());


        PendingIntent alarmIntent = PendingIntent.getBroadcast(Alarm.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch (view.getId()) {
            case R.id.set_Btn:
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);

                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cancelBtn:
                alarm.cancel(alarmIntent);
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
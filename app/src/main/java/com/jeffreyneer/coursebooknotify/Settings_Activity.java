package com.jeffreyneer.coursebooknotify;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Jeffrey Neer on 5/24/2016.
 */
public class Settings_Activity extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private Switch AlarmSwitch;
    private boolean AlarmSet;
    public final String DATABASE = "database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

          /* Retrieve a PendingIntent that will perform a broadcast
          * This of it like a sort of storage container for all the alarms for this particular class(or context)*/
        Intent alarmIntent = new Intent(Settings_Activity.this, AlarmReceiver.class);
        alarmIntent.setAction("com.jeffreyneer.coursebooknotify.alarm");
        pendingIntent = PendingIntent.getBroadcast(Settings_Activity.this, 0, alarmIntent, 0);

        //Stuff with alarm switch
        AlarmSwitch = (Switch) findViewById(R.id.AlarmSwitch);

        SharedPreferences database = getSharedPreferences(DATABASE, 0);
        AlarmSet = database.getBoolean("AlarmOn", false);

        if(AlarmSet){
            AlarmSwitch.setChecked(true);
        }else{
            AlarmSwitch.setChecked(false);
        }

        //TODO: REMOVE WHEN FINISHED WITH ALARM STUFF
        final Context TemporaryContext = this;

        //attach a listener to check for changes in state
        AlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Update the database
                    SharedPreferences database = getSharedPreferences(DATABASE, 0);
                    SharedPreferences.Editor databaseEditor = database.edit();
                    databaseEditor.putBoolean("AlarmOn", true);

                    databaseEditor.commit();
                    //activate the alarm
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    int interval = 10000;

                    manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_FIFTEEN_MINUTES,  AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);


                    Toast.makeText(TemporaryContext, "Alarm Set", Toast.LENGTH_SHORT).show();

                }else{
                    //update the database
                    SharedPreferences database = getSharedPreferences(DATABASE, 0);
                    SharedPreferences.Editor databaseEditor = database.edit();
                    databaseEditor.putBoolean("AlarmOn", false);
                    databaseEditor.commit();
                    
                    //Cancel the alarm
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    Toast.makeText(TemporaryContext, "Alarm Canceled", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}

package com.jeffreyneer.coursebooknotify;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;


/**
 * Created by Jeffrey Neer on 5/24/2016.
 */
public class Settings_Activity extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private Switch AlarmSwitch;
    private boolean AlarmSet;
    private int AlarmTimeSet;
    private Spinner AlarmSpiner;
    public final String DATABASE = "database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

          /* Retrieve a PendingIntent that will perform a broadcast
          * This of it like a sort of storage container for all the alarms for this particular class(or context)*/
        Intent alarmIntent = new Intent(Settings_Activity.this, AlarmReceiver.class);
        alarmIntent.setAction("com.jeffreyneer.coursebooknotify.alarm");
        pendingIntent = PendingIntent.getBroadcast(Settings_Activity.this, 11, alarmIntent, 0);

        //Stuff with alarm switch
        AlarmSwitch = (Switch) findViewById(R.id.AlarmSwitch);
        AlarmSpiner = (Spinner) findViewById(R.id.AlarmSpinner);

        //Populate spinner adapter and set adapter to spinner
        ArrayAdapter<CharSequence> AlarmSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.Alarm_Spinner, android.R.layout.simple_spinner_item);
        AlarmSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AlarmSpiner.setAdapter(AlarmSpinnerAdapter);





        SharedPreferences database = getSharedPreferences(DATABASE, 0);
        AlarmSet = database.getBoolean("AlarmOn", false);
        AlarmTimeSet = database.getInt("AlarmTime", 0);


        if(AlarmSet){
            AlarmSwitch.setChecked(true);
            AlarmSpiner.setEnabled(true);
            AlarmSpiner.setSelection(AlarmTimeSet);
        }else{
            AlarmSwitch.setChecked(false);
            AlarmSpiner.setEnabled(false);
            AlarmSpiner.setSelection(0);
        }

        final Context MainSettingsMenuContext = this;

        //attach a listener to check for changes in state
        AlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //Update the database
                    SharedPreferences database = getSharedPreferences(DATABASE, 0);
                    SharedPreferences.Editor databaseEditor = database.edit();
                    databaseEditor.putBoolean("AlarmOn", true);
                    databaseEditor.putInt("AlarmTime", 0);

                    databaseEditor.apply();
                    //activate the alarm
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    long interval = AlarmManager.INTERVAL_HALF_HOUR;
                    AlarmSpiner.setSelection(0);
                    AlarmSpiner.setEnabled(true);

                    manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval,  interval, pendingIntent);
                    ComponentName receiver = new ComponentName(MainSettingsMenuContext, BootReceiver.class);
                    PackageManager pm = MainSettingsMenuContext.getPackageManager();

                    pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                }else{
                    //update the database
                    SharedPreferences database = getSharedPreferences(DATABASE, 0);
                    SharedPreferences.Editor databaseEditor = database.edit();
                    databaseEditor.putBoolean("AlarmOn", false);
                    databaseEditor.apply();

                    //Disable the alarm spinner
                    AlarmSpiner.setEnabled(false);
                    AlarmSpiner.setSelection(0);

                    //Cancel the alarm
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);

                    ComponentName receiver = new ComponentName(MainSettingsMenuContext, BootReceiver.class);
                    PackageManager pm = MainSettingsMenuContext.getPackageManager();

                    pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                }

            }
        });

        AlarmSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SharedPreferences database = getSharedPreferences(DATABASE, 0);
                AlarmSet = database.getBoolean("AlarmOn", false);
                if(AlarmSet){
                    //Find the required interval between alarms
                    long interval;
                    switch(position){
                        case 0:
                            interval = AlarmManager.INTERVAL_HALF_HOUR;
                            break;
                        case 1:
                            interval = AlarmManager.INTERVAL_HOUR;
                            break;
                        case 2:
                            interval = AlarmManager.INTERVAL_HOUR * 3;
                            break;
                        case 3:
                            interval = AlarmManager.INTERVAL_HOUR * 6;
                            break;
                        case 4:
                            interval = AlarmManager.INTERVAL_DAY;
                            break;
                        default:
                            interval = AlarmManager.INTERVAL_DAY;
                            break;
                    }

                    //Save interval data to database
                    SharedPreferences.Editor databaseEditor = database.edit();
                    databaseEditor.putInt("AlarmTime", position);
                    databaseEditor.apply();
                    //Cancel any current alarms
                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(pendingIntent);
                    manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval,  interval, pendingIntent);

                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });





    }


}

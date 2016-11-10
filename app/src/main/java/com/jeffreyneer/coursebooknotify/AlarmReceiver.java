package com.jeffreyneer.coursebooknotify;

/**
 * Project: CourseBook Notify
 * Author: Jeffrey Neer
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class AlarmReceiver extends BroadcastReceiver {
    public boolean fail = false;
    public final String DATABASE = "database";

    Context AlarmContext;

    @Override
    public void onReceive(Context context, Intent intent){
        AlarmContext = context;
        ArrayList<SchoolClass> newschoolClasses = loadSchoolClassArray();
        CourseBookLookup cbLookup = new CourseBookLookup();
        cbLookup.execute(newschoolClasses);


    }







    private class CourseBookLookup extends AsyncTask<ArrayList<SchoolClass>, Void, ArrayList<SchoolClass>> {

        @Override
        protected ArrayList<SchoolClass> doInBackground(ArrayList<SchoolClass>... inputClassList) {
            ArrayList<SchoolClass> oldClassList = inputClassList[0];
            ArrayList<SchoolClass> updatedSchoolList = new ArrayList<>();
            for (int i = 0; i < oldClassList.size(); i++) {
                BufferedReader reader = null;
                StringBuilder builder = new StringBuilder();
                try {
                    String courseDatastuff = oldClassList.get(i).getmSchool() + oldClassList.get(i).getMcNumber() + "." + oldClassList.get(i).getMsNumber() + "." + oldClassList.get(i).getmSemeseter();

                    URL url = new URL("http://coursebook.utdallas.edu/" + courseDatastuff);


                    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        builder.append(line.trim());
                    }
                } catch (Exception e) {
                    return null;
                } finally {
                    if (reader != null) try {
                        reader.close();
                    } catch (IOException logOrIgnore) {
                    }
                }
                String Pagedata = builder.toString();
                String checkBeforeHTML = "transparent; \" title=\"";
                String checkAfterHTML = "\"></div> </td><td style=\"text-align: center;\">";
                int sizeOfCheckBeforeHTML = Pagedata.indexOf(checkBeforeHTML);
                if (sizeOfCheckBeforeHTML < 0) {
                    fail = true;
                    return null;
                }
                String HTMLAfterCheckBeforeHTML = Pagedata.substring(sizeOfCheckBeforeHTML + checkBeforeHTML.length());
                int sizeOfHTMLAfterCheckBeforeHTML = HTMLAfterCheckBeforeHTML.indexOf(checkAfterHTML);
                if (sizeOfHTMLAfterCheckBeforeHTML < 0) {
                    fail = true;
                    return null;
                }


                String ExtractedPrecentFullString = HTMLAfterCheckBeforeHTML.substring(0, sizeOfHTMLAfterCheckBeforeHTML);

                Log.d("AlarmReciever", "Old class precent filled: " + oldClassList.get(i).getmFilled());
                String OldPrecent = oldClassList.get(i).getmFilled();
                updatedSchoolList.add(oldClassList.get(i));
                updatedSchoolList.get(i).setmFilled(ExtractedPrecentFullString);
                Log.d("AlarmReciever", "New class precent filled: " + updatedSchoolList.get(i).getmFilled());
                if(OldPrecent.trim().equals("100% Filled")){
                    if(!updatedSchoolList.get(i).getmFilled().trim().equals("100% Filled")){
                        updatedSchoolList.get(i).setUpdatedFlag(true);
                    }
                }
            }
            return updatedSchoolList;

        }

        @Override
        protected void onPostExecute(ArrayList<SchoolClass> result) {

            // txt.setText(result);

            SharedPreferences database = AlarmContext.getSharedPreferences(DATABASE, 0);

            SharedPreferences.Editor databaseEditor = database.edit();

            for (int i = 0; i < result.size(); i++) {
                databaseEditor.putString("class_" + (i+1) + "_filled", result.get(i).getmFilled());
                if(result.get(i).getUpdatedFlag()){
                    result.get(i).setUpdatedFlag(false);

                    // Notification stuff.
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AlarmContext)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle("CourseBook Notify")
                            .setContentText(result.get(i).getmSchool().toUpperCase() + " " + result.get(i).getMcNumber() + "." + result.get(i).getMsNumber() + " is now open!");

                    Intent resultIntent = new Intent(AlarmContext, MainActivity.class);

                    PendingIntent resultPendingIntent = PendingIntent.getActivity(AlarmContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder.setContentIntent(resultPendingIntent);

                    // Sets an ID for the notification
                    // Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr = (NotificationManager) AlarmContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    // Builds the notification and issues it.
                    mNotifyMgr.notify(i, mBuilder.build());

                }
            }
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            databaseEditor.apply();

        }

    }

    ArrayList<SchoolClass> loadSchoolClassArray() {
        SharedPreferences database = AlarmContext.getSharedPreferences(DATABASE, 0);
        ArrayList<SchoolClass> newschoolClasses = new ArrayList<>();
        int numOfClasses = database.getInt("total_classes", 0);
        for (int i = 1; i < numOfClasses + 1; i++) {
            newschoolClasses.add(new SchoolClass(
                    database.getString("class_" + i + "_school", ""),
                    database.getString("class_" + i + "_cNumber", ""),
                    database.getString("class_" + i + "_sNumber", ""),
                    database.getString("class_" + i + "_semester", ""),
                    database.getString("class_" + i + "_filled", "")));
        }
        return newschoolClasses;
    }

}

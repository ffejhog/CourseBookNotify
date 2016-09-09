package com.jeffreyneer.coursebooknotify;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/** The main entry point for this android app. Handles the main interface and stuff
 * @author Jeffrey Neer
 * @version 1.0.0
 */

public class MainActivity extends AppCompatActivity {

    /** Used to determine if the network relieve function has failed */
    public boolean fail = false;
    /** Name of the SharedPreferences database */
    public final String DATABASE = "database";
    /** Number representing the OPEN_NEW_CLASS activity */
    static final int OPEN_NEW_CLASS = 1;
    /** Number representing the OPEN_SETTINGS activity */
    static final int OPEN_SETTINGS = 2;
    /** Create a variable for use with SharedPreferences */
    SharedPreferences database;
    /** The array containing the list of classes to lookup */
    ArrayList<SchoolClass> schoolClasses = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    SchoolClass_Adapter adapter;
    static myOnClickListener myOnClickListener;
    RecyclerView rvSchoolClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //sets the view to the main layout
        //Code adds the appropriate stuff for the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = getSharedPreferences(DATABASE, 0);

        myOnClickListener = new myOnClickListener();

        rvSchoolClass = (RecyclerView) findViewById(R.id.mainList);


        ArrayList<SchoolClass> newschoolClasses = loadSchoolClassArray();
        schoolClasses.clear();
        schoolClasses.addAll(newschoolClasses);

        adapter = new SchoolClass_Adapter(schoolClasses);

        rvSchoolClass.setAdapter(adapter);


        rvSchoolClass.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                CourseBookLookup cbLookup = new CourseBookLookup();
                cbLookup.execute(schoolClasses);

            }
        });


    }

    public int viewToDelete = -1;

    class myOnClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            viewToDelete = (int) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Do you want to delete this class?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            return true;
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    deleteClassFromDatabase(viewToDelete);
                    viewToDelete = -1;
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    viewToDelete = -1;
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings_Activity.class);
                startActivityForResult(intent, OPEN_SETTINGS);
                return true;
            case R.id.action_refresh:
                CourseBookLookup cbLookup = new CourseBookLookup();
                cbLookup.execute(schoolClasses);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void openAddClass_Activity(View view) {
        Intent intent = new Intent(this, AddClass_Activity.class);
        startActivityForResult(intent, OPEN_NEW_CLASS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onResume() {
        this.adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_NEW_CLASS) {
            if (resultCode == RESULT_OK) {
                ArrayList<SchoolClass> newschoolClasses = loadSchoolClassArray();
                schoolClasses.clear();
                schoolClasses.addAll(newschoolClasses);
                this.adapter.notifyDataSetChanged();
            }
        }
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

                updatedSchoolList.add(oldClassList.get(i));
                updatedSchoolList.get(i).setmFilled(ExtractedPrecentFullString);
            }
            return updatedSchoolList;

        }

        @Override
        protected void onPostExecute(ArrayList<SchoolClass> result) {

            // txt.setText(result);

            SharedPreferences database = getSharedPreferences(DATABASE, 0);

            SharedPreferences.Editor databaseEditor = database.edit();

            for (int i = 0; i < result.size(); i++) {
                databaseEditor.putString("class_" + (i+1) + "_filled", result.get(i).getmFilled());

            }

            databaseEditor.apply();
            ArrayList<SchoolClass> newschoolClasses = loadSchoolClassArray();
            schoolClasses.clear();
            schoolClasses.addAll(newschoolClasses);
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);

        }

    }

    ArrayList<SchoolClass> loadSchoolClassArray() {
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

    void deleteClassFromDatabase(int numOfClassToDelete){
        SharedPreferences database = getSharedPreferences(DATABASE, 0);

        SharedPreferences.Editor databaseEditor = database.edit();

        int numOfClasses = database.getInt("total_classes", 0);

        for (int i = numOfClassToDelete+1; i < numOfClasses; i++) {
            databaseEditor.putString("class_" + i + "_school",  database.getString("class_" + (i+1) + "_school", ""));
            databaseEditor.putString("class_" + i + "_cNumber",  database.getString("class_" + (i+1) + "_cNumber", ""));
            databaseEditor.putString("class_" + i + "_sNumber",  database.getString("class_" + (i+1) + "_sNumber", ""));
            databaseEditor.putString("class_" + i + "_semester",  database.getString("class_" + (i+1) + "_semester", ""));
            databaseEditor.putString("class_" + i + "_filled",  database.getString("class_" + (i+1) + "_filled", ""));
        }
        databaseEditor.remove("class_" + numOfClasses + "_school");
        databaseEditor.remove("class_" + numOfClasses + "_cNumber");
        databaseEditor.remove("class_" + numOfClasses + "_sNumber");
        databaseEditor.remove("class_" + numOfClasses + "_semester");
        databaseEditor.remove("class_" + numOfClasses + "_filled");

        databaseEditor.putInt("total_classes", numOfClasses-1);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
        databaseEditor.apply();

        ArrayList<SchoolClass> newschoolClasses = loadSchoolClassArray();

        schoolClasses.clear();
        schoolClasses.addAll(newschoolClasses);
        adapter.notifyDataSetChanged();

    }

}


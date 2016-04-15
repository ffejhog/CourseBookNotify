package com.jeffreyneer.coursebooknotify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final String DATABASE = "database";
    static final int OPEN_NEW_CLASS = 1;
    SharedPreferences database;
    ArrayList<SchoolClass> schoolClasses = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    SchoolClass_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = getSharedPreferences(DATABASE, 0);
        RecyclerView rvSchoolClass = (RecyclerView) findViewById(R.id.mainList);


        int numOfClasses = database.getInt("total_classes", 0);
        for(int i = 1; i< numOfClasses+1; i++){
            schoolClasses.add(new SchoolClass(
                    database.getString("class_" + i +"_school", ""),
                    database.getString("class_" + i +"_cNumber", ""),
                    database.getString("class_" + i +"_sNumber", ""),
                    database.getString("class_" + i +"_semester", ""),
                    database.getString("class_" + i +"_filled", "")));
        }

        //Init School items

        adapter = new SchoolClass_Adapter(schoolClasses);

        rvSchoolClass.setAdapter(adapter);

        rvSchoolClass.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);
                //Call async method for refresh
            }
        });
    }


    public void openAddClass_Activity(View view){
        Intent intent = new Intent(this, AddClass_Activity.class);
        startActivityForResult(intent, OPEN_NEW_CLASS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume(){
        this.adapter.notifyDataSetChanged();
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_NEW_CLASS) {
            if (resultCode == RESULT_OK) {
                ArrayList<SchoolClass> newschoolClasses = new ArrayList<>();
                int numOfClasses = database.getInt("total_classes", 0);
                for(int i = 1; i< numOfClasses+1; i++){
                    newschoolClasses.add(new SchoolClass(
                            database.getString("class_" + i +"_school", ""),
                            database.getString("class_" + i +"_cNumber", ""),
                            database.getString("class_" + i +"_sNumber", ""),
                            database.getString("class_" + i +"_semester", ""),
                            database.getString("class_" + i +"_filled", "")));
                }
                schoolClasses.clear();
                schoolClasses.addAll(newschoolClasses);
                this.adapter.notifyDataSetChanged();
            }
        }
    }
}

package com.jeffreyneer.coursebooknotify;

import android.content.Intent;
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

    ArrayList<SchoolClass> schoolClasses = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvSchoolClass = (RecyclerView) findViewById(R.id.mainList);

        //Init School items
        schoolClasses.add(new SchoolClass("lit", "3317", "002", "16f", "100% Filled"));
        schoolClasses.add(new SchoolClass("cs", "1234", "001", "16f", "75% Filled"));
        schoolClasses.add(new SchoolClass("se", "5645", "003", "17s", "80% Filled"));
        schoolClasses.add(new SchoolClass("mech", "5783", "501", "17s", "15% Filled"));
        schoolClasses.add(new SchoolClass("atec", "4534", "502", "16f", "45% Filled"));
        schoolClasses.add(new SchoolClass("mkt", "4534", "H02", "16f", "78% Filled"));
        schoolClasses.add(new SchoolClass("opre", "7864", "654", "16f", "78% Filled"));
        schoolClasses.add(new SchoolClass("mis", "7835", "785", "16f", "78% Filled"));
        schoolClasses.add(new SchoolClass("bis", "4537", "002", "16f", "45% Filled"));
        schoolClasses.add(new SchoolClass("ecs", "7853", "001", "16f", "53% Filled"));



        SchoolClass_Adapter adapter = new SchoolClass_Adapter(schoolClasses);

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
        startActivity(intent);
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



}

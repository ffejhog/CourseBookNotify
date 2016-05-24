package com.jeffreyneer.coursebooknotify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AddClass_Activity extends AppCompatActivity {
    public final String DATABASE = "database";
    public boolean fail = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Semester_Spinner_Object> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getSemesterList());
        Spinner spinner = (Spinner) findViewById(R.id.spinner_semester);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }

    public void searchForClassPercent(View view){

        EditText schoolName = (EditText) findViewById(R.id.editText_school);
        EditText classNumber = (EditText) findViewById(R.id.editText_classnumber);
        EditText sectionNumber = (EditText) findViewById(R.id.editText_sectionnumber);

        TextView percentOutTextView = (TextView) findViewById(R.id.textView_PercentOutput);

        if(schoolName.getText().toString().toLowerCase().trim().equals("") || classNumber.getText().toString().trim().equals("") || sectionNumber.getText().toString().trim().equals("")){
            percentOutTextView.setText("Please fill in all boxes");
            return;
        }


        CourseBookLookup cbLookup = new CourseBookLookup();
        Semester_Spinner_Object semester_selected = (Semester_Spinner_Object) ((Spinner) findViewById(R.id.spinner_semester)).getSelectedItem();
        cbLookup.execute(schoolName.getText().toString().toLowerCase().trim(),classNumber.getText().toString().trim(),sectionNumber.getText().toString().trim(),semester_selected.getNameCode());
        try {
            cbLookup.get();
            if (fail) {
                percentOutTextView.setText("Class not found");
                fail = false;
            } else {
                Intent intent = this.getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }catch (Exception e){

        }

    }

    private class CourseBookLookup extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();
            try {
            String courseDatastuff = params[0] + params[1] + "." + params[2] + "." + params[3];
            URL url = new URL("http://coursebook.utdallas.edu/" + courseDatastuff);



                reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                for (String line; (line = reader.readLine()) != null; ) {
                    builder.append(line.trim());
                }
            }catch( Exception e){
                return "ERROR";
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
                fail=true;
                return "NULL";
            }
            String HTMLAfterCheckBeforeHTML = Pagedata.substring(sizeOfCheckBeforeHTML + checkBeforeHTML.length());
            int sizeOfHTMLAfterCheckBeforeHTML = HTMLAfterCheckBeforeHTML.indexOf(checkAfterHTML);
            if (sizeOfHTMLAfterCheckBeforeHTML < 0) {
                fail=true;
                return "NULL";
            }

            //TODO: Remove redundancy after class is tested
            String ExtractedPrecentFullString = HTMLAfterCheckBeforeHTML.substring(0, sizeOfHTMLAfterCheckBeforeHTML);

            return ExtractedPrecentFullString;
        }

        @Override
        protected void onPostExecute(String result) {

             // txt.setText(result);
            if(result=="NULL"){
                return;
            }

            EditText schoolName = (EditText) findViewById(R.id.editText_school);
            EditText classNumber = (EditText) findViewById(R.id.editText_classnumber);
            EditText sectionNumber = (EditText) findViewById(R.id.editText_sectionnumber);
            Semester_Spinner_Object semester_selected = (Semester_Spinner_Object) ((Spinner) findViewById(R.id.spinner_semester)).getSelectedItem();

            SharedPreferences database = getSharedPreferences(DATABASE, 0);
            int numOfClasses = database.getInt("total_classes", 0);
            SharedPreferences.Editor databaseEditor = database.edit();
            databaseEditor.putInt("total_classes", numOfClasses+1);

            databaseEditor.putString("class_" + (numOfClasses+1) +"_school", schoolName.getText().toString().toUpperCase());
            databaseEditor.putString("class_" + (numOfClasses+1) +"_cNumber", classNumber.getText().toString());
            databaseEditor.putString("class_" + (numOfClasses+1) +"_sNumber", sectionNumber.getText().toString());
            databaseEditor.putString("class_" + (numOfClasses+1) +"_semester", semester_selected.getNameCode());
            databaseEditor.putString("class_" + (numOfClasses+1) +"_filled", result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            databaseEditor.apply();


        }

    }

    List<Semester_Spinner_Object> getSemesterList(){
        String[] semestersNames = getResources().getStringArray(R.array.semester_array_names);
        String[] semestersNamecodes = getResources().getStringArray(R.array.semester_array_name_Codes);

        ArrayList<Semester_Spinner_Object> semesterObjectList = new ArrayList<>();

        for(int i =0; i< semestersNames.length;i++) {

            semesterObjectList.add(new Semester_Spinner_Object(semestersNames[i],semestersNamecodes[i]));
        }
        return semesterObjectList;
    }


}




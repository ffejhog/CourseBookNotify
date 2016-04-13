package com.jeffreyneer.coursebooknotify;

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

public class AddClass_Activity extends AppCompatActivity {




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
        Spinner semester = (Spinner) findViewById(R.id.spinner_semester);

        TextView percentOutTextView = (TextView) findViewById(R.id.textView_PercentOutput);
        CourseBookLookup cbLookup = new CourseBookLookup();
        Semester_Spinner_Object semester_selected = (Semester_Spinner_Object) ((Spinner) findViewById(R.id.spinner_semester)).getSelectedItem();
        cbLookup.execute(schoolName.getText().toString(),classNumber.getText().toString(),sectionNumber.getText().toString(),semester_selected.getNameCode());


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
                System.out.println("Class does not exist");
                return "Class does not exist";
            }
            String HTMLAfterCheckBeforeHTML = Pagedata.substring(sizeOfCheckBeforeHTML + checkBeforeHTML.length());
            int sizeOfHTMLAfterCheckBeforeHTML = HTMLAfterCheckBeforeHTML.indexOf(checkAfterHTML);
            if (sizeOfHTMLAfterCheckBeforeHTML < 0) {
                System.out.println("Class does not exist");
                return "Class does not exist";
            }

            //TODO: Remove redundancy after class is tested
            String ExtractedPrecentFullString = HTMLAfterCheckBeforeHTML.substring(0, sizeOfHTMLAfterCheckBeforeHTML);

            return ExtractedPrecentFullString;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView percentOutTextView = (TextView) findViewById(R.id.textView_PercentOutput);
            percentOutTextView.setText(result); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
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




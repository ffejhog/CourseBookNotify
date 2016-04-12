package com.jeffreyneer.coursebooknotify;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AddClass_Activity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);


    }

    public void searchForClassPercent(View view){

        EditText schoolName = (EditText) findViewById(R.id.editText_school);
        EditText classNumber = (EditText) findViewById(R.id.editText_classnumber);
        EditText sectionNumber = (EditText) findViewById(R.id.editText_sectionnumber);
        EditText semester = (EditText) findViewById(R.id.editText_semester);


        CourseBookLookup cbLookup = new CourseBookLookup();
        cbLookup.execute(schoolName.getText().toString(),classNumber.getText().toString(),sectionNumber.getText().toString(),semester.getText().toString());


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
                return "ERROR";
            }
            String HTMLAfterCheckBeforeHTML = Pagedata.substring(sizeOfCheckBeforeHTML + checkBeforeHTML.length());
            int sizeOfHTMLAfterCheckBeforeHTML = HTMLAfterCheckBeforeHTML.indexOf(checkAfterHTML);
            if (sizeOfHTMLAfterCheckBeforeHTML < 0) {
                System.out.println("Class does not exist");
                return "ERROR";
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


}

package com.jeffreyneer.coursebooknotify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Jeffrey Neer on 4/12/2016.
 */

public class CoursebookWebParser {

    String school, classNumber, sectionNumber, semester;


    CoursebookWebParser() {
        String school = "lit";
        String classNumber = "3317";
        String sectionNumber = "002";
        String semester = "16f";

    }

    CoursebookWebParser(String schoolInput, String classNumberInput, String sectionNumberInput, String semesterInput) {
        school = schoolInput;
        classNumber = classNumberInput;
        sectionNumber = sectionNumberInput;
        semester = semesterInput;
    }


    String retrieveCourseInfo() throws Exception {
        String courseDatastuff = school + classNumber + "." + sectionNumber + "." + semester;
        URL url = new URL("http://coursebook.utdallas.edu/" + courseDatastuff);
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null; ) {
                builder.append(line.trim());
            }
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
            return "-1";
        }
        String HTMLAfterCheckBeforeHTML = Pagedata.substring(sizeOfCheckBeforeHTML + checkBeforeHTML.length());
        int sizeOfHTMLAfterCheckBeforeHTML = HTMLAfterCheckBeforeHTML.indexOf(checkAfterHTML);
        if (sizeOfHTMLAfterCheckBeforeHTML < 0) {
            System.out.println("Class does not exist");
            return "-1";
        }

        //TODO: Remove redundancy after class is tested
        String ExtractedPrecentFullString = HTMLAfterCheckBeforeHTML.substring(0, sizeOfHTMLAfterCheckBeforeHTML);

        return ExtractedPrecentFullString;


    }
}





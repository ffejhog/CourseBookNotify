package com.jeffreyneer.coursebooknotify;

/**
 * Created by Jeffrey Neer on 4/12/2016.
 */
public class Semester_Spinner_Object {
    public String name;
    public String nameCode;

    Semester_Spinner_Object(String inname, String innameCode){
        this.name = inname;
        this.nameCode = innameCode;

    }

    public String toString(){
        return this.name;
    }

    public String getNameCode(){
        return this.nameCode;
    }

}

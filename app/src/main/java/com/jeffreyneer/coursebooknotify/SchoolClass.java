package com.jeffreyneer.coursebooknotify;

/**
 * Created by Jeffrey Neer on 4/13/2016.
 */
public class SchoolClass {
    private String mSchool, mcNumber, msNumber, mSemeseter, mFilled;
    private boolean updatedFlag;

    public String getmSchool() {
        return mSchool;
    }

    public String getMcNumber() {
        return mcNumber;
    }

    public String getMsNumber() {
        return msNumber;
    }

    public String getmSemeseter() {
        return mSemeseter;
    }

    public String getmFilled() { return mFilled; }

    public boolean getUpdatedFlag() { return updatedFlag;}


    public void setmFilled(String input) {
        mFilled = input;
    }

    public void setUpdatedFlag(Boolean input) {updatedFlag = input;}

    public SchoolClass(String school, String cNumber, String sNumber, String semester, String filled){
        mSchool = school;
        mcNumber = cNumber;
        msNumber = sNumber;
        mSemeseter = semester;
        mFilled = filled;
        updatedFlag = false;
    }




}

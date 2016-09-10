package com.jeffreyneer.coursebooknotify;


/** The object that holds all the the data for a single class to monitor
 * @author Jeffrey Neer
 * @version 1.0.0
 */
public class SchoolClass {
    private String mSchool, mcNumber, msNumber, mSemeseter, mFilled;
    /** Checks to see if the object was updated during a refresh or alarm */
    private boolean updatedFlag;

    /** Get the School the class is in
     * @return A string representation of the school the class is apart of (ex. CS, SE, LIT, etc...)
     */
    public String getmSchool() {
        return mSchool;
    }
    /** Get the Number of the class
     * @return A string representation of the number of the class (ex. 1337, 2035, 3345, etc...)
     */
    public String getMcNumber() {
        return mcNumber;
    }
    /** Get the Section number of the class
     * @return A string representation of the section number of the class (ex. 501, 001, 002, etc...)
     */
    public String getMsNumber() {
        return msNumber;
    }
    /** Get the semester the class is in
     * @return A string representation of the semester the class is apart of (ex. 16f, 17u, 18s, etc...)
     */
    public String getmSemeseter() {
        return mSemeseter;
    }
    /** Get the percentage the class is full
     * @return A string representation of the percentage the class is full (ex. 95% Filled, 15% Filled, 100% Filled, etc...)
     */
    public String getmFilled() { return mFilled; }
    /** Returns whether the object has been updated during an alarm(for notification purposes
     * @return Returns true if the object had been updated
     */
    public boolean getUpdatedFlag() { return updatedFlag;}

    /** Allows the anount the class is filled to be set
     * @param input the new amount the class is filled
     */
    public void setmFilled(String input) {
        mFilled = input;
    }

    /** Sets whether the object had been updated in an alarm or not(for notification purposes)
     * @param input True - the object has been updated
     */
    public void setUpdatedFlag(Boolean input) {updatedFlag = input;}

    /** The constructor for a class object
     * @param school The school the class is in (ex. CS, SE, LIT, etc...)
     * @param cNumber The number of the class (ex. 1337, 2035, 3345, etc...)
     * @param sNumber The section number of the class (ex. 501, 001, 002, etc...)
     * @param semester The semester of the class (ex. 16f, 17u, 18s, etc...)
     * @param filled The filled percentage of the class (ex. 95% Filled, 15% Filled, 100% Filled, etc...)
     */
    public SchoolClass(String school, String cNumber, String sNumber, String semester, String filled){
        mSchool = school;
        mcNumber = cNumber;
        msNumber = sNumber;
        mSemeseter = semester;
        mFilled = filled;
        updatedFlag = false;
    }




}

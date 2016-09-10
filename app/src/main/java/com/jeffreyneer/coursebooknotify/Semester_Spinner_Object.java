package com.jeffreyneer.coursebooknotify;

/** A spinner object for the semester spinner on the add new class page.
 * @author Jeffrey Neer
 * @version 1.0.0
 */
public class Semester_Spinner_Object {
    /** The name of the semester*/
    public String name;
    /** The name code of the semester(ex. 15s, 16u, 18f,. etc...) */
    public String nameCode;

    /** Default constructor for the Semester Spinner objects
     *  @param inname The incoming name of the semester
     *  @param innameCode The incoming name code of the semester
     */
    Semester_Spinner_Object(String inname, String innameCode){
        this.name = inname;
        this.nameCode = innameCode;

    }

    /**Returns a string representation of the semester
     * @return The string representing the semester (Ex Fall 2016, Summer 2017, etc...)
     */

    public String toString(){
        return this.name;
    }

    /**Returns a string representation of the semester code
     * @return The string representing the semester code(Ex 16f, 17u, etc...)
     */
    public String getNameCode(){
        return this.nameCode;
    }

}

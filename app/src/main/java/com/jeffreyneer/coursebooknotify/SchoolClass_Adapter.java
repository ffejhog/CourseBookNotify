package com.jeffreyneer.coursebooknotify;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Project: CourseBook Notify
 * Author: Jeffrey Neer
 */
public class SchoolClass_Adapter extends RecyclerView.Adapter<SchoolClass_Adapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access



    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView schoolClassSection;
        public TextView semesterHolder;
        public TextView filledHolder;
        public View mView;

        public ViewHolder(View itemView) {
        super(itemView);
            schoolClassSection = (TextView) itemView.findViewById(R.id.school_number_section);
            semesterHolder = (TextView) itemView.findViewById(R.id.semester);
            filledHolder = (TextView) itemView.findViewById(R.id.Fill_amount);
            mView = itemView;


        }



    }



    //Store List of objects
    private List<SchoolClass> mSchoolClass;


    public SchoolClass_Adapter(List<SchoolClass> SchoolClass){
        mSchoolClass = SchoolClass;
    }

    @Override
    public SchoolClass_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate layout
        View schoolClassView = inflater.inflate(R.layout.item_class, parent, false);
        schoolClassView.setOnLongClickListener(MainActivity.myOnClickListener);
        //Return a holder instance
        ViewHolder viewHolder = new ViewHolder(schoolClassView);


        return viewHolder;

        }
    public void onBindViewHolder(SchoolClass_Adapter.ViewHolder viewHolder, int position){
        //Get object based on position
        SchoolClass schoolClass = mSchoolClass.get(position);

        //Set items based on object
        TextView textView = viewHolder.schoolClassSection;
        String setText = schoolClass.getmSchool() + " " + schoolClass.getMcNumber() + "." + schoolClass.getMsNumber();
        textView.setText(setText);

        TextView semesterText = viewHolder.semesterHolder;
        String outputTextLetter = schoolClass.getmSemeseter().substring(2);
        String outputText;
        if(outputTextLetter.equals("f")){
            outputText = "Fall 20" +  schoolClass.getmSemeseter().substring(0,2);
        }else if (outputTextLetter.equals("s")){
            outputText = "Spring 20" +  schoolClass.getmSemeseter().substring(0,2);
        }else{
            outputText = "Summer 20" +  schoolClass.getmSemeseter().substring(0,2);
        }
        semesterText.setText(outputText);

        TextView filledText = viewHolder.filledHolder;
        filledText.setText(schoolClass.getmFilled());

        viewHolder.mView.setTag(position);
    }
    //Return total number of items
    @Override
    public int getItemCount(){
        return mSchoolClass.size();
    }

}

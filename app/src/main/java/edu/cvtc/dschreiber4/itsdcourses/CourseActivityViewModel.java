package edu.cvtc.dschreiber4.itsdcourses;

import android.os.Bundle;
import androidx.lifecycle.ViewModel;

public class CourseActivityViewModel extends ViewModel {

    //Constants
    public static final String ORIGINAL_COURSE_TITLE = "edu.cvtc.dschreiber4.itsdcourses.ORIGINAL_COURSE_TITLE";
    public static final String ORIGINAL_COURSE_DESCRIPTION = "edu.cvtc.dschreiber4.itsdcourses.ORIGINAL_COURSE_DESCRIPTION";

    //Attributes
    public String mOriginalCourseTitle;
    public String mOriginalCourseDescription;
    public boolean mIsNewlyCreated = true;

    //Methods
    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_COURSE_TITLE, mOriginalCourseTitle);
        outState.putString(ORIGINAL_COURSE_DESCRIPTION, mOriginalCourseDescription);
    }

    public void restoreState(Bundle inState) {
        mOriginalCourseTitle = inState.getString(ORIGINAL_COURSE_TITLE);
        mOriginalCourseDescription = inState.getString(ORIGINAL_COURSE_DESCRIPTION);
    }

}

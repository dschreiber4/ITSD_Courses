package edu.cvtc.dschreiber4.itsdcourses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import edu.cvtc.dschreiber4.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class DataManager {

    //Attributes
    private static DataManager ourInstance = null;
    private List<CourseInfo> mCourses = new ArrayList<>();

    //Methods
    //Get instance
    public static DataManager getInstance(){
        if (ourInstance == null) {
        ourInstance = new DataManager();
        }
        return ourInstance;
    }

    //Return the list array of courses
    public List<CourseInfo> getCourses() {
        return mCourses;
    }

    //Use the cursor as an object to loop through the table and records
    private static void loadCoursesFromDatabase(Cursor cursor) {

        //Retrieve the field locations of of items in the db
        int listTitlePosition = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        int listDescriptionPosition = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
        int idPosition = cursor.getColumnIndex(CourseInfoEntry._ID);

        //Create and clear a DataManager instance
        DataManager dm = getInstance();
        dm.mCourses.clear();

        //Loop through the cursor rows and add new object to the array.
        while (cursor.moveToNext()) {
            String listTitle = cursor.getString(listTitlePosition);
            String listDescription = cursor.getString(listDescriptionPosition);
            int id = cursor.getInt(idPosition);

            CourseInfo list = new CourseInfo(id, listTitle, listDescription);

            dm.mCourses.add(list);
        }
        //Close cursor
        cursor.close();
    }

    static void loadFromDatabase(ITSDCoursesOpenHelper dbHelper) {
        //Open the db in read mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Create a list array to return
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                CourseInfoEntry._ID
        };

        //Create an order field
        String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;

        //Populate the cursor with the query results
        final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME,
                courseColumns, null, null, null, null,
                courseOrderBy);

        //Load the array list
        loadCoursesFromDatabase(courseCursor);
    }

    public int createNewCourse() {
        //Create and empty object for use on the activity screen.
        CourseInfo course = new CourseInfo(null ,null);

        //Add to course object
        mCourses.add(course);

        //return the size of the array.
        return mCourses.size();
    }

    public void removeCourse(int index) {
        mCourses.remove(index);
    }
}

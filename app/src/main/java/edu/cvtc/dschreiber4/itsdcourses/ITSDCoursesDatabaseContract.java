package edu.cvtc.dschreiber4.itsdcourses;

import android.provider.BaseColumns;

public final class ITSDCoursesDatabaseContract {

    //Private Constructor
    private ITSDCoursesDatabaseContract() {

    }

    public static final class CourseInfoEntry implements BaseColumns {
        //Constants for tables and fields
        public static final  String TABLE_NAME = "course_info";
        public static final  String COLUMN_COURSE_TITLE = "course_title";
        public static final  String COLUMN_COURSE_DESCRIPTION = "course_description";

        //Constants for index names and values
        public static final String INDEX1 = TABLE_NAME + "_index1";
        public static final String SQL_CREATE_INDEX1 =
                "CREATE_INDEX " + INDEX1 + " ON " + TABLE_NAME +
                    "(" + COLUMN_COURSE_TITLE + ")";

        //Constant to create the table using table name, fields, and id
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + "INTEGER PRIMARY KEY, " +
                        COLUMN_COURSE_TITLE + " TEXT NOT NULL" +
                        COLUMN_COURSE_DESCRIPTION + " TEXT";
    }
}

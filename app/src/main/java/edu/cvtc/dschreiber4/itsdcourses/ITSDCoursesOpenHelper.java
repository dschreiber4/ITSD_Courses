package edu.cvtc.dschreiber4.itsdcourses;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import edu.cvtc.dschreiber4.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class ITSDCoursesOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ITSDCourses_dschreiber4.db";
    public static final int DATABASE_VERSION = 1;

    public ITSDCoursesOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(CourseInfoEntry.SQL_CREATE_INDEX1);

        ITSDCoursesDataWorker worker = new ITSDCoursesDataWorker(db);
        worker.insertCourses();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

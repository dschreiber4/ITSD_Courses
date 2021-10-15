package edu.cvtc.dschreiber4.itsdcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import edu.cvtc.dschreiber4.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class CourseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    //Constants
    public static final String COURSE_ID =
            "edu.cvtc.dschreiber4.itsdcourses.COURSE_ID";
    public static final String ORIGINAL_COURSE_TITLE =
            "edu.cvtc.dschreiber4.itsdcourses.ORIGINAL_COURSE_TITLE";
    public static final String ORIGINAL_COURSE_DESCRIPTION =
            "edu.cvtc.dschreiber4.itsdcourses.ORIGINAL_COURSE_DESCRIPTION";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_COURSES = 0;

    //Initialize the new CourseInfo to empty
    private CourseInfo mCourse = new CourseInfo(0, "", "");

    //Attributes
    private boolean mIsNewCourse;
    private boolean mIsCancelling;
    private int mCourseId;
    private int mCourseTitlePosition;
    private int mCourseDescriptionPosition;
    private String mOriginalCourseTitle;
    private String mOriginalCourseDescription;

    //Objects
    private EditText mTextCourseTitle;
    private EditText mTextCourseDescription;
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private Cursor mCourseCursor;

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        //DBOpenHelper instance
        mDbOpenHelper = new ITSDCoursesOpenHelper(this);

        readDisplayStateValues();

        //Check the state for null and save the value or restore the original value
        if (savedInstanceState == null) {
            saveOriginalCourseValues();
        }else {
            restoreOriginalCourseValues(savedInstanceState);
        }

        //Object references
        mTextCourseTitle = findViewById(R.id.text_course_title);
        mTextCourseDescription = findViewById(R.id.text_course_description);

        // If it is not a new course, load the course into the layout
        if (!mIsNewCourse) {
            LoaderManager.getInstance(this).initLoader(LOADER_COURSES, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Action bar items
        int id = item.getItemId();
        //No action if id exists
        if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Did the user cancel the process?
        if (mIsCancelling) {
            // Is this a new course?
            if (mIsNewCourse) {
                // Delete the new course.
                deleteCourseFromDatabase();
            } else {
                // Put the original values on the screen.
                storePreviousCourseValues();
            }
        } else {
            // Save the data when leaving the activity.
            saveCourse();
        }
    }

    private void deleteCourseFromDatabase() {
        //Create selections
        final String selection = CourseInfoEntry._ID + " = ? ";
        final String[] selectionArgs = {Integer.toString(mCourseId)};

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override public String loadInBackground() {
                // Get connection to the database. Use the writable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
                // Call the delete method
                db.delete(CourseInfoEntry.TABLE_NAME, selection, selectionArgs);
                return null;
            }
        };    task.loadInBackground();
    }

    private void saveCourse() {
        //Get values from the layout
        String courseTitle = mTextCourseTitle.getText().toString();
        String courseDescription = mTextCourseDescription.getText().toString();

        //Call the method to write to the DB
        saveCourseToDatabase(courseTitle, courseDescription);
    }

    private void saveCourseToDatabase(String courseTitle, String courseDescription) {
        //Create selections
        final String selection = CourseInfoEntry._ID + " = ? ";
        final String[] selectionArgs = {Integer.toString(mCourseId)};

        //Use a ContentValues object to put info into
        ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, courseTitle);
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, courseDescription);

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override public String loadInBackground() {
                // Get connection to the database. Use the writable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
                // Call the delete method
                db.update(CourseInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                return null;
            }
        };    task.loadInBackground();
    }

    private void displayCourse() {
        //Get the values based on column positions
        String courseTitle = mCourseCursor.getString(mCourseTitlePosition);
        String courseDescription = mCourseCursor.getString(mCourseDescriptionPosition);

        //Use the info to populate the layout
        mTextCourseTitle.setText(courseTitle);
        mTextCourseDescription.setText(courseDescription);
    }

    private void restoreOriginalCourseValues(Bundle savedInstanceState) {
        //Get original values for existing courses
        mOriginalCourseTitle = savedInstanceState.getString(ORIGINAL_COURSE_TITLE);
        mOriginalCourseDescription = savedInstanceState.getString(ORIGINAL_COURSE_DESCRIPTION);
    }

    private void saveOriginalCourseValues() {
        //Save values if course is new
        if (!mIsNewCourse) {
            mOriginalCourseTitle = mCourse.getTitle();
            mOriginalCourseDescription = mCourse.getDescription();
        }
    }

    private void readDisplayStateValues() {

        //Pass the intent into the activity
        Intent intent = getIntent();

        //Get the course id passed into the intent
        mCourseId = intent.getIntExtra(COURSE_ID, ID_NOT_SET);

        //Create a new id if none set
        mIsNewCourse = mCourseId == ID_NOT_SET;
        if(mIsNewCourse) {
            createNewCourse();
        }
    }

    private void createNewCourse() {

        //Create the ContentValues object to hold the DB fields
        ContentValues values = new ContentValues();

        //set the values for a new course to empty
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, "");
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, "");

        //Connect to the DB
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        //Insert the new row into the DB and assign the new id.
        mCourseId = (int)db.insert(CourseInfoEntry.TABLE_NAME, null, values);

    }

    private void storePreviousCourseValues() {
        mCourse.setTitle(mOriginalCourseTitle);
        mCourse.setDescription(mOriginalCourseDescription);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader (int id, @Nullable Bundle args) {
        // Create a local cursor loader
        CursorLoader loader = null;

        //Check if the id is in loader
        if (id == LOADER_COURSES) {
            loader = createLoaderCourses();
        }
        return loader;
    }

    private CursorLoader createLoaderCourses() {
        return new CursorLoader(this) {
            @Override
            public Cursor loadInBackground() {
                //Open DB connection
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                //Set the new id
                String selection = ITSDCoursesDatabaseContract.CourseInfoEntry._ID + " = ? ";
                String[] selectionArgs = {Integer.toString(mCourseId)};

                //Create column lists you are pulling from the DB
                String[] courseColumns = {
                        CourseInfoEntry.COLUMN_COURSE_TITLE,
                        CourseInfoEntry.COLUMN_COURSE_DESCRIPTION
                };
                //Fill the cursor
                return db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                        selection, selectionArgs,null, null, null);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Check to see if this is your cursor for your loader
        if (loader.getId() == LOADER_COURSES) {
            loadFinishedCourses(data);
        }
    }

    private void loadFinishedCourses(Cursor data) {

        // Populate cursor with the data
        mCourseCursor = data;
        // Get the positions of the fields in the cursor for the layout
        mCourseTitlePosition = mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        mCourseDescriptionPosition = mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);

        // Make sure that you have moved to the correct record.
        mCourseCursor.moveToNext();

        // Display the course.
        displayCourse();
    }
    @Override
    // Called during cleanup
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Check to see if this is your cursor for your loader
        if (loader.getId() == LOADER_COURSES) {
            // If the cursor is not null, close it
            if (mCourseCursor != null) {
                mCourseCursor.close();
            }
        }
    }
}

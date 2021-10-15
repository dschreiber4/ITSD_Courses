package edu.cvtc.dschreiber4.itsdcourses;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import edu.cvtc.dschreiber4.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    // Member variables
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mCoursesLayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;

    //Constant
    public static final int LOADER_COURSES = 0;

    // Boolean to check if the 'onCreateLoader' method has run
    private boolean mIsCreated = false;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new ITSDCoursesOpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CourseActivity.class));
            }
        });
        initializeDisplayContent();
    }

    private void initializeDisplayContent() {

        // Retrieve the information from your database
        DataManager.loadFromDatabase(mDbOpenHelper);

        // Set a reference to your list of items layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_items);
        mCoursesLayoutManager = new LinearLayoutManager(this);

        // Get your courses
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, null);

        // Display the courses
        displayCourses();
    }

    private void displayCourses() {
        mRecyclerItems.setLayoutManager(mCoursesLayoutManager);
        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get the latest data from the DB
        loadCourses();
    }

    private void loadCourses() {
        //Open the DB in Read mode
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        //Create a list of columns from the DB to return
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                CourseInfoEntry._ID
        };

        //Create the order by field with RecyclerAdapter
        String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;

        //Populate the cursor
        final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                null, null, null, null,
                courseOrderBy);

        //Associate the cursor with RecyclerAdapter
        mCourseRecyclerAdapter.changeCursor(courseCursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        //match id with action bar icon
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Create new cursor loader
        CursorLoader loader = null;

        if (id == LOADER_COURSES) {
            loader = new CursorLoader(this){

                @Override
                public Cursor loadInBackground() {
                    mIsCreated = true;

                    //Open the DB in read mode
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    // Create a list of columns you want to return.
                    String[] courseColumns = {
                            CourseInfoEntry.COLUMN_COURSE_TITLE,
                            CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                            CourseInfoEntry._ID};

                    // Create an order by field for sorting purposes
                    String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;

                    // Populate your cursor with the results of the query
                    return db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                            null, null, null, null,
                            courseOrderBy);
                }
            };
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_COURSES && mIsCreated) {
            // Associate the cursor with your RecyclerAdapter
            mCourseRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_COURSES) {
            // Change the cursor to null
            mCourseRecyclerAdapter.changeCursor(null);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onResume();

        //Use restart loader instead of initLoader to refresh each time instead of only the fist load
        LoaderManager.getInstance(this).restartLoader(LOADER_COURSES, null, this);

    }
}






















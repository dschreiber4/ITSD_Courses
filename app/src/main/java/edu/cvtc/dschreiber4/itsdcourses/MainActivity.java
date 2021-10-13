package edu.cvtc.dschreiber4.itsdcourses;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Member variables
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mCoursesLayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;


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
                Snackbar.make(view, "Write something here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        // Fill the RecyclerAdapter with your courses
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);

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
}
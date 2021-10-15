package edu.cvtc.dschreiber4.itsdcourses;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cvtc.dschreiber4.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    // Member variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;
    private int mCourseTitlePosition;
    private int mCourseDescriptionPosition;
    private int mIdPosition;

    //Constructor
    public CourseRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        //Retrieve the positions of columns we are interested in
        populateColumnPositions();
    }

    private void populateColumnPositions() {

        if (mCursor != null) {
            //Get column indexes from the cursor
            mCourseTitlePosition = mCursor.getColumnIndex(
                    CourseInfoEntry.COLUMN_COURSE_TITLE);
            mCourseDescriptionPosition = mCursor.getColumnIndex(
                    CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
            mIdPosition = mCursor.getColumnIndex(
                    CourseInfoEntry._ID);
        }
    }

    public void changeCursor(Cursor cursor) {
        // close cursor
        if (mCursor != null) {
            mCursor.close();
        }

        //Create new cursor
        mCursor = cursor;

        //Get Positions of columns
        populateColumnPositions();

        //Tell Activity there has been a change
        notifyDataSetChanged();
    }

    //Methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
       return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Move the cursor to the correct position
        mCursor.moveToPosition(position);

        //Get the actual values
        String  courseTitle = mCursor.getString(mCourseTitlePosition);
        String  courseDescription = mCursor.getString(mCourseDescriptionPosition);
        int id = mCursor.getInt(mIdPosition);

        //Pass in the information to holder
        holder.mCourseTitle.setText(courseTitle);
        holder.mCourseDescription.setText(courseDescription);
        holder.mId = id;  }

    @Override
    public int getItemCount() {
        //Return 0 if null or return the record count
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Attributes for inner class
        public final TextView mCourseTitle;
        public final TextView mCourseDescription;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourseTitle = (TextView)itemView.findViewById(R.id.course_title);
            mCourseDescription = (TextView)itemView.findViewById(R.id.course_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CourseActivity.class);
                    intent.putExtra(CourseActivity.COURSE_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

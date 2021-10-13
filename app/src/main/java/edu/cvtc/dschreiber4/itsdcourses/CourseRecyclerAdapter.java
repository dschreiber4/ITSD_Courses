package edu.cvtc.dschreiber4.itsdcourses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    // Member variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<CourseInfo> mCourses;


    //Constructor
    public CourseRecyclerAdapter(Context context, List<CourseInfo> courses) {
        mContext = context;
        mCourses = courses;
        mLayoutInflater = LayoutInflater.from(context);  }

    //Methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
       return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CourseInfo course = mCourses.get(position);
        holder.mCourseTitle.setText(course.getTitle());
        holder.mCourseDescription.setText(course.getDescription());
        holder.mCurrentPosition = position;  }

    @Override
    public int getItemCount() {
        
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Attributes for inner class
        public final TextView mCourseTitle;
        public final TextView mCourseDescription;
        public int mCurrentPosition;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourseTitle = (TextView)itemView.findViewById(R.id.course_title);
            mCourseDescription = (TextView)itemView.findViewById(R.id.course_description);
            
        }
    }
}

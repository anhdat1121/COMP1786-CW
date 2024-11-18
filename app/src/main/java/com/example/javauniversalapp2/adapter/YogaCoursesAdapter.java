package com.example.javauniversalapp2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javauniversalapp2.MainActivity;
import com.example.javauniversalapp2.R;
import com.example.javauniversalapp2.db.entity.YogaCourse;

import java.util.ArrayList;

public class YogaCoursesAdapter extends RecyclerView.Adapter<YogaCoursesAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<YogaCourse> yogaCoursesList;
    private ArrayList<YogaCourse> yogaCourseListFull;
    private MainActivity mainActivity;

    // Constructor
    public YogaCoursesAdapter(Context context, ArrayList<YogaCourse> yogaCoursesList, MainActivity mainActivity) {
        this.context = context;
        this.yogaCoursesList = yogaCoursesList;
        this.yogaCourseListFull = new ArrayList<>(yogaCoursesList);
        this.mainActivity = mainActivity;
    }

    // ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public TextView day;
        public TextView time;
        public TextView capacity;
        public TextView price;
        public TextView type;
        public TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.courseName = itemView.findViewById(R.id.course_name);
            this.day = itemView.findViewById(R.id.day);
            this.time = itemView.findViewById(R.id.time_of_course);
            this.capacity = itemView.findViewById(R.id.capacity);
            this.price = itemView.findViewById(R.id.price);
            this.type = itemView.findViewById(R.id.type_of_course);
            this.description = itemView.findViewById(R.id.description);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final YogaCourse yogaCourse = yogaCoursesList.get(position);


        holder.courseName.setText(yogaCourse.getCourseName() != null ? yogaCourse.getCourseName() : "N/A");
        holder.day.setText(yogaCourse.getDayOfWeek() != null ? yogaCourse.getDayOfWeek() : "N/A");
        holder.time.setText(yogaCourse.getTimeOfCourse() != null ? yogaCourse.getTimeOfCourse() : "N/A");
        holder.capacity.setText(String.valueOf(yogaCourse.getCapacity()));
        holder.price.setText(String.format("$%.2f", yogaCourse.getPrice()));
        holder.type.setText(yogaCourse.getTypeOfCourse() != null ? yogaCourse.getTypeOfCourse() : "N/A");
        holder.description.setText(yogaCourse.getDescription() != null ? yogaCourse.getDescription() : "N/A");


        holder.itemView.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                mainActivity.addAndEditYogaCourse(true, yogaCourse, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return yogaCoursesList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<YogaCourse> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(yogaCourseListFull); // Show all if no filter
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (YogaCourse item : yogaCourseListFull) {
                        if (item.getCourseName().toLowerCase().contains(filterPattern) ||
                                item.getDayOfWeek().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                yogaCoursesList.clear();
                if (results.values != null) {
                    yogaCoursesList.addAll((ArrayList<YogaCourse>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}

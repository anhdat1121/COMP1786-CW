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

import com.example.javauniversalapp2.R;
import com.example.javauniversalapp2.ClassInstanceActivity;
import com.example.javauniversalapp2.db.entity.ClassInstance;

import java.util.ArrayList;

public class ClassInstancesAdapter extends RecyclerView.Adapter<ClassInstancesAdapter.MyViewHolder>implements Filterable {

    // Variables
    private Context context;
    private ArrayList<ClassInstance> instanceList;
    private ArrayList<ClassInstance> instanceListFull; // List cho search
    private ClassInstanceActivity instanceActivity;

    // Constructor
    public ClassInstancesAdapter(Context context, ArrayList<ClassInstance> instanceList, ClassInstanceActivity instanceActivity) {
        this.context = context;
        this.instanceList = instanceList;
        this.instanceListFull = new ArrayList<>(instanceList);
        this.instanceActivity = instanceActivity;
    }

    // ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView teacherTextView;
        public TextView courseTextView;
        public TextView additionalCommentTextView;
        public TextView titleTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateTextView = itemView.findViewById(R.id.date);
            this.teacherTextView = itemView.findViewById(R.id.teacher);
            this.courseTextView = itemView.findViewById(R.id.course_select);
            this.additionalCommentTextView = itemView.findViewById(R.id.additional_comment);
            this.titleTextView = itemView.findViewById(R.id.title_class_instance);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_instance_list_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClassInstance instance = instanceList.get(position);
        holder.titleTextView.setText("Class Instance Number " + (position + 1));
        holder.dateTextView.setText(instance.getDate());
        holder.teacherTextView.setText(instance.getTeacher());
        holder.courseTextView.setText(String.valueOf(instance.getCourseName()));
        holder.additionalCommentTextView.setText(instance.getAdditionalComments());

        holder.itemView.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                instanceActivity.addAndEditInstance(true, instance, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return instanceList.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<ClassInstance> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(instanceListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (ClassInstance item : instanceListFull) {
                        if (item.getTeacher().toLowerCase().contains(filterPattern)) {
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
                instanceList.clear();
                if (results.values != null) {
                    instanceList.addAll((ArrayList<ClassInstance>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}






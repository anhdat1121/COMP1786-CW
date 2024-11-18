package com.example.javauniversalapp2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.javauniversalapp2.adapter.ClassInstancesAdapter;
import com.example.javauniversalapp2.db.YogaDbHelper;
import com.example.javauniversalapp2.db.entity.ClassInstance;
import com.example.javauniversalapp2.db.entity.YogaCourse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassInstanceActivity extends AppCompatActivity {

    private ClassInstancesAdapter instancesAdapter;
    private ArrayList<ClassInstance> instanceArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private YogaDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_instance);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class Instances");

        // RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view_class_instances);
        db = new YogaDbHelper(this);

        // Load instances from database
        instanceArrayList.addAll(db.getAllClassInstances());

        instancesAdapter = new ClassInstancesAdapter(this, instanceArrayList, ClassInstanceActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(instancesAdapter);

        // Floating Action Button to add a new instance
        FloatingActionButton addButton = findViewById(R.id.add_class_instance_button);
        addButton.setOnClickListener(view -> addAndEditInstance(false, null, -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                instancesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.course_navbar) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.class_instance_navbar) {
            Intent intent1 = new Intent(this, ClassInstanceActivity.class);
            startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }

    public void addAndEditInstance(final boolean isUpdated, final ClassInstance instance, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.activity_add_class_instance, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClassInstanceActivity.this);
        alertDialogBuilder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.class_instance_title);
        final EditText dateEditText = view.findViewById(R.id.date);
        final EditText teacherEditText = view.findViewById(R.id.teacher);
        final Spinner courseSpinner = view.findViewById(R.id.course_spinner);
        final EditText additionalCommentEditText = view.findViewById(R.id.additional_comment);

        dialogTitle.setText(!isUpdated ? "Add Class Instance" : "Edit Class Instance");
        List<YogaCourse> courses = db.getAllYogaCourses();

        // Set up the course spinner
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCourseNames(courses));
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // Set existing data if updating
        if (isUpdated && instance != null) {
            dateEditText.setText(instance.getDate());
            teacherEditText.setText(instance.getTeacher());
            int courseSpinnerPosition = getCoursePosition(instance.getCourseId(), courses);
            courseSpinner.setSelection(courseSpinnerPosition);
            additionalCommentEditText.setText(instance.getAdditionalComments());
        }

        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP){
                showDialogDate(dateEditText);
                return true;
            }
            return false;
        });

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", (dialogInterface, i) -> {

                    if (TextUtils.isEmpty(dateEditText.getText().toString())) {
                        Toast.makeText(ClassInstanceActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(teacherEditText.getText().toString())) {
                        Toast.makeText(ClassInstanceActivity.this, "Please enter a teacher", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (courseSpinner.getSelectedItem() == null) {
                        Toast.makeText(ClassInstanceActivity.this, "Please select a course", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialogInterface.dismiss();


                    String selectedCourse = courseSpinner.getSelectedItem().toString();
                    if (isUpdated && instance != null) {
                        updateInstance(dateEditText.getText().toString(),
                                teacherEditText.getText().toString(),
                                selectedCourse,
                                additionalCommentEditText.getText().toString(), position);
                    } else {
                        createInstance(dateEditText.getText().toString(),
                                teacherEditText.getText().toString(),
                                selectedCourse,
                                additionalCommentEditText.getText().toString());
                    }
                })
                .setNegativeButton(isUpdated ? "Delete" : "Cancel", (dialogInterface, i) -> {
                    if (isUpdated) {
                        deleteInstance(instance, position);
                    } else {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialogDate(EditText dateEditText){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
          this,
                (view, year, month, dayOfMonth) -> {
                  String date = year + "-" + (month + 1) +"-"+dayOfMonth;
                  dateEditText.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private List<String> getCourseNames(List<YogaCourse> courses) {
        List<String> courseNames = new ArrayList<>();
        for (YogaCourse course : courses) {
            courseNames.add(course.getCourseName());
        }
        return courseNames;
    }

    private int getCoursePosition(int courseId, List<YogaCourse> courses) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() == courseId) {
                return i;
            }
        }
        return -1;
    }

    private void deleteInstance(ClassInstance instance, int position) {
        instanceArrayList.remove(position);
        db.deleteClassInstance(instance);
        instancesAdapter.notifyDataSetChanged();
    }


private void updateInstance(String date, String teacher, String course, String additionalComment, int position) {
    ClassInstance instance = instanceArrayList.get(position);


    boolean hasChanged = false;

    if (!instance.getDate().equals(date)) {
        instance.setDate(date);
        hasChanged = true;
    }

    if (!instance.getTeacher().equals(teacher)) {
        instance.setTeacher(teacher);
        hasChanged = true;
    }

    int courseId = getCourseIdByName(course);
    if (instance.getCourseId() != courseId) {
        instance.setCourseId(courseId);
        instance.setCourseName(getCourseNameById(courseId));
        hasChanged = true;
    }

    if (!instance.getAdditionalComments().equals(additionalComment)) {
        instance.setAdditionalComments(additionalComment);
        hasChanged = true;
    }

    if (hasChanged) {
        db.updateClassInstance(instance);
        instanceArrayList.set(position, instance);
        instancesAdapter.notifyDataSetChanged();
    } else {
        Toast.makeText(ClassInstanceActivity.this, "No changes made", Toast.LENGTH_SHORT).show();
    }
}



    private void createInstance(String date, String teacher, String course, String additionalComment) {
        long id = db.insertClassInstance(date, teacher, getCourseIdByName(course), additionalComment);
        ClassInstance instance = db.getClassInstance(id);

        if (instance != null) {

            instance.setCourseName(getCourseNameById(instance.getCourseId()));
            instanceArrayList.add(0, instance);
            instancesAdapter.notifyDataSetChanged();
        }
    }
    private int getCourseIdByName(String courseName) {
        List<YogaCourse> courses = db.getAllYogaCourses();
        for (YogaCourse course : courses) {
            if (course.getCourseName().equals(courseName)) {
                return course.getId();
            }
        }
        return -1;
    }
    public String getCourseNameById(int courseId) {
        List<YogaCourse> courses = db.getAllYogaCourses();
        for (YogaCourse course : courses) {
            if (course.getId() == courseId) {
                return course.getCourseName();
            }
        }
        return "Unknown";
    }
}

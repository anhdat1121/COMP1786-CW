package com.example.javauniversalapp2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.example.javauniversalapp2.adapter.YogaCoursesAdapter;
import com.example.javauniversalapp2.db.YogaDbHelper;
import com.example.javauniversalapp2.db.entity.YogaCourse;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {


    private YogaCoursesAdapter yogaCoursesAdapter;
    private ArrayList<YogaCourse> yogaCourseArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private YogaDbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yoga Courses");


        recyclerView = findViewById(R.id.recycler_view_courses);
        db = new YogaDbHelper(this);


        yogaCourseArrayList.addAll(db.getAllYogaCourses());

        yogaCoursesAdapter = new YogaCoursesAdapter(this, yogaCourseArrayList, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(yogaCoursesAdapter);

        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> addAndEditYogaCourse(false, null, -1));
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
                yogaCoursesAdapter.getFilter().filter(newText);
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

    public void addAndEditYogaCourse(final boolean isUpdated, final YogaCourse yogaCourse, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.activity_add_course, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.new_course_title);
        final EditText courseName = view.findViewById(R.id.course_name);
        final Spinner dayOfWeekSpinner = view.findViewById(R.id.day_of_week);
        final EditText timeEditText = view.findViewById(R.id.time_of_course);
        final EditText capacity = view.findViewById(R.id.capacity);
        final EditText duration = view.findViewById(R.id.duration);
        final EditText price = view.findViewById(R.id.price);
        final Spinner typeOfCourseSpinner = view.findViewById(R.id.type_of_course);
        final EditText description = view.findViewById(R.id.description);

        dialogTitle.setText(!isUpdated ? "Add Yoga Instance" : "Edit Course");


        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.yoga_course_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfCourseSpinner.setAdapter(typeAdapter);

        // dat du lieu hien co neu update
        if (isUpdated && yogaCourse != null) {
            courseName.setText(yogaCourse.getCourseName());
            int daySpinnerPosition = dayAdapter.getPosition(yogaCourse.getDayOfWeek());
            dayOfWeekSpinner.setSelection(daySpinnerPosition);
            timeEditText.setText(yogaCourse.getTimeOfCourse());
            capacity.setText(String.valueOf(yogaCourse.getCapacity()));
            duration.setText(String.valueOf(yogaCourse.getDuration()));
            price.setText(String.valueOf(yogaCourse.getPrice()));
            int typeSpinnerPosition = typeAdapter.getPosition(yogaCourse.getTypeOfCourse());
            typeOfCourseSpinner.setSelection(typeSpinnerPosition);
            description.setText(yogaCourse.getDescription());
        }
        //Chon Tgian
        timeEditText.setOnClickListener(v -> {

            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Appointment time")
                    .build();

            picker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");


            picker.addOnPositiveButtonClickListener(dialog -> {
                String formattedTime = String.format("%02d:%02d", picker.getHour(), picker.getMinute());
                timeEditText.setText(formattedTime);
            });
        });

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", (dialogInterface, i) -> {

                    if (TextUtils.isEmpty(courseName.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Please enter a course name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (dayOfWeekSpinner.getSelectedItem() == null) {
                        Toast.makeText(MainActivity.this, "Please select a day of the week", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(timeEditText.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Please select a time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(capacity.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Please enter capacity", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(duration.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Please enter duration", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(price.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Please enter price", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialogInterface.dismiss();


                    String selectedDayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
                    String selectedTypeOfCourse = typeOfCourseSpinner.getSelectedItem().toString();
                    if (isUpdated && yogaCourse != null) {
                        updateYogaCourse(courseName.getText().toString(), selectedDayOfWeek, timeEditText.getText().toString(),
                                Integer.parseInt(capacity.getText().toString()), Integer.parseInt(duration.getText().toString()),
                                Double.parseDouble(price.getText().toString()), selectedTypeOfCourse,
                                description.getText().toString(), position);
                    } else {
                        createYogaCourse(courseName.getText().toString(), selectedDayOfWeek, timeEditText.getText().toString(),
                                Integer.parseInt(capacity.getText().toString()), Integer.parseInt(duration.getText().toString()),
                                Double.parseDouble(price.getText().toString()), selectedTypeOfCourse,
                                description.getText().toString());
                    }
                })
                .setNegativeButton(isUpdated ? "Delete" : "Cancel", (dialogInterface, i) -> {
                    if (isUpdated) {
                        deleteYogaCourse(yogaCourse, position);
                    } else {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteYogaCourse(YogaCourse yogaCourse, int position) {
        yogaCourseArrayList.remove(position);
        db.deleteYogaCourse(yogaCourse);
        yogaCoursesAdapter.notifyDataSetChanged();
    }

    private void updateYogaCourse(String courseName, String dayOfWeek, String timeOfCourse, int capacity, int duration, double price,
                                  String typeOfCourse, String description, int position) {
        YogaCourse yogaCourse = yogaCourseArrayList.get(position);

        yogaCourse.setCourseName(courseName);
        yogaCourse.setDayOfWeek(dayOfWeek);
        yogaCourse.setTimeOfCourse(timeOfCourse);
        yogaCourse.setCapacity(capacity);
        yogaCourse.setDuration(duration);
        yogaCourse.setPrice(price);
        yogaCourse.setTypeOfCourse(typeOfCourse);
        yogaCourse.setDescription(description);

        db.updateYogaCourse(yogaCourse);

        yogaCourseArrayList.set(position, yogaCourse);
        yogaCoursesAdapter.notifyDataSetChanged();
    }

    private void createYogaCourse(String courseName, String dayOfWeek, String timeOfCourse, int capacity, int duration, double price,
                                  String typeOfCourse, String description) {
        long id = db.insertYogaCourse(courseName, dayOfWeek, timeOfCourse, capacity, duration, price, typeOfCourse, description);
        YogaCourse yogaCourse = db.getYogaCourse(id);
        if (yogaCourse != null) {
            yogaCourseArrayList.add(0, yogaCourse);
            yogaCoursesAdapter.notifyDataSetChanged();
        }
    }

}

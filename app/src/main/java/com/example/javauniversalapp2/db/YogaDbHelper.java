package com.example.javauniversalapp2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.javauniversalapp2.db.entity.YogaCourse;
import com.example.javauniversalapp2.db.entity.ClassInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;
import android.widget.Toast;

public class YogaDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "yoga_db";
    private Context context;

    public YogaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the yoga course table
        sqLiteDatabase.execSQL(YogaCourse.CREATE_TABLE);
        sqLiteDatabase.execSQL(ClassInstance.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if it exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + YogaCourse.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ClassInstance.TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Insert a new yoga course
    public long insertYogaCourse(String courseName, String dayOfWeek, String timeOfCourse, int capacity, int duration,
                                 double price, String typeOfCourse, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(YogaCourse.COLUMN_COURSE_NAME, courseName);
        values.put(YogaCourse.COLUMN_DAY_OF_WEEK, dayOfWeek);
        values.put(YogaCourse.COLUMN_TIME_OF_COURSE, timeOfCourse);
        values.put(YogaCourse.COLUMN_CAPACITY, capacity);
        values.put(YogaCourse.COLUMN_DURATION, duration);
        values.put(YogaCourse.COLUMN_PRICE, price);
        values.put(YogaCourse.COLUMN_TYPE_OF_COURSE, typeOfCourse);
        values.put(YogaCourse.COLUMN_DESCRIPTION, description);

        // Insert row
        long id = db.insert(YogaCourse.TABLE_NAME, null, values);
        db.close(); // Close database connection
        return id;
    }

    // Get a single yoga course by ID
    public YogaCourse getYogaCourse(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(YogaCourse.TABLE_NAME,
                new String[]{
                        YogaCourse.COLUMN_ID,
                        YogaCourse.COLUMN_COURSE_NAME,
                        YogaCourse.COLUMN_DAY_OF_WEEK,
                        YogaCourse.COLUMN_TIME_OF_COURSE,
                        YogaCourse.COLUMN_CAPACITY,
                        YogaCourse.COLUMN_DURATION,
                        YogaCourse.COLUMN_PRICE,
                        YogaCourse.COLUMN_TYPE_OF_COURSE,
                        YogaCourse.COLUMN_DESCRIPTION},
                YogaCourse.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        YogaCourse yogaCourse = new YogaCourse(
                cursor.getInt(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_COURSE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DAY_OF_WEEK)),
                cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_TIME_OF_COURSE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_CAPACITY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DURATION)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_TYPE_OF_COURSE)),
                cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DESCRIPTION))
        );

        cursor.close();
        return yogaCourse;
    }

    // Get all yoga courses
    public ArrayList<YogaCourse> getAllYogaCourses() {
        ArrayList<YogaCourse> courses = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + YogaCourse.TABLE_NAME + " ORDER BY " + YogaCourse.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                YogaCourse yogaCourse = new YogaCourse();
                yogaCourse.setId(cursor.getInt(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_ID)));
                yogaCourse.setCourseName(cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_COURSE_NAME)));
                yogaCourse.setDayOfWeek(cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DAY_OF_WEEK)));
                yogaCourse.setTimeOfCourse(cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_TIME_OF_COURSE)));
                yogaCourse.setCapacity(cursor.getInt(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_CAPACITY)));
                yogaCourse.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DURATION)));
                yogaCourse.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_PRICE)));
                yogaCourse.setTypeOfCourse(cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_TYPE_OF_COURSE)));
                yogaCourse.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DESCRIPTION)));

                courses.add(yogaCourse);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return courses;
    }

    // Update a yoga course
    public int updateYogaCourse(YogaCourse yogaCourse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(YogaCourse.COLUMN_COURSE_NAME, yogaCourse.getCourseName());
        values.put(YogaCourse.COLUMN_DAY_OF_WEEK, yogaCourse.getDayOfWeek());
        values.put(YogaCourse.COLUMN_TIME_OF_COURSE, yogaCourse.getTimeOfCourse());
        values.put(YogaCourse.COLUMN_CAPACITY, yogaCourse.getCapacity());
        values.put(YogaCourse.COLUMN_DURATION, yogaCourse.getDuration());
        values.put(YogaCourse.COLUMN_PRICE, yogaCourse.getPrice());
        values.put(YogaCourse.COLUMN_TYPE_OF_COURSE, yogaCourse.getTypeOfCourse());
        values.put(YogaCourse.COLUMN_DESCRIPTION, yogaCourse.getDescription());

        int rowsUpdated = db.update(YogaCourse.TABLE_NAME, values, YogaCourse.COLUMN_ID + " = ?",
                new String[]{String.valueOf(yogaCourse.getId())});

        db.close();
        return rowsUpdated;
    }

    // Delete a yoga course
    public void deleteYogaCourse(YogaCourse yogaCourse) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(YogaCourse.TABLE_NAME, YogaCourse.COLUMN_ID + " = ?",
                new String[]{String.valueOf(yogaCourse.getId())});
        db.close();
    }

    // Insert a new class instance
    public long insertClassInstance(String date, String teacher, int courseId, String additionalComments) {
        if (!checkDayOfWeekAndDate(courseId, date)) {
            String dayOfWeekCourse = getDayOfWeekCourse(courseId);
            Log.d("YogaDbHelper", "Date does not match the day of week for the course. Date: " + date + ", Expected Day: " + dayOfWeekCourse);
            Toast.makeText(context, "The date does not match the day of week of course. " + dayOfWeekCourse, Toast.LENGTH_SHORT).show();
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ClassInstance.COLUMN_DATE, date);
        values.put(ClassInstance.COLUMN_TEACHER, teacher);
        values.put(ClassInstance.COLUMN_COURSE_ID, courseId);
        values.put(ClassInstance.COLUMN_ADDITIONAL_COMMENTS, additionalComments);

        // Insert row
        long id = db.insert(ClassInstance.TABLE_NAME, null, values);
        db.close();
        return id;
    }


    public int updateClassInstance(ClassInstance classInstance) {
        if(!checkDayOfWeekAndDate(classInstance.getCourseId(), classInstance.getDate())){
            String dayOfWeekCourse = getDayOfWeekCourse(classInstance.getCourseId());
//            Log.d("YogaDbHelper", "Date does not match the day of week for the course. Date: " + classInstance.getDate() + ", Expected Day: " + dayOfWeekCourse);
            Toast.makeText(context, "The date does not match the day of week of course. "+dayOfWeekCourse, Toast.LENGTH_SHORT).show();
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ClassInstance.COLUMN_DATE, classInstance.getDate());
        values.put(ClassInstance.COLUMN_TEACHER, classInstance.getTeacher());
        values.put(ClassInstance.COLUMN_COURSE_ID, classInstance.getCourseId());
        values.put(ClassInstance.COLUMN_ADDITIONAL_COMMENTS, classInstance.getAdditionalComments());

        int rowsUpdated = db.update(ClassInstance.TABLE_NAME, values, ClassInstance.COLUMN_ID + " = ?",
                new String[]{String.valueOf(classInstance.getId())});

        db.close();
        return rowsUpdated;
    }



    //check day va date
    public boolean checkDayOfWeekAndDate(int courseId, String dateString){
        String dayOfWeek = getDayOfWeekCourse(courseId);
        if (dayOfWeek == null) {
            return false;
        }
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            Date date = formatDate.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            boolean result = dayOfWeek.equals(getDayOfWeek(day));
            Log.d("YogaDbHelper", "Date: " + dateString + ", Day: " + getDayOfWeek(day) + ", Expected: " + dayOfWeek + ", Match: " + result);
            return result;
        }catch(ParseException e){
            e.printStackTrace();
            return false;
        }
    }

    public String getDayOfWeekCourse(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(YogaCourse.TABLE_NAME,
                new String[]{YogaCourse.COLUMN_DAY_OF_WEEK},
                YogaCourse.COLUMN_ID + "=?",
                new String[]{String.valueOf(courseId)},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_DAY_OF_WEEK));
            cursor.close();
            return dayOfWeek;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    private String getDayOfWeek(int day){
        switch (day){
            case Calendar.SUNDAY:
                return "SUNDAY";
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            default:
                return "Unknown";
        }
    }


    public ClassInstance getClassInstance(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ClassInstance.TABLE_NAME,
                new String[]{
                        ClassInstance.COLUMN_ID,
                        ClassInstance.COLUMN_DATE,
                        ClassInstance.COLUMN_TEACHER,
                        ClassInstance.COLUMN_COURSE_ID,
                        ClassInstance.COLUMN_ADDITIONAL_COMMENTS},
                ClassInstance.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ClassInstance classInstance = new ClassInstance(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_TEACHER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_COURSE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_ADDITIONAL_COMMENTS))
            );
            cursor.close();
            return classInstance;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
    }

    // Get all class instances

    public ArrayList<ClassInstance> getAllClassInstances() {
        ArrayList<ClassInstance> instances = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get class instances along with the course names
        String selectQuery = "SELECT ci.*, yc." + YogaCourse.COLUMN_COURSE_NAME +
                " FROM " + ClassInstance.TABLE_NAME + " ci " +
                " LEFT JOIN " + YogaCourse.TABLE_NAME + " yc " +
                " ON ci." + ClassInstance.COLUMN_COURSE_ID + " = yc." + YogaCourse.COLUMN_ID +
                " ORDER BY ci." + ClassInstance.COLUMN_ID + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ClassInstance classInstance = new ClassInstance();
                classInstance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_ID)));
                classInstance.setDate(cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_DATE)));
                classInstance.setTeacher(cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_TEACHER)));
                classInstance.setCourseId(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_COURSE_ID)));
                classInstance.setAdditionalComments(cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_ADDITIONAL_COMMENTS)));

                // Get course name from the joined result
                classInstance.setCourseName(cursor.getString(cursor.getColumnIndexOrThrow(YogaCourse.COLUMN_COURSE_NAME)));

                instances.add(classInstance);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return instances;
    }



    // Delete a class instance
    public void deleteClassInstance(ClassInstance classInstance) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ClassInstance.TABLE_NAME, ClassInstance.COLUMN_ID + " = ?",
                new String[]{String.valueOf(classInstance.getId())});
        db.close();
    }
}

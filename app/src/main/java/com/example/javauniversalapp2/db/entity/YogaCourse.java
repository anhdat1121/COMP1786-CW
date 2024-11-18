package com.example.javauniversalapp2.db.entity;

public class YogaCourse {
    // 1- Constants for Database
    public static final String TABLE_NAME = "yogacourses";
    public static final String COLUMN_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
    public static final String COLUMN_TIME_OF_COURSE = "time_of_course";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TYPE_OF_COURSE = "type_of_course";
    public static final String COLUMN_DESCRIPTION = "description";

    // 2- Variables
    private int id;
    private String courseName;
    private String dayOfWeek;
    private String timeOfCourse;
    private int capacity;
    private int duration;
    private double price;
    private String typeOfCourse;
    private String description;

    // 3- Constructors
    public YogaCourse() {}

    public YogaCourse(int id, String courseName, String dayOfWeek, String timeOfCourse, int capacity, int duration,
                      double price, String typeOfCourse, String description) {
        this.id = id;
        this.courseName = courseName;
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.typeOfCourse = typeOfCourse;
        this.description = description;
    }

    // 4- Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTypeOfCourse() {
        return typeOfCourse;
    }

    public void setTypeOfCourse(String typeOfCourse) {
        this.typeOfCourse = typeOfCourse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // 5- SQL Query: Creating the Table
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                    COLUMN_DAY_OF_WEEK + " TEXT NOT NULL, " +
                    COLUMN_TIME_OF_COURSE + " TEXT NOT NULL, " +
                    COLUMN_CAPACITY + " INTEGER NOT NULL, " +
                    COLUMN_DURATION + " INTEGER NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_TYPE_OF_COURSE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT" +
                    ")";
}

package com.example.javauniversalapp2.db.entity;

public class ClassInstance {
    // 1- Constants for Database
    public static final String TABLE_NAME = "class_instances";
    public static final String COLUMN_ID = "instance_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TEACHER = "teacher";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_ADDITIONAL_COMMENTS = "additional_comments";

    // 2- Variables
    private int id;
    private String date;
    private String teacher;
    private int courseId;
    private String additionalComments;
    //test
    private String courseName;

    // 3- Constructors
    public ClassInstance() {}

    public ClassInstance(int id, String date, String teacher, int courseId, String additionalComments) {
        this.id = id;
        this.date = date;
        this.teacher = teacher;
        this.courseId = courseId;
        this.additionalComments = additionalComments;
    }

    // 4- Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    // 5- SQL Query: Creating the Table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_TEACHER + " TEXT NOT NULL, "
            + COLUMN_COURSE_ID + " INTEGER NOT NULL, "
            + COLUMN_ADDITIONAL_COMMENTS + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + YogaCourse.TABLE_NAME + "(" + YogaCourse.COLUMN_ID + ")"
            + ")";
}
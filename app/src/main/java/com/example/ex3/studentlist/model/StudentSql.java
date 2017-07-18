package com.example.ex3.studentlist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by menachi on 17/05/2017.
 */

public class StudentSql {
    static final String STUDENT_TABLE = "students";
    static final String STUDENT_ID = "stid";
    static final String STUDENT_NAME = "name";
    static final String STUDENT_CHECK = "checked";
    static final String STUDENT_IMAGE_URL = "imageUrl";

    static List<Student> getAllStudents(SQLiteDatabase db) {
        Cursor cursor = db.query("students", null, null, null, null, null, null);
        List<Student> list = new LinkedList<Student>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(STUDENT_ID);
            int nameIndex = cursor.getColumnIndex(STUDENT_NAME);
            int checkIndex = cursor.getColumnIndex(STUDENT_CHECK);
            int imageUrlIndex = cursor.getColumnIndex(STUDENT_IMAGE_URL);

            do {
                Student st = new Student();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                list.add(st);
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addStudent(SQLiteDatabase db, Student st) {
        ContentValues values = new ContentValues();
        values.put(STUDENT_ID, st.id);
        values.put(STUDENT_NAME, st.name);
        if (st.checked) {
            values.put(STUDENT_CHECK, 1);
        } else {
            values.put(STUDENT_CHECK, 0);
        }
        values.put(STUDENT_IMAGE_URL, st.imageUrl);
        db.insert(STUDENT_TABLE, STUDENT_ID, values);
    }

    static Student getStudent(SQLiteDatabase db, String stId) {
        return null;
    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + STUDENT_TABLE +
                " (" +
                STUDENT_ID + " TEXT PRIMARY KEY, " +
                STUDENT_NAME + " TEXT, " +
                STUDENT_CHECK + " NUMBER, " +
                STUDENT_IMAGE_URL + " TEXT);");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + STUDENT_TABLE + ";");
        onCreate(db);
    }


}

package com.example.fp.androidapp.model;

/**
 * Created by Aviv Gold on 6/11/2017.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by menachi on 17/05/2017.
 */

public class StudentSql {
    static final String STUDENT_TABLE = "students";
    static final String STUDENT_PHONE="phone";
    static final String STUDENT_DATE="date";
    static final String STUDENT_TIME="time";
    static final String ADDRESS="address";
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
            int phone =cursor.getColumnIndex(STUDENT_PHONE);
            int time =cursor.getColumnIndex(STUDENT_TIME);
            int date =cursor.getColumnIndex(STUDENT_DATE);
            int address =cursor.getColumnIndex(ADDRESS);

            do {
                Student st = new Student();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                st.address=cursor.getString(address);
                st.phone = cursor.getString(phone );
                st.birthTime = cursor.getString(time);
                st.birthDate = cursor.getString(date);
                list.add(st);
            } while (cursor.moveToNext());
        }
        return list;
    }
    static List<Student> getAllStudentsByName(SQLiteDatabase db , String name) {
        Cursor cursor = db.query("students", null, null, null, null, null, null);
        List<Student> list = new LinkedList<Student>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(STUDENT_ID);
            int nameIndex = cursor.getColumnIndex(STUDENT_NAME);
            int checkIndex = cursor.getColumnIndex(STUDENT_CHECK);
            int imageUrlIndex = cursor.getColumnIndex(STUDENT_IMAGE_URL);
            int phone =cursor.getColumnIndex(STUDENT_PHONE);
            int time =cursor.getColumnIndex(STUDENT_TIME);
            int date =cursor.getColumnIndex(STUDENT_DATE);
            int address =cursor.getColumnIndex(ADDRESS);

            do {
                Student st = new Student();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                st.address=cursor.getString(address);
                st.phone = cursor.getString(phone );
                st.birthTime = cursor.getString(time);
                st.birthDate = cursor.getString(date);
                if(st.name.equals(name))
                    list.add(st);
            } while (cursor.moveToNext());
        }
        return list;
    }
    static void addStudent(SQLiteDatabase db, Student st) {
        ContentValues values = new ContentValues();
        values.put(STUDENT_ID, st.id);
        values.put(STUDENT_PHONE, st.phone);
        values.put(STUDENT_TIME, st.birthTime);
        values.put(STUDENT_DATE, st.birthDate);
        values.put(STUDENT_NAME, st.name);
        values.put(ADDRESS, st.address);

        if (st.checked) {
            values.put(STUDENT_CHECK, 1);
        } else {
            values.put(STUDENT_CHECK, 0);
        }
        values.put(STUDENT_IMAGE_URL, st.imageUrl);
        db.insert(STUDENT_TABLE, STUDENT_ID, values);
    }

    static Student getStudent(SQLiteDatabase db, String stId) {

        if(stId!=null) {
            Cursor cursor = db.query(STUDENT_TABLE, null, STUDENT_ID + "=?", new String[]{stId}, null, null, null);
            if (cursor.getCount() != 0) {


                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(STUDENT_ID);
                int nameIndex = cursor.getColumnIndex(STUDENT_NAME);
                int checkIndex = cursor.getColumnIndex(STUDENT_CHECK);
                int imageUrlIndex = cursor.getColumnIndex(STUDENT_IMAGE_URL);
                int phone = cursor.getColumnIndex(STUDENT_PHONE);
                int time = cursor.getColumnIndex(STUDENT_TIME);
                int date = cursor.getColumnIndex(STUDENT_DATE);
                int address =cursor.getColumnIndex(ADDRESS);

                Student st = new Student();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                st.phone = cursor.getString(phone);
                st.birthTime = cursor.getString(time);
                st.address= cursor.getString(address);
                st.birthDate = cursor.getString(date);
                return st;
            }

        }
        return null;

    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + STUDENT_TABLE +
                " (" +
                STUDENT_ID + " TEXT PRIMARY KEY, " +
                STUDENT_NAME + " TEXT, " +
                STUDENT_CHECK + " NUMBER, " +
                STUDENT_IMAGE_URL + " TEXT, "+
                STUDENT_PHONE+" TEXT, "
                +STUDENT_DATE+" TEXT, "
                +STUDENT_TIME+" TEXT, " +
                ADDRESS+" TEXT);");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + STUDENT_TABLE + ";");
        onCreate(db);
    }
    static public void deleteStudent(SQLiteDatabase db, Student st)
    {
        db.delete(STUDENT_TABLE,STUDENT_ID+"=?",new String[]{st.id});
    }
    static public void updateStudent(SQLiteDatabase db, Student st)
    {
        deleteStudent(db,st);
        addStudent(db,st);
        /*ContentValues values = new ContentValues();
        values.put(STUDENT_ID, st.id);
        values.put(STUDENT_PHONE, st.phone);
        values.put(STUDENT_TIME, st.birthTime);
        values.put(STUDENT_DATE, st.birthDate);
        values.put(STUDENT_NAME, st.name);
        values.put(ADDRESS, st.address);

        if (st.checked) {
            values.put(STUDENT_CHECK, 1);
        } else {
            values.put(STUDENT_CHECK, 0);
        }
        values.put(STUDENT_IMAGE_URL, st.imageUrl);
        Log.d("Mife",st.id);
        db.update(STUDENT_TABLE, values, st.id+"="+STUDENT_ID, null);*/
        /*int checked;
        if(st.checked)
            checked = 1;
        else
            checked = 0;
        String strSQL = "UPDATE "+STUDENT_TABLE+" SET "+STUDENT_ID+" = "+st.id+" , "+STUDENT_PHONE+" = "+st.phone+" , "+STUDENT_TIME+" = "+st.birthTime+" , "+STUDENT_DATE+
                " = "+st.birthDate+" , "+STUDENT_NAME+" = "+st.name+" , "+ADDRESS+" = "+st.address+" , "+STUDENT_CHECK+" = "+checked+" WHERE "+STUDENT_ID+" = "+ st.id;

        db.execSQL(strSQL);*/
    }

}


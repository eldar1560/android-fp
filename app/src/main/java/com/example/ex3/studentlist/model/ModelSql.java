package com.example.ex3.studentlist.model;

/**
 * Created by Aviv Gold on 6/11/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ModelSql extends SQLiteOpenHelper {
    ModelSql(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StudentSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StudentSql.onUpgrade(db, oldVersion, newVersion);
    }

}


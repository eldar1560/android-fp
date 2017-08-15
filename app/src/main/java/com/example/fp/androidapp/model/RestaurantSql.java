package com.example.fp.androidapp.model;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fp.androidapp.MyApplication;
import com.example.fp.androidapp.R;

import java.util.LinkedList;
import java.util.List;


public class RestaurantSql {
    static final String RESTAURANT_TABLE = "restaurants";
    static final String RESTAURANT_USERNAME="userName";
    static final String RESTAURANT_DATE="date";
    static final String RESTAURANT_TIME="time";
    static final String ADDRESS="address";
    static final String RESTAURANT_ID = "stid";
    static final String RESTAURANT_NAME = "name";
    static final String RESTAURANT_CHECK = "checked";
    static final String RESTAURANT_IMAGE_URL = "imageUrl";
    static final String RESTAURANT_FOOD_NAME = "foodName";

    static List<Restaurant> getAllRestaurants(SQLiteDatabase db) {
        Cursor cursor = db.query("restaurants", null, null, null, null, null, null);
        List<Restaurant> list = new LinkedList<Restaurant>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(RESTAURANT_ID);
            int nameIndex = cursor.getColumnIndex(RESTAURANT_NAME);
            int checkIndex = cursor.getColumnIndex(RESTAURANT_CHECK);
            int imageUrlIndex = cursor.getColumnIndex(RESTAURANT_IMAGE_URL);
            int userName =cursor.getColumnIndex(RESTAURANT_USERNAME);
            int time =cursor.getColumnIndex(RESTAURANT_TIME);
            int date =cursor.getColumnIndex(RESTAURANT_DATE);
            int address =cursor.getColumnIndex(ADDRESS);
            int foodName = cursor.getColumnIndex(RESTAURANT_FOOD_NAME);
            do {
                Restaurant st = new Restaurant();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                st.address=cursor.getString(address);
                st.userName = cursor.getString(userName );
                st.orderTime = cursor.getString(time);
                st.orderDate = cursor.getString(date);
                st.foodName = cursor.getString(foodName);
                list.add(st);
            } while (cursor.moveToNext());
        }
        return list;
    }
    static List<Restaurant> getAllRestaurantsByFilter(SQLiteDatabase db , String content , String field) {
        Cursor cursor = db.query("restaurants", null, null, null, null, null, null);
        List<Restaurant> list = new LinkedList<Restaurant>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(RESTAURANT_ID);
            int nameIndex = cursor.getColumnIndex(RESTAURANT_NAME);
            int checkIndex = cursor.getColumnIndex(RESTAURANT_CHECK);
            int imageUrlIndex = cursor.getColumnIndex(RESTAURANT_IMAGE_URL);
            int userName =cursor.getColumnIndex(RESTAURANT_USERNAME);
            int time =cursor.getColumnIndex(RESTAURANT_TIME);
            int date =cursor.getColumnIndex(RESTAURANT_DATE);
            int address =cursor.getColumnIndex(ADDRESS);
            int foodName =cursor.getColumnIndex(RESTAURANT_FOOD_NAME);
            do {
                Restaurant st = new Restaurant();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                st.address=cursor.getString(address);
                st.userName = cursor.getString(userName );
                st.orderTime = cursor.getString(time);
                st.orderDate = cursor.getString(date);
                st.foodName = cursor.getString(foodName);
                String [] restaurant_variables = MyApplication.getMyContext().getResources().getStringArray(R.array.restaurant_variables);
                if(restaurant_variables[0].equals(field)) {
                    /*if (st.name.equals(content))
                        list.add(st);*/
                    if(st.name.toLowerCase().equals(content.toLowerCase()))
                        list.add(st);
                }
                else if(restaurant_variables[1].equals(field)) {
                    /*if (st.foodName.equals(content))
                        list.add(st);*/
                    if(st.foodName.toLowerCase().equals(content.toLowerCase()))
                        list.add(st);
                }
                else if(restaurant_variables[2].equals(field)) {
                    if (st.userName.equals(content))
                        list.add(st);
                }
                else
                    list.add(st);
            } while (cursor.moveToNext());
        }
        return list;
    }
    static void addRestaurant(SQLiteDatabase db, Restaurant st) {
        ContentValues values = new ContentValues();
        values.put(RESTAURANT_ID, st.id);
        values.put(RESTAURANT_USERNAME, st.userName);
        values.put(RESTAURANT_TIME, st.orderTime);
        values.put(RESTAURANT_DATE, st.orderDate);
        values.put(RESTAURANT_NAME, st.name);
        values.put(RESTAURANT_FOOD_NAME, st.foodName);
        values.put(ADDRESS, st.address);

        if (st.checked) {
            values.put(RESTAURANT_CHECK, 1);
        } else {
            values.put(RESTAURANT_CHECK, 0);
        }
        values.put(RESTAURANT_IMAGE_URL, st.imageUrl);
        db.insert(RESTAURANT_TABLE, RESTAURANT_ID, values);
    }

    static Restaurant getRestaurant(SQLiteDatabase db, String stId) {

        if(stId!=null) {
            Cursor cursor = db.query(RESTAURANT_TABLE, null, RESTAURANT_ID + "=?", new String[]{stId}, null, null, null);
            if (cursor.getCount() != 0) {


                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(RESTAURANT_ID);
                int nameIndex = cursor.getColumnIndex(RESTAURANT_NAME);
                int checkIndex = cursor.getColumnIndex(RESTAURANT_CHECK);
                int imageUrlIndex = cursor.getColumnIndex(RESTAURANT_IMAGE_URL);
                int userName = cursor.getColumnIndex(RESTAURANT_USERNAME);
                int time = cursor.getColumnIndex(RESTAURANT_TIME);
                int date = cursor.getColumnIndex(RESTAURANT_DATE);
                int address =cursor.getColumnIndex(ADDRESS);
                int foodName = cursor.getColumnIndex(RESTAURANT_FOOD_NAME);

                Restaurant st = new Restaurant();
                st.id = cursor.getString(idIndex);
                st.name = cursor.getString(nameIndex);
                st.checked = (cursor.getInt(checkIndex) == 1);
                st.imageUrl = cursor.getString(imageUrlIndex);
                st.userName = cursor.getString(userName);
                st.orderTime = cursor.getString(time);
                st.address= cursor.getString(address);
                st.orderDate = cursor.getString(date);
                st.foodName = cursor.getString(foodName);
                return st;
            }

        }
        return null;

    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RESTAURANT_TABLE +
                " (" +
                RESTAURANT_ID + " TEXT PRIMARY KEY, " +
                RESTAURANT_NAME + " TEXT, " +
                RESTAURANT_FOOD_NAME + " TEXT, " +
                RESTAURANT_CHECK + " NUMBER, " +
                RESTAURANT_IMAGE_URL + " TEXT, "+
                RESTAURANT_USERNAME+" TEXT, "
                +RESTAURANT_DATE+" TEXT, "
                +RESTAURANT_TIME+" TEXT, " +
                ADDRESS+" TEXT);");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + RESTAURANT_TABLE + ";");
        onCreate(db);
    }
    static public void deleteRestaurant(SQLiteDatabase db, Restaurant st)
    {
        db.delete(RESTAURANT_TABLE,RESTAURANT_ID+"=?",new String[]{st.id});
    }
    static public void updateRestaurant(SQLiteDatabase db, Restaurant st)
    {
        deleteRestaurant(db,st);
        addRestaurant(db,st);
        /*ContentValues values = new ContentValues();
        values.put(RESTAURANT_ID, st.id);
        values.put(RESTAURANT_USERNAME, st.userName);
        values.put(RESTAURANT_TIME, st.orderTime);
        values.put(RESTAURANT_DATE, st.orderDate);
        values.put(RESTAURANT_NAME, st.name);
        values.put(ADDRESS, st.address);

        if (st.checked) {
            values.put(RESTAURANT_CHECK, 1);
        } else {
            values.put(RESTAURANT_CHECK, 0);
        }
        values.put(RESTAURANT_IMAGE_URL, st.imageUrl);
        Log.d("Mife",st.id);
        db.update(RESTAURANT_TABLE, values, st.id+"="+RESTAURANT_ID, null);*/
        /*int checked;
        if(st.checked)
            checked = 1;
        else
            checked = 0;
        String strSQL = "UPDATE "+RESTAURANT_TABLE+" SET "+RESTAURANT_ID+" = "+st.id+" , "+RESTAURANT_USERNAME+" = "+st.userName+" , "+RESTAURANT_TIME+" = "+st.orderTime+" , "+RESTAURANT_DATE+
                " = "+st.orderDate+" , "+RESTAURANT_NAME+" = "+st.name+" , "+ADDRESS+" = "+st.address+" , "+RESTAURANT_CHECK+" = "+checked+" WHERE "+RESTAURANT_ID+" = "+ st.id;

        db.execSQL(strSQL);*/
    }

}


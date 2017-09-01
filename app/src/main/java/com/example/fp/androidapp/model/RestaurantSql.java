package com.example.fp.androidapp.model;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fp.androidapp.MyApplication;
import com.example.fp.androidapp.R;

import java.util.LinkedList;
import java.util.List;


public class RestaurantSql {
    public static final String RESTAURANT_TABLE = "restaurants";
    public static final String RESTAURANT_USERNAME="userName";
    public static final String RESTAURANT_DATE="date";
    public static final String RESTAURANT_TIME="time";
    public static final String RESTAURANT_IS_REMOVED = "isRemoved";
    public static final String RESTAURANT_LIKES = "likes";
    public static final String RESTAURANT_USER_LIKES = "userLikes";
    public static final String ADDRESS="address";
    public static final String RESTAURANT_ID = "stid";
    public static final String RESTAURANT_NAME = "name";
    public static final String RESTAURANT_CHECK = "checked";
    public static final String RESTAURANT_IMAGE_URL = "imageUrl";
    public static final String RESTAURANT_FOOD_NAME = "foodName";
    public static final String RESTAURANT_LAST_UPDATE_DATE = "lastUpdateDate";

    static List<Restaurant> getAllRestaurants(SQLiteDatabase db) {
        Cursor cursor = db.query(RESTAURANT_TABLE, null, null, null, null, null, null);
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
            int isRemovedIndex = cursor.getColumnIndex(RESTAURANT_IS_REMOVED);
            int likesIndex = cursor.getColumnIndex(RESTAURANT_LIKES);
            int userLikesIndex = cursor.getColumnIndex(RESTAURANT_USER_LIKES);
            int lastUpdateIndex = cursor.getColumnIndex(RESTAURANT_LAST_UPDATE_DATE);
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
                st.isRemoved = cursor.getInt(isRemovedIndex);
                st.likes = cursor.getInt(likesIndex);
                st.userLikes = cursor.getString(userLikesIndex);
                st.lastUpdateDate = cursor.getDouble(lastUpdateIndex);
                list.add(st);
            } while (cursor.moveToNext());
        }
        return list;
    }
    static List<Restaurant> getAllRestaurantsByFilter(SQLiteDatabase db , String content , String field) {
        Cursor cursor = db.query(RESTAURANT_TABLE, null, null, null, null, null, null); //must implement that way becuase we check without case sensitive and cannot achieve this with regular query.
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
            int isRemovedIndex = cursor.getColumnIndex(RESTAURANT_IS_REMOVED);
            int likesIndex = cursor.getColumnIndex(RESTAURANT_LIKES);
            int userLikesIndex = cursor.getColumnIndex(RESTAURANT_USER_LIKES);
            int lastUpdateIndex = cursor.getColumnIndex(RESTAURANT_LAST_UPDATE_DATE);
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
                st.isRemoved = cursor.getInt(isRemovedIndex);
                st.likes = cursor.getInt(likesIndex);
                st.userLikes = cursor.getString(userLikesIndex);
                st.lastUpdateDate = cursor.getDouble(lastUpdateIndex);
                String [] restaurant_variables = MyApplication.getMyContext().getResources().getStringArray(R.array.restaurant_variables);
                if(restaurant_variables[0].equals(field)) {
                    if(st.name.toLowerCase().contains(content.toLowerCase()))
                        list.add(st);
                }
                else if(restaurant_variables[1].equals(field)) {
                    if(st.foodName.toLowerCase().contains(content.toLowerCase()))
                        list.add(st);
                }
                else if(restaurant_variables[2].equals(field)) {
                    if (st.userName.contains(content))
                        list.add(st);
                }else if(restaurant_variables[3].equals(field)){
                    if(st.address.contains(content))
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
        values.put(RESTAURANT_IS_REMOVED, st.isRemoved);
        values.put(RESTAURANT_LIKES, st.likes);
        values.put(RESTAURANT_USER_LIKES, st.userLikes);
        values.put(RESTAURANT_LAST_UPDATE_DATE, st.lastUpdateDate);

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
                int isRemovedIndex = cursor.getColumnIndex(RESTAURANT_IS_REMOVED);
                int likesIndex = cursor.getColumnIndex(RESTAURANT_LIKES);
                int userLikesIndex = cursor.getColumnIndex(RESTAURANT_USER_LIKES);
                int lastUpdateIndex = cursor.getColumnIndex(RESTAURANT_LAST_UPDATE_DATE);

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
                st.isRemoved = cursor.getInt(isRemovedIndex);
                st.likes = cursor.getInt(likesIndex);
                st.userLikes = cursor.getString(userLikesIndex);
                st.lastUpdateDate = cursor.getDouble(lastUpdateIndex);
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
                RESTAURANT_IS_REMOVED + " NUMBER, " +
                RESTAURANT_LIKES + " NUMBER, " +
                RESTAURANT_USER_LIKES + " TEXT, "+
                RESTAURANT_USERNAME+" TEXT, "
                +RESTAURANT_DATE+" TEXT, "
                +RESTAURANT_TIME+" TEXT, " +
                RESTAURANT_LAST_UPDATE_DATE+" DOUBLE, " +
                ADDRESS+" TEXT);");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RESTAURANT_TABLE);
        onCreate(db);
    }
    static public void deleteRestaurant(SQLiteDatabase db, Restaurant st)
    {
        db.delete(RESTAURANT_TABLE,RESTAURANT_ID+"=?",new String[]{st.id});
    }
    static public void updateRestaurant(SQLiteDatabase db, Restaurant st)
    {
        ContentValues values = new ContentValues();
        values.put(RESTAURANT_ID, st.id);
        values.put(RESTAURANT_USERNAME, st.userName);
        values.put(RESTAURANT_TIME, st.orderTime);
        values.put(RESTAURANT_DATE, st.orderDate);
        values.put(RESTAURANT_NAME, st.name);
        values.put(RESTAURANT_FOOD_NAME, st.foodName);
        values.put(ADDRESS, st.address);
        values.put(RESTAURANT_IS_REMOVED, st.isRemoved);
        values.put(RESTAURANT_LIKES, st.likes);
        values.put(RESTAURANT_USER_LIKES, st.userLikes);
        values.put(RESTAURANT_LAST_UPDATE_DATE, st.lastUpdateDate);
        if (st.checked) {
            values.put(RESTAURANT_CHECK, 1);
        } else {
            values.put(RESTAURANT_CHECK, 0);
        }
        values.put(RESTAURANT_IMAGE_URL, st.imageUrl);
        db.update(RESTAURANT_TABLE, values, RESTAURANT_ID+"=?",new String[]{st.id});
    }

}


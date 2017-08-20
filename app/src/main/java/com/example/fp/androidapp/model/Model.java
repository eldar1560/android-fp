package com.example.fp.androidapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.fp.androidapp.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.example.fp.androidapp.model.ModelFiles.saveImageToFile;



public class Model {
   ModelSql Sql;
    private ModelFirebase modelFirebase;
    public final static Model instace = new Model();

    private Model()
    {
        Sql = new ModelSql(MyApplication.getMyContext());
        modelFirebase = new ModelFirebase();
        synchRestaurantsDbAndregisterRestaurantsUpdates();
    }
    /*public List<Restaurant> getAllRestaurants(){
        //RestaurantSql.onUpgrade(Sql.getReadableDatabase(),0,0);
        return RestaurantSql.getAllRestaurants(
                Sql.getReadableDatabase());

    }*/
    public void getAllRestaurants(final getAllRestaurantsAndObserveCallback callback){

        //5. read from local db
        List<Restaurant> data = RestaurantSql.getAllRestaurants(Sql.getReadableDatabase());

        //6. return list of students
        callback.onComplete(data);

    }
    public List<Restaurant> getAllRestaurantsByFilter(String content , String field){
        return RestaurantSql.getAllRestaurantsByFilter(
                Sql.getReadableDatabase(),content,field);

    }
    public interface getAllRestaurantsAndObserveCallback {
        void onComplete(List<Restaurant> list);
        void onCancel();
    }
    public void addRestaurant(Restaurant st) {
        //RestaurantSql.addRestaurant(modelSql.getWritableDatabase(),st);
        modelFirebase.addRestaurant(st);
    }
    public  void deleteRestaurant(Restaurant st){
        //RestaurantSql.deleteRestaurant(Sql.getWritableDatabase(),st);
        modelFirebase.deleteRestaurant(st);
    }
    public  void updateRestaurant(Restaurant st){
        //RestaurantSql.updateRestaurant(Sql.getWritableDatabase(),st);
        modelFirebase.addRestaurant(st);
    } //same functionallity as update
    public Restaurant getRestaurant(String stId) {
        return RestaurantSql.getRestaurant(Sql.getReadableDatabase(),stId);

    }
    public void getRestaurant(String stId, final getRestaurantCallback callback) {
        //return RestaurantSql.getRestaurant(modelSql.getReadableDatabase(),stId);
        modelFirebase.getRestaurant(stId, new ModelFirebase.getRestaurantCallback() {
            @Override
            public void onComplete(Restaurant restaurant) {
                callback.onComplete(restaurant);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }
    public interface getRestaurantCallback {
        void onComplete(Restaurant restaurant);

        void onCancel();
    }
    private void synchRestaurantsDbAndregisterRestaurantsUpdates() {
        //1. get local lastUpdateTade
        SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
        Log.d("TAG","lastUpdateDate: " + lastUpdateDate);

        modelFirebase.registerRestaurantsUpdates(lastUpdateDate,new ModelFirebase.RegisterRestaurantsUpdatesCallback() {
            @Override
            public void onRestaurantUpdate(Restaurant restaurant) {
                //3. update the local db
                RestaurantSql.addRestaurant(Sql.getWritableDatabase(), restaurant);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < restaurant.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) restaurant.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + restaurant.lastUpdateDate);
                }
                EventBus.getDefault().post(new UpdateRestaurantEvent(restaurant));
            }

            @Override
            public void onRestaurantDeleted(Restaurant restaurant) {
                //3. update the local db
                RestaurantSql.deleteRestaurant(Sql.getWritableDatabase(), restaurant);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < restaurant.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) restaurant.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + restaurant.lastUpdateDate);
                }
                EventBus.getDefault().post(new DeleteRestaurantEvent(restaurant));
            }

            @Override
            public void onRestaurantChanged(Restaurant restaurant) {
                //3. update the local db
                RestaurantSql.updateRestaurant(Sql.getWritableDatabase(), restaurant);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < restaurant.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) restaurant.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + restaurant.lastUpdateDate);
                }
                EventBus.getDefault().post(new UpdateRestaurantEvent(restaurant));
            }

        });
    }
    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                saveImageToFile(imageBmp,fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });


    }


    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }
    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        final String fileName = URLUtil.guessFileName(url, null, null);
        ModelFiles.loadImageFromFileAsynch(fileName, new ModelFiles.LoadImageFromFileAsynch() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null){
                    Log.d("TAG","getImage from local success " + fileName);
                    listener.onSuccess(bitmap);
                }else {
                    modelFirebase.getImage(url, new GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            String fileName = URLUtil.guessFileName(url, null, null);
                            Log.d("TAG","getImage from FB success " + fileName);
                            saveImageToFile(image,fileName);
                            listener.onSuccess(image);
                        }

                        @Override
                        public void onFail() {
                            Log.d("TAG","getImage from FB fail ");
                            listener.onFail();
                        }
                    });

                }
            }
        });
    }

    public class UpdateRestaurantEvent {
        public final Restaurant restaurant;
        public UpdateRestaurantEvent(Restaurant restaurant) {
            this.restaurant = restaurant;
        }
    }
    public class DeleteRestaurantEvent {
        public final Restaurant restaurant;
        public DeleteRestaurantEvent(Restaurant restaurant) {
            this.restaurant = restaurant;
        }
    }
}

package com.example.fp.androidapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.fp.androidapp.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

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
    }

    public void registerUpdates(){
        Log.d("mife","registered");
        synchRestaurantsDbAndregisterRestaurantsUpdates();
    }
    public void unRegisterUpdates(){
        Log.d("mife","unRegistered");
        modelFirebase.unRegisterRestaurantsUpdates();
    }
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
        modelFirebase.addRestaurant(st);
    }
    public  void deleteRestaurant(Restaurant st){
        st.isRemoved = 1;
        modelFirebase.addRestaurant(st);
    }
    public  void updateRestaurant(Restaurant st){
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
        Log.d("mife","lastUpdateDate: " + lastUpdateDate);

        modelFirebase.registerRestaurantsUpdates(lastUpdateDate,new ModelFirebase.RegisterRestaurantsUpdatesCallback() {
            @Override
            public void onRestaurantUpdate(Restaurant restaurant) {
                //3. update the local db
                if(restaurant.isRemoved == 1){
                    if(RestaurantSql.getRestaurant(Sql.getReadableDatabase(),restaurant.id) != null){
                        RestaurantSql.deleteRestaurant(Sql.getWritableDatabase(),restaurant);
                    }
                }else {
                    if(RestaurantSql.getRestaurant(Sql.getReadableDatabase(),restaurant.id) != null){
                        RestaurantSql.updateRestaurant(Sql.getWritableDatabase(),restaurant);
                    }else{
                        RestaurantSql.addRestaurant(Sql.getWritableDatabase(),restaurant);
                    }
                }
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < restaurant.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) restaurant.lastUpdateDate);
                    prefEd.commit();
                    Log.d("mife","StudnetsLastUpdateDate: " + restaurant.lastUpdateDate);
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
}

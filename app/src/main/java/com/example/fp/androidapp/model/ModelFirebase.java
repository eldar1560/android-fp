package com.example.fp.androidapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class ModelFirebase {

    List<ChildEventListener> listeners = new LinkedList<ChildEventListener>();
    public void addRestaurant(Restaurant st) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        Map<String, Object> value = new HashMap<>();
        value.put("id", st.id);
        value.put("name", st.name);
        value.put("imageUrl", st.imageUrl);
        value.put("checked", st.checked);
        value.put("lastUpdateDate", ServerValue.TIMESTAMP);
        value.put("foodName", st.foodName);
        value.put("address", st.address);
        value.put("userName", st.userName);
        value.put("orderDate", st.orderDate);
        value.put("orderTime", st.orderTime);
        myRef.child(st.id).setValue(value);
    }


    public void deleteRestaurant (Restaurant st){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        myRef.child(st.id).removeValue();
    }
    interface getRestaurantCallback {
        void onComplete(Restaurant restaurant);

        void onCancel();
    }

    public void getRestaurant(String stId, final getRestaurantCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        myRef.child(stId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                callback.onComplete(restaurant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    interface getAllRestaurantsAndObserveCallback {
        void onComplete(List<Restaurant> list);
        void onCancel();
    }
    public void getAllRestaurantsAndObserve(final getAllRestaurantsAndObserveCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Restaurant> list = new LinkedList<Restaurant>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snap.getValue(Restaurant.class);
                    list.add(restaurant);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }





    public void saveImage(Bitmap imageBmp, String name, final Model.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }


    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                listener.onFail();
            }
        });
    }


    interface RegisterRestaurantsUpdatesCallback{
        void onRestaurantUpdate(Restaurant restaurant);
        void onRestaurantDeleted(Restaurant restaurant);
        void onRestaurantChanged(Restaurant restaurant);
    }
    public void registerRestaurantsUpdates(double lastUpdateDate,
                                        final RegisterRestaurantsUpdatesCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate);
        ChildEventListener listener = myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Mife","onChildAdded called");
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                callback.onRestaurantUpdate(restaurant);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("Mife","onChildChanged called");
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                callback.onRestaurantChanged(restaurant);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Mife","onChildremoved called");
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                callback.onRestaurantDeleted(restaurant);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                callback.onRestaurantUpdate(restaurant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listeners.add(listener);
    }
//    public void getAllRestaurantsAndObserve(double lastUpdateDate,
//                                         final getAllRestaurantsAndObserveCallback callback) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("students");
//
//        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        List<Restaurant> list = new LinkedList<Restaurant>();
//                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                            Restaurant restaurant = snap.getValue(Restaurant.class);
//                            list.add(restaurant);
//                        }
//                        callback.onComplete(list);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//    }

}
















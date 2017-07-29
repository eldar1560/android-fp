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

/**
 * Created by menachi on 24/05/2017.
 */

public class ModelFirebase {

    List<ChildEventListener> listeners = new LinkedList<ChildEventListener>();
    public void addStudent(Student st) {
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


    public void deleteStudent (Student st){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        myRef.child(st.id).removeValue();
    }
    interface GetStudentCallback {
        void onComplete(Student student);

        void onCancel();
    }

    public void getStudent(String stId, final GetStudentCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        myRef.child(stId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                callback.onComplete(student);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    interface GetAllStudentsAndObserveCallback {
        void onComplete(List<Student> list);
        void onCancel();
    }
    public void getAllStudentsAndObserve(final GetAllStudentsAndObserveCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Student> list = new LinkedList<Student>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Student student = snap.getValue(Student.class);
                    list.add(student);
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


    interface RegisterStudentsUpdatesCallback{
        void onStudentUpdate(Student student);
        void onStudentDeleted(Student student);
    }
    public void registerStudentsUpdates(double lastUpdateDate,
                                        final RegisterStudentsUpdatesCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");
        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate);
        ChildEventListener listener = myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Mife","onChildAdded called");
                Student student = dataSnapshot.getValue(Student.class);
                callback.onStudentUpdate(student);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Student student = dataSnapshot.getValue(Student.class);
                callback.onStudentUpdate(student);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Mife","onChildremoved called");
                Student student = dataSnapshot.getValue(Student.class);
                callback.onStudentDeleted(student);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Student student = dataSnapshot.getValue(Student.class);
                callback.onStudentUpdate(student);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listeners.add(listener);
    }
//    public void getAllStudentsAndObserve(double lastUpdateDate,
//                                         final GetAllStudentsAndObserveCallback callback) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("students");
//
//        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        List<Student> list = new LinkedList<Student>();
//                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                            Student student = snap.getValue(Student.class);
//                            list.add(student);
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
















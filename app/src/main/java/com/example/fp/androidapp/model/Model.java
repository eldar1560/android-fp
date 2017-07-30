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

/**
 * Created by menachi on 19/04/2017.
 */

public class Model {
   ModelSql Sql;
    private ModelFirebase modelFirebase;
    public final static Model instace = new Model();

    private Model()
    {
        Sql = new ModelSql(MyApplication.getMyContext());
        modelFirebase = new ModelFirebase();
        synchStudentsDbAndregisterStudentsUpdates();
    }
    /*public List<Student> getAllStudents(){
        //StudentSql.onUpgrade(Sql.getReadableDatabase(),0,0);
        return StudentSql.getAllStudents(
                Sql.getReadableDatabase());

    }*/
    public void getAllStudents(final GetAllStudentsAndObserveCallback callback){

        //5. read from local db
        List<Student> data = StudentSql.getAllStudents(Sql.getReadableDatabase());

        //6. return list of students
        callback.onComplete(data);

    }
    public List<Student> getAllStudentsByFilter(String content , String field){
        return StudentSql.getAllStudentsByFilter(
                Sql.getReadableDatabase(),content,field);

    }
    public interface GetAllStudentsAndObserveCallback {
        void onComplete(List<Student> list);
        void onCancel();
    }
    public void addStudent(Student st) {
        //StudentSql.addStudent(modelSql.getWritableDatabase(),st);
        modelFirebase.addStudent(st);
    }
    public  void deleteStudent(Student st){
        //StudentSql.deleteStudent(Sql.getWritableDatabase(),st);
        modelFirebase.deleteStudent(st);
    }
    public  void updateStudent(Student st){
        //StudentSql.updateStudent(Sql.getWritableDatabase(),st);
        modelFirebase.addStudent(st);} //same functionallity as update
    public Student getStudent(String stId) {
        return StudentSql.getStudent(Sql.getReadableDatabase(),stId);

    }
    public void getStudent(String stId, final GetStudentCallback callback) {
        //return StudentSql.getStudent(modelSql.getReadableDatabase(),stId);
        modelFirebase.getStudent(stId, new ModelFirebase.GetStudentCallback() {
            @Override
            public void onComplete(Student student) {
                callback.onComplete(student);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }
    public interface GetStudentCallback {
        void onComplete(Student student);

        void onCancel();
    }
    private void synchStudentsDbAndregisterStudentsUpdates() {
        //1. get local lastUpdateTade
        SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
        Log.d("TAG","lastUpdateDate: " + lastUpdateDate);

        modelFirebase.registerStudentsUpdates(lastUpdateDate,new ModelFirebase.RegisterStudentsUpdatesCallback() {
            @Override
            public void onStudentUpdate(Student student) {
                //3. update the local db
                StudentSql.addStudent(Sql.getWritableDatabase(),student);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < student.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) student.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + student.lastUpdateDate);
                }
                EventBus.getDefault().post(new UpdateStudentEvent(student));
            }

            @Override
            public void onStudentDeleted(Student student) {
                //3. update the local db
                StudentSql.deleteStudent(Sql.getWritableDatabase(),student);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < student.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) student.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + student.lastUpdateDate);
                }
                EventBus.getDefault().post(new DeleteStudentEvent(student));
            }

            @Override
            public void onStudentChanged(Student student) {
                //3. update the local db
                StudentSql.updateStudent(Sql.getWritableDatabase(),student);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < student.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) student.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + student.lastUpdateDate);
                }
                EventBus.getDefault().post(new UpdateStudentEvent(student));
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

    public class UpdateStudentEvent {
        public final Student student;
        public UpdateStudentEvent(Student student) {
            this.student = student;
        }
    }
    public class DeleteStudentEvent {
        public final Student student;
        public DeleteStudentEvent(Student student) {
            this.student = student;
        }
    }
}

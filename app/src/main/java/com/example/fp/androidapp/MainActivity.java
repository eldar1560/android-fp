package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Random;

import static android.view.View.GONE;


public class MainActivity extends Activity implements RestaurantMainFragment.StudentMainFragmentListener{
    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;

    RestaurantMainFragment restaurantMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResult(RESAULT_FAIL);
        ActionBar bar = getActionBar();
        bar.setTitle("New Restaurant");
        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getFragmentManager().findFragmentById(R.id.main_fragment_container) != null){
            restaurantMainFragment = RestaurantMainFragment.newInstance();

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.replace(R.id.main_fragment_container, restaurantMainFragment);
            tran.commit();
        }else {
            restaurantMainFragment = RestaurantMainFragment.newInstance();

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.main_fragment_container, restaurantMainFragment, "tag");
            tran.commit();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCancel() {
        onBackPressed();
    }

    @Override
    public void onSave(EditText nameEt , EditText foodNameEt , EditText addressEt , CheckBox cbEt , MyTimePicker ot , MyDatePicker od , final ProgressBar progressBar , Bitmap imageBitmap) {
        if(foodNameEt.getText().toString().equals("") || nameEt.getText().toString().equals("") || addressEt.getText().toString().equals("") || ot.getText().toString().equals("") || od.getText().toString().equals("")) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Restaurant")
                    .setMessage("Do not leave a field empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        int random_id;
        String random_id_string = "";
        boolean isFound = true;
        while(isFound) {
            Random rand = new Random();
            random_id = rand.nextInt(2147483647);
            Log.d("Mife", "random id : " + random_id);
            random_id_string = String.valueOf(random_id);
            if(Model.instace.getStudent(random_id_string) == null){
                Log.d("Mife", "found new id");
                isFound = false;
            }
        }
        Log.d("TAG","Btn Save click");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Student st = new Student(random_id_string , nameEt.getText().toString(), foodNameEt.getText().toString(), user.getEmail() , addressEt.getText().toString() , cbEt.isChecked(),"" , ot.getText().toString() , od.getText().toString());
        if (imageBitmap != null) {
            Model.instace.saveImage(imageBitmap, st.id + ".jpeg", new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    st.imageUrl = url;
                    Model.instace.addStudent(st);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("New Restaurant")
                            .setMessage("The save operation was completed successfully.")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    setResult(RESAULT_SUCCESS);
                    progressBar.setVisibility(GONE);
                    //finish();
                }

                @Override
                public void fail() {
                    //notify operation fail,...
                    setResult(RESAULT_SUCCESS);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("New Restaurant")
                            .setMessage("The save operation was completed , without image...")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    progressBar.setVisibility(GONE);
                    //finish();
                }
            });
        }else{
            Model.instace.addStudent(st);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Restaurant")
                    .setMessage("The save operation was completed successfully.")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
            setResult(RESAULT_SUCCESS);
            progressBar.setVisibility(GONE);
            //finish();
        }
        /*if(Model.instace.getStudent(idEt.getText().toString()) != null) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Restaurant")
                    .setMessage("Id is already exist!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }*/
        /*Student new_student = new Student(idEt.getText().toString() , nameEt.getText().toString(), phoneEt.getText().toString() , addressEt.getText().toString() , cbEt.isChecked(),"" , bt.getText().toString() , bd.getText().toString());
        Model.instace.addStudent(new_student);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("New Restaurant")
                .setMessage("The save operation was completed successfully.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        setResult(RESAULT_SUCCESS);*/
    }
}

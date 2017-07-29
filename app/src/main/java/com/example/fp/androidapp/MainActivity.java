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
    public void onSave(EditText nameEt , EditText idEt , EditText phoneEt , EditText addressEt , CheckBox cbEt , MyTimePicker bt , MyDatePicker bd , final ProgressBar progressBar , Bitmap imageBitmap) {
        Log.d("Mife",bt.getText().toString() + " , " + bd.getText().toString());
        if(idEt.getText().toString().equals("") || nameEt.getText().toString().equals("") || phoneEt.getText().toString().equals("") || addressEt.getText().toString().equals("") || bt.getText().toString().equals("") || bd.getText().toString().equals("")) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Restaurant")
                    .setMessage("Do not leave a field empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        Log.d("TAG","Btn Save click");
        final Student st = new Student(idEt.getText().toString() , nameEt.getText().toString(), phoneEt.getText().toString() , addressEt.getText().toString() , cbEt.isChecked(),"" , bt.getText().toString() , bd.getText().toString());
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

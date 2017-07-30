package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
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

public class RestaurantEditActivity extends Activity implements RestaurantEditFragment.StudentEditFragmentListener {

    final static int RESAULT_SUCCESS_SAVE = 0;
    final static int RESAULT_SUCCESS_DELETE = 2;
    final static int RESAULT_FAIL = 1;
    Student st_edit;

    RestaurantEditFragment restaurantEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_edit);
        setResult(RESAULT_FAIL);

        ActionBar bar = getActionBar();
        bar.setTitle("Edit Restaurant");
        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String stId = intent.getStringExtra("STID");

        if(getFragmentManager().findFragmentById(R.id.edit_fragment_container) != null){
            restaurantEditFragment = RestaurantEditFragment.newInstance(stId);

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.replace(R.id.edit_fragment_container, restaurantEditFragment);
            tran.commit();

        }else {
            restaurantEditFragment = RestaurantEditFragment.newInstance(stId);

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.edit_fragment_container, restaurantEditFragment, "tag");
            tran.commit();
        }

        Model.instace.getStudent(stId, new Model.GetStudentCallback() {
            @Override
            public void onComplete(Student student) {
                RestaurantEditActivity.this.st_edit = student;
                Log.d("TAG","got student name: " + student.name);
            }

            @Override
            public void onCancel() {
                Log.d("TAG","get student cancell" );

            }
        });

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
    public void onSave(EditText foodNameEt , EditText nameEt , EditText userNameEt , EditText addressEt , CheckBox cbEt , MyTimePicker ot , MyDatePicker od , final ProgressBar progressBar , Bitmap imageBitmap) {
        if(foodNameEt.getText().toString().equals("") || nameEt.getText().toString().equals("") || userNameEt.getText().toString().equals("") || addressEt.getText().toString().equals("") || ot.getText().toString().equals("") || od.getText().toString().equals("")) {
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Restaurant")
                    .setMessage("Do not leave a field empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //Model.instace.deleteStudent(st_edit);
        st_edit.foodName = foodNameEt.getText().toString();
        st_edit.name = nameEt.getText().toString();
        st_edit.userName = userNameEt.getText().toString();
        st_edit.address = addressEt.getText().toString();
        st_edit.checked = cbEt.isChecked();
        st_edit.orderTime = ot.getText().toString();
        st_edit.orderDate = od.getText().toString();
        //Model.instace.addStudent(st_edit);
        if (imageBitmap != null) {
            Model.instace.saveImage(imageBitmap, st_edit.id + ".jpeg", new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    st_edit.imageUrl = url;
                    Model.instace.updateStudent(st_edit);
                    new AlertDialog.Builder(RestaurantEditActivity.this)
                            .setTitle("Edit Restaurant")
                            .setMessage("The save operation was completed successfully.")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    setResult(RESAULT_SUCCESS_SAVE);
                    progressBar.setVisibility(GONE);
                    //finish();
                }

                @Override
                public void fail() {
                    //notify operation fail,...
                    Model.instace.updateStudent(st_edit);
                    new AlertDialog.Builder(RestaurantEditActivity.this)
                            .setTitle("Edit Restaurant")
                            .setMessage("The save operation was completed , without image...")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    setResult(RESAULT_SUCCESS_SAVE);
                    progressBar.setVisibility(GONE);
                    //finish();
                }
            });
        }else{
            Model.instace.updateStudent(st_edit);
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Restaurant")
                    .setMessage("The save operation was completed successfully.")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
            setResult(RESAULT_SUCCESS_SAVE);
            progressBar.setVisibility(GONE);
            //finish();
        }
        /*Model.instace.updateStudent(st_edit);
        new AlertDialog.Builder(RestaurantEditActivity.this)
                .setTitle("Edit Restaurant")
                .setMessage("The save operation was completed successfully.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        setResult(RESAULT_SUCCESS_SAVE);*/
    }

    @Override
    public void onDelete() {
        Model.instace.deleteStudent(st_edit);
        new AlertDialog.Builder(RestaurantEditActivity.this)
                .setTitle("Edit Restaurant")
                .setMessage("The delete operation was completed successfully.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        onBackPressed();
                    }
                })
                .show();
        setResult(RESAULT_SUCCESS_DELETE);
    }
}

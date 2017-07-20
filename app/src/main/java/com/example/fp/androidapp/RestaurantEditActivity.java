package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;

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

        st_edit = Model.instace.getStudent(stId);

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
    public void onSave(EditText idEt , EditText nameEt , EditText phoneEt , EditText addressEt , CheckBox cbEt , MyTimePicker bt , MyDatePicker bd) {
        if(idEt.getText().toString().equals("") || nameEt.getText().toString().equals("") || phoneEt.getText().toString().equals("") || addressEt.getText().toString().equals("") || bt.getText().toString().equals("") || bd.getText().toString().equals("")) {
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Restaurant")
                    .setMessage("Do not leave a field empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        /*if(Model.instace.getStudent(idEt.getText().toString()) != null && (!idEt.getText().toString().equals(st_edit.id))) {
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Student")
                    .setMessage("Id is already exist!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }*/
        if(!idEt.getText().toString().equals(st_edit.id)){
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Restaurant")
                    .setMessage("Cannot change ID!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        Model.instace.deleteStudent(st_edit);
        st_edit.id = idEt.getText().toString();
        st_edit.name = nameEt.getText().toString();
        st_edit.phone = phoneEt.getText().toString();
        st_edit.address = addressEt.getText().toString();
        st_edit.checked = cbEt.isChecked();
        st_edit.birthTime = bt.getText().toString();
        st_edit.birthDate = bd.getText().toString();
        Model.instace.addStudent(st_edit);
        new AlertDialog.Builder(RestaurantEditActivity.this)
                .setTitle("Edit Restaurant")
                .setMessage("The save operation was completed successfully.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        setResult(RESAULT_SUCCESS_SAVE);
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

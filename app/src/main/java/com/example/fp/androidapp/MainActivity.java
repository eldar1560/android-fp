package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;



public class MainActivity extends Activity implements StudentMainFragment.StudentMainFragmentListener{
    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;

    StudentMainFragment studentMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResult(RESAULT_FAIL);
        ActionBar bar = getActionBar();
        bar.setTitle("New Students");
        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getFragmentManager().findFragmentById(R.id.main_fragment_container) != null){
            studentMainFragment = StudentMainFragment.newInstance();

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.replace(R.id.main_fragment_container,studentMainFragment);
            tran.commit();
        }else {
            studentMainFragment = StudentMainFragment.newInstance();

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.main_fragment_container, studentMainFragment, "tag");
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
    public void onSave(EditText nameEt , EditText idEt , EditText phoneEt , EditText addressEt , CheckBox cbEt , MyTimePicker bt , MyDatePicker bd) {
        Log.d("Mife",bt.getText().toString() + " , " + bd.getText().toString());
        if(idEt.getText().toString().equals("") || nameEt.getText().toString().equals("") || phoneEt.getText().toString().equals("") || addressEt.getText().toString().equals("") || bt.getText().toString().equals("") || bd.getText().toString().equals("")) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Student")
                    .setMessage("Do not leave a field empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        if(Model.instace.getStudent(idEt.getText().toString()) != null) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Student")
                    .setMessage("Id is already exist!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        Student new_student = new Student(idEt.getText().toString() , nameEt.getText().toString(), phoneEt.getText().toString() , addressEt.getText().toString() , cbEt.isChecked(),"" , bt.getText().toString() , bd.getText().toString());
        Model.instace.addStudent(new_student);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("New Student")
                .setMessage("The save operation was completed successfully.")
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        setResult(RESAULT_SUCCESS);
    }
}

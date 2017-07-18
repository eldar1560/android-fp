package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;

import java.util.List;

public class StudentsListActivity extends Activity implements StudentListFragment.StudentListFragmentListener {
    StudentListFragment studentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        ActionBar bar = getActionBar();
        bar.setTitle("Students List");

        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getFragmentManager().findFragmentById(R.id.list_fragment_container) != null){
            List<Student> data = Model.instace.getAllStudents();
            
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            studentListFragment = StudentListFragment.newInstance();
            tran.replace(R.id.list_fragment_container , studentListFragment);
            tran.commit();
        }else {
            Log.d("TAG" , "fragment is null");

            studentListFragment = StudentListFragment.newInstance();

            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.list_fragment_container, studentListFragment, "tag");
            tran.commit();
        }

    }

    static final int REQUEST_ID = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID){
            if (resultCode == MainActivity.RESAULT_SUCCESS){
                //operation success
                Log.d("TAG","operation success");

                //FrameLayout fl = (FrameLayout)findViewById(R.id.list_fragment_container);
                //fl.invalidate();

                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.detach(studentListFragment);
                tran.attach(studentListFragment);
                tran.commit();
            }else{
                Log.d("TAG","operation fail");
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "Add").setIcon(R.drawable.button_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(StudentsListActivity.this,MainActivity.class);
                startActivityForResult(intent,REQUEST_ID);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSelect(AdapterView<?> parent, View view, int position, long id , List<Student> data) {
        Log.d("TAG", "row item was clicked at position: " + position);
        Intent intent = new Intent(StudentsListActivity.this,StudentDetailsActivity.class);
        intent.putExtra("STID",data.get(position).id);
        Log.d("TAG","student id selected = " + data.get(position).id);
        startActivityForResult(intent , REQUEST_ID);
    }


}











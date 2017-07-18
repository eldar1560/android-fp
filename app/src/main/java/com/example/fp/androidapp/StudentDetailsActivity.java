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

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;

public class StudentDetailsActivity extends Activity{

    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;

    StudentDetailsFragment studentDetailsFragment;

    Student st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        setResult(RESAULT_FAIL);

        ActionBar bar = getActionBar();
        bar.setTitle("Students Details");
        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String stId = intent.getStringExtra("STID");
        st = Model.instace.getStudent(stId);

        studentDetailsFragment = StudentDetailsFragment.newInstance(st.id);

        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.details_fragment_container,studentDetailsFragment,"tag");
        tran.commit();



    }

    static final int REQUEST_ID = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID){
            if (resultCode == StudentEditActivity.RESAULT_SUCCESS_SAVE){
                //operation success save
                Log.d("TAG","operation success save");
                setResult(RESAULT_SUCCESS);

                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.remove(studentDetailsFragment);
                studentDetailsFragment = StudentDetailsFragment.newInstance(st.id);
                tran.add(R.id.details_fragment_container,studentDetailsFragment,"tag");
                tran.commit();


            }else if(resultCode == StudentEditActivity.RESAULT_SUCCESS_DELETE) {
                //operation success delete
                Log.d("TAG","operation success delete");
                setResult(RESAULT_SUCCESS);
                onBackPressed();
            }
            else
            {
                Log.d("TAG","operation fail");
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "Edit").setIcon(R.drawable.edit_button)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(StudentDetailsActivity.this,StudentEditActivity.class);
                intent.putExtra("STID",st.id);
                startActivityForResult(intent,REQUEST_ID);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

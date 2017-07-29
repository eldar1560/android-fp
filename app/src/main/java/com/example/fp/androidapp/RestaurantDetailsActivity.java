package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;

import java.util.Locale;

public class RestaurantDetailsActivity extends Activity{

    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;

    RestaurantDetailsFragment restaurantDetailsFragment;

    Student st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        setResult(RESAULT_FAIL);

        ActionBar bar = getActionBar();
        bar.setTitle("Restaurant Details");
        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String stId = intent.getStringExtra("STID");
        Model.instace.getStudent(stId, new Model.GetStudentCallback() {
            @Override
            public void onComplete(Student student) {
                RestaurantDetailsActivity.this.st = student;
                Log.d("TAG","got student name: " + student.name);
            }

            @Override
            public void onCancel() {
                Log.d("TAG","get student cancell" );

            }
        });

        restaurantDetailsFragment = RestaurantDetailsFragment.newInstance(st.id);

        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.details_fragment_container, restaurantDetailsFragment,"tag");
        tran.commit();



    }

    static final int REQUEST_ID = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID){
            if (resultCode == RestaurantEditActivity.RESAULT_SUCCESS_SAVE){
                //operation success save
                Log.d("TAG","operation success save");
                setResult(RESAULT_SUCCESS);

                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.remove(restaurantDetailsFragment);
                restaurantDetailsFragment = RestaurantDetailsFragment.newInstance(st.id);
                tran.add(R.id.details_fragment_container, restaurantDetailsFragment,"tag");
                tran.commit();


            }else if(resultCode == RestaurantEditActivity.RESAULT_SUCCESS_DELETE) {
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
        menu.add(0,1,0,"Show On Map").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(RestaurantDetailsActivity.this,RestaurantEditActivity.class);
                intent.putExtra("STID",st.id);
                startActivityForResult(intent,REQUEST_ID);
                return true;
            case 1:
                Model.instace.getStudent(st.id, new Model.GetStudentCallback() {
                    @Override
                    public void onComplete(Student student) {
                        RestaurantDetailsActivity.this.st = student;
                        Log.d("TAG","got student name: " + student.name);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG","get student cancell" );

                    }
                });
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s",st.address);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent2);
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

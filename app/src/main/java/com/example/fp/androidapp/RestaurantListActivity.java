package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.fp.androidapp.model.Student;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RestaurantListActivity extends Activity implements RestaurantListFragment.StudentListFragmentListener {
    RestaurantListFragment restaurantListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        ActionBar bar = getActionBar();
        bar.setTitle("Restaurants List");

        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        if(getFragmentManager().findFragmentById(R.id.list_fragment_container) != null){
            
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            restaurantListFragment = RestaurantListFragment.newInstance("" ,"");
            tran.replace(R.id.list_fragment_container , restaurantListFragment);
            tran.commit();
        }else {
            Log.d("TAG" , "fragment is null");

            restaurantListFragment = RestaurantListFragment.newInstance("","");


            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.list_fragment_container, restaurantListFragment, "tag");
            tran.commit();
        }

    }

    static final int REQUEST_ID = 1;
    static final int REQUEST_WRITE_STORAGE = 11;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID){
            if (resultCode == MainActivity.RESAULT_SUCCESS){
                //operation success
                Log.d("TAG","operation success");

                //FrameLayout fl = (FrameLayout)findViewById(R.id.list_fragment_container);
                //fl.invalidate();
                /*FragmentTransaction tran = getFragmentManager().beginTransaction();
                restaurantListFragment = RestaurantListFragment.newInstance("");
                tran.replace(R.id.list_fragment_container , restaurantListFragment);
                tran.commit();*/
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.detach(restaurantListFragment);
                tran.attach(restaurantListFragment);
                tran.commit();
            }else{
                Log.d("TAG","operation fail");
            }

        } else{
            if (resultCode == REQUEST_WRITE_STORAGE){
                Log.d("TAG", "REQUEST_WRITE_STORAGE");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "Add").setIcon(R.drawable.button_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0,1,0,"ShowAll").setIcon(R.drawable.show_all)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(RestaurantListActivity.this,MainActivity.class);
                startActivityForResult(intent,REQUEST_ID);
                return true;
            case 1:
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                restaurantListFragment = RestaurantListFragment.newInstance("","");
                tran.replace(R.id.list_fragment_container , restaurantListFragment);
                tran.commit();
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
        Intent intent = new Intent(RestaurantListActivity.this,RestaurantDetailsActivity.class);
        intent.putExtra("STID",data.get(position).id);
        Log.d("TAG","student id selected = " + data.get(position).id);
        startActivityForResult(intent , REQUEST_ID);
    }

    @Override
    public void onSearch(String content ,String field) {
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        restaurantListFragment = RestaurantListFragment.newInstance(content,field);
        tran.replace(R.id.list_fragment_container , restaurantListFragment);
        tran.commit();
        Log.d("Mife" , "the string is :" +content + " , "+field);
    }


}











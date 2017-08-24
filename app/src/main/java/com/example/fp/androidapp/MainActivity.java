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
import com.example.fp.androidapp.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import static android.view.View.GONE;


public class MainActivity extends Activity implements RestaurantMainFragment.RestaurantMainFragmentListener{
    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;

    RestaurantMainFragment restaurantMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResult(RESAULT_FAIL);
        ActionBar bar = getActionBar();
        bar.setTitle("New Post");
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
        if(foodNameEt.getText().toString().equals("") || nameEt.getText().toString().equals("") || addressEt.getText().toString().equals("") || ot.getText().toString().equals("") || od.getText().toString().equals("") || imageBitmap == null) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Post")
                    .setMessage("Do not leave a field or image empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }else if(nameEt.getText().toString().contains("\n") || foodNameEt.getText().toString().contains("\n") || addressEt.getText().toString().contains("\n")){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("New Post")
                    .setMessage("Do not put an enter in the fields!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        int random_id;
        String random_id_string = "";
        boolean isFound = false;
        while(!isFound) {
            Random rand = new Random();
            random_id = rand.nextInt(2147483647);
            Log.d("Mife", "random id : " + random_id);
            random_id_string = String.valueOf(random_id);
            if(Model.instace.getRestaurant(random_id_string) == null){
                Log.d("Mife", "found new id");
                isFound = true;
            }
        }
        Log.d("TAG","Btn Save click");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Restaurant st = new Restaurant(random_id_string , nameEt.getText().toString(), foodNameEt.getText().toString(), user.getEmail() , addressEt.getText().toString() , cbEt.isChecked(),"" , ot.getText().toString() , od.getText().toString());
        Model.instace.saveImage(imageBitmap, st.id + ".jpeg", new Model.SaveImageListener() {
            @Override
            public void complete(String url) {
                st.imageUrl = url;
                Model.instace.addRestaurant(st);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New Post")
                        .setMessage("The save operation was completed successfully.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                setResult(RESAULT_SUCCESS);
                progressBar.setVisibility(GONE);
            }

            @Override
            public void fail() {
                //notify operation fail,...
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New Post")
                        .setMessage("Image couldn't be loaded , try again...")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                progressBar.setVisibility(GONE);
                return;
            }
        });

    }
}

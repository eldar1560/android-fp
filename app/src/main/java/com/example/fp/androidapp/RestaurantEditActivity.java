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
import com.example.fp.androidapp.model.Restaurant;

import static android.view.View.GONE;

public class RestaurantEditActivity extends Activity implements RestaurantEditFragment.RestaurantEditFragmentListener {

    final static int RESAULT_SUCCESS_SAVE = 0;
    final static int RESAULT_SUCCESS_DELETE = 2;
    final static int RESAULT_FAIL = 1;
    Restaurant st_edit;

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

        Model.instace.getRestaurant(stId, new Model.getRestaurantCallback() {
            @Override
            public void onComplete(Restaurant restaurant) {
                RestaurantEditActivity.this.st_edit = restaurant;
                Log.d("TAG","got restaurant name: " + restaurant.name);
            }

            @Override
            public void onCancel() {
                Log.d("TAG","get restaurant cancell" );

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
    public void onSave(EditText foodNameEt , EditText nameEt , EditText addressEt , CheckBox cbEt , MyTimePicker ot , MyDatePicker od , final ProgressBar progressBar , Bitmap imageBitmap) {
        if(foodNameEt.getText().toString().equals("") || nameEt.getText().toString().equals("") ||  addressEt.getText().toString().equals("") || ot.getText().toString().equals("") || od.getText().toString().equals("")) {
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Restaurant")
                    .setMessage("Do not leave a field or image empty!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }else if(nameEt.getText().toString().contains("\n") || foodNameEt.getText().toString().contains("\n") || addressEt.getText().toString().contains("\n")){
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("New Restaurant")
                    .setMessage("Do not put an enter in the fields")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        st_edit.foodName = foodNameEt.getText().toString();
        st_edit.name = nameEt.getText().toString();
        st_edit.address = addressEt.getText().toString();
        st_edit.checked = cbEt.isChecked();
        st_edit.orderTime = ot.getText().toString();
        st_edit.orderDate = od.getText().toString();
        if (imageBitmap != null) {
            Model.instace.saveImage(imageBitmap, st_edit.id + ".jpeg", new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    st_edit.imageUrl = url;
                    Model.instace.updateRestaurant(st_edit);
                    new AlertDialog.Builder(RestaurantEditActivity.this)
                            .setTitle("Edit Restaurant")
                            .setMessage("The save operation was completed successfully.")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    setResult(RESAULT_SUCCESS_SAVE);
                    progressBar.setVisibility(GONE);
                }

                @Override
                public void fail() {
                    //notify operation fail,...
                    Model.instace.updateRestaurant(st_edit);
                    new AlertDialog.Builder(RestaurantEditActivity.this)
                            .setTitle("Edit Restaurant")
                            .setMessage("The save operation was completed , without the new image...")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    setResult(RESAULT_SUCCESS_SAVE);
                    progressBar.setVisibility(GONE);
                }
            });
        }else{
            Model.instace.updateRestaurant(st_edit);
            new AlertDialog.Builder(RestaurantEditActivity.this)
                    .setTitle("Edit Restaurant")
                    .setMessage("The save operation was completed successfully.")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
            setResult(RESAULT_SUCCESS_SAVE);
            progressBar.setVisibility(GONE);
        }
    }

    @Override
    public void onDelete() {
        Model.instace.deleteRestaurant(st_edit);
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

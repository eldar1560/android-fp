package com.example.fp.androidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Restaurant;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class RestaurantDetailsActivity extends Activity implements RestaurantDetailsFullImageFragment.RestaurantDetailsFullImageFragmentListener , RestaurantDetailsFragment.RestaurantDetailsFragmentListener{

    final static int RESAULT_SUCCESS = 0;
    final static int RESAULT_FAIL = 1;

    RestaurantDetailsFragment restaurantDetailsFragment;
    RestaurantDetailsFullImageFragment restaurantDetailsFullImageFragment;

    Restaurant st;
    String stId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        setResult(RESAULT_FAIL);

        ActionBar bar = getActionBar();
        bar.setTitle("Post Details");
        bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        stId = intent.getStringExtra("STID");
        st = Model.instace.getRestaurant(stId);
        Log.d("Mife","got restaurant name: " + st.name);
        restaurantDetailsFragment = RestaurantDetailsFragment.newInstance(st.id);

        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.details_fragment_container, restaurantDetailsFragment,"tag");
        tran.commit();
        new CountDownTimer(50, 50) { //for the case the fragment is not replaced already

            public void onTick(long millisUntilFinished) {
                RestaurantDetailsActivity.this.invalidateOptionsMenu();
            }

            public void onFinish() {
                RestaurantDetailsActivity.this.invalidateOptionsMenu();
            }

        }.start();

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
                RestaurantDetailsActivity.this.invalidateOptionsMenu();


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
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        Fragment rlf;
        rlf = getFragmentManager().findFragmentById(R.id.details_fragment_container);
        if(rlf instanceof RestaurantDetailsFragment) {
            st = Model.instace.getRestaurant(stId);
            menu.add(0, 0, 0, "Show On Map").setIcon(R.drawable.googlemaps)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            if (st.userName.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                menu.add(0, 1, 0, "Edit").setIcon(R.drawable.edit_icon)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            } else {
                menu.add(0, 2, 0, "Contact User").setIcon(R.drawable.gmail)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 0:
                st = Model.instace.getRestaurant(stId);
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s",st.address);
                Intent intent_map = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent_map);
                return true;
            case 1:
                Intent intent = new Intent(RestaurantDetailsActivity.this,RestaurantEditActivity.class);
                intent.putExtra("STID",st.id);
                startActivityForResult(intent,REQUEST_ID);
                return true;
            case 2:
                try
                {
                    Intent intent_gmail = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + st.userName));
                    //intent_gmail.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    //intent_gmail.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent_gmail);
                }
                catch(Exception e)
                {
                    Toast.makeText(MyApplication.getMyContext(), "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBack() {
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        restaurantDetailsFragment = RestaurantDetailsFragment.newInstance(st.id);
        tran.replace(R.id.details_fragment_container , restaurantDetailsFragment);
        tran.commit();
        new CountDownTimer(50, 50) { //for the case the fragment is not replaced already

            public void onTick(long millisUntilFinished) {
                RestaurantDetailsActivity.this.invalidateOptionsMenu();
            }

            public void onFinish() {
                RestaurantDetailsActivity.this.invalidateOptionsMenu();
            }

        }.start();
    }

    @Override
    public void onFullImage() {
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        restaurantDetailsFullImageFragment = RestaurantDetailsFullImageFragment.newInstance(st.id);
        tran.replace(R.id.details_fragment_container , restaurantDetailsFullImageFragment);
        tran.commit();
        new CountDownTimer(50, 50) { //for the case the fragment is not replaced already

            public void onTick(long millisUntilFinished) {
                RestaurantDetailsActivity.this.invalidateOptionsMenu();
            }

            public void onFinish() {
                RestaurantDetailsActivity.this.invalidateOptionsMenu();
            }

        }.start();
    }
}

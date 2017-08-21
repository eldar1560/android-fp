package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Restaurant;


public class RestaurantDetailsFragment extends Fragment {

    TextView stu_name , stu_userName , stu_address ,stu_foodName , stu_ot , stu_od;
    ImageView imageView;
    ProgressBar progressBar;
    CheckBox stu_cb;
    Restaurant st;
    private static final String ARG_PARAM1 = "param1";
    private String stId;

    interface RestaurantDetailsFragmentListener{
        void onFullImage();
    }

    RestaurantDetailsFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof RestaurantDetailsFragmentListener){
            listener = (RestaurantDetailsFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement RestaurantDetailsFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RestaurantDetailsFragmentListener){
            listener = (RestaurantDetailsFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement RestaurantDetailsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static RestaurantDetailsFragment newInstance(String param1){
        RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1 , param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            stId = getArguments().getString(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View contentView = inflater.inflate(R.layout.fragment_restaurant_details, container, false);

        Log.d("TAG","stid = " + stId);


        Model.instace.getRestaurant(stId, new Model.getRestaurantCallback() {
            @Override
            public void onComplete(Restaurant restaurant) {
                RestaurantDetailsFragment.this.st = restaurant;
                Log.d("TAG","got restaurant name: " + restaurant.name);
                stu_foodName = (TextView) contentView.findViewById(R.id.stu_foodName);
                stu_foodName.setText("Food Name : " + st.foodName);
                stu_name = (TextView) contentView.findViewById(R.id.stu_name);
                stu_name.setText("Restaurant Name : " + st.name);

                stu_userName = (TextView) contentView.findViewById(R.id.stu_userName);
                stu_userName.setText("User Name : " + st.userName);

                stu_address = (TextView) contentView.findViewById(R.id.stu_address);
                stu_address.setText("Restaurant Address : " + st.address);

                stu_ot = (TextView) contentView.findViewById(R.id.stu_ot);
                stu_ot.setText("Order time : "+st.orderTime);

                stu_od = (TextView) contentView.findViewById(R.id.stu_od);
                stu_od.setText("Order date : "+st.orderDate);

                stu_cb = (CheckBox) contentView.findViewById(R.id.stu_cb);
                stu_cb.setChecked(st.checked);
                imageView = (ImageView) contentView.findViewById(R.id.stu_image);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null)
                            listener.onFullImage();
                    }
                });
                progressBar = (ProgressBar) contentView.findViewById(R.id.stu_progressBar);
                if (st.imageUrl != null && !st.imageUrl.isEmpty() && !st.imageUrl.equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instace.getImage(st.imageUrl, new Model.GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFail() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                Log.d("TAG","get restaurant cancell" );

            }
        });


        return contentView;
    }
}

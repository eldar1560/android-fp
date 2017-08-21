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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Restaurant;


public class RestaurantDetailsFullImageFragment extends Fragment {

    ImageView imageView;
    ProgressBar progressBar;

    Restaurant st;
    private static final String ARG_PARAM1 = "param1";
    private String stId;
    interface RestaurantDetailsFullImageFragmentListener{
        void onBack();
    }

    RestaurantDetailsFullImageFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof RestaurantDetailsFullImageFragmentListener){
            listener = (RestaurantDetailsFullImageFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement RestaurantDetailsFullImageFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RestaurantDetailsFullImageFragmentListener){
            listener = (RestaurantDetailsFullImageFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement RestaurantDetailsFullImageFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static RestaurantDetailsFullImageFragment newInstance(String param1){
        RestaurantDetailsFullImageFragment fragment = new RestaurantDetailsFullImageFragment();
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
        final View contentView = inflater.inflate(R.layout.fragment_restaurant_details_full_image, container, false);

        Log.d("mife","stid = " + stId);
        Button backBtn = (Button) contentView.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onBack();
            }
        });
        imageView = (ImageView) contentView.findViewById(R.id.full_image);
        progressBar = (ProgressBar) contentView.findViewById(R.id.full_image_progressBar);
        Model.instace.getRestaurant(stId, new Model.getRestaurantCallback() {
            @Override
            public void onComplete(Restaurant restaurant) {
                RestaurantDetailsFullImageFragment.this.st = restaurant;
                Log.d("TAG","got restaurant name: " + restaurant.name);


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

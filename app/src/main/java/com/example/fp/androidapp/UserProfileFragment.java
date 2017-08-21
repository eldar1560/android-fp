package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    public static UserProfileFragment newInstance(){
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    interface RestaurantUserProfileFragmentListener{
        void onBack();
    }

    RestaurantUserProfileFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof RestaurantUserProfileFragmentListener){
            listener = (RestaurantUserProfileFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement RestaurantUserProfileFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RestaurantUserProfileFragmentListener){
            listener = (RestaurantUserProfileFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement RestaurantUserProfileFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final EditText currPassEt = (EditText) contentView.findViewById(R.id.profileCurrentPass);
        final EditText newPassEt= (EditText) contentView.findViewById(R.id.profileNewPass);
        final ProgressBar progressBar = (ProgressBar) contentView.findViewById(R.id.profilePb);

        TextView userEmail = (TextView)  contentView.findViewById(R.id.profileEmail);
        userEmail.setText(currentUser.getEmail());

        Button changePassBtn = (Button) contentView.findViewById(R.id.profileChangePass);
        Button backBtn = (Button) contentView.findViewById(R.id.profileBack);

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currPassEt.getText().toString().equals("") || newPassEt.getText().toString().equals("")) {
                    Toast.makeText(MyApplication.getMyContext(), "Do not leave a field empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = EmailAuthProvider
                        .getCredential(currentUser.getEmail(), currPassEt.getText().toString());
                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    currentUser.updatePassword(newPassEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(MyApplication.getMyContext(), "Password Updated",
                                                        Toast.LENGTH_SHORT).show();
                                                Log.d("mife", "Password updated");
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(MyApplication.getMyContext(), "Password update failed : "+task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                                Log.d("mife", "Error password not updated");
                                            }
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(MyApplication.getMyContext(), "Authentication failed : "+task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("mife", "Error auth failed");
                                }
                            }
                        });
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onBack();

            }
        });

        return contentView;
    }


}

package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


public class AuthUIFragment extends Fragment {
    private FirebaseAuth mAuth;
    public static AuthUIFragment newInstance(){
        AuthUIFragment fragment = new AuthUIFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    interface StudentAuthFragmentListener{
        void onSignIn(String email,String password);
        void onSignUp(String email,String password);
        void onAlreadyLoggedIn();
    }

    StudentAuthFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof StudentAuthFragmentListener){
            listener = (StudentAuthFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement StudentAuthFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof StudentAuthFragmentListener){
            listener = (StudentAuthFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement StudentAuthFragmentListener");
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
        View contentView = inflater.inflate(R.layout.fragment_auth_ui, container, false);
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            if(listener != null)
                listener.onAlreadyLoggedIn();
        }
        final EditText emailEt = (EditText) contentView.findViewById(R.id.authEmail);
        final EditText passwordEt= (EditText) contentView.findViewById(R.id.authPassword);

        Button signInBtn = (Button) contentView.findViewById(R.id.authSignIn);
        Button signUpBtn = (Button) contentView.findViewById(R.id.authSignUp);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(listener!=null)
                listener.onSignIn(emailEt.getText().toString(),passwordEt.getText().toString());

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onSignUp(emailEt.getText().toString(),passwordEt.getText().toString());

            }
        });

        return contentView;
    }


}

package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.zip.Inflater;


public class RestaurantListFragment extends Fragment {

    List<Student> data;
    LayoutInflater inflater;
    ListView list;
    StudentsListAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String content , field;
    public static RestaurantListFragment newInstance(String param1 , String param2){
        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1 , param1);
        args.putString(ARG_PARAM2 , param2);
        fragment.setArguments(args);
        return fragment;
    }
    // This method will be called when a MessageEvent is posted
    // (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Model.UpdateStudentEvent event) {
        //Toast.makeText(MyApplication.getMyContext(), "someone added or edited restaurant", Toast.LENGTH_SHORT).show();
        Log.d("Mife","got new/edit restaurant");
        boolean exist = false;
        for (Student st: data){
            if (st.id.equals(event.student.id)){
                st = event.student;
                exist = true;
                break;
            }
        }
        if (!exist){
            data.add(event.student);
        }
        adapter.notifyDataSetChanged();
        list.setSelection(adapter.getCount() - 1);

    }
    /*//for changing value of already created item
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Model.ChangeStudentEvent event) {
        Toast.makeText(MyApplication.getMyContext(), "got changing student event", Toast.LENGTH_SHORT).show();
        Log.d("Mife","got changing restaurant");
        for (Student st: data){
            if (st.id.equals(event.student.id)){
                data.remove(st);
                data.add(event.student);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        list.setSelection(adapter.getCount() - 1);

    }*/
    //for deletion
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Model.DeleteStudentEvent event) {
        //Toast.makeText(MyApplication.getMyContext(), "someone deleted restaurant", Toast.LENGTH_SHORT).show();
        Log.d("Mife","got delete restaurant");
        boolean exist = false;
        for (Student st: data){
            if (st.id.equals(event.student.id)){
                st = event.student;
                exist = true;
                break;
            }
        }
        if (exist){
            data.remove(event.student);
        }
        adapter.notifyDataSetChanged();
        list.setSelection(adapter.getCount() - 1);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            content = getArguments().getString(ARG_PARAM1);
            field = getArguments().getString(ARG_PARAM2);
        }
    }

    interface StudentListFragmentListener{
        void onSelect(AdapterView<?> parent, View view, int position, long id , List<Student> data);
        void onSearch(String content ,String field);
    }

    StudentListFragmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof StudentListFragmentListener ){
            listener = (StudentListFragmentListener) activity;
        }else{
            throw new RuntimeException(activity.toString() + " must implement StudentListFragmentListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof StudentListFragmentListener ){
            listener = (StudentListFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement StudentListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    class StudentsListAdapter extends BaseAdapter {
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class CBListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                int pos = (int)v.getTag();
                Student st = data.get(pos);
                st.checked = !st.checked;
                Model.instace.updateStudent(st);
            }
        }

        CBListener listener = new  CBListener();

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.restaurants_list_row,null);
                CheckBox cb = (CheckBox) convertView.findViewById(R.id.strow_cb);
                cb.setOnClickListener(listener);
            }

            TextView name = (TextView) convertView.findViewById(R.id.strow_name);
            TextView foodName = (TextView) convertView.findViewById(R.id.strow_foodName);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.strow_cb);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.strow_image);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.strow_progressBar);
            final Student st = data.get(position);
            name.setText(st.name);
            foodName.setText(st.foodName);
            cb.setChecked(st.checked);
            cb.setTag(position);

            imageView.setTag(st.imageUrl);
            //imageView.setImageDrawable(MyApplication.getMyContext().getDrawable(R.drawable.avatar));

            if (st.imageUrl != null && !st.imageUrl.isEmpty() && !st.imageUrl.equals("")){
                progressBar.setVisibility(View.VISIBLE);
                Model.instace.getImage(st.imageUrl, new Model.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(st.imageUrl)) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            return convertView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        View contentView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        list = (ListView) contentView.findViewById(R.id.reslist_list);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if(content.equals("")) {
            //data = Model.instace.getAllStudents();
            Model.instace.getAllStudents(new Model.GetAllStudentsAndObserveCallback() {
                @Override
                public void onComplete(List<Student> list) {
                    data = list;
                    //adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        else
            data = Model.instace.getAllStudentsByFilter(content,field);

        adapter = new StudentsListAdapter();
        final EditText searchTxt = (EditText) contentView.findViewById(R.id.search);
        final Spinner dropList = (Spinner)  contentView.findViewById(R.id.spinner);
        ImageButton searchBtn = (ImageButton) contentView.findViewById(R.id.search_button);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onSearch(searchTxt.getText().toString(),dropList.getSelectedItem().toString());
            }
        });
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener != null)
                    listener.onSelect(parent,view,position,id ,data);

            }
        });

        return contentView;
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}

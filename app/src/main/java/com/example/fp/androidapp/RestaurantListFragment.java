package com.example.fp.androidapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fp.androidapp.model.Model;
import com.example.fp.androidapp.model.Student;

import java.util.List;


public class RestaurantListFragment extends Fragment {

    List<Student> data;
    LayoutInflater inflater;
    ListView list;
    StudentsListAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private String name;
    public static RestaurantListFragment newInstance(String param1){
        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1 , param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            name = getArguments().getString(ARG_PARAM1);
    }

    interface StudentListFragmentListener{
        void onSelect(AdapterView<?> parent, View view, int position, long id , List<Student> data);
        void onSearch(String restName);
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
                Log.d("Mife","before:"+st.checked);
                st.checked = !st.checked;
                Log.d("Mife","after:"+st.checked);
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
            TextView id = (TextView) convertView.findViewById(R.id.strow_id);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.strow_cb);

            Student st = data.get(position);
            name.setText(st.name);
            id.setText(st.id);
            cb.setChecked(st.checked);
            cb.setTag(position);

            return convertView;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflater = inflater;
        View contentView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        list = (ListView) contentView.findViewById(R.id.stlist_list);
        if(name.equals(""))
            data = Model.instace.getAllStudents();
        else
            data = Model.instace.getAllStudentsByName(name);

        adapter = new StudentsListAdapter();
        final EditText searchTxt = (EditText) contentView.findViewById(R.id.search);
        ImageButton searchBtn = (ImageButton) contentView.findViewById(R.id.search_button);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onSearch(searchTxt.getText().toString());
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
}

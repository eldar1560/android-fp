package com.example.ex3.studentlist.model;

import com.example.ex3.studentlist.MainActivity;
import com.example.ex3.studentlist.MyTimePicker;

import java.util.LinkedList;
import java.util.List;


public class Model {
    public final static Model instace = new Model();

    private Model(){
        for(int i=0;i<20;i++){
            Student st = new Student();
            st.name = "Student" + i;
            st.id = "" + i * 17;
            st.checked = false;
            st.imageUrl = "";
            st.address = "Rishon";
            st.phone = " 0500000000";
            st.birthTime = "12:00";
            st.birthDate = "01/01/2017";
            data.add(st);
        }
    }


    private List<Student> data = new LinkedList<Student>();

    public List<Student> getAllStudents(){
        return data;
    }

    public void addStudent(Student st){
        data.add(st);
    }

    public void deleteStudent(Student st) {data.remove(st);}

    public Student getStudent(String stId) {
        for (Student s : data){
            if (s.id.equals(stId)){
                return s;
            }
        }
        return null;
    }
}

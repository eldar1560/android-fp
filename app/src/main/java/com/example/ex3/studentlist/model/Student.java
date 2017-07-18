package com.example.ex3.studentlist.model;


import com.example.ex3.studentlist.MyDatePicker;
import com.example.ex3.studentlist.MyTimePicker;

public class Student {
    public String id;
    public String name;
    public Boolean checked;
    public String imageUrl;
    public String phone;
    public String address;
    public MyTimePicker birthTime;
    public MyDatePicker birthDate;

    public Student(){}
    public Student (String id , String name , String phone , String address , Boolean checked , String imageUrl , MyTimePicker birthTime , MyDatePicker birthDate){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address ;
        this.imageUrl = imageUrl;
        this.checked = checked;
        this.birthTime = birthTime;
        this.birthDate = birthDate;
    }
}

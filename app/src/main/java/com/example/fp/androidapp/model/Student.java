package com.example.fp.androidapp.model;


public class Student {
    public String id;
    public String name;
    public Boolean checked;
    public String imageUrl;
    public String phone;
    public String address;
    public String birthTime;
    public String birthDate;

    public Student(){}
    public Student (String id , String name , String phone , String address , Boolean checked , String imageUrl , String birthTime , String birthDate){
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

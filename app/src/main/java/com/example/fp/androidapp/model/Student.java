package com.example.fp.androidapp.model;


public class Student {
    public String id;
    public String foodName;
    public String name;
    public Boolean checked;
    public String imageUrl;
    public String userName;
    public String address;
    public String orderTime;
    public String orderDate;
    public double lastUpdateDate;

    public Student(){}
    public Student (String id , String name ,String foodName, String userName , String address , Boolean checked , String imageUrl , String orderTime , String orderDate){
        this.id = id;
        this.foodName = foodName;
        this.name = name;
        this.userName = userName;
        this.address = address ;
        this.imageUrl = imageUrl;
        this.checked = checked;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
    }
}

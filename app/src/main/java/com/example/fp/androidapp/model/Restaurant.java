package com.example.fp.androidapp.model;


public class Restaurant {
    public String id;
    public String foodName;
    public String name;
    public Boolean checked;
    public String imageUrl;
    public String userName;
    public String address;
    public String orderTime;
    public String orderDate;
    public int isRemoved;
    public int likes;
    public String userLikes;
    public double lastUpdateDate;

    public Restaurant(){}
    public Restaurant(String id , String name , String foodName, String userName , String address , Boolean checked , String imageUrl , String orderTime , String orderDate){
        this.id = id;
        this.foodName = foodName;
        this.name = name;
        this.userName = userName;
        this.address = address ;
        this.imageUrl = imageUrl;
        this.checked = checked;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.isRemoved = 0;
        this.likes = 0;
        this.userLikes = "";
    }
}

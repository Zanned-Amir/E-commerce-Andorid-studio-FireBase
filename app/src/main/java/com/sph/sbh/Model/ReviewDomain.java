package com.sph.sbh.Model;

public class ReviewDomain {
    private  String Name;
    private  String Description ;
    private  String PicUrl ;
    private double rating ;
    private  int ItemId;
    public ReviewDomain() {
    }

    public ReviewDomain(String name, String description, String picUrl, double rating, int itemID) {
        this.Name = name;
        this.Description = description;
        this.PicUrl = picUrl;
        this.rating = rating;
        this.ItemId = itemID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}

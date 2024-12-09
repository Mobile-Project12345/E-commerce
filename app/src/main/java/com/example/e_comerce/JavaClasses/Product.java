package com.example.e_comerce.JavaClasses;

import android.graphics.Bitmap;

public class Product {

    public String Name;
    public Double Cost;
    public int Quantity;
    public int Id;
    public Bitmap Image;  // Store the image as Bitmap

    // Constructor to initialize all fields
    public Product() {

    }
    // Constructor to initialize all fields
    public Product(int Id, String Name, Double Cost, int Quantity, Bitmap Image) {
        this.Id = Id;
        this.Name = Name;
        this.Cost = Cost;
        this.Quantity = Quantity;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getCost() {
        return Cost;
    }

    public void setCost(Double cost) {
        Cost = cost;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }
}

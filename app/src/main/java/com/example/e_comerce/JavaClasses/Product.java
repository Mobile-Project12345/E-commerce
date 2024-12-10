package com.example.e_comerce.JavaClasses;

import android.graphics.Bitmap;

public class Product {

    private String name;
    private double cost;
    private int quantity;
    private int id;
    private Bitmap image;  // Store the image as Bitmap

    // Constructor to initialize all fields
    public Product(int id, String name, double cost, int quantity, Bitmap image) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.image = image;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    // Optional: Resize image to a smaller size (useful for large images)
    public Bitmap resizeImage(int maxWidth, int maxHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = Math.round(ratio * width);
        int newHeight = Math.round(ratio * height);
        return Bitmap.createScaledBitmap(image, newWidth, newHeight, false);
    }
}

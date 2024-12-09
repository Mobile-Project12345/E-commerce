package com.example.e_comerce.JavaClasses;

import android.graphics.Bitmap;

import java.util.List;

public class Category {
    public String name;
    public List<Product> Products;
    public Bitmap imageBitmap;
    public  int id;

    public Category()
    {


    }
    public Category(String Name,List<Product>products,   Bitmap Image)
    {
        this.name=Name;
        this.Products=products;
        this.imageBitmap=Image;

    }

    public String getName() {
        return name;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

}

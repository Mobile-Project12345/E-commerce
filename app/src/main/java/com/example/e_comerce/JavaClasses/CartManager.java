package com.example.e_comerce.JavaClasses;


import java.util.ArrayList;

import java.util.List;



public class CartManager {

    private static CartManager instance;

    private final List<Product> cartItems;



    private CartManager() {

        cartItems = new ArrayList<>();

    }



    public static synchronized CartManager getInstance() {

        if (instance == null) {

            instance = new CartManager();

        }

        return instance;

    }



    public void addToCart(Product product) {

        cartItems.add(product);

    }



    public List<Product> getCartItems() {

        return cartItems;

    }



    public void clearCart() {

        cartItems.clear();

    }

}
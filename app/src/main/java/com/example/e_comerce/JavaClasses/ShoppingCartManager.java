package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartManager {
    private Context context;
    private List<Product> cart;
    private SharedPreferences sharedPreferences;
    private static final String CART_PREFS = "cart_prefs";
    private static final String CART_KEY = "cart_key";

    public ShoppingCartManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
        this.cart = loadCart();
    }

    // Add product to cart with specified quantity
    public void addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;  // Prevent adding invalid products or quantities
        }

        // Check if the product already exists in the cart
        for (Product p : cart) {
            if (p.getId() == product.getId()) {
                p.setQuantity(p.getQuantity() + quantity);  // Update quantity if product already exists
                saveCart();
                return;
            }
        }
        // Add new product with specified quantity if it's not already in the cart
        product.setQuantity(quantity);
        cart.add(product);
        saveCart();
    }

    // Overloaded method for default quantity of 1
    public void addToCart(Product product) {
        addToCart(product, 1);  // Default quantity of 1
    }

    // Retrieve all items in the cart
    public List<Product> getCartItems() {
        return cart != null ? cart : new ArrayList<>();
    }

    // Remove a specific product from the cart
    public void removeItem(Product product) {
        if (product == null) {
            return;  // Prevent removing null items
        }

        // Remove the product with matching ID from the cart
        boolean isRemoved = cart.removeIf(p -> p.getId() == product.getId());

        // If item is removed, save updated cart
        if (isRemoved) {
            saveCart();
        }
    }

    // Update the quantity of a specific product in the cart
    public void updateQuantity(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;  // Prevent invalid quantities
        }
        for (Product p : cart) {
            if (p.getId() == product.getId()) {
                p.setQuantity(quantity);  // Update quantity for the product
                saveCart();
                return;
            }
        }
    }

    // Get the total price of all items in the cart
    public double getTotal() {
        double total = 0;
        for (Product product : cart) {
            total += product.getCost() * product.getQuantity();  // Calculate total price
        }
        return total;
    }

    // Clear all items from the cart
    public void clearCart() {
        cart.clear();
        saveCart();
    }

    // Get the total number of items in the cart
    public int getItemCount() {
        return cart != null ? cart.size() : 0;
    }

    // Save the cart to SharedPreferences
    private void saveCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String cartJson = gson.toJson(cart);  // Convert cart list to JSON string
        editor.putString(CART_KEY, cartJson);  // Save JSON to SharedPreferences
        editor.apply();
    }

    // Load the cart from SharedPreferences
    private List<Product> loadCart() {
        try {
            Gson gson = new Gson();
            String cartJson = sharedPreferences.getString(CART_KEY, null);
            if (cartJson == null || cartJson.isEmpty()) {
                return new ArrayList<>();  // Return an empty list if no cart data exists
            } else {
                java.lang.reflect.Type type = new TypeToken<List<Product>>() {}.getType();
                return gson.fromJson(cartJson, type);  // Convert JSON string back to a list of products
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any errors that occur during loading
            return new ArrayList<>();  // Return an empty list in case of error
        }
    }

    // Check if a product is already in the cart
    public boolean isProductInCart(Product product) {
        return cart.stream().anyMatch(p -> p.getId() == product.getId());  // Use stream for efficiency
    }
}

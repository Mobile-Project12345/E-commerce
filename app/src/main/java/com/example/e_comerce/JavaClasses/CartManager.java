package com.example.e_comerce.JavaClasses;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Product> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    // Singleton instance
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add product to the cart or update quantity if it already exists
    public void addToCart(Product product) {
        for (Product item : cartItems) {
            if (item.getId() == product.getId()) { // Assuming products have a unique ID
                item.setQuantity(item.getQuantity() + 1); // Increment quantity
                return;
            }
        }
        product.setQuantity(1); // Set initial quantity for a new product
        cartItems.add(product);
    }

    // Remove product from the cart
    public void removeFromCart(int productId) {
        cartItems.removeIf(item -> item.getId() == productId);
    }

    // Update the quantity of a product in the cart
    public void updateQuantity(int productId, int quantity) {
        for (Product item : cartItems) {
            if (item.getId() == productId) {
                item.setQuantity(quantity);
                if (quantity <= 0) {
                    cartItems.remove(item); // Remove item if quantity is 0 or less
                }
                return;
            }
        }
    }

    // Get the list of cart items
    public List<Product> getCartItems() {
        return cartItems;
    }

    // Calculate the total price of the cart
    public double calculateTotalPrice() {
        double total = 0;
        for (Product item : cartItems) {
            total += item.getCost() * item.getQuantity();
        }
        return total;
    }

    // Clear all items in the cart
    public void clearCart() {
        cartItems.clear();
    }
}

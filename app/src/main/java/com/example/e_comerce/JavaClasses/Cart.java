package com.example.e_comerce.JavaClasses;

import com.example.e_comerce.JavaClasses.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<Product> products;

    public Cart() {
        products = new ArrayList<>();
    }

    // Add or update product quantity in the cart
    public void addProductToCart(Product product, int quantity) {
        if (quantity <= 0) return;  // Prevent adding invalid quantities
        for (Product p : products) {
            if (p.getId() == product.getId()) {  // Compare by product ID for uniqueness
                p.setQuantity(p.getQuantity() + quantity);  // Increase quantity by specified amount
                return;
            }
        }
        product.setQuantity(quantity);  // Add new product to cart if not already there
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    // Remove product from cart
    public void removeProductFromCart(Product product) {
        products.removeIf(p -> p.getId() == product.getId());  // Remove by product ID
    }

    // Get the total price considering quantities
    public double getTotalPrice() {
        double total = 0;
        for (Product product : products) {
            total += product.getCost() * product.getQuantity();  // Multiply cost by quantity
        }
        return total;
    }

    // Clear all products from the cart
    public void clearCart() {
        products.clear();
    }
}

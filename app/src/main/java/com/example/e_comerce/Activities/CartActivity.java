package com.example.e_comerce.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.JavaClasses.CartAdapter;
import com.example.e_comerce.JavaClasses.CartManager;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.R;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private Button checkoutButton;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);
        totalPriceTextView = findViewById(R.id.orderTotal); // Add this TextView to activity_cart.xml

        // Get cart items
        List<Product> cartItems = CartManager.getInstance().getCartItems();

        // Initialize the adapter with the OnCartChangeListener
        cartAdapter = new CartAdapter(this, cartItems, this);

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Calculate and display the initial total price
        updateTotalPrice();

        // Handle checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                CartManager.getInstance().clearCart(); // Clear cart after checkout
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }
        });
    }

    // Method to update total price in the TextView
    private void updateTotalPrice() {
        double total = 0;
        for (Product item : CartManager.getInstance().getCartItems()) {
            total += item.getCost() * item.getQuantity();
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    // Implementation of OnCartChangeListener interface
    @Override
    public void onCartUpdated(double newTotal) {
        totalPriceTextView.setText(String.format("Total: $%.2f", newTotal));
    }
}
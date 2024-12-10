package com.example.e_comerce.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.ShoppingCartManager;
import com.example.e_comerce.JavaClasses.ShoppingCartAdapter;
import com.example.e_comerce.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    private ListView cartListView;
    private ShoppingCartAdapter cartAdapter;
    private ShoppingCartManager cartManager;
    private TextView totalTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Initialize views
        cartListView = findViewById(R.id.cartListView);
        totalTextView = findViewById(R.id.totalTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Initialize ShoppingCartManager and Adapter
        cartManager = new ShoppingCartManager(this);

        // Load cart items and set up adapter
        List<Product> cartItems = cartManager.getCartItems();
        if (cartItems == null) {
            cartItems = new ArrayList<>();  // Fallback to an empty list if cart is null
        }
        cartAdapter = new ShoppingCartAdapter(this, cartItems, cartManager);  // Pass cartManager to the adapter for interaction
        cartListView.setAdapter(cartAdapter);

        // Update the total initially
        updateTotal();

        // Set up a listener for checkout
        checkoutButton.setOnClickListener(v -> {
            if (cartManager.getCartItems().isEmpty()) {
                Toast.makeText(ShoppingCartActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                // Handle checkout logic (e.g., navigate to checkout page, process payment)
                // For now, you can show a simple message or implement a basic action
                // startActivity(new Intent(ShoppingCartActivity.this, CheckoutActivity.class));
            }
        });
    }

    // Update the total price displayed in the UI
    private void updateTotal() {
        double total = cartManager.getTotal();
        if (total == 0) {
            totalTextView.setText("Your cart is empty.");
        } else {
            totalTextView.setText(String.format("Total: $%.2f", total));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the cart data
        List<Product> updatedCartItems = cartManager.getCartItems();
        if (updatedCartItems == null) {
            updatedCartItems = new ArrayList<>();
        }
        cartAdapter.updateCart(updatedCartItems); // Update the adapter with the new cart
        updateTotal();  // Update the total price when the activity is resumed
    }
}

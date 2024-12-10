package com.example.e_comerce.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.JavaClasses.CartAdapter;
import com.example.e_comerce.JavaClasses.CartManager;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.ProductAdapter;
import com.example.e_comerce.R;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private ProductAdapter cartAdapter;
    private Button checkoutButton;
    private CartAdapter prodAdapter;
    private List<Product> products;
    private RecyclerView.Adapter ProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        prodAdapter = new CartAdapter(this, products); // listOfProducts should be your data source

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Get cart items
        List<Product> cartItems = CartManager.getInstance().getCartItems();

        CartAdapter cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);


        // Handle checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                CartManager.getInstance().clearCart(); // Clear cart after checkout
                cartAdapter.notifyDataSetChanged();
            }
        });
    }
}
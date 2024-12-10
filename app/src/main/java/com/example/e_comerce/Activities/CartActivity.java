package com.example.e_comerce.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.DatabaseAccess.DbAccsessTransaction;
import com.example.e_comerce.JavaClasses.CartAdapter;
import com.example.e_comerce.JavaClasses.CartManager;
import com.example.e_comerce.JavaClasses.Customer;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private Button checkoutButton;
    private TextView totalPriceTextView;
Customer loggedInUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        loggedInUser = (Customer) getIntent().getSerializableExtra("loggedInUser"); // Note: changed from "loggedInUser" to match the sender

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


               boolean TransactionDone=true;

                for (int i=0;i<cartItems.size();i++) {

                    DbAccsessTransaction trans = new DbAccsessTransaction(this);
                    String date=getCurrentDate();
                    if (trans.addTransaction(loggedInUser.id, cartItems.get(i).Id, getCurrentDate(), 1000000)==-1)
                    {
                        Toast.makeText(this, "Their is only "+ cartItems.get(i).getQuantity()+" product of " +cartItems.get(i).getName()+" in Stock", Toast.LENGTH_SHORT).show();
                        TransactionDone=false;
                    }
                }
              //  2024-10-12

                if(TransactionDone)
                {
                    Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();

                CartManager.getInstance().clearCart(); // Clear cart after checkout
                cartAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(this, CustomerHomePage.class);

                    startActivity(intent);
               // updateTotalPrice();
                }
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
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}
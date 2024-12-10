package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;
import com.example.e_comerce.R;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> productList;
    private ShoppingCartManager cartManager;  // Pass ShoppingCartManager here

    // Pass ShoppingCartManager when creating the Adapter
    public ProductAdapter(Context context, List<Product> productList, ShoppingCartManager cartManager) {
        super(context, R.layout.product_item, productList);
        this.context = context;
        this.productList = productList;
        this.cartManager = cartManager;  // Initialize the cartManager
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        }

        Product product = productList.get(position);

        // Get views from the layout
        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productTitle = convertView.findViewById(R.id.productTitle);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        Button addToCartButton = convertView.findViewById(R.id.addToCartButton);

        // Set values from the product object to the views
        productImage.setImageBitmap(product.getImage());  // Set Bitmap image
        productTitle.setText(product.getName());  // Set product name
        productPrice.setText(String.format("$%.2f", product.getCost()));  // Set price

        // Check if the product is already in the cart
        if (cartManager.isProductInCart(product)) {
            // If product is already in the cart, update button state
            addToCartButton.setEnabled(false); // Disable the button after adding
            addToCartButton.setText("Added to Cart"); // Change text to show product is added
        } else {
            // If product is not in the cart, allow adding to the cart
            addToCartButton.setEnabled(true);
            addToCartButton.setText("Add to Cart");
        }

        // Handle Add to Cart button
        addToCartButton.setOnClickListener(v -> {
            // Add product to shopping cart
            cartManager.addToCart(product);

            // Show a Toast message to indicate product is added to cart
            Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();

            // Disable the button and update its text after adding
            addToCartButton.setEnabled(false); // Disable the button after adding
            addToCartButton.setText("Added to Cart"); // Change text to show product is added
        });

        return convertView;
    }
}

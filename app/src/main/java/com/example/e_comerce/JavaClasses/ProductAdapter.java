package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_comerce.R;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, R.layout.product_item, productList);
        this.context = context;
        this.productList = productList;
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
        TextView productqun = convertView.findViewById(R.id.productquantity);
        Button addToCartButton = convertView.findViewById(R.id.addToCartButton);

        // Set values from the product object to the views
        productImage.setImageBitmap(product.getImage());  // Set Bitmap image
        productTitle.setText(product.getName());  // Set product name
        productPrice.setText(String.format("$%.2f", product.getCost()));  // Set price
        productqun.setText(String.valueOf(product.getQuantity()));

        // Handle Add to Cart button (this example doesn't have functionality)
        addToCartButton.setOnClickListener(v -> {
            // Implement the action when Add to Cart is clicked (e.g., add to cart logic)
        });

        return convertView;
    }
}
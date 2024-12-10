package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_comerce.R;
import java.util.List;

public class ShoppingCartAdapter extends BaseAdapter {

    private Context context;
    private List<Product> cartItems;
    private ShoppingCartManager cartManager;

    public ShoppingCartAdapter(Context context, List<Product> cartItems, ShoppingCartManager cartManager) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartManager = cartManager;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cartItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        Product product = cartItems.get(position);

        // Set product details
        TextView productTitle = convertView.findViewById(R.id.productTitle);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        TextView productQuantity = convertView.findViewById(R.id.productQuantity);
        Button increaseButton = convertView.findViewById(R.id.increaseButton);
        Button decreaseButton = convertView.findViewById(R.id.decreaseButton);
        Button removeButton = convertView.findViewById(R.id.removeButton);

        productTitle.setText(product.getName());
        productPrice.setText("$" + product.getCost());
        productQuantity.setText("Quantity: " + product.getQuantity());

        // Increase Quantity Button
        increaseButton.setOnClickListener(v -> {
            cartManager.updateQuantity(product, product.getQuantity() + 1);
            updateCart(cartManager.getCartItems());  // Refresh cart data
        });

        // Decrease Quantity Button
        decreaseButton.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                cartManager.updateQuantity(product, product.getQuantity() - 1);
                updateCart(cartManager.getCartItems());  // Refresh cart data
            } else {
                Toast.makeText(context, "Minimum quantity reached", Toast.LENGTH_SHORT).show();
            }
        });

        // Remove Button
        removeButton.setOnClickListener(v -> {
            cartManager.removeItem(product);
            updateCart(cartManager.getCartItems());  // Refresh cart data
            Toast.makeText(context, "Product removed", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    // Method to update the cart data
    public void updateCart(List<Product> updatedCartItems) {
        this.cartItems = updatedCartItems;
        notifyDataSetChanged();  // Refresh the ListView
    }
}

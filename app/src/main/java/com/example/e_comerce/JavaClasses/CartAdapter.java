package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_comerce.R;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<Product> cartItems;
    private final OnCartChangeListener onCartChangeListener;

    // Interface to notify the activity about cart updates
    public interface OnCartChangeListener {
        void onCartUpdated(double newTotal);
    }

    public CartAdapter(Context context, List<Product> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.onCartChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);

        // Set product details in the ViewHolder
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$%.2f", product.getCost()));
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
        holder.productImage.setImageBitmap(product.getImage());

        // Handle increasing quantity
        holder.increaseQuantityButton.setOnClickListener(v -> {
            product.setQuantity(product.getQuantity() + 1);
            notifyItemChanged(position);
            updateTotal();
        });

        // Handle decreasing quantity
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                notifyItemRemoved(position);
            }
            updateTotal();
        });

        // Handle item removal
        holder.deleteItemButton.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            updateTotal();
        });
    }

    // Update total price and notify listener
    private void updateTotal() {
        double total = 0;
        for (Product item : cartItems) {
            total += item.getCost() * item.getQuantity();
        }
        onCartChangeListener.onCartUpdated(total);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        final TextView productName, productPrice, productQuantity;
        final ImageView productImage;
        final ImageButton increaseQuantityButton, decreaseQuantityButton, deleteItemButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.cartProductImage);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            deleteItemButton = itemView.findViewById(R.id.deleteItemButton);
        }
    }
}
package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<transaction> transactionList;

    public TransactionAdapter(Context context, List<transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        transaction currentTransaction = transactionList.get(position);
        Product product = currentTransaction.product;


        Bitmap productImage = product.getImage();
        if (productImage != null) {
            holder.productImage.setImageBitmap(productImage);
        }


        holder.productName.setText(product.getName());
        holder.productQuantity.setText("Quantity: " + product.getQuantity());
        holder.productCost.setText("Cost: $" + product.getCost());

        holder.transactionDate.setText("Transaction Date: " + currentTransaction.Date);
        holder.userEmail.setText(currentTransaction.CustomerEmail);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productQuantity, productCost, transactionDate, userEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productCost = itemView.findViewById(R.id.product_cost);
            transactionDate = itemView.findViewById(R.id.transaction_date);
            userEmail = itemView.findViewById(R.id.user_email);
        }
    }
}

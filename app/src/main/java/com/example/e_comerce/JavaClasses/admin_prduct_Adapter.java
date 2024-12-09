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

public class admin_prduct_Adapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> productList;

    public admin_prduct_Adapter(Context context, List<Product> productList) {
        super(context, R.layout.product_item, productList);
        this.context = context;
        this.productList = productList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_product_item, parent, false);
        }

        Product product = productList.get(position);

        // Get views from the layout
        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productTitle = convertView.findViewById(R.id.productTitle);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        Button editprodyct = convertView.findViewById(R.id.edit_product);
        Button deleteproduct = convertView.findViewById(R.id.delete_product);
        Button increment_qun = convertView.findViewById(R.id.increment);
        Button decrement_qun = convertView.findViewById(R.id.decrement);

        // Set values from the product object to the views
        productImage.setImageBitmap(product.getImage());  // Set Bitmap image
        productTitle.setText(product.getName());  // Set product name
        productPrice.setText(String.format("$%.2f", product.getCost()));  // Set price



        return convertView;
    }

}

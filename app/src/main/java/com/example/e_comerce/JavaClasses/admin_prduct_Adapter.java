package com.example.e_comerce.JavaClasses;

import static androidx.core.content.ContextCompat.startActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_comerce.Activities.AdminHomePage;
import com.example.e_comerce.Activities.edit_category;
import com.example.e_comerce.Activities.edit_product;
import com.example.e_comerce.Activities.report_generate;
import com.example.e_comerce.DatabaseAccess.DbAccessProduct;
import com.example.e_comerce.R;

import java.io.ByteArrayOutputStream;
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
        TextView productqun = convertView.findViewById(R.id.productquntity);
        Button editprodyct = convertView.findViewById(R.id.edit_product);
        Button deleteproduct = convertView.findViewById(R.id.delete_product);


        // Set values from the product object to the views
        productImage.setImageBitmap(product.getImage());  // Set Bitmap image
        productTitle.setText(product.getName());  // Set product name
        productPrice.setText(String.format("$%.2f", product.getCost()));  // Set price
        productqun.setText(String.valueOf("qunatity  "+product.Quantity));


       deleteproduct.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               DbAccessProduct dbAccessProduct=new DbAccessProduct(context);
               dbAccessProduct.deleteProduct(product.Id);
               Toast.makeText(context, product.Name+" is deleted", Toast.LENGTH_SHORT).show();
               Intent temp_intent = new Intent(context, AdminHomePage.class);

               context.startActivity(temp_intent);


           }
       });

        editprodyct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temp_intent = new Intent(context, edit_product.class);

                // Pass product details via intent extras
                temp_intent.putExtra("PRODUCT_ID", product.getId());
                temp_intent.putExtra("PRODUCT_NAME", product.getName());
                temp_intent.putExtra("PRODUCT_COST", product.getCost());
                temp_intent.putExtra("PRODUCT_QUANTITY", product.getQuantity());

                // Convert Bitmap to byte array to pass in intent
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                product.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                temp_intent.putExtra("PRODUCT_IMAGE", byteArray);

                context.startActivity(temp_intent);
            }
        });






        return convertView;
    }



}

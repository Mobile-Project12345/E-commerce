package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_comerce.Activities.AdminHomePage;
import com.example.e_comerce.Activities.edit_category;
import com.example.e_comerce.DatabaseAccess.DbAccsessCategory;
import com.example.e_comerce.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class admin_category_Adapter extends BaseAdapter {

    private List<Category> categoryList;
    private Context context;

    public admin_category_Adapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_category_item, parent, false);
            holder = new ViewHolder();
            holder.categoryImage = convertView.findViewById(R.id.productImage);
            holder.categoryName = convertView.findViewById(R.id.productTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category category = categoryList.get(position);

        // Set category image
        Bitmap imageBitmap = category.getImageBitmap();
        if (imageBitmap != null) {
            holder.categoryImage.setImageBitmap(imageBitmap);
        }

        // Set category name
        holder.categoryName.setText(category.getName());

        Button editCategory = convertView.findViewById(R.id.edit_product);
        Button deleteCategory = convertView.findViewById(R.id.delete_product);

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbAccsessCategory dbAccessCategory = new DbAccsessCategory(context);
                dbAccessCategory.deleteCategory(category.id);
                Toast.makeText(context, category.name + " is deleted", Toast.LENGTH_SHORT).show();
                Intent tempIntent = new Intent(context, AdminHomePage.class);
                context.startActivity(tempIntent);
            }
        });

        editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(context, edit_category.class);

                // Pass category ID
                editIntent.putExtra("CATEGORY_ID", category.id);

                // Pass category name
                editIntent.putExtra("CATEGORY_NAME", category.name);

                // Convert bitmap to byte array to pass in intent
                if (category.imageBitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    category.imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    editIntent.putExtra("CATEGORY_IMAGE", byteArray);
                }

                context.startActivity(editIntent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
    }
}
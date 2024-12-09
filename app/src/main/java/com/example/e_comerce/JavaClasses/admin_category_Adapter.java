package com.example.e_comerce.JavaClasses;

import static androidx.core.content.ContextCompat.startActivity;

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

import com.example.e_comerce.Activities.edit_category;
import com.example.e_comerce.R;

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

        Category category = categoryList.get(position);//name

        // Set category image
        Bitmap imageBitmap = category.getImageBitmap();
        if (imageBitmap != null) {
            holder.categoryImage.setImageBitmap(imageBitmap);
        }

        // Set category name
        holder.categoryName.setText(category.getName());

        Button editcategory = convertView.findViewById(R.id.edit_product);
        Button deletecategory = convertView.findViewById(R.id.delete_product);

        deletecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "delete " + category.name, Toast.LENGTH_SHORT).show();
            }
        });

        editcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temp_intent = new Intent(context, edit_category.class);
                temp_intent.putExtra("id",category.id);
                temp_intent.putExtra("name",category.name);
                /// temp_intent.putExtra("image",category.imageBitmap);
                context.startActivity(temp_intent);

            }
        });




        return convertView;
    }

    private static class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
    }
}

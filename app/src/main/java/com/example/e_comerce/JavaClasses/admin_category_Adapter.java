package com.example.e_comerce.JavaClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        Category category = categoryList.get(position);

        // Set category image
        Bitmap imageBitmap = category.getImageBitmap();
        if (imageBitmap != null) {
            holder.categoryImage.setImageBitmap(imageBitmap);
        }

        // Set category name
        holder.categoryName.setText(category.getName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
    }
}

package com.example.e_comerce.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.JavaClasses.CategoryAdapter;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.admin_category_Adapter;
import com.example.e_comerce.JavaClasses.admin_prduct_Adapter;
import com.example.e_comerce.R;

import java.util.ArrayList;
import java.util.List;

public class ManageCateogry extends AppCompatActivity {

    private admin_category_Adapter categoryAdapter;
    private List<Category> ListOfCateogry;
    private ListView categoryListView;
    Button AddCateogry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_cateogry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AddCateogry=findViewById(R.id.Add_product);

        AddCateogry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent man_intent = new Intent(ManageCateogry.this, AddCategory.class);
                startActivity(man_intent);
                finish();
            }
        });
        categoryListView = findViewById(R.id.productListView);
        Button back =findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent man_intent = new Intent(ManageCateogry.this, AdminHomePage.class);
                startActivity(man_intent);
                finish();
            }
        });



        Bitmap bitmapcat1book = BitmapFactory.decodeResource(getResources(), R.drawable.books);
        Bitmap bitmapcat2elect = BitmapFactory.decodeResource(getResources(), R.drawable.electronics);
        Bitmap bitmapcat2shoes = BitmapFactory.decodeResource(getResources(), R.drawable.shoes);

        // Create category list
        ListOfCateogry = new ArrayList<>();
        ListOfCateogry.add(new Category("Books", null, bitmapcat1book));
        ListOfCateogry.add(new Category("Electronics", null, bitmapcat2elect));
        ListOfCateogry.add(new Category("Shoes", null, bitmapcat2shoes));

        // Set up adapter and attach to ListView
        categoryAdapter = new admin_category_Adapter(this, ListOfCateogry);
        categoryListView.setAdapter(categoryAdapter);




    }
}
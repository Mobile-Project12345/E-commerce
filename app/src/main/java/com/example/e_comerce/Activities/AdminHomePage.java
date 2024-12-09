package com.example.e_comerce.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.DatabaseAccess.DbAccessProduct;
import com.example.e_comerce.DatabaseAccess.DbAccsessCategory;
import com.example.e_comerce.JavaClasses.Admin;
import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.JavaClasses.CategoryAdapter;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.ProductAdapter;
import com.example.e_comerce.JavaClasses.admin_prduct_Adapter;
import com.example.e_comerce.R;

import java.util.ArrayList;
import java.util.List;

public class AdminHomePage extends AppCompatActivity {

    private ListView productListView;
    private admin_prduct_Adapter productAdapter;
    private List<Category> ListOfCateogry;
    Button addProduct;
DbAccsessCategory dbAccessProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbAccessProduct=new DbAccsessCategory(this);

        ListOfCateogry=  dbAccessProduct.getAllCategoriesWithProducts();


        addProduct=findViewById(R.id.Add_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent man_intent = new Intent(AdminHomePage.this, AddProduct.class);
                startActivity(man_intent);
                //finish();
            }
        });
        /////manage category layout
        Button mangecat=findViewById(R.id.manage_category);

        mangecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent man_intent = new Intent(AdminHomePage.this, ManageCateogry.class);
                startActivity(man_intent);
                //finish();
            }
        });


        ///////

        // Move the user retrieval and toast outside of the WindowInsetsCompat listener
        Admin loggedInUser = (Admin) getIntent().getSerializableExtra("loggedInUser"); // Note: changed from "loggedInUser" to match the sender

        // Now you can use loggedUser in your activity
        if (loggedInUser != null) {
            // Do something with the logged user, like setting a welcome message
            // For example:
            Toast.makeText(this, "Hello " + loggedInUser.UserName, Toast.LENGTH_SHORT).show();
        }


        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        productListView = findViewById(R.id.productListView);



        // Set up category RecyclerView
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, ListOfCateogry, position -> {
            // Show products for the selected category
            List<Product> selectedProducts = ListOfCateogry.get(position).Products;
            updateProductList(selectedProducts);
        });

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Set initial product list to the first category
        updateProductList(ListOfCateogry.get(0).Products);


    }

    private void updateProductList(List<Product> products) {
        productAdapter = new admin_prduct_Adapter(this, products);
        productListView.setAdapter(productAdapter);
    }


}
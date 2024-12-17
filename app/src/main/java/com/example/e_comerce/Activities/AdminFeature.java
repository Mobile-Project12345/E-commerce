package com.example.e_comerce.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.R;
import com.google.android.material.card.MaterialCardView;

public class AdminFeature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_feature);

        // Remove the line with R.id.main since it doesn't exist
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title_actions), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find card views
        MaterialCardView cardAddProduct = findViewById(R.id.card_add_product);
        MaterialCardView cardManageCategory = findViewById(R.id.card_manage_category);
        MaterialCardView cardGenerateReport = findViewById(R.id.card_generate_report);

        // Add Product click listener
        cardAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProductIntent = new Intent(AdminFeature.this, AddProduct.class);
                startActivity(addProductIntent);
            }
        });

        // Manage Category click listener
        cardManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageCategoryIntent = new Intent(AdminFeature.this, ManageCateogry.class);
                startActivity(manageCategoryIntent);
            }
        });

        // Generate Report click listener
        cardGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateReportIntent = new Intent(AdminFeature.this, report_generate.class);
                startActivity(generateReportIntent);
            }
        });
    }
}
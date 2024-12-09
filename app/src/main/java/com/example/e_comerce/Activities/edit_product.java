package com.example.e_comerce.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.R;

public class edit_product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent=getIntent();

        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");
        String cost=intent.getStringExtra("cost");
        String quantity=intent.getStringExtra("quantity");
        ///String image=intent.getStringExtra("image");

        Toast.makeText(edit_product.this, "edit"+ name, Toast.LENGTH_SHORT).show();


    }
}
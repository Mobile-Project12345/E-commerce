package com.example.e_comerce.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.DatabaseAccess.DbAccessProduct;
import com.example.e_comerce.DatabaseAccess.DbAccsessCategory;
import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.R;

import java.io.IOException;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    private ImageView imageViewProduct;
    private Button buttonPickImage, buttonAddProduct;
    private EditText editTextProductName, editTextPrice, editTextQuantity;
    private Spinner spinnerCategory;
    private Bitmap selectedImageBitmap;
    DbAccessProduct dbAccessProduct;
    List<Category> allCategories;

    // ActivityResultLauncher for image picking
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbAccessProduct=new DbAccessProduct(this);

        DbAccsessCategory dbAccess = new DbAccsessCategory(this);
         allCategories = dbAccess.getAllCategories();


        // Initialize views
        initializeViews();

        // Setup image picker launcher
        setupImagePickerLauncher();

        // Setup category spinner
        setupCategorySpinner();

        // Setup button click listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        imageViewProduct = findViewById(R.id.imageViewProduct);
        buttonPickImage = findViewById(R.id.buttonPickImage);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerCategory = findViewById(R.id.spinnerCategory);

    }

    private void setupImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        try {
                            // Convert Uri to Bitmap
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            // Set the image in ImageView
                            imageViewProduct.setImageBitmap(selectedImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void setupCategorySpinner() {
        // Setup categories for spinner

        String[] categoryNames = new String[allCategories.size()];

// Populate the array with names
        for (int i = 0; i < allCategories.size(); i++) {
            categoryNames[i] = allCategories.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        // Image Pick Button Listener
        buttonPickImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(galleryIntent);
        });

        // Add Product Button Listener
        buttonAddProduct.setOnClickListener(v -> {
            // Get values from input fields
            String productName = editTextProductName.getText().toString().trim();
            String priceStr = editTextPrice.getText().toString().trim();
            String quantityStr = editTextQuantity.getText().toString().trim();
            String selectedCategory = spinnerCategory.getSelectedItem().toString();

            // Validate inputs
            if (productName.isEmpty()) {
                editTextProductName.setError("Product name is required");
                return;
            }

            if (priceStr.isEmpty()) {
                editTextPrice.setError("Price is required");
                return;
            }

            if (quantityStr.isEmpty()) {
                editTextQuantity.setError("Quantity is required");
                return;
            }

            // Parse price and quantity
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);
          int selectedCategoryId = getCegoryId(selectedCategory);
           long addedprodect= dbAccessProduct.addProduct(productName,price,quantity,selectedImageBitmap,selectedCategoryId);
           if(addedprodect==-1)
           {
               Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
           }else

            // Show toast
            Toast.makeText(this, "Added Succesfully", Toast.LENGTH_LONG).show();
            Intent man_intent = new Intent(AddProduct.this, AdminHomePage.class);
            startActivity(man_intent);
            finish();
        });
    }

    private int getCegoryId(String categoryName) {
        for (Category category : allCategories) { // Assuming allCategories is your list of Category objects
            if (category.name.equals(categoryName)) {
                return category.id; // Return the category ID if the name matches
            }
        }

        Toast.makeText(this, "this cayegory name is not found your category", Toast.LENGTH_LONG).show();
        return 0;
    }

}
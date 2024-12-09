package com.example.e_comerce.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class edit_product extends AppCompatActivity {

    private TextInputEditText editTextName, editTextCost, editTextQuantity;
    private ImageView imageViewProduct;
    private Button btnSelectImage, btnSaveChanges;
    private Bitmap selectedImageBitmap;
    private String productId;
    DbAccessProduct dbAccessProduct=new DbAccessProduct(this);

    // Launcher for image gallery selection
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        // Convert selected image to bitmap
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        imageViewProduct.setImageBitmap(selectedImageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

        // Edge-to-edge system bars handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        editTextName = findViewById(R.id.editTextProductName);
        editTextCost = findViewById(R.id.editTextProductCost);
        editTextQuantity = findViewById(R.id.editTextProductQuantity);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Retrieve product details from intent
        Intent intent = getIntent();
        if (intent != null) {
            productId = String.valueOf(intent.getIntExtra("PRODUCT_ID", -1));

            // Set product name
            String productName = intent.getStringExtra("PRODUCT_NAME");
            editTextName.setText(productName);

            // Set product cost
            Double productCost = intent.getDoubleExtra("PRODUCT_COST", 0.0);
            editTextCost.setText(String.format("%.2f", productCost));

            // Set product quantity
            int productQuantity = intent.getIntExtra("PRODUCT_QUANTITY", 0);
            editTextQuantity.setText(String.valueOf(productQuantity));

            // Set product image
            byte[] byteArray = intent.getByteArrayExtra("PRODUCT_IMAGE");
            if (byteArray != null) {
                Bitmap productImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageViewProduct.setImageBitmap(productImage);
            }
        }

        // Image selection button listener
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(galleryIntent);
            }
        });

        // Save changes button listener
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields
                String productName = editTextName.getText().toString().trim();
                String productCostStr = editTextCost.getText().toString().trim();
                String productQuantityStr = editTextQuantity.getText().toString().trim();

                // Check if any field is empty
                if (productName.isEmpty()) {
                    editTextName.setError("Product name cannot be empty");
                    editTextName.requestFocus();
                    return;
                }

                if (productCostStr.isEmpty()) {
                    editTextCost.setError("Product cost cannot be empty");
                    editTextCost.requestFocus();
                    return;
                }

                if (productQuantityStr.isEmpty()) {
                    editTextQuantity.setError("Product quantity cannot be empty");
                    editTextQuantity.requestFocus();
                    return;
                }

                // Validate cost
                Double productCost;
                try {
                    productCost = Double.parseDouble(productCostStr);
                } catch (NumberFormatException e) {
                    editTextCost.setError("Invalid cost format");
                    editTextCost.requestFocus();
                    return;
                }

                // Validate quantity
                int productQuantity;
                try {
                    productQuantity = Integer.parseInt(productQuantityStr);
                } catch (NumberFormatException e) {
                    editTextQuantity.setError("Invalid quantity format");
                    editTextQuantity.requestFocus();
                    return;
                }

                // Check if an image is selected
                if (selectedImageBitmap == null && imageViewProduct.getDrawable() == null) {
                    Toast.makeText(edit_product.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use the existing image from the ImageView if no new image was selected
                Bitmap finalImageBitmap = selectedImageBitmap != null ? selectedImageBitmap :
                        ((BitmapDrawable)imageViewProduct.getDrawable()).getBitmap();

                // Create a new Product object
                Product updatedProduct = new Product(
                        Integer.parseInt(productId),  // Convert productId to int
                        productName,
                        productCost,
                        productQuantity,
                        finalImageBitmap
                );



                dbAccessProduct.UpdateProduct(updatedProduct);

                // Show success message
                Toast.makeText(edit_product.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(edit_product.this, AdminHomePage.class);
                startActivity(intent);
                // Optionally, return to the previous screen

            }
        });
    }
}
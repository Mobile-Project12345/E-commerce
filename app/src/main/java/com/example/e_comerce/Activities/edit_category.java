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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.DatabaseAccess.DbAccsessCategory;
import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class edit_category extends AppCompatActivity {

    private TextInputEditText editTextCategoryName;
    private ImageView imageViewCategory;
    private Button btnSelectImage, btnSaveChanges;
    private Bitmap selectedImageBitmap;
    private int categoryId;
    private DbAccsessCategory dbAccessCategory;

    // Launcher for image gallery selection
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        // Convert selected image to bitmap
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        imageViewCategory.setImageBitmap(selectedImageBitmap);
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
        setContentView(R.layout.activity_edit_category);

        // Edge-to-edge system bars handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database access
        dbAccessCategory = new DbAccsessCategory(this);

        // Initialize views
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        imageViewCategory = findViewById(R.id.imageViewCategory);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Retrieve category details from intent
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve category ID
            categoryId = intent.getIntExtra("CATEGORY_ID", -1);

            // Set category name
            String categoryName = intent.getStringExtra("CATEGORY_NAME");
            editTextCategoryName.setText(categoryName);

            // Set category image
            byte[] byteArray = intent.getByteArrayExtra("CATEGORY_IMAGE");
            if (byteArray != null) {
                Bitmap categoryImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageViewCategory.setImageBitmap(categoryImage);
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
                String categoryName = editTextCategoryName.getText().toString().trim();

                // Check if category name is empty
                if (categoryName.isEmpty()) {
                    editTextCategoryName.setError("Category name cannot be empty");
                    editTextCategoryName.requestFocus();
                    return;
                }

                // Check if an image is selected
                if (selectedImageBitmap == null && imageViewCategory.getDrawable() == null) {
                    Toast.makeText(edit_category.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Use the existing image from the ImageView if no new image was selected
                Bitmap finalImageBitmap = selectedImageBitmap != null ? selectedImageBitmap :
                        ((BitmapDrawable)imageViewCategory.getDrawable()).getBitmap();

                // Create a new Category object
                Category updatedCategory = new Category(
                        categoryName,
                        null,  // You might want to pass the list of products if needed
                        finalImageBitmap
                );
                updatedCategory.id = categoryId;  // Set the category ID

                // Update the category in the database
                dbAccessCategory.updateCategory(updatedCategory);

                // Show success message
                Toast.makeText(edit_category.this, "Category updated successfully", Toast.LENGTH_SHORT).show();

                // Return to the previous screen
                Intent intent = new Intent(edit_category.this, AdminHomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
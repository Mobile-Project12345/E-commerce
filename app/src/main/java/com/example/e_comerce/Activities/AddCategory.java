package com.example.e_comerce.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.e_comerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class AddCategory extends AppCompatActivity {

    private ImageView categoryImageView;
    private TextInputEditText categoryNameEditText;
    private MaterialButton addImageButton;
    private MaterialButton saveCategoryButton;
    private Bitmap selectedImageBitmap;
    private DbAccsessCategory dbAccsessCategory ;

    // ActivityResultLauncher for image gallery selection
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        // Convert URI to Bitmap
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                        // Set the image to ImageView
                        categoryImageView.setImageBitmap(selectedImageBitmap);
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
        setContentView(R.layout.activity_add_cateogry);

        // Edge-to-edge insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbAccsessCategory=new DbAccsessCategory(this);

        // Initialize views
        categoryImageView = findViewById(R.id.categoryImageView);
        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        addImageButton = findViewById(R.id.addImageButton);
        saveCategoryButton = findViewById(R.id.saveCategoryButton);

        // Set up image selection button
        addImageButton.setOnClickListener(v -> openGallery());

        // Set up save category button
        saveCategoryButton.setOnClickListener(v -> saveCategory());
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void saveCategory() {
        // Get category name from EditText
        String categoryName = categoryNameEditText.getText().toString().trim();

        // Validate inputs
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageBitmap == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

long addcateogry=dbAccsessCategory.addCategory(categoryName,selectedImageBitmap);
        if(addcateogry==-1)
        {
            Toast.makeText(this, "Failed Category is  exists", Toast.LENGTH_SHORT).show();

        }
        else   Toast.makeText(this, "Category saved successfully!", Toast.LENGTH_SHORT).show();


    }
}
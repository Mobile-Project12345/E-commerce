package com.example.e_comerce.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.DatabaseAccess.DbAccsessCategory;
import com.example.e_comerce.JavaClasses.Admin;
import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.JavaClasses.CategoryAdapter;
import com.example.e_comerce.JavaClasses.Customer;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.ProductAdapter;
import com.example.e_comerce.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerHomePage extends AppCompatActivity {



    private ListView productListView;
    private ProductAdapter productAdapter;
    private List<Category> ListOfCateogry;
    private EditText searchEditText;
    Customer loggedInUser;
    private static final int SPEECH_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         loggedInUser = (Customer) getIntent().getSerializableExtra("loggedInUser"); // Note: changed from "loggedInUser" to match the sender

        FloatingActionButton fabActions = findViewById(R.id.fab_actions);
        fabActions.setOnClickListener(v -> {

            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);
        });

        /// barcode

        Button btnbarcode=findViewById(R.id.barcode);

        ActivityResultLauncher<Intent> barcodeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String scannedBarcode = result.getData().getStringExtra("SCAN_RESULT");
                        //dd.setText(scannedBarcode);
                        filterProducts(scannedBarcode);

                    }
                });

        btnbarcode.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomePage.this, CaptureActivity.class);
            barcodeLauncher.launch(intent);
        });



        Button btnSpeak=findViewById(R.id.voice);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechRecognition();
            }
        });


        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        productListView = findViewById(R.id.productListView);
        searchEditText = findViewById(R.id.searchEditText);
        DbAccsessCategory dbAccessProduct = new DbAccsessCategory(this);

        ListOfCateogry=  dbAccessProduct.getAllCategoriesWithProducts();


        if(ListOfCateogry.isEmpty())
        {

            Toast.makeText(CustomerHomePage.this, "is empty ", Toast.LENGTH_SHORT).show();

        }
        else
        {
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

        searchEditText.setFocusable(false); // Initially non-focusable

        searchEditText.setOnClickListener(v -> {
            searchEditText.setFocusableInTouchMode(true); // Enable focus
            searchEditText.setFocusable(true); // Allow typing
            searchEditText.requestFocus(); // Request focus to open the keyboard
        });


        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Update the product list in the ListView
    private void updateProductList(List<Product> products) {
        productAdapter = new ProductAdapter(this, products);
        productListView.setAdapter(productAdapter);
    }

    // Filter products across all categories based on the search query
    private void filterProducts(String query) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Category category : ListOfCateogry) {
            for (Product product : category.Products) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
        }

        updateProductList(filteredProducts); // Update the ListView with filtered results
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                ///tvResult.setText(results.get(0));
                filterProducts(results.get(0));
            }
        }
    }
    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
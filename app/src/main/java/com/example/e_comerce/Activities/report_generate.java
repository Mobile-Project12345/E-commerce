package com.example.e_comerce.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_comerce.DatabaseAccess.DbAccsessTransaction;
import com.example.e_comerce.JavaClasses.TransactionAdapter;
import com.example.e_comerce.JavaClasses.transaction;
import com.example.e_comerce.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class report_generate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_generate);

        transaction trans=new transaction();

        List<transaction> list_transaction;
        List<transaction> list_transaction_display;
        list_transaction_display = new ArrayList<>();



        DbAccsessTransaction dbAccsessTransaction=new DbAccsessTransaction(this);



        list_transaction=dbAccsessTransaction.getAllTransactions();


        EditText date_taken=findViewById(R.id.date);
        Button btn_generate=findViewById(R.id.generate_report_button);



        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list_transaction_display.clear();

                String generat_date=date_taken.getText().toString();
                ///String date="2024-11-09";
                for (int i = 0; i < list_transaction.size(); i++) {
                    if (list_transaction.get(i).Date.equals(generat_date)) {
                        list_transaction_display.add(list_transaction.get(i));
                    }

                }

                RecyclerView recyclerView = findViewById(R.id.transactions_recycler_view);
                TransactionAdapter adapter = new TransactionAdapter(report_generate.this, list_transaction_display);
                recyclerView.setLayoutManager(new LinearLayoutManager(report_generate.this));
                recyclerView.setAdapter(adapter);



            }
        });








    }
}
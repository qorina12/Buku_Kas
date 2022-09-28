package com.example.bukukasnusantara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    Button kembali;
    RecyclerView recyclerView;

    DatabaseHelper db;
    ArrayList<String> id, tanggal, nominal, keterangan, kategori;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        kembali = findViewById(R.id.btn_kembali_detail);
        recyclerView = findViewById(R.id.recyclerView);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = new DatabaseHelper(DetailActivity.this);
        id = new ArrayList<>();
        tanggal = new ArrayList<>();
        nominal = new ArrayList<>();
        keterangan = new ArrayList<>();
        kategori = new ArrayList<>();

        storeDataInArrays();
        customAdapter = new CustomAdapter(DetailActivity.this, id, tanggal, nominal, keterangan, kategori);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                tanggal.add(cursor.getString(1));
                nominal.add(cursor.getString(2));
                keterangan.add(cursor.getString(3));
                kategori.add(cursor.getString(4));
            }
        }
    }
}
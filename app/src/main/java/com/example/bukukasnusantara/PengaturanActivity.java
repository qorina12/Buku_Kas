package com.example.bukukasnusantara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PengaturanActivity extends AppCompatActivity {
    Button simpan, kembali;
    EditText oldPassword, newPassword;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        db = new DatabaseHelper(this);
        simpan = findViewById(R.id.btn_simpan_pengaturan);
        kembali = findViewById(R.id.btn_kembali_pengaturan);
        oldPassword = findViewById(R.id.et_old_password);
        newPassword = findViewById(R.id.et_new_password);


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassString = oldPassword.getText().toString().trim();
                String newPassString = newPassword.getText().toString().trim();

                Boolean res = db.changePassword(oldPassString, newPassString);
                if (res == true)
                {
                    Toast.makeText(PengaturanActivity.this, "Berhasil ganti password", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(PengaturanActivity.this, "Gagal ganti password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
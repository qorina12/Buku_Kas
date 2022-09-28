package com.example.bukukasnusantara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText etuser, etpassword;
    Button BtnLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etuser = (EditText) findViewById(R.id.etUsername);
        etpassword = (EditText) findViewById(R.id.etPassword);
        BtnLogin = (Button) findViewById(R.id.btnLogin);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etuser.getText().toString().trim();
                String password = etpassword.getText().toString().trim();
                Boolean res = db.checkuser(username, password);
                Log.e("Username", username);
                Log.e("Password", password);
                Log.e("res", res.toString());
                if (res == true)
                {
                    Intent homepage = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(homepage);
                    Toast.makeText(LoginActivity.this, "Log in Berhasil ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Log in Gagal ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
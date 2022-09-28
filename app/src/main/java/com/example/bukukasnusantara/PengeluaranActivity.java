package com.example.bukukasnusantara;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class PengeluaranActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText tanggal, nominal, keterangan;
    ImageView tanggalButton;
    Button simpan, kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
        tanggal = findViewById(R.id.et_date_pengeluaran);
        nominal = findViewById(R.id.et_nominal_pengeluaran);
        keterangan = findViewById(R.id.et_keterangan_pengeluaran);
        tanggalButton = findViewById(R.id.iv_date_pengeluaran);
        simpan = findViewById(R.id.btn_simpan_pengeluaran);
        kembali = findViewById(R.id.btn_kembali_pengeluaran);

        tanggalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(PengeluaranActivity.this);
                String tanggals = tanggal.getText().toString().trim();
                String nominals = nominal.getText().toString().trim();
                String keterangans = keterangan.getText().toString().trim();
                db.addData(tanggals, nominals, keterangans, "pengeluaran");
//                Toast.makeText(PemasukanActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        String monthString = String.valueOf(month+1);
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        String dayOfMonthString = String.valueOf(dayOfMonth);
        if (dayOfMonthString.length() == 1) {
            dayOfMonthString = "0" + dayOfMonthString;
        }

        String date = dayOfMonthString + "/" + monthString + "/" + year;
        tanggal.setText(date);
    }
}
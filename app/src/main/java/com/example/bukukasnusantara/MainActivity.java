package com.example.bukukasnusantara;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView pemasukan, pengeluaran, detail, pengaturan;
    GraphView graphView;
    TextView tv_pemasukan, tv_pengeluaran;

    DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;

    LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>(new DataPoint[0]);
    LineGraphSeries<DataPoint> dataSeriesOut = new LineGraphSeries<>(new DataPoint[0]);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date dateNow = Calendar.getInstance().getTime();
    SimpleDateFormat sdfInt = new SimpleDateFormat("yyyyMM");
    int dateNowInt = Integer.parseInt(sdfInt.format(dateNow));

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pemasukan = findViewById(R.id.iv_pemasukan);
        pengeluaran = findViewById(R.id.iv_pengeluaran);
        detail = findViewById(R.id.iv_detail);
        pengaturan = findViewById(R.id.iv_pengaturan);
        graphView = findViewById(R.id.graph);
        tv_pemasukan = findViewById(R.id.tv_pemasukan);
        tv_pengeluaran = findViewById(R.id.tv_pengeluaran);

        db = new DatabaseHelper(this);
        sqLiteDatabase = db.getWritableDatabase();

//        insertData();

        graphView.addSeries(dataSeries);
        graphView.addSeries(dataSeriesOut);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);


        dataSeries.resetData(grabData());
        dataSeriesOut.resetData(grabDataOut());

        dataSeries.setColor(Color.GREEN);
        dataSeriesOut.setColor(Color.RED);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if (isValueX){
//                    Log.d("value", String.valueOf(value));
//                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
//                    Date date = null;
//                    try {
//                        date = originalFormat.parse(String.valueOf(value));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
////                    originalFormat.format(new Date((int) value));
//                    SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
////                    String formatedDate = newFormat.format(originalFormat);
//                    return newFormat.format(date);

                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        pemasukan.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, PemasukanActivity.class);
                startActivityForResult(mainIntent, 0);
            }
        });

        pengeluaran.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, PengeluaranActivity.class);
                startActivityForResult(mainIntent, 0);
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(mainIntent);
            }
        });

        pengaturan.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, PengaturanActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    DataPoint[] grabData() {
        String[] column = {"tanggal", "nominal", "kategori"};
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query("cashflow", column, "kategori = 'pemasukan'", null, null, null,null);

        Integer[][] valueArray = new Integer[cursor.getCount()][2];
        String[] kategoriArray = new String[cursor.getCount()];
        DataPoint[] dataPoints = new DataPoint[cursor.getCount()];

        int pemasukanInt = 0;


        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            String  resolutionDateReordered = cursor.getString(0).replaceAll("(\\d+)/(\\d+)/(\\d+)","$3$2$1");
            valueArray[i][0] = Integer.parseInt(resolutionDateReordered);
            valueArray[i][1] = cursor.getInt(1);
            kategoriArray[i] = cursor.getString(2);
        }

//        Log.e("Array before", Arrays.deepToString(valueArray));
        Arrays.sort(valueArray, Comparator.comparingInt(o -> o[0]));
//        Log.e("Array after", Arrays.deepToString(valueArray));

        for (int i = 0; i < cursor.getCount(); i++){
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = null;
            try {
                date = originalFormat.parse(String.valueOf(valueArray[i][0]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long second = date.getTime();
            int dateTemp = Integer.parseInt(originalFormat.format(date));
            int dateNowAwalBulan = dateNowInt*100; //20211100
            int dateNowAkhirBulan = (dateNowInt*100)+100;  //20211100 > 20211200
            Log.d("Awal", String.valueOf(dateNowAwalBulan));
            Log.d("Akhir", String.valueOf(dateNowAkhirBulan));
            Log.d("DAteTemp", String.valueOf(dateTemp));
            if (dateNowAwalBulan < dateTemp && dateTemp < dateNowAkhirBulan){
                Log.d("Loop?", "in");
                pemasukanInt += valueArray[i][1];
            }

            tv_pemasukan.setText("Pemasukan: " + printCurrency(Double.valueOf(pemasukanInt), "IDR"));
            dataPoints[i] = new DataPoint(second, valueArray[i][1]);
            Log.d("Second", String.valueOf(second));
        }
        Log.e("Data", dataPoints.toString());
        return dataPoints;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    DataPoint[] grabDataOut() {
        String[] column = {"tanggal", "nominal", "kategori"};
        @SuppressLint("Recycle") Cursor cursorOut = sqLiteDatabase.query("cashflow", column, "kategori = 'pengeluaran'", null, null, null,null);

        Integer[][] valueArrayOut = new Integer[cursorOut.getCount()][2];
        String[] kategoriArray = new String[cursorOut.getCount()];
        DataPoint[] dataPointsOut = new DataPoint[cursorOut.getCount()];

        int pengeluaranInt = 0;

        for (int i = 0; i < cursorOut.getCount(); i++){
            cursorOut.moveToNext();
            String  resolutionDateReordered = cursorOut.getString(0).replaceAll("(\\d+)/(\\d+)/(\\d+)","$3$2$1");
            valueArrayOut[i][0] = Integer.parseInt(resolutionDateReordered);
            valueArrayOut[i][1] = cursorOut.getInt(1);
            kategoriArray[i] = cursorOut.getString(2);
        }

//        Log.e("Array before", Arrays.deepToString(valueArray));
        Arrays.sort(valueArrayOut, Comparator.comparingInt(o -> o[0]));
//        Log.e("Array after", Arrays.deepToString(valueArray));

        for (int i = 0; i < cursorOut.getCount(); i++){
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = null;
            try {
                date = originalFormat.parse(String.valueOf(valueArrayOut[i][0]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long second = date.getTime();
            int dateTemp = Integer.parseInt(originalFormat.format(date));
            if ((dateNowInt*100) < dateTemp && dateTemp < ((dateNowInt*100)+100)){
                pengeluaranInt += valueArrayOut[i][1];
            }
            tv_pengeluaran.setText("Pengeluaran: " + printCurrency(Double.valueOf(pengeluaranInt), "IDR"));

            dataPointsOut[i] = new DataPoint(second, valueArrayOut[i][1]);
            Log.d("Second", String.valueOf(second));
        }
        Log.e("Data", dataPointsOut.toString());
        return dataPointsOut;
    }

    private String printCurrency(Double currencyAmount, String outputCurrency) {
        Locale locale;

        if (outputCurrency.equals("IDR")) {
            locale = new Locale("id", "ID");
        } else if (outputCurrency.equals("Dollars")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("en", "US");
        }

        Currency currency = Currency.getInstance(locale);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        return numberFormat.format(currencyAmount);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dataSeries.resetData(grabData());
        dataSeriesOut.resetData(grabDataOut());

//        reload();
        //OR
//        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
}
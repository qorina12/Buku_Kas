package com.example.bukukasnusantara;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList id, tanggal, nominal, keterangan, kategori;


    CustomAdapter(Context context, ArrayList id, ArrayList tanggal, ArrayList nominal, ArrayList keterangan, ArrayList kategori){
        this.context = context;
        this.id = id;
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.keterangan = keterangan;
        this.kategori = kategori;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String nmnl = String.valueOf(nominal.get(position));
        String nmnl2 = printCurrency(Double.valueOf(nmnl), "IDR");
        holder.nominal.setText(nmnl2);
//        holder.nominal.setText(String.valueOf(nominal.get(position)));

        holder.keterangan.setText(String.valueOf(keterangan.get(position)));
        holder.tanggal.setText(String.valueOf(tanggal.get(position)));
        String ktgr = String.valueOf(kategori.get(position));
        if (ktgr.equals("pemasukan")) {
            holder.kategori.setImageDrawable(context.getResources().getDrawable(R.drawable.daco_4326153));
            holder.simbol.setText("[+]");
        } else if (ktgr.equals("pengeluaran")) {
            holder.kategori.setImageDrawable(context.getResources().getDrawable(R.drawable.daco_4326153__1_));
            holder.simbol.setText("[-]");
        }
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

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView simbol, nominal, keterangan, tanggal;
        ImageView kategori;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            simbol = itemView.findViewById(R.id.tv_plus_minus);
            nominal = itemView.findViewById(R.id.tv_nominal_cardview);
            keterangan = itemView.findViewById(R.id.tv_keterangan_cardview);
            tanggal = itemView.findViewById(R.id.tv_tanggal_cardview);
            kategori = itemView.findViewById(R.id.iv_icon_kategori);
        }
    }
}



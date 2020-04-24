package com.example.kouveepetshopapps.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.kouveepetshopapps.R;
import com.example.kouveepetshopapps.model.NamaHargaLayanan;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteHargaLayananAdapter extends ArrayAdapter<NamaHargaLayanan> {
    List<NamaHargaLayanan> namaHargaLayananListFull;

    public AutoCompleteHargaLayananAdapter(@NonNull Context context, @NonNull List<NamaHargaLayanan> namaHargaLayananList) {
        super(context, 0, new ArrayList<>(namaHargaLayananList));
        this.namaHargaLayananListFull = new ArrayList<>(namaHargaLayananList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return namaHargaLayananFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_auto_complete_harga_layanan, parent, false
            );
        }

        //TextView id_harga_layanan = convertView.findViewById(R.id.id_harga_layanan_auto_complete);
        TextView nama_harga_layanan = convertView.findViewById(R.id.nama_harga_layanan_auto_complete);

        NamaHargaLayanan namaHargaItem = getItem(position);

        if (namaHargaItem != null) {
            //id_harga_layanan.setText(String.valueOf(namaHargaItem.getId_harga_layanan()));
            nama_harga_layanan.setText(namaHargaItem.getNama_harga_layanan());
        }

        return convertView;
    }

    private Filter namaHargaLayananFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<NamaHargaLayanan> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(namaHargaLayananListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (NamaHargaLayanan item : namaHargaLayananListFull) {
                    if (item.getNama_harga_layanan().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((NamaHargaLayanan) resultValue).getNama_harga_layanan();
        }
    };
}

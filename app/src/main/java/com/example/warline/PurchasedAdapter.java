package com.example.warline;

// PurchasedAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class PurchasedAdapter extends ArrayAdapter<Riwayat> {

    public PurchasedAdapter(Context context, List<Riwayat> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Riwayat item = getItem(position);
        if (item == null) {
            return convertView; // or create a new View if necessary
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.purchased_item, parent, false);
        }

        TextView jumlahTextView = convertView.findViewById(R.id.countTextView);
        TextView hargaTextView = convertView.findViewById(R.id.hargaTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView productTextView = convertView.findViewById(R.id.productTextView);

        hargaTextView.setText(": " + item.getHarga());
        jumlahTextView.setText(": " + item.getJumlah());
        dateTextView.setText(": " + item.gettanggal());
        productTextView.setText(": " + item.getProdukName());

        return convertView;
    }
}

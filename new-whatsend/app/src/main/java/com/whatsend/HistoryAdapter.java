package com.whatsend;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    DBConfig config;
    SQLiteDatabase db;

    ArrayList idList, nameList, phoneNumberList, dateList;

    Fragment fg;


    public HistoryAdapter(Fragment fg, ArrayList idList, ArrayList nameList, ArrayList phoneNumberList, ArrayList dateList){
        config = new DBConfig(fg.getContext());
        this.fg = fg;
        this.idList = idList;
        this.nameList = nameList;
        this.phoneNumberList = phoneNumberList;
        this.dateList = dateList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vw = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
        return new ViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final String id = (String) idList.get(i);
        final String name = (String) nameList.get(i);
        final String phoneNumber = (String) phoneNumberList.get(i);
        final String date = (String) dateList.get(i);
        viewHolder.txtId.setText(id);
        viewHolder.txtName.setText(name);
        viewHolder.txtDate.setText(date);
        viewHolder.txtPhoneNumber.setText(formatPhoneNumber(phoneNumber));

        viewHolder.dataItem.setOnClickListener( v -> {
            sendMessage(name, phoneNumber);
        });

    }

    @Override
    public int getItemCount() {
        return idList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtId, txtName, txtDate, txtPhoneNumber;
        private CardView dataItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txt_id);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtPhoneNumber = itemView.findViewById(R.id.txt_phone_number);
            dataItem = itemView.findViewById(R.id.data_item);
        }

    }

    public String formatPhoneNumber(String phoneNumber) {
        // Menghapus karakter non-digit dari nomor telepon
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        // Mengecek apakah nomor telepon memiliki panjang yang cukup
        if (digitsOnly.length() < 9) {
            return phoneNumber; // Mengembalikan format awal jika nomor telepon tidak valid
        }

        // Membuat format dengan kode negara, kode area, dan nomor
        String formattedNumber = "+" + digitsOnly.substring(0, 2) + " " +
                digitsOnly.substring(2, 5) + "-" +
                digitsOnly.substring(5, 9) + "-" +
                digitsOnly.substring(9);

        return formattedNumber;
    }

    private void sendMessage(String name, String phoneNumber)
    {
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.whatsapp");

        addData(name, phoneNumber);

        this.fg.startActivity(intent);
    }

    private void addData(String name, String phoneNumber) {

        db = config.getReadableDatabase();
        db.execSQL("INSERT INTO tbl_history (name, phone_number, date) VALUES('" +
                name + "','" +
                phoneNumber + "','" +
                getCurrentDateTime() + "')");
        ((HistoryFragment) fg).showData();
    }

    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = sdf.format(currentDate);

        return formattedDateTime;
    }


}

package com.whatsend;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fredporciuncula.phonemoji.PhonemojiTextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SendFragment extends Fragment {

    View root;

    DBConfig config;
    SQLiteDatabase db;
    Cursor cursor;

    private EditText edtName, edtMessage;
    private Button btnSend;
    private PhonemojiTextInputEditText edtPhoneNumber;


    public SendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_send, container, false);

        config = new DBConfig(getContext());

        edtName = root.findViewById(R.id.edt_name);
        edtMessage = root.findViewById(R.id.edt_message);
        edtPhoneNumber = root.findViewById(R.id.edt_phone_number);
        btnSend = root.findViewById(R.id.btn_send);

        btnSend.setOnClickListener(v -> sendMessage());

        Log.d("Date", getCurrentDateTime());

        return root;
    }

    private String cleanPhoneNumber(String phoneNumber) {
        String noSpaces = phoneNumber.replace(" ", "");
        return noSpaces.replace("+", "").replace("-", "");
    }

    private void sendMessage()
    {
        String name = edtName.getText().toString();
        String message = edtMessage.getText().toString();
        String phoneNumber = cleanPhoneNumber(edtPhoneNumber.getText().toString());

        if( name.isEmpty() || phoneNumber.isEmpty() || phoneNumber.length() < 12 ) {
            Toast.makeText(getContext(), "Name or phone number is incorrect", Toast.LENGTH_SHORT).show();
        }else{

            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.whatsapp");

            addData(name, phoneNumber);

            startActivity(intent);

        }

    }

    private void addData(String name, String phoneNumber) {

        db = config.getReadableDatabase();
        db.execSQL("INSERT INTO tbl_history (name, phone_number, date) VALUES('" +
                name + "','" +
                phoneNumber + "','" +
                getCurrentDateTime() + "')");
    }

    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = sdf.format(currentDate);

        return formattedDateTime;
    }

}
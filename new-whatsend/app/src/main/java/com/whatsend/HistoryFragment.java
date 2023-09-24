package com.whatsend;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    DBConfig config;
    SQLiteDatabase db;
    Cursor cursor;

    View root;

    TextView txtBlank;
    RecyclerView rcvData;

    RecyclerView.Adapter<HistoryAdapter.ViewHolder> adapterData;
    RecyclerView.LayoutManager linearLayoutManagerData;

    ArrayList idList, nameList, phoneNumberList, dateList;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_history, container, false);

        config = new DBConfig(getContext());

        txtBlank = root.findViewById(R.id.txt_blank);

        showData();

        return root;
    }

    public void showData()
    {
        db = config.getReadableDatabase();

        idList = new ArrayList<>();
        nameList = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        dateList = new ArrayList<>();

        linearLayoutManagerData = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapterData = new HistoryAdapter(this, idList, nameList, phoneNumberList, dateList);

        rcvData = root.findViewById(R.id.rcv_data);


        rcvData.setLayoutManager(linearLayoutManagerData);
        rcvData.setHasFixedSize(true);
        rcvData.setAdapter(adapterData);

        cursor = db.rawQuery("SELECT * FROM tbl_history ORDER BY id DESC",null);
//
        cursor.moveToFirst();

        if( cursor.getCount() > 0 ) {
            for (int count = 0; count < cursor.getCount(); count++) {
                cursor.moveToPosition(count);
                idList.add(cursor.getString(0));
                nameList.add(cursor.getString(1));
                phoneNumberList.add(cursor.getString(2));
                dateList.add(cursor.getString(3));
            }

            rcvData.setVisibility(View.VISIBLE);
            txtBlank.setVisibility(View.GONE);

        }else{
            rcvData.setVisibility(View.GONE);
            txtBlank.setVisibility(View.VISIBLE);
        }

    }


}
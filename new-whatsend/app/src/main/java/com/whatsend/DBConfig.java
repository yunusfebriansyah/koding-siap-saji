package com.whatsend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DBConfig extends SQLiteOpenHelper {
    // konfigurasi database
    static final String db_name = "db_whatsend.db";
    static final int db_version = 1;

    public DBConfig(@Nullable Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        // buat tabel user
        sql = "CREATE TABLE tbl_history (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT(100) NOT NULL, phone_number TEXT(100) NOT NULL, date TEXT(100) NOT NULL)";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}

package com.vikrant.inventory_app2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vikrant.inventory_app2.data.InventoryContract.InventoryEntry;

/**
 * Created by Vikrant on 03-01-2018.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="invent.db";
    private static final int DATABASE_VERSION=1;

    public InventoryDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INVENT_TABLE =  "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_INVENT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_INVENT_PRICE + " INTEGER NOT NULL, "
                 + InventoryEntry.COLUMN_INVENT_IMAGE +" TEXT NOT NULL, "
                + InventoryEntry.COLUMN_INVENT_QUANTITY + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_INVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

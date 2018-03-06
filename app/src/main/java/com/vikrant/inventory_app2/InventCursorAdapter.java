package com.vikrant.inventory_app2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikrant.inventory_app2.data.InventoryContract.InventoryEntry;

/**
 * Created by Vikrant on 03-01-2018.
 */

public class InventCursorAdapter extends CursorAdapter {
int quantityvalue;
int pos;

    public InventCursorAdapter(Context context, Cursor c) {

        super(context, c, 0 /* flags */);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ImageView buy;


        TextView mName =  view.findViewById(R.id.list_name);
        final TextView mQuantity =  view.findViewById(R.id.list_quantity);
        TextView mPrice =  view.findViewById(R.id.list_price);
        ImageView mimage =  view.findViewById(R.id.list_image);

        String name = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_NAME));
         String quantity = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_QUANTITY));
        String price = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_PRICE));
        //int imageColumnIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_IMAGE);

       Log.v("xxxxxxxxxxxxxxxxxxxxx",cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_IMAGE)));

        String imagestring = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_IMAGE));



        mimage.setImageURI(Uri.parse(imagestring));

        mName.setText(name);
        mQuantity.setText(quantity);
        mPrice.setText(price);
        mimage.setImageURI(Uri.parse(imagestring));
        final long id = cursor.getLong(cursor.getColumnIndex(InventoryEntry._ID));


        buy = view.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
        int pos =cursor.getPosition();
            @Override
            public void onClick(View view) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                    cursor.moveToPosition(pos);
                 quantityvalue = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_QUANTITY));
                if (quantityvalue> 0) {

                    quantityvalue--;

                    Uri makenewuri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                    values.put(InventoryEntry.COLUMN_INVENT_QUANTITY, quantityvalue--);
                    resolver.update(makenewuri, values, null, null);
                    context.getContentResolver().notifyChange(makenewuri, null);


                } else Toast.makeText(view.getContext(), "Out of Stock", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
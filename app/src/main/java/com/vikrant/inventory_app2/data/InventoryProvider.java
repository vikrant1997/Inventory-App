package com.vikrant.inventory_app2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.vikrant.inventory_app2.data.InventoryContract.InventoryEntry;

/**
 * Created by Vikrant on 03-01-2018.
 */

public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = ContentProvider.class.getSimpleName();
    private static final int TABLE1 = 100;
    private static final int TABLE1_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ITEMS, TABLE1);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ITEMS + "/#", TABLE1_ID);
    }

    private InventoryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new InventoryDbHelper(getContext());

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TABLE1:
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, null, null, null, null, null);
                break;
            case TABLE1_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
           default:
                throw new IllegalArgumentException("Cannot query this " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }







    @Override
    public Uri insert(Uri uri,ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TABLE1:
                return insertitem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertitem(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        String name=values.getAsString(InventoryEntry.COLUMN_INVENT_NAME);
        if(name.isEmpty())
            //throw new IllegalArgumentException("Item requires a name ");
           Toast.makeText(getContext(),"Please Enter name ", Toast.LENGTH_SHORT).show();
        String price=values.getAsString(InventoryEntry.COLUMN_INVENT_PRICE);
        if(price.isEmpty())
            throw new IllegalArgumentException("Item requires price ");
        String quantity=values.getAsString(InventoryEntry.COLUMN_INVENT_QUANTITY);
        if(quantity.isEmpty())
            throw new IllegalArgumentException("Item requires quantity ");


        // Insert the new pet with the given values
        long id = database.insert(InventoryEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
           // Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
       getContext().getContentResolver().notifyChange(uri, null);


        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri,ContentValues contentValues,String selection,String[] selectionArgs) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case TABLE1:
                return updateitem(uri, contentValues, selection, selectionArgs);
            case TABLE1_ID:
                selection=InventoryEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateitem(uri,contentValues,selection,selectionArgs);
                default:
                    throw new IllegalArgumentException("update is not supported for "+uri);
        }

    }
    private int updateitem(Uri uri,ContentValues values,String selection,String[] selectionArgs){
        if(values.containsKey(InventoryEntry.COLUMN_INVENT_NAME)){
            String name=values.getAsString(InventoryEntry.COLUMN_INVENT_NAME);
            if(name.isEmpty())
                throw new IllegalArgumentException("Item requires a name ");
        }
        if(values.containsKey(InventoryEntry.COLUMN_INVENT_QUANTITY)){
            String quantity=values.getAsString(InventoryEntry.COLUMN_INVENT_QUANTITY);
            if(quantity.isEmpty())
                throw new IllegalArgumentException("Item requires quantity ");
        }
        if(values.containsKey(InventoryEntry.COLUMN_INVENT_PRICE)){
            String price=values.getAsString(InventoryEntry.COLUMN_INVENT_PRICE);
            if(price.isEmpty())
                throw new IllegalArgumentException("Item requires price ");
        }
        if(values.size()==0){
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri,String selection,String[] selectionArgs) {
        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match){
            case TABLE1:
                rowsDeleted=db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case TABLE1_ID:
                selection=InventoryEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);
                break;
                default:throw new IllegalArgumentException("delete not supported for "+uri);

        }
        if(rowsDeleted>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
return rowsDeleted;
    }



    @Override
    public String getType(Uri uri) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case TABLE1:
                return InventoryContract.CONTENT_LIST_TYPE;
            case TABLE1_ID:
                return InventoryContract.CONTENT_ITEM_TYPE;
                default:throw new IllegalArgumentException("Unknown uri "+uri+" With match "+match);
        }
    }
}

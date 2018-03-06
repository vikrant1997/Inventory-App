package com.vikrant.inventory_app2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.vikrant.inventory_app2.data.InventoryContract.InventoryEntry;
import com.vikrant.inventory_app2.data.InventoryDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
private ImageView quantityshow;


private InventoryDbHelper mDbHelper;
private InventCursorAdapter mCursorAdapter;
private static final int INVENT_LOADER=10001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab =(FloatingActionButton)  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        quantityshow=findViewById(R.id.buy);

        ListView itemsList=findViewById(R.id.display_items);
        mCursorAdapter=new InventCursorAdapter(this,null);
        itemsList.setAdapter(mCursorAdapter);
        itemsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(CatalogActivity.this,EditorActivity.class);
                Uri currentItemUri= ContentUris.withAppendedId(InventoryEntry.CONTENT_URI,id);
                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });




        getLoaderManager().initLoader(INVENT_LOADER,null,this);

    }

private void insertitem(){

//    SQLiteDatabase db=mDbHelper.getWritableDatabase();
    ContentValues values=new ContentValues();
    values.put(InventoryEntry.COLUMN_INVENT_NAME,"car");
    values.put(InventoryEntry.COLUMN_INVENT_PRICE,150);
    values.put(InventoryEntry.COLUMN_INVENT_QUANTITY,12);
   values.put(InventoryEntry.COLUMN_INVENT_IMAGE,"android.resource://"+getPackageName()+"/"+R.drawable.ic_add);
     //long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
    Uri newuri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
}




    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_insert_dummy_data:
                insertitem();

                return true;
            case R.id.action_delete_all_data:
                deleteAllitems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllitems() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[]projection={InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENT_IMAGE,
                InventoryEntry.COLUMN_INVENT_NAME,
                            InventoryEntry.COLUMN_INVENT_PRICE,
                InventoryEntry.COLUMN_INVENT_QUANTITY};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                InventoryEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }



}

package com.vikrant.inventory_app2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikrant.inventory_app2.data.InventoryContract.InventoryEntry;
import com.vikrant.inventory_app2.data.InventoryDbHelper;

/**
 * Created by Vikrant on 02-01-2018.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int Editor_loader=0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 0;
    private InventoryDbHelper mDbHelper;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private TextView mQuantityText;
    private ImageView mMinus;
    private ImageView mPlus;
    private ImageView camera;
    private int quantity=0;
    private Uri mCurrentIntentUri;
    private Uri imageUri;
    private Uri newUri;
    private boolean infoItemHasChanged=false;
    private boolean itemhaschanged=false;
    private boolean mImagefound=false;

    private TextView restock;
    private ImageView plus;
    private ImageView minus;
    private TextView orderbutton;
    private EditText Emailid;
    private EditText orderquantity;

    private View.OnTouchListener mTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            itemhaschanged=true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
       setconstatnts();
       Intent intent=getIntent();
       mCurrentIntentUri=intent.getData();
       restock=findViewById(R.id.Restock);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        orderbutton=findViewById(R.id.order);
        Emailid=findViewById(R.id.email_id);
        orderquantity=findViewById(R.id.orderquantity);

       if(mCurrentIntentUri==null){

           restock.setVisibility(View.INVISIBLE);
           setTitle("Add a new item ");
           invalidateOptionsMenu();
       }else {
           restock.setVisibility(View.VISIBLE);
           plus.setVisibility(View.INVISIBLE);
           minus.setVisibility(View.INVISIBLE);
           setTitle("Edit a item ");
           getLoaderManager().initLoader(Editor_loader,null,this);
       }
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityText.setOnTouchListener(mTouchListener);
        camera.setOnTouchListener(mTouchListener);

        orderbutton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                String morderquantity=orderquantity.getText().toString();
                String mEmailid=Emailid.getText().toString();
                Intent email = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
                email.setType("plain/text");
                email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mEmailid});
                email.putExtra(android.content.Intent.EXTRA_SUBJECT, "Order Quantity");
                email.putExtra(android.content.Intent.EXTRA_TEXT, "I am manager of Gucci company \n"+"This Email is to order "+morderquantity+" quantities");

            /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(email, "Send mail..."));


            }
        });


    }

 String imagestring;
    private void insert() {
       // mDbHelper = new InventoryDbHelper(this);
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();

        String quantityString = mQuantityText.getText().toString().trim();




        if(mCurrentIntentUri!=null&&mImagefound==false){
        Cursor cursor=getContentResolver().query(mCurrentIntentUri,null,null,null,null);

        if(cursor.moveToFirst())
        {  imagestring=cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_IMAGE));}

       }
       else if(mCurrentIntentUri!=null&&mImagefound==true){
            imagestring=imageUri.toString();
        }
       else if(mCurrentIntentUri==null&&mImagefound==true)
            imagestring=imageUri.toString();
        else if(mCurrentIntentUri==null&&mImagefound==false){
            imagestring=null;
        }




        if(TextUtils.isEmpty(nameString)){
            Toast.makeText(this,"Please Enter Name ",Toast.LENGTH_SHORT).show();
        return;}
        if(TextUtils.isEmpty(priceString)){
            Toast.makeText(this,"Please Enter Price ",Toast.LENGTH_SHORT).show();
        return;}
        if(TextUtils.isEmpty(quantityString)){
            Toast.makeText(this,"Please Enter Quantity ",Toast.LENGTH_SHORT).show();
        return;}
        if(imagestring==null){
            Toast.makeText(this,"Please ChooseImage ",Toast.LENGTH_SHORT).show();
        return;}

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INVENT_NAME, nameString);

        values.put(InventoryEntry.COLUMN_INVENT_IMAGE, imagestring);


        values.put(InventoryEntry.COLUMN_INVENT_QUANTITY, Integer.valueOf(quantityString));
        values.put(InventoryEntry.COLUMN_INVENT_PRICE, Integer.valueOf(priceString));

        //long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

/*if(!TextUtils.isEmpty(nameString)&&!TextUtils.isEmpty(priceString)&&!TextUtils.isEmpty(quantityString)&&!TextUtils.isEmpty(imagestring)) {*/
    if (mCurrentIntentUri == null) {
        Uri newuri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
        if (newuri == null)
            Toast.makeText(this, "Insertion Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Insertion Successful", Toast.LENGTH_SHORT).show();

    } else {

        int RowAffected = getContentResolver().update(mCurrentIntentUri, values, null, null);
        if (RowAffected == 0)
            Toast.makeText(this, "Updation Failed", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Updation Successful", Toast.LENGTH_SHORT).show();

    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentIntentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_done:
                    insert();
                    finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!itemhaschanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButton=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                DialogInterface.OnClickListener PositiveButton=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogInterface!=null)
                            dialogInterface.dismiss();
                    }
                };
                showUnsavedChangesDialog(discardButton,PositiveButton);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!itemhaschanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        DialogInterface.OnClickListener discardButton=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NavUtils.navigateUpFromSameTask(EditorActivity.this);
            }
        };
        DialogInterface.OnClickListener PositiveButton=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null)
                    dialogInterface.dismiss();
            }
        };
        showUnsavedChangesDialog(discardButton,PositiveButton);
        // Show dialog that there are unsaved changes

    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener,DialogInterface.OnClickListener PositiveButton) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("DISCARD", discardButtonClickListener);
        builder.setNegativeButton("KEEP EDITING", PositiveButton);
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Delete?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentIntentUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentIntentUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this,"SUCCESSFULLY DELETED", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void displayQuantity(int numberofitems){

        TextView mQuantityText=(TextView)findViewById(R.id.display_quantity);
        mQuantityText.setText(""+numberofitems);

    }
public void setminus(){
    mMinus.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(quantity>0){
                quantity--;
                displayQuantity(quantity);
                return;
            }


        }
    });
}
public void setPlus(){

    mPlus.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            quantity++;
            displayQuantity(quantity);
            return;
        }
    });
}
public void setconstatnts(){
    mNameEditText=(EditText)findViewById(R.id.name);
    mPriceEditText=(EditText)findViewById(R.id.price);
    mQuantityText=findViewById(R.id.display_quantity);
    mMinus=(ImageView)findViewById(R.id.minus);
    mPlus=(ImageView)findViewById(R.id.plus);
    camera=findViewById(R.id.action_camera);
   /* mNameEditText.setOnTouchListener(mTouchListener);
    mPriceEditText.setOnTouchListener(mTouchListener);*/
    setPlus();
    setminus();
   setcamera();
}
public void setcamera(){
    camera.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            tryToOpenImageSelector();
            infoItemHasChanged = true;
        }
    });
}

    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        openImageSelector();
    }
    private void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageSelector();
                    // permission was granted
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                mImagefound=true;
                imageUri = resultData.getData();
                camera.setImageURI(imageUri);
                camera.setMinimumHeight(100);
                camera.setMinimumWidth(100);
                camera.setScaleType(ImageView.ScaleType.CENTER_CROP);

                camera.invalidate();
               // Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVENT_IMAGE,
                InventoryEntry.COLUMN_INVENT_NAME,
                InventoryEntry.COLUMN_INVENT_QUANTITY,
                InventoryEntry.COLUMN_INVENT_PRICE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
               mCurrentIntentUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_PRICE);
            int imageColumnIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_INVENT_IMAGE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
           int price = cursor.getInt(priceColumnIndex);
           String image=cursor.getString(imageColumnIndex);



            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityText.setText(Integer.toString(quantity));
            camera.setImageURI(Uri.parse(image));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            mNameEditText.setText("");
            mPriceEditText.setText("");
            mQuantityText.setText("");
            camera.setImageURI(null);
    }
}

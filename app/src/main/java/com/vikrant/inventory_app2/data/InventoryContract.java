package com.vikrant.inventory_app2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vikrant on 03-01-2018.
 */

public class InventoryContract {

    public static final String CONTENT_AUTHORITY="com.book.inventory_app2";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;



    private InventoryContract(){}
    public static final class InventoryEntry implements BaseColumns{
        public static final String TABLE_NAME="INVENT";
        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_INVENT_NAME="name";
        public static final String COLUMN_INVENT_PRICE="price";
        public static final String COLUMN_INVENT_QUANTITY="quantity";
        public static final String COLUMN_INVENT_IMAGE="image";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEMS);

    }
}

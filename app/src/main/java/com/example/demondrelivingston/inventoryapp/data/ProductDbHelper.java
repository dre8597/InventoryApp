package com.example.demondrelivingston.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.demondrelivingston.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by demondrelivingston on 1/2/18.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "warehouse.db";

    /**
     * Database version. If I change the database schema, I must increment the database version.
     */
    private static final int DATABASE_VERSION = 2;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a String that Contains the SQL statement to create the product table
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_AMOUNT + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_PRODUCT_IMAGE + " TEXT NOT NULL DEFAULT 'No images', "
                + ProductEntry.COLUMN_PRODUCT_SUPPLIER + " TEXT NOT NULL);";
        //Execute the SQ: statement
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void saleMade(long itemId, int amount) {
        SQLiteDatabase db = getWritableDatabase();
        if (amount > 0) {
            amount--;
        }
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_AMOUNT, String.valueOf(amount));
        String selection = ProductEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};
        db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public Cursor readStock() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_AMOUNT,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                ProductEntry.COLUMN_PRODUCT_IMAGE};

        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }
}

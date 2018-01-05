package com.example.demondrelivingston.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demondrelivingston.inventoryapp.data.ProductContract.ProductEntry;
import com.example.demondrelivingston.inventoryapp.data.ProductProvider;

/**
 * Created by demondrelivingston on 1/2/18.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    //Object from mainActivity
    private final MainActivity activity;

    public ProductCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0/*flags*/);
        this.activity = context;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Inflate a list item view using the layout specified in product_item.xml
        return LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        //Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        ImageView imageView = (ImageView) view.findViewById(R.id.picture);
        Button buy = (Button) view.findViewById(R.id.buy_button);

        //Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_AMOUNT);
        int imageColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);
        final int idColumnIndex = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));

        //Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        final String quantity = "Quantity: " + String.valueOf(productQuantity);
        String imageUriString = cursor.getString(imageColumnIndex);
        Uri image = Uri.parse(imageUriString);

        //Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(quantity);
        imageView.setImageURI(image);


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viexw) {
                Uri productUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, idColumnIndex);
                itemSold(context, productUri, productQuantity);
            }
        });
    }

    private void itemSold(Context context, Uri productUri, int currentQuantityInStock) {

        // Subtract 1 from current value if quantity of product >= 1
        int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock - 1 : 0;

        if (currentQuantityInStock == 0) {
            Toast.makeText(context.getApplicationContext(), "Item is out of stock", Toast.LENGTH_SHORT).show();
        }

        // Update table by using new value of quantity
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_AMOUNT, newQuantityValue);
        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);
        if (numRowsUpdated > 0) {
            // Show error message in Logs with info about pass update.
            Log.i(LOG_TAG, "Item has been sold");
        } else {
            Toast.makeText(context.getApplicationContext(), "Item is currently out of stock", Toast.LENGTH_SHORT).show();
            // Show error message in Logs with info about fail update.
            Log.e(LOG_TAG, "Issue with restocking");
        }
    }
}

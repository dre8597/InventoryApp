package com.example.demondrelivingston.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demondrelivingston.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by demondrelivingston on 1/2/18.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0/*flags*/);
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
    public void bindView(View view, Context context, Cursor cursor) {
        //Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        ImageView imageView = (ImageView) view.findViewById(R.id.picture);

        //Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_AMOUNT);
        int imageColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

        //Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        int productQuantity = cursor.getInt(quantityColumnIndex);
        String quantity = "Quantity: "+String.valueOf(productQuantity) ;
        Uri image = Uri.parse(cursor.getString(imageColumnIndex));
        //Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText("$"+productPrice);
        quantityTextView.setText(quantity);

        //Use glide to show imported image
        Glide.with(context).load(image).into(imageView);

        final long id = cursor.getLong(cursor.getColumnIndex(ProductEntry._ID));

    }
}

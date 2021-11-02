package com.mark.stylish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mark.stylish.objects.Product;
import com.mark.stylish.objects.Variants;

public class StylishDataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "stylish.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "stylish";
    public static final String KEY_ID = "_id";
    public static final String COLUMN_PRODUCT_ID = "productId";
    public static final String COLUMN_PRODUCT_MAIN_IMAGE = "productMainImage";
    public static final String COLUMN_PRODUCT_TITLE = "productTitle";
    public static final String COLUMN_PRODUCT_PRICE = "productPrice";
    public static final String COLUMN_PRODUCT_SELECTED_COLOR = "productSelectedColor";
    public static final String COLUMN_PRODUCT_SELECTED_SIZE = "productSelectedSize";
    public static final String COLUMN_PRODUCT_STOCK = "productStock";
    public static final String COLUMN_PRODUCT_SELECTED_AMOUNT = "productSelectedAmount";

    public StylishDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE "
                + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_ID + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_MAIN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_TITLE + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + COLUMN_PRODUCT_SELECTED_COLOR + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_SELECTED_SIZE + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_STOCK + " INTEGER NOT NULL, "
                + COLUMN_PRODUCT_SELECTED_AMOUNT + " INTEGER NOT NULL"
                + ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertProductInfo(SQLiteDatabase db, Product product, Variants variants, int amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_ID, product.getId());
        contentValues.put(COLUMN_PRODUCT_MAIN_IMAGE, product.getMainImage());
        contentValues.put(COLUMN_PRODUCT_TITLE, product.getTitle());
        contentValues.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        contentValues.put(COLUMN_PRODUCT_SELECTED_COLOR, "#" + variants.getColorCode());
        contentValues.put(COLUMN_PRODUCT_SELECTED_SIZE, variants.getSize());
        contentValues.put(COLUMN_PRODUCT_STOCK, variants.getStock());
        contentValues.put(COLUMN_PRODUCT_SELECTED_AMOUNT, amount);

        db.insert(TABLE_NAME, null, contentValues);
    }

    public void updateProductInfo(SQLiteDatabase db, Product product, Variants variants, int amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_SELECTED_AMOUNT, amount);

        String selection = COLUMN_PRODUCT_TITLE + "='" + product.getTitle() + "'AND "
                + COLUMN_PRODUCT_SELECTED_COLOR + "='" + "#" + variants.getColorCode() + "'AND "
                + COLUMN_PRODUCT_SELECTED_SIZE + "='" + variants.getSize() + "'";


        db.update(TABLE_NAME, contentValues, selection, null);
    }

    public Cursor getAllProductInfo(SQLiteDatabase db) {
        return  db.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor queryDifferentProduct(SQLiteDatabase db, Product product, Variants variants) {
        String[] columns = new String[] {COLUMN_PRODUCT_TITLE,
                COLUMN_PRODUCT_SELECTED_COLOR, COLUMN_PRODUCT_SELECTED_SIZE};

        String selection = COLUMN_PRODUCT_TITLE + "='" + product.getTitle() + "'AND "
                + COLUMN_PRODUCT_SELECTED_COLOR + "='" + "#" + variants.getColorCode() + "'AND "
                + COLUMN_PRODUCT_SELECTED_SIZE + "='" + variants.getSize() + "'";

        Cursor cursor = db.query(TABLE_NAME,
                columns,
                selection,
                null,
                null,
                null,
                null);

        return cursor;
    }

    public void plusProductAmount(SQLiteDatabase db, long id, int amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_SELECTED_AMOUNT, amount);
        String where = KEY_ID + "=" + id;

        db.update(TABLE_NAME, contentValues, where, null);
    }

    public void minusProductAmount(SQLiteDatabase db, long id, int amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRODUCT_SELECTED_AMOUNT, amount);
        String where = KEY_ID + "=" + id;

        db.update(TABLE_NAME, contentValues, where, null);
    }

    public boolean removeProductInfo(SQLiteDatabase db, long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    public boolean isProductExist(SQLiteDatabase db, Product product, Variants variants) {
        String[] columns = new String[] {COLUMN_PRODUCT_TITLE,
                COLUMN_PRODUCT_SELECTED_COLOR, COLUMN_PRODUCT_SELECTED_SIZE};

        String selection = COLUMN_PRODUCT_TITLE + "='" + product.getTitle() + "'AND "
                + COLUMN_PRODUCT_SELECTED_COLOR + "='" + "#" + variants.getColorCode() + "'AND "
                + COLUMN_PRODUCT_SELECTED_SIZE + "='" + variants.getSize() + "'";

        Cursor cursor = db.query(TABLE_NAME,
                columns,
                selection,
                null,
                null,
                null,
                null);

        return cursor.getCount() > 0;
    }
}

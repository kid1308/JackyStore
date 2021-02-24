package com.thangtd.jackystore.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.thangtd.jackystore.objects.AuthorObj;
import com.thangtd.jackystore.objects.BookObj;
import com.thangtd.jackystore.objects.ProductObj;
import com.thangtd.jackystore.objects.PublisherObj;

/**
 * Created by thangtd2016 on 02/03/2018.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Jacky_20190530.db";
    private static final int vs = 1;
    public SQLiteDatabase database;

    // init table
    public static final String TB_PRODUCTS = "PRODUCTS";
    public static final String TB_BOOK_DETAILS = "BOOK_DETAILS";
    public static final String TB_PUBLISHERS = "PUBLISHERS";
    public static final String TB_AUTHORS = "AUTHORS";

    // init columns name in table
    // PRODUCTS
    public static final String PRO_ORDINAL = "_id";
    public static final String PRO_CODE = "CODE_PRO";
    public static final String PRO_TYPE = "TYPE_PRO";

    // BOOK_DETAILS
    public static final String BOOK_ORDINAL = "_id";
    public static final String BOOK_ISBN = "ISBN_BOOK";
    public static final String BOOK_TITLE = "TITLE_BOOK";
    public static final String BOOK_AUTHOR = "AUTHOR_BOOK";
    public static final String BOOK_TRANSLATOR = "TRANSLATOR_BOOK";
    public static final String BOOK_PUBLISHER = "PUBLISHER_BOOK";
    public static final String BOOK_PAGES = "PAGES_BOOK";
    public static final String BOOK_RELEASED_DATE = "RELEASED_DATE_BOOK";
    public static final String BOOK_ADDED_DATE = "ADDED_DATE_BOOK";
    public static final String BOOK_PURCHASED_DATE = "PURCHASED_DATE_BOOK";
    public static final String BOOK_FINISHED_DATE = "FINISHED_DATE_BOOK";
    public static final String BOOK_LOCATION = "LOCATION_BOOK";
    public static final String BOOK_NOTE = "NOTE_BOOK";
    public static final String BOOK_KIND = "KIND_BOOK";
    public static final String BOOK_COVER = "COVER_BOOK";

    // PUBLISHERS
    public static final String PUBLISHER_ORDINAL = "_id";
    public static final String PUBLISHER_NAME = "NAME_PUBLISHER";

    // AUTHORS
    public static final String AUTHOR_ORDINAL = "_id";
    public static final String AUTHOR_NAME = "NAME_AUTHOR";

// ....

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, vs);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCreateProductsTable = "CREATE TABLE IF NOT EXISTS " + TB_PRODUCTS + "(" + PRO_ORDINAL
                + " INTEGER PRIMARY KEY, " + PRO_CODE + " INTEGER UNIQUE, " + PRO_TYPE + " INTEGER UNIQUE)";
        Log.d("debug", sqlCreateProductsTable);
        db.execSQL(sqlCreateProductsTable);

        String sqlCreateBookDetailsTable = "CREATE TABLE IF NOT EXISTS " + TB_BOOK_DETAILS + "(" + BOOK_ORDINAL
                + " INTEGER PRIMARY KEY, " + BOOK_ISBN + " INTEGER UNIQUE, " + BOOK_TITLE + " TEXT, "
                + BOOK_AUTHOR + " TEXT, " + BOOK_TRANSLATOR + " TEXT, " + BOOK_PUBLISHER + " TEXT, "
                + BOOK_PAGES + " INTEGER, " + BOOK_RELEASED_DATE + " DATE, " + BOOK_ADDED_DATE + " DATE, "
                + BOOK_PURCHASED_DATE + " DATE, " + BOOK_FINISHED_DATE + " DATE, " + BOOK_LOCATION + " TEXT, "
                + BOOK_NOTE + " TEXT, " + BOOK_KIND + " INTEGER, " + BOOK_COVER + " BLOB)";
        Log.d("debug", sqlCreateBookDetailsTable);
        db.execSQL(sqlCreateBookDetailsTable);

        String sqlCreatePublishersTable = "CREATE TABLE IF NOT EXISTS " + TB_PUBLISHERS + "(" + PUBLISHER_ORDINAL
                + " INTEGER PRIMARY KEY, " + PUBLISHER_NAME + " TEXT UNIQUE)";
        Log.d("debug", sqlCreatePublishersTable);
        db.execSQL(sqlCreatePublishersTable);

        String sqlCreateAuthorsTable = "CREATE TABLE IF NOT EXISTS " + TB_AUTHORS + "(" + AUTHOR_ORDINAL
                + " INTEGER PRIMARY KEY, " + AUTHOR_NAME + " TEXT UNIQUE)";
        Log.d("debug", sqlCreateAuthorsTable);
        db.execSQL(sqlCreateAuthorsTable);

        // create more table if need
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUCTS);
//        db.execSQL("DROP TABLE IF EXISTS " + TB_BOOK_DETAILS);
//        db.execSQL("DROP TABLE IF EXISTS " + TB_PUBLISHERS);
        onCreate(db);
    }

    /* Methods */
    // PRODUCTS table
    public void INSERT_PRODUCT(ProductObj mPro) {
        ContentValues values = new ContentValues();
        values.put(PRO_CODE, mPro.getCode());
        values.put(PRO_TYPE, mPro.getType());
        database.insertWithOnConflict(TB_PRODUCTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void DELETE_PRODUCT_BY_ID(int mID) {
        database.delete(TB_PRODUCTS, PRO_ORDINAL + " = " + mID, null);
    }

    public void DELETE_PRODUCT_BY_CODE(Integer mCode) {
        database.delete(TB_PRODUCTS, PRO_CODE + " = " + mCode, null);
    }

    // update 1 record in table
    public void UPDATE_PRODUCT_BY_ID(ProductObj mPro, int mID) {
        ContentValues values = new ContentValues();
        values.put(PRO_TYPE, mPro.getType());

        database.update(TB_PRODUCTS, values, "_id = " + mID, null);
    }

    // select all records
    public Cursor SELECT_ALL_PRODUCTS() {
        return database.query(TB_PRODUCTS, new String[]{PRO_ORDINAL, PRO_CODE, PRO_TYPE}, null,
                null, null, null, PRO_ORDINAL);
    }

    public void DELETE_ALL_PRODUCTS() {
        database.delete(TB_PRODUCTS, null, null);
    }

    // BOOK_DETAILS table
    public void INSERT_BOOK(BookObj mBook) {
        ContentValues values = new ContentValues();
        values.put(BOOK_ISBN, mBook.getIsbn());
        values.put(BOOK_TITLE, mBook.getTitle());
        values.put(BOOK_AUTHOR, mBook.getAuthor());
        values.put(BOOK_TRANSLATOR, mBook.getTranslator());
        values.put(BOOK_PUBLISHER, mBook.getPublisher());
        values.put(BOOK_PAGES, mBook.getPages());
        values.put(BOOK_RELEASED_DATE, mBook.getReleased_date());
        values.put(BOOK_ADDED_DATE, mBook.getAdded_date());
        values.put(BOOK_PURCHASED_DATE, mBook.getPurchased_date());
        values.put(BOOK_FINISHED_DATE, mBook.getFinished_date());
        values.put(BOOK_LOCATION, mBook.getLocation());
        values.put(BOOK_NOTE, mBook.getNote());
        values.put(BOOK_KIND, mBook.getKind());
        values.put(BOOK_COVER, mBook.getCover());
        database.insertWithOnConflict(TB_BOOK_DETAILS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void DELETE_BOOK_BY_ID(int mID) {
        database.delete(TB_BOOK_DETAILS, BOOK_ORDINAL + " = " + mID, null);
    }

    public void DELETE_BOOK_BY_CODE(String mCode) {
        database.delete(TB_BOOK_DETAILS, BOOK_ISBN + " = " + mCode, null);
    }

    // update 1 record in table
    public void UPDATE_BOOK_BY_ISBN(BookObj mBook, String mISBN) {
        ContentValues values = new ContentValues();
        values.put(BOOK_TITLE, mBook.getTitle());
        values.put(BOOK_AUTHOR, mBook.getAuthor());
        values.put(BOOK_TRANSLATOR, mBook.getTranslator());
        values.put(BOOK_PUBLISHER, mBook.getPublisher());
        values.put(BOOK_PAGES, mBook.getPages());
        values.put(BOOK_RELEASED_DATE, mBook.getReleased_date());
        values.put(BOOK_PURCHASED_DATE, mBook.getPurchased_date());
        values.put(BOOK_FINISHED_DATE, mBook.getFinished_date());
        values.put(BOOK_LOCATION, mBook.getLocation());
        values.put(BOOK_NOTE, mBook.getNote());
        values.put(BOOK_KIND, mBook.getKind());
        values.put(BOOK_COVER, mBook.getCover());
        database.update(TB_BOOK_DETAILS, values, BOOK_ISBN + " = '" + mISBN + "'", null);
    }

    // update 1 record in table
    public void UPDATE_FINISHED_DATE_BY_ISBN(BookObj mBook, String mISBN) {
        ContentValues values = new ContentValues();
        values.put(BOOK_FINISHED_DATE, mBook.getFinished_date());
        database.update(TB_BOOK_DETAILS, values, BOOK_ISBN + " = '" + mISBN + "'", null);
    }

    // select all records
    public Cursor SELECT_ALL_BOOKS() {
        return database.query(TB_BOOK_DETAILS, new String[]{BOOK_ORDINAL, BOOK_ISBN, BOOK_TITLE, BOOK_AUTHOR, BOOK_TRANSLATOR,
                        BOOK_PUBLISHER, BOOK_PAGES, BOOK_RELEASED_DATE, BOOK_ADDED_DATE, BOOK_PURCHASED_DATE, BOOK_FINISHED_DATE,
                        BOOK_LOCATION, BOOK_NOTE, BOOK_KIND, BOOK_COVER},
                null, null, null, null, BOOK_TITLE);
    }

    public void DELETE_ALL_BOOKS() {
        database.delete(TB_BOOK_DETAILS, null, null);
    }

    // PUBLISHERS table
    public void INSERT_PUBLISHER(PublisherObj mPub) {
        ContentValues values = new ContentValues();
        values.put(PUBLISHER_NAME, mPub.getName());
        database.insertWithOnConflict(TB_PUBLISHERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void DELETE_PUBLISHER_BY_ID(int mID) {
        database.delete(TB_PUBLISHERS, PUBLISHER_ORDINAL + " = " + mID, null);
    }

    // select all records
    public Cursor SELECT_ALL_PUBLISHERS() {
        return database.query(TB_PUBLISHERS, new String[]{PUBLISHER_ORDINAL, PUBLISHER_NAME}, null,
                null, null, null, PUBLISHER_ORDINAL);
    }

    public void DELETE_ALL_PUBLISHERS() {
        database.delete(TB_PUBLISHERS, null, null);
    }

    // AUTHORS table
    public void INSERT_AUTHOR(AuthorObj mAut) {
        ContentValues values = new ContentValues();
        values.put(AUTHOR_NAME, mAut.getName());
        database.insertWithOnConflict(TB_AUTHORS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void DELETE_AUTHOR_BY_ID(int mID) {
        database.delete(TB_AUTHORS, AUTHOR_ORDINAL + " = " + mID, null);
    }

    // select all records
    public Cursor SELECT_ALL_AUTHORS() {
        return database.query(TB_AUTHORS, new String[]{AUTHOR_ORDINAL, AUTHOR_NAME}, null,
                null, null, null, AUTHOR_ORDINAL);
    }

    public void DELETE_ALL_AUTHORS() {
        database.delete(TB_AUTHORS, null, null);
    }

    public Cursor SELECTSQL(String sql) {
        return database.rawQuery(sql, null);
    }

    // close database
    public void CloseDB() {
        if (database != null && database.isOpen())
            database.close();
    }
}

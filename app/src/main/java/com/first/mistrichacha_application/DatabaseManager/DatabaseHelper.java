package com.first.mistrichacha_application.DatabaseManager;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.first.mistrichacha_application.Model.CartModel;

import java.util.ArrayList;

import static android.icu.text.MessagePattern.ArgType.SELECT;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Mistri_Chacha.db";
    public static final String TABLE_NAME = "CartViewTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "ITEM_ID";
    public static final String COL_3 = "ITEM_NAME";
    public static final String COL_4 = "PRICE";
    public static final String COL_5 = "QUANTITY";
    public static final String COL_6 = "DISCRIPTION";
    public static final String COL_7 = "CATEGORY";
    public static final String COL_8 = "IMAGE";
    public static final String COL_9 = "TOTAL";
    public static final String COL_10 = "COLOR";
    public static final String COL_11 = "SIZE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ITEM_ID TEXT,ITEM_NAME TEXT,PRICE TEXT,QUANTITY TEXT,DISCRIPTION TEXT,CATEGORY TEXT,IMAGE TEXT,TOTAL TEXT,COLOR TEXT,SIZE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String itemid, String item, String price, String quantity,
                              String description, String category , String image , String color , String size) {

      String  total = String.valueOf(Double.parseDouble(price) * Double.parseDouble(quantity));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, itemid);
        contentValues.put(COL_3, item);
        contentValues.put(COL_4, price);
        contentValues.put(COL_5, quantity);
        contentValues.put(COL_6, description);
        contentValues.put(COL_7, category);
        contentValues.put(COL_8, image);
        contentValues.put(COL_9, total);
        contentValues.put(COL_10, color);
        contentValues.put(COL_11, size);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
       }

    public Integer deleteData(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ITEM_ID = ? AND ITEM_NAME = ?", new String[]{id, name});
    }

    public Integer deletAlldata() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    public boolean updateData(String itemid, String item, String price,
                              String quantity, String description,
                              String category, String image , String color , String size) {

      String  total = String.valueOf(Double.parseDouble(price) * Double.parseDouble(quantity));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, itemid);
        contentValues.put(COL_3, item);
        contentValues.put(COL_4, price);
        contentValues.put(COL_5, quantity);
        contentValues.put(COL_6, description);
        contentValues.put(COL_7, category);
        contentValues.put(COL_8, image);
        contentValues.put(COL_9, total);
        contentValues.put(COL_10, color);
        contentValues.put(COL_11, size);
        db.update(TABLE_NAME, contentValues, "ITEM_ID = ?", new String[]{itemid});
        return true;
     }

    public ArrayList<CartModel> getAllcartProducts() {

        ArrayList<CartModel> cartList = new ArrayList<CartModel>();
        // Select All Query
        try {
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CartModel model = new CartModel();
                    model.setId(cursor.getString(cursor.getColumnIndex(COL_2)));
                    model.setName(cursor.getString(cursor.getColumnIndex(COL_3)));
                    model.setPrice(cursor.getString(cursor.getColumnIndex(COL_4)));
                    model.setQuantity(cursor.getString(cursor.getColumnIndex(COL_5)));
                    model.setDescription(cursor.getString(cursor.getColumnIndex(COL_6)));
                    model.setTotal(cursor.getInt(cursor.getColumnIndex(COL_9)));
                    model.setQuan(cursor.getInt(cursor.getColumnIndex(COL_5)));
                    model.setMaincategory(cursor.getString(cursor.getColumnIndex(COL_7)));
                    model.setImage(cursor.getString(cursor.getColumnIndex(COL_8)));
                    model.setColor(cursor.getString(cursor.getColumnIndex(COL_10)));
                    model.setSize(cursor.getString(cursor.getColumnIndex(COL_11)));
                    cartList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return contact list
        return cartList;
    }

    public boolean update(String itemid, String price, String quantity, String total) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Float tp = Float.parseFloat(total);
            int q = Integer.parseInt(quantity);
            Float p = Float.parseFloat(price);

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_9, String.valueOf(p + tp));
            contentValues.put(COL_5, String.valueOf(q + 1));
            db.update(TABLE_NAME, contentValues, "ITEM_ID = ?", new String[]{itemid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public  String quantity(String itemid){
        SQLiteDatabase db = this.getWritableDatabase();
        String quan = "";
        String selectQuery = "SELECT " +COL_5 + "  FROM " + TABLE_NAME + " where `" + ITEM_ID + "`=" + "?";

        Cursor c = db.rawQuery(selectQuery, new String[] { itemid });
        if (c.moveToFirst()) {
            quan = c.getString(c.getColumnIndex(COL_5));
        }
        c.close();
        return  quan ;
    }


    public boolean incrementData(String itemid, String price, String quantity, String total) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Float tp = Float.parseFloat(total);
            int q = Integer.parseInt(quantity);
            Float p = Float.parseFloat(price);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_9, String.valueOf(p + tp));
            contentValues.put(COL_5, String.valueOf(q + 1));
            db.update(TABLE_NAME, contentValues, "ITEM_ID = ?", new String[]{itemid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean decrementData(String itemid, String price, String quantity, String total) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Float tp = Float.parseFloat(total);
            int q = Integer.parseInt(quantity);
            Float p = Float.parseFloat(price);

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_9, String.valueOf(tp - p));
            contentValues.put(COL_5, String.valueOf(q - 1));
            db.update(TABLE_NAME, contentValues, "ITEM_ID = ?", new String[]{itemid});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean ifBothExists(String id, String name) {
        Cursor cursor = null;
        String checkQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "= '" + id + "' AND " + COL_3 + "= '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Cursor getBothSelectData(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "= '" + id + "' AND " + COL_3 + "= '" + name + "'", null);
        return res;
    }

}

package fit.bstu.lab_05_06.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.bstu.lab_05_06.Product;

/**
 * Created by andre on 14.10.2017.
 */

public class ProductDBHelper extends SQLiteOpenHelper {

    public ProductDBHelper(Context context) {
        super(context, "PRODUCTS_DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS foo (id INTEGER, name TEXT, count INTEGER, price REAL, img_path TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

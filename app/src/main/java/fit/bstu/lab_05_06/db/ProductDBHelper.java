package fit.bstu.lab_05_06.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fit.bstu.lab_05_06.Constants;

/**
 * Created by andre on 14.10.2017.
 */

public class ProductDBHelper extends SQLiteOpenHelper {

    public ProductDBHelper(Context context) {
        super(context, "PRODUCTS_DB", null, Constants.SQL_LITE_DB_CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS products (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, count INTEGER, price REAL, img_path TEXT, is_saved INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ProductDBVersionController versionController = new ProductDBVersionController(db);
        versionController.upgrateDatabase(oldVersion, newVersion);
    }
}

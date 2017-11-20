package fit.bstu.lab_05_06.db.sqllite;

import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

/**
 * Created by andre on 21.10.2017.
 */

public class ProductDBVersionController {

    private ArrayMap<Integer,ISQLLiteVersion> versionController;

    public ProductDBVersionController(SQLiteDatabase db) {
        this.db = db;
        versionController =  new ArrayMap<>();
        versionController.put(2, (database) -> {
            try {
                database.execSQL("ALTER TABLE products ADD COLUMN is_saved INTEGER;");
            } catch (Exception err) {
                err.printStackTrace();
            }
        });
    }

    private SQLiteDatabase db;

    public void upgrateDatabase(int from, int to) {
        for (int i = from + 1; i<=to; i++) {
            versionController.get(i).upgrade(db);
        }
    }
}

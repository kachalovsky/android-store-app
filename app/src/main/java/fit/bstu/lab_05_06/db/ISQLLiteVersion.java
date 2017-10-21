package fit.bstu.lab_05_06.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by andre on 21.10.2017.
 */

public interface ISQLLiteVersion {
    public void upgrade(SQLiteDatabase db);
}

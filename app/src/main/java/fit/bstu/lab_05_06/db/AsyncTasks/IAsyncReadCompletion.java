package fit.bstu.lab_05_06.db.AsyncTasks;

import android.database.Cursor;

/**
 * Created by andre on 18.10.2017.
 */

public interface IAsyncReadCompletion {
    void OnComplete(Cursor cursor);
}

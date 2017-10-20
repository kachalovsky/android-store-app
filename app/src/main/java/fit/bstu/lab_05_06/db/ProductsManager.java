package fit.bstu.lab_05_06.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import fit.bstu.lab_05_06.db.AsyncTasks.IAsyncReadCompletion;
import fit.bstu.lab_05_06.db.AsyncTasks.IAsyncWriteCompletion;
import fit.bstu.lab_05_06.models.Product;

/**
 * Created by andre on 16.10.2017.
 */

public class ProductsManager {
    ProductDBHelper dbHelper;

    public ProductsManager(Context context) {
        dbHelper = new ProductDBHelper(context);
    }

    public void insert(Product product, IAsyncWriteCompletion completion) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    db.insertOrThrow("products", null, product.getValues());
                }catch (Exception e){
                    e.printStackTrace();
                }
                dbHelper.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                completion.OnComplete();
            }
        };
        task.execute();
    }

    public void update(Product product, IAsyncWriteCompletion completion) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    db.update("products", product.getValues(), "_id=" + product.getId(), null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                dbHelper.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                completion.OnComplete();
            }
        };
        task.execute();
    }

    public void delete(Product product, IAsyncWriteCompletion completion) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    db.delete("products", "_id=" + product.getId(), null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                dbHelper.close();
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                completion.OnComplete();
            }
        };
        task.execute();
    }

    public void getProducts(final String where, final String order, IAsyncReadCompletion completion) {
        AsyncTask<Void, Void, Cursor> task = new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    String query = "SELECT * FROM products";
                    if (where != null && where.length() > 0) query += " WHERE " + where;
                    if (order != null && order.length() > 0) query += " ORDER BY  " + order;
                    query += ";";
                    return db.rawQuery(query, null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                dbHelper.close();
                return null;
            }

            @Override
            protected void onPostExecute(Cursor result) {
                completion.OnComplete(result);
            }
        };
        task.execute();
    }
}

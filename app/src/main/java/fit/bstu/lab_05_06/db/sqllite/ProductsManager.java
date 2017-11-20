package fit.bstu.lab_05_06.db.sqllite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import fit.bstu.lab_05_06.db.sqllite.AsyncTasks.IAsyncReadCompletion;
import fit.bstu.lab_05_06.db.sqllite.AsyncTasks.IAsyncWriteCompletion;
import fit.bstu.lab_05_06.models.Product.ProductModel;

/**
 * Created by andre on 16.10.2017.
 */

public class ProductsManager {
    ProductDBHelper dbHelper;

    public ProductsManager(Context context) {
        dbHelper = new ProductDBHelper(context);
    }

    public void insert(ProductModel productModel, IAsyncWriteCompletion completion) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    db.insertOrThrow("products", null, productModel.getValues());
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

    public void update(ProductModel productModel, IAsyncWriteCompletion completion) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    db.update("products", productModel.getValues(), "_id=" + productModel.getIdentifier(), null);
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

    public void delete(ProductModel productModel, IAsyncWriteCompletion completion) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                try{
                    db.delete("products", "_id=" + productModel.getIdentifier(), null);
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

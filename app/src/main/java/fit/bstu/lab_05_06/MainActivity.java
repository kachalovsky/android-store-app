package fit.bstu.lab_05_06;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import fit.bstu.lab_05_06.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.db.ProductDBHelper;
import fit.bstu.lab_05_06.db.ProductsCursorAdapter;
import fit.bstu.lab_05_06.models.Product;
import fit.bstu.lab_05_06.products_list.ProductsListAdapter;

public class MainActivity extends AppCompatActivity {
    ProductsCursorAdapter productAdapter;
    ListView productsListView;
    ProductDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new ProductDBHelper(this);
        prepareListOfProducts();
    }

    private void prepareListOfProducts() {
        productsListView = (ListView)findViewById(R.id.listView);
        //productListAdapter = new ProductsListAdapter(this, R.layout.product_list_item);
        try{
            productAdapter = new ProductsCursorAdapter(this, fetchCursor(), R.layout.product_list_item);
            productsListView.setAdapter(productAdapter);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Cursor fetchCursor() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor productCursor = db.rawQuery("SELECT * FROM products", null);
        return productCursor;
    }

    public void onButtonClick(View v) {
        Intent intent = new Intent(this, MainActivityOfChain.class);
        intent.putExtra(MainActivityOfChain.RESULT_KEY, new Product());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Product product = (Product)data.getSerializableExtra(MainActivityOfChain.RESULT_KEY);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try{
                db.insertOrThrow("products", null, product.getValues());
            }catch (Exception e){
                e.printStackTrace();
            }

            dbHelper.close();
            productAdapter.changeCursor(fetchCursor());
        }
    }
}

package fit.bstu.lab_05_06;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.Serializable;

import fit.bstu.lab_05_06.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.db.AsyncTasks.IAsyncWriteCompletion;
import fit.bstu.lab_05_06.db.ProductDBHelper;
import fit.bstu.lab_05_06.db.ProductsCursorAdapter;
import fit.bstu.lab_05_06.db.ProductsManager;
import fit.bstu.lab_05_06.models.Product;
import fit.bstu.lab_05_06.products_list.ProductsListAdapter;

public class MainActivity extends AppCompatActivity {
    ProductsCursorAdapter productAdapter;
    ListView productsListView;
    ProductsManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new ProductsManager(this);
        prepareListOfProducts();
    }

    private void prepareListOfProducts() {
        productsListView = (ListView)findViewById(R.id.listView);
        //productListAdapter = new ProductsListAdapter(this, R.layout.product_list_item);
        try{
            dbManager.getProducts(null, null, newCursor -> {
                productAdapter = new ProductsCursorAdapter(this, newCursor, R.layout.product_list_item);
                productsListView.setAdapter(productAdapter);
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonClick(View v) {

        Intent intent = new Intent(this, MainActivityOfChain.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Serializable createdProduct = data.getSerializableExtra(MainActivityOfChain.CREATED_KEY);
        Serializable editedProduct = data.getSerializableExtra(MainActivityOfChain.UPDATED_KEY);
        IAsyncWriteCompletion copmletion = () -> {
            dbManager.getProducts(null, null, (newCursor) -> {
                productAdapter.changeCursor(newCursor);
            });
        };
        if (createdProduct != null) {
            Product product = (Product)createdProduct;
            dbManager.insert(product, copmletion);
        }

        if (editedProduct != null) {
            Product product = (Product)editedProduct;
            dbManager.update(product, copmletion);
        }
    }
}

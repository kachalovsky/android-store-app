package fit.bstu.lab_05_06;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import fit.bstu.lab_05_06.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.products_list.ProductsListAdapter;

public class MainActivity extends AppCompatActivity {
    ProductsListAdapter productListAdapter;
    ListView productsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productsListView = (ListView)findViewById(R.id.listView);
        productListAdapter = new ProductsListAdapter(this, R.layout.product_list_item);
        productsListView.setAdapter(productListAdapter);
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
            productListAdapter.listOfProducts.add(product);
            productListAdapter.notifyDataSetChanged();
        }
    }
}

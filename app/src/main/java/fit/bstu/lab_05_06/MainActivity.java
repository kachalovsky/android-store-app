package fit.bstu.lab_05_06;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;

import fit.bstu.lab_05_06.db.AsyncTasks.IAsyncReadCompletion;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.db.AsyncTasks.IAsyncWriteCompletion;
import fit.bstu.lab_05_06.db.ProductsCursorAdapter;
import fit.bstu.lab_05_06.db.ProductsManager;
import fit.bstu.lab_05_06.models.Product;
import fit.bstu.lab_05_06.shared_modules.order_controller.OrderController;

public class MainActivity extends AppCompatActivity {
    enum OrderType {
        COUNT,
        PRICE
    }

    ProductsCursorAdapter productAdapter;
    ListView productsListView;
    ProductsManager dbManager;
    String currentWhere = null;
    OrderController<OrderType> priceOrderController;
    OrderController<OrderType> countOrderController;
    OrderController<OrderType> currentOrderController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new ProductsManager(this);
        priceOrderController = new OrderController(OrderType.PRICE, "Price");
        countOrderController = new OrderController(OrderType.COUNT, "Count");
        currentOrderController = priceOrderController;
        prepareListOfProducts();
        setListenersForOrderBtns();
    }

    private void prepareListOfProducts() {
        productsListView = (ListView)findViewById(R.id.listView);
        try{
            dbManager.getProducts(null, null, newCursor -> {
                productAdapter = new ProductsCursorAdapter(this, newCursor, R.layout.product_list_item);
                productsListView.setAdapter(productAdapter);
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListenersForOrderBtns() {
        Button priceBtn = (Button)findViewById(R.id.btn_sort_price);
        Button countBtn = (Button)findViewById(R.id.btn_sort_count);
        Button saveBtn = (Button)findViewById(R.id.btn_sort_save);
        priceBtn.setOnClickListener(v -> {
            currentOrderController = priceOrderController;
            currentOrderController.nextState();
            refreshListViewByCurrentOrder(newCursor -> {
                productAdapter.changeCursor(newCursor);
                productAdapter.notifyDataSetChanged();
            });
        });
        countBtn.setOnClickListener(v -> {
            currentOrderController = countOrderController;
            currentOrderController.nextState();
            refreshListViewByCurrentOrder(newCursor -> {
                productAdapter.swapCursor(newCursor);
            });
        });
        saveBtn.setOnClickListener(v -> {
            if (currentWhere == null) {
                currentWhere = "is_saved=1";
                v.setBackgroundColor(getResources().getColor(R.color.colorPicker));
            }
            else {
                currentWhere = null;
                v.setBackgroundColor(getResources().getColor(R.color.transparent));

            }
            refreshListViewByCurrentOrder(newCursor -> {
                productAdapter.swapCursor(newCursor);
            });
        });
    }

    public void onButtonClick(View v) {
        Intent intent = new Intent(this, MainActivityOfChain.class);
        startActivityForResult(intent, 0);
    }

    private void refreshListViewByOrder(String orderItem, OrderController.States state, IAsyncReadCompletion completion) {
        switch (state) {
            case UP:
                dbManager.getProducts(currentWhere, orderItem, completion);
                break;
            case DOWN:
                dbManager.getProducts(currentWhere, orderItem +" DESC", completion);
                break;
            case UNSELECT:
                dbManager.getProducts(currentWhere, null, completion);
                break;
        }
    }

    public void refreshListViewByCurrentOrder(IAsyncReadCompletion completion) {
        switch (currentOrderController.getOrderType()) {
            case PRICE:
                refreshListViewByOrder("price", currentOrderController.getState(), completion);
                Button btnPrice = (Button) findViewById(R.id.btn_sort_price);
                btnPrice.setText(currentOrderController.getOrderName());
                break;
            case COUNT:
                refreshListViewByOrder("count", currentOrderController.getState(), completion);
                Button btnCount = (Button) findViewById(R.id.btn_sort_count);
                btnCount.setText(currentOrderController.getOrderName());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Serializable createdProduct = data.getSerializableExtra(MainActivityOfChain.CREATED_KEY);
        Serializable editedProduct = data.getSerializableExtra(MainActivityOfChain.UPDATED_KEY);
        IAsyncWriteCompletion copmletion = () -> {
            refreshListViewByCurrentOrder((newCursor) -> {
                productAdapter.changeCursor(newCursor);
                productAdapter.notifyDataSetChanged();
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

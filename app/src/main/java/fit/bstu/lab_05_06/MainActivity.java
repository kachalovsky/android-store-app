package fit.bstu.lab_05_06;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;


import fit.bstu.lab_05_06.auth.AuthActivity;
import fit.bstu.lab_05_06.auth.AuthManager;
import fit.bstu.lab_05_06.db.firebase.FireBaseManager;
import fit.bstu.lab_05_06.db.firebase.IFireBaseQueryProcess;
import fit.bstu.lab_05_06.db.firebase.IFireBaseQueryResult;
import fit.bstu.lab_05_06.db.sqllite.AsyncTasks.IAsyncReadCompletion;
import fit.bstu.lab_05_06.models.Product.ProductFirebase;
import fit.bstu.lab_05_06.products_list.ProductsListAdapter;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.db.sqllite.AsyncTasks.IAsyncWriteCompletion;
import fit.bstu.lab_05_06.db.sqllite.ProductsCursorAdapter;
import fit.bstu.lab_05_06.db.sqllite.ProductsManager;
import fit.bstu.lab_05_06.models.Product.ProductModel;
import fit.bstu.lab_05_06.shared_modules.order_controller.OrderController;

public class MainActivity extends AppCompatActivity {
    enum OrderType {
        COUNT,
        PRICE
    }

    ProductsCursorAdapter productAdapter;
    ListView productsListView;
    ProductsManager dbManager;
    String currentWhereField = null;
    AuthManager authManager;
    OrderController<OrderType> priceOrderController;
    OrderController<OrderType> countOrderController;
    OrderController<OrderType> currentOrderController;
    GoogleApiClient mGoogleApiClient;
    final MainActivity controller = this;
    IFireBaseQueryProcess currentFirebaseProcess = null;
    IFireBaseQueryResult fireBaseCompletion = new IFireBaseQueryResult() {
        @Override
        public void onResult(ArrayList<ProductFirebase> list) {
            productsListView = (ListView)findViewById(R.id.listView);
            ProductsListAdapter adapter = new ProductsListAdapter(controller, R.layout.product_list_item);
            adapter.listOfProductModels = list;
            productsListView.setAdapter(adapter);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        authManager = AuthManager.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Read from the database
        FireBaseManager.getDbReference().addValueEventListener(new ValueEventListener() {
            String TAG = "111";
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fireBaseCompletion.onResult(FireBaseManager.createProductsList(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
       // myRef.orderByKey()
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authManager = AuthManager.getInstance();
        setContentView(R.layout.activity_main);
        dbManager = new ProductsManager(this);
        priceOrderController = new OrderController(OrderType.PRICE, "Price");
        countOrderController = new OrderController(OrderType.COUNT, "Count");
        currentOrderController = priceOrderController;
        prepareListOfProducts();
        setListenersForOrderBtns();
        setUserManagingListeners();
    }

    private void setUserManagingListeners() {
        TextView tw = (TextView) findViewById(R.id.lbl_email_info);
        tw.setText("Hello, " + authManager.getUserEmail());
        Button logout = (Button) findViewById(R.id.btn_logout);
        logout.setOnClickListener(b -> {
            authManager.logOut();
            Intent loginIntent = new Intent(this, AuthActivity.class);
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            } else {
                mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        try{
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                });
                mGoogleApiClient.connect();
            }

            startActivity(loginIntent);
        });
    }

    private void prepareListOfProducts() {
        productsListView = (ListView)findViewById(R.id.listView);
//        try{
//            dbManager.getProducts(null, null, newCursor -> {
//                productAdapter = new ProductsCursorAdapter(this, newCursor, R.layout.product_list_item);
//                productsListView.setAdapter(productAdapter);
//            });
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void setListenersForOrderBtns() {
        Button priceBtn = (Button)findViewById(R.id.btn_sort_price);
        Button countBtn = (Button)findViewById(R.id.btn_sort_count);
        Button saveBtn = (Button)findViewById(R.id.btn_sort_save);
        priceBtn.setOnClickListener(v -> {
            currentOrderController = priceOrderController;
            currentOrderController.nextState();
            refreshListViewByCurrentOrder(newCursor -> {
//                productAdapter.changeCursor(newCursor);
//                productAdapter.notifyDataSetChanged();
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
            if (currentFirebaseProcess == null) {
                currentFirebaseProcess = list -> {
                    ArrayList<ProductFirebase> filtered = new ArrayList<>();
                    for (ProductFirebase item: list) {
                        if(item.getSaved()) filtered.add(item);
                    }
                    return  filtered;
                };
                v.setBackgroundColor(getResources().getColor(R.color.colorPicker));
            }
            else {
                currentFirebaseProcess = null;
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
                FireBaseManager.query(orderItem, false, currentFirebaseProcess, fireBaseCompletion);
                break;
            case DOWN:
                FireBaseManager.query(orderItem, true, currentFirebaseProcess, fireBaseCompletion);
                break;
            case UNSELECT:
                FireBaseManager.query("identifier", false, currentFirebaseProcess, fireBaseCompletion);
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
            ProductModel productModel = (ProductModel)createdProduct;
            String id = FireBaseManager.getDbReference().push().getKey();
            productModel.setIdentifier(id);
            ProductFirebase dbModel = productModel.getFirebaseInstance();
            dbModel.setUserEmail(authManager.getUserEmail());
            FireBaseManager.getDbReference().child(id).setValue(dbModel);
            dbManager.insert(productModel, copmletion);
        }

        if (editedProduct != null) {
            ProductModel productModel = (ProductModel)editedProduct;
            FireBaseManager.getDbReference().child(productModel.getIdentifier()).setValue(productModel.getFirebaseInstance());
            dbManager.update(productModel, copmletion);
        }
    }
}

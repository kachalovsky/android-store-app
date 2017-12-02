package fit.bstu.lab_05_06.shared_modules.list_of_items;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.db.firebase.FireBaseManager;
import fit.bstu.lab_05_06.db.firebase.IFireBaseQueryProcess;
import fit.bstu.lab_05_06.db.firebase.IFireBaseQueryResult;
import fit.bstu.lab_05_06.db.sqllite.AsyncTasks.IAsyncReadCompletion;
import fit.bstu.lab_05_06.models.Product.ProductFirebase;
import fit.bstu.lab_05_06.products_list.ProductsListAdapter;
import fit.bstu.lab_05_06.db.sqllite.ProductsCursorAdapter;
import fit.bstu.lab_05_06.db.sqllite.ProductsManager;
import fit.bstu.lab_05_06.models.Product.ProductModel;
import fit.bstu.lab_05_06.shared_modules.drill_down.DrillDownFragment;
import fit.bstu.lab_05_06.shared_modules.order_controller.OrderController;



public class ListOfItems extends Fragment {
    enum OrderType {
        COUNT,
        PRICE
    }

    public void setDelegate(IListOfItemsBehavior delegate) {
        this.delegate = delegate;
    }

    IListOfItemsBehavior delegate;
    Context context;
    ProductsCursorAdapter productAdapter;
    ListView productsListView;
    ProductsManager dbManager;
    OrderController<OrderType> priceOrderController;
    OrderController<OrderType> countOrderController;
    OrderController<OrderType> currentOrderController;
    IFireBaseQueryProcess currentFirebaseProcess = null;
    IFireBaseQueryResult fireBaseCompletion = new IFireBaseQueryResult() {
        @Override
        public void onResult(ArrayList<ProductFirebase> list) {
            productsListView = (ListView)getView().findViewById(R.id.listView);
            ProductsListAdapter adapter;
            if (productsListView.getAdapter() == null) {
                adapter = new ProductsListAdapter(getActivity(), R.layout.product_list_item);
                productsListView.setAdapter(adapter);
            } else adapter = (ProductsListAdapter)productsListView.getAdapter();
            adapter.listOfProductModels = list;
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_list_fragment, null);
        dbManager = new ProductsManager(getActivity());
        priceOrderController = new OrderController(OrderType.PRICE, "Price");
        countOrderController = new OrderController(OrderType.COUNT, "Count");
        currentOrderController = priceOrderController;
        prepareListOfProducts(view);
        setListenersForOrderBtns(view);
        //setTitle("Hello, " + authManager.getUserEmail());
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void prepareListOfProducts(View view) {
        productsListView = (ListView)view.findViewById(R.id.listView);
        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delegate.itemDidSelected(((ProductsListAdapter)productsListView.getAdapter()).listOfProductModels.get(position));
            }
        });
        productsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        productsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                ProductsListAdapter adapter = (ProductsListAdapter) productsListView.getAdapter();
                if (checked) adapter.selectedItems.add(adapter.listOfProductModels.get(position).getIdentifier());
                else adapter.selectedItems.remove(adapter.listOfProductModels.get(position).getIdentifier());
                refreshListViewByCurrentOrder(newCursor -> {
//                productAdapter.changeCursor(newCursor);
//                productAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                ProductsListAdapter adapter = (ProductsListAdapter) productsListView.getAdapter();
                switch(item.getItemId()) {
                    case R.id.cab_save:
                        for (String identifier : adapter.selectedItems) {
                            FirebaseDatabase.getInstance().getReference("products").child(identifier).child("saved").setValue(true);
                        }
                        break;
                    case R.id.cab_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setMessage("Do you sure that you want to delete this item(s)? It is PERMANENT operation.")
                                .setTitle("WARNING");

                        builder.setPositiveButton("Cancel", (DialogInterface.OnClickListener) (dialog, id) -> {
                            // User clicked OK button
                        });
                        builder.setNegativeButton("Delete", (DialogInterface.OnClickListener) (dialog, id) -> {
                            for (String identifier : adapter.selectedItems) {
                                FirebaseDatabase.getInstance().getReference("products").child(identifier).removeValue();
                            }
                            adapter.selectedItems.clear();
                        });
                        AlertDialog dialog = builder.create();

                        dialog.show();
                        break;
                }


                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ProductsListAdapter adapter = (ProductsListAdapter) productsListView.getAdapter();
                adapter.selectedItems.clear();
            }
        });
    }

    private void setListenersForOrderBtns(View view) {
        Button priceBtn = (Button)view.findViewById(R.id.btn_sort_price);
        Button countBtn = (Button)view.findViewById(R.id.btn_sort_count);
        Button saveBtn = (Button)view.findViewById(R.id.btn_sort_save);
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
                Button btnPrice = (Button) getView().findViewById(R.id.btn_sort_price);
                btnPrice.setText(currentOrderController.getOrderName());
                break;
            case COUNT:
                refreshListViewByOrder("count", currentOrderController.getState(), completion);
                Button btnCount = (Button) getView().findViewById(R.id.btn_sort_count);
                btnCount.setText(currentOrderController.getOrderName());
                break;
        }
    }
}

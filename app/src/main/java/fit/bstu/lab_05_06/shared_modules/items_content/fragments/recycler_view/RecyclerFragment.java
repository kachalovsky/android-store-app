package fit.bstu.lab_05_06.shared_modules.items_content.fragments.recycler_view;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.db.firebase.FireBaseManager;
import fit.bstu.lab_05_06.db.firebase.IFireBaseQueryProcess;
import fit.bstu.lab_05_06.db.firebase.IFireBaseQueryResult;
import fit.bstu.lab_05_06.shared_modules.items_content.behavior.IItemsContentDelegate;
import fit.bstu.lab_05_06.shared_modules.items_content.fragments.BaseFragment;


public class RecyclerFragment extends BaseFragment {
//    enum OrderType {
//        COUNT,
//        PRICE
//    }
    Context context;
    RecyclerView productsRecyclerView;
    RecyclerViewAdapter adapter;
//    ProductsCursorAdapter productAdapter;
//    ListView productsListView;
//    ProductsManager dbManager;
//    OrderController<OrderType> priceOrderController;
//    OrderController<OrderType> countOrderController;
//    OrderController<OrderType> currentOrderController;
    IFireBaseQueryProcess currentFirebaseProcess = null;
    IFireBaseQueryResult fireBaseCompletion = list -> {
       adapter.setProducts(list);
       adapter.notifyItemRangeInserted(0, list.size());
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
        View view = inflater.inflate(R.layout.activity_main_recycler_fragment, null);
        //dbManager = new ProductsManager(getActivity());
//        priceOrderController = new OrderController(OrderType.PRICE, "Price");
//        countOrderController = new OrderController(OrderType.COUNT, "Count");
//        currentOrderController = priceOrderController;
        prepareListOfProducts(view);
       // setListenersForOrderBtns(view);
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void prepareListOfProducts(View view) {
        productsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new RecyclerViewAdapter(new ArrayList<>());
        productsRecyclerView.setAdapter(adapter);
        FireBaseManager.query("identifier", false, currentFirebaseProcess, fireBaseCompletion);
//        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                delegate.itemDidSelected(((ProductsListAdapter)productsListView.getAdapter()).listOfProductModels.get(position));
//            }
//        });
//        productsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        productsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                ProductsListAdapter adapter = (ProductsListAdapter) productsListView.getAdapter();
//                if (checked) adapter.selectedItems.add(adapter.listOfProductModels.get(position).getIdentifier());
//                else adapter.selectedItems.remove(adapter.listOfProductModels.get(position).getIdentifier());
//                refreshListViewByCurrentOrder(newCursor -> {
////                productAdapter.changeCursor(newCursor);
////                productAdapter.notifyDataSetChanged();
//                });
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                MenuInflater inflater = mode.getMenuInflater();
//                inflater.inflate(R.menu.context_menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                ProductsListAdapter adapter = (ProductsListAdapter) productsListView.getAdapter();
//                switch(item.getItemId()) {
//                    case R.id.cab_save:
//                        for (String identifier : adapter.selectedItems) {
//                            FirebaseDatabase.getInstance().getReference("products").child(identifier).child("saved").setValue(true);
//                        }
//                        break;
//                    case R.id.cab_delete:
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                        builder.setMessage("Do you sure that you want to delete this item(s)? It is PERMANENT operation.")
//                                .setTitle("WARNING");
//
//                        builder.setPositiveButton("Cancel", (DialogInterface.OnClickListener) (dialog, id) -> {
//                            // User clicked OK button
//                        });
//                        builder.setNegativeButton("Delete", (DialogInterface.OnClickListener) (dialog, id) -> {
//                            for (String identifier : adapter.selectedItems) {
//                                FirebaseDatabase.getInstance().getReference("products").child(identifier).removeValue();
//                            }
//                            adapter.selectedItems.clear();
//                        });
//                        AlertDialog dialog = builder.create();
//
//                        dialog.show();
//                        break;
//                }
//
//
//                return true;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
////                ProductsListAdapter adapter = (ProductsListAdapter) productsListView.getAdapter();
////                adapter.selectedItems.clear();
//            }
//        });
    }

//    private void setListenersForOrderBtns(View view) {
//        Button priceBtn = (Button)view.findViewById(R.id.btn_sort_price);
//        Button countBtn = (Button)view.findViewById(R.id.btn_sort_count);
//        Button saveBtn = (Button)view.findViewById(R.id.btn_sort_save);
//        priceBtn.setOnClickListener(v -> {
//            currentOrderController = priceOrderController;
//            currentOrderController.nextState();
//            refreshListViewByCurrentOrder(newCursor -> {
////                productAdapter.changeCursor(newCursor);
////                productAdapter.notifyDataSetChanged();
//            });
//        });
//        countBtn.setOnClickListener(v -> {
//            currentOrderController = countOrderController;
//            currentOrderController.nextState();
//
//        });
//        saveBtn.setOnClickListener(v -> {
//            if (currentFirebaseProcess == null) {
//                currentFirebaseProcess = list -> {
//                    ArrayList<ProductFirebase> filtered = new ArrayList<>();
//                    for (ProductFirebase item: list) {
//                        if(item.getSaved()) filtered.add(item);
//                    }
//                    return  filtered;
//                };
//                v.setBackgroundColor(getResources().getColor(R.color.colorPicker));
//            }
//            else {
//                currentFirebaseProcess = null;
//                v.setBackgroundColor(getResources().getColor(R.color.transparent));
//
//            }
//        });
//    }

//    private void refreshListViewByOrder(String orderItem, OrderController.States state, IAsyncReadCompletion completion) {
//        switch (state) {
//            case UP:
//                FireBaseManager.query(orderItem, false, currentFirebaseProcess, fireBaseCompletion);
//                break;
//            case DOWN:
//                FireBaseManager.query(orderItem, true, currentFirebaseProcess, fireBaseCompletion);
//                break;
//            case UNSELECT:
//                FireBaseManager.query("identifier", false, currentFirebaseProcess, fireBaseCompletion);
//                break;
//        }
//    }
//
//    public void refreshListViewByCurrentOrder(IAsyncReadCompletion completion) {
//        switch (currentOrderController.getOrderType()) {
//            case PRICE:
//                refreshListViewByOrder("price", currentOrderController.getState(), completion);
//                Button btnPrice = (Button) getView().findViewById(R.id.btn_sort_price);
//                btnPrice.setText(currentOrderController.getOrderName());
//                break;
//            case COUNT:
//                refreshListViewByOrder("count", currentOrderController.getState(), completion);
//                Button btnCount = (Button) getView().findViewById(R.id.btn_sort_count);
//                btnCount.setText(currentOrderController.getOrderName());
//                break;
//        }
//    }
}

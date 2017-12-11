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
    Context context;
    RecyclerView productsRecyclerView;
    RecyclerViewAdapter adapter;

    public enum ContentTypes {
        Collection,
        Table
    }

    public void setItemViewId(int itemViewId) {
        this.itemViewId = itemViewId;
    }

    int itemViewId;


    public void setContentType(ContentTypes contentType) {
        this.contentType = contentType;
    }

    ContentTypes contentType = ContentTypes.Collection;

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
        prepareListOfProducts(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void prepareListOfProducts(View view) {
        productsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        switch (contentType) {
            case Collection:
                productsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;
            case Table:
                productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
        }
        adapter = new RecyclerViewAdapter(new ArrayList<>(), delegate, itemViewId);
        productsRecyclerView.setAdapter(adapter);
        FireBaseManager.query("identifier", false, currentFirebaseProcess, fireBaseCompletion);
    }
}

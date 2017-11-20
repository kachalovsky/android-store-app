package fit.bstu.lab_05_06.db.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

//import fit.bstu.lab_05_06.auth.AuthManager;
import fit.bstu.lab_05_06.models.Product.ProductFirebase;

/**
 * Created by andre on 19.11.2017.
 */

public class FireBaseManager {
    public static DatabaseReference getDbReference() {
        return dbReference;
    }

    private static DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("products");//.child(AuthManager.getInstance().getUserEmail());

   public static void query(String orderByKey, Boolean disc, IFireBaseQueryProcess process, IFireBaseQueryResult completion) {
 //      String email = AuthManager.getInstance().getUserEmail();
       Query currentRef = dbReference.orderByChild(orderByKey);
        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ProductFirebase> list = createProductsList(dataSnapshot);
                if (process != null) {
                    list = process.onProcess(list);
                }
                if (disc) {
                    Collections.reverse(list);
                }
                completion.onResult(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static ArrayList<ProductFirebase> createProductsList(DataSnapshot snapshot) {
        ArrayList<ProductFirebase> list = new ArrayList<>();
        for (DataSnapshot children: snapshot.getChildren()) {
            ProductFirebase product = children.getValue(ProductFirebase.class);
            list.add(product);
        }
        return list;
    }
}

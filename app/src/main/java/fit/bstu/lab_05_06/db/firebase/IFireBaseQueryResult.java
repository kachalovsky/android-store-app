package fit.bstu.lab_05_06.db.firebase;

import java.util.ArrayList;

import fit.bstu.lab_05_06.models.Product.ProductFirebase;

/**
 * Created by andre on 19.11.2017.
 */

public interface IFireBaseQueryResult {
    void onResult(ArrayList<ProductFirebase> list);
}

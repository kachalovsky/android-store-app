package fit.bstu.lab_05_06.shared_modules.drill_down;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.models.Product.ProductModel;

/**
 * Created by andre on 01.12.2017.
 */

public class DrillDownController extends AppCompatActivity {
    public static String BUNDLE_ARGUMENT_KEY = "SENDED_PRODUCT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drill_down_activity);
        ProductModel sendedProductModel = (ProductModel) getIntent().getSerializableExtra(BUNDLE_ARGUMENT_KEY);
        if (sendedProductModel != null) {
            FragmentManager fragmentManager = getFragmentManager();
            DrillDownFragment chainItem = new DrillDownFragment();
            chainItem.setProductModel(sendedProductModel);
            FragmentTransaction transManager = fragmentManager.beginTransaction();
            transManager.add(R.id.drill_down_fragment, chainItem);
            transManager.commit();
        }
    }
}

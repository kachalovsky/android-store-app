package fit.bstu.lab_05_06.shared_modules.drill_down;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.models.Product.ProductModel;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;

import static fit.bstu.lab_05_06.shared_modules.chain_of_activities.MainActivityOfChain.UPDATED_KEY;

/**
 * Created by andre on 21.11.2017.
 */

public class DrillDownController extends AppCompatActivity {
    private ProductModel productModel;
    public static String BUNDLE_ARGUMENT_KEY = "SENDED_PRODUCT";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drill_down);
        ProductModel sendedProductModel = (ProductModel) getIntent().getSerializableExtra(BUNDLE_ARGUMENT_KEY);
        if (sendedProductModel != null) {
            productModel = sendedProductModel;
            prepaireData();
        }
    }

    protected void prepaireData() {
        TextView lblName = (TextView)findViewById(R.id.lbl_drill_down_name);
        ImageView coverImg = (ImageView)findViewById(R.id.img_drill_down_cover);
        TextView lblCount = (TextView)findViewById(R.id.lbl_drill_down_count);
        TextView lblPrice = (TextView)findViewById(R.id.lbl_drill_down_price);
        TextView lblDescription = (TextView)findViewById(R.id.lbl_drill_down_description);

        lblName.setText(productModel.getName());
        lblCount.setText("Count: " + productModel.getCount().toString());
        lblPrice.setText(productModel.getPrice().toString() + "$");
        lblDescription.setText(productModel.getDescription());

        if(productModel.getImagePath() != null) {
            coverImg.setImageBitmap(BitmapFactory.decodeFile(productModel.getImagePath()));
        }
    }
}

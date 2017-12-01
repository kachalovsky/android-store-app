package fit.bstu.lab_05_06.shared_modules.drill_down;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DrillDownFragment extends Fragment {
    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    private ProductModel productModel;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       //
        if (productModel != null) {
            View view = inflater.inflate(R.layout.drill_down, null);
            prepaireData(view);
            return view;
        }
        return inflater.inflate(R.layout.drill_down_placeholder, null);
    }

    protected void prepaireData(View view) {
        TextView lblName = (TextView)view.findViewById(R.id.lbl_drill_down_name);
        ImageView coverImg = (ImageView)view.findViewById(R.id.img_drill_down_cover);
        TextView lblCount = (TextView)view.findViewById(R.id.lbl_drill_down_count);
        TextView lblPrice = (TextView)view.findViewById(R.id.lbl_drill_down_price);
        TextView lblDescription = (TextView)view.findViewById(R.id.lbl_drill_down_description);
        lblDescription.setMovementMethod(new ScrollingMovementMethod());
        lblName.setText(productModel.getName());
        lblCount.setText("Count: " + productModel.getCount().toString());
        lblPrice.setText(productModel.getPrice().toString() + "$");
        lblDescription.setText(productModel.getDescription());
        String imgBase64 = productModel.getImagePath();
        if (imgBase64 != null) {
            byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            coverImg.setImageBitmap(decodedByte);
        }
    }
}

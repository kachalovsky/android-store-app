package fit.bstu.lab_05_06.products_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fit.bstu.lab_05_06.MainActivity;
import fit.bstu.lab_05_06.models.Product.ProductFirebase;
import fit.bstu.lab_05_06.models.Product.ProductModel;
import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.MainActivityOfChain;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;

/**
 * Created by andre on 12.10.2017.
 */

public class ProductsListAdapter extends ArrayAdapter<ProductFirebase> {

    private Context context;
    private int itemLayoutResId;

    public ArrayList<ProductFirebase> listOfProductModels = new ArrayList();

    public ProductsListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
        this.itemLayoutResId = resource;
    }

    @Override
    public int getCount() {
        return listOfProductModels.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rowView= inflater.inflate(itemLayoutResId, null, true);
        ProductFirebase productModel = listOfProductModels.get(position);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.product_list_item_title);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.product_list_item_price);
        TextView txtCount = (TextView) rowView.findViewById(R.id.product_list_item_count);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.product_list_item_img);

        Button deleteBtn = (Button) rowView.findViewById(R.id.product_list_item_btn_del);
        Button editBtn = (Button) rowView.findViewById(R.id.product_list_item_btn_edit);
        Button saveBtn = (Button) rowView.findViewById(R.id.product_list_item_btn_save);

        txtTitle.setText(productModel.getName());
        txtPrice.setText(productModel.getPrice().toString());
        txtCount.setText(productModel.getCount().toString());
        saveBtn.setText(productModel.getSaved() ? "Unsave" : "Save");
        if (productModel.getImgPath() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(productModel.getImgPath()));
        }

        saveBtn.setOnClickListener(v -> {
            productModel.setSaved(!productModel.getSaved());
            FirebaseDatabase.getInstance().getReference("products").child(productModel.getIdentifier()).setValue(productModel);
        });

        deleteBtn.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("products").child(productModel.getIdentifier()).removeValue();
        });

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivityOfChain.class);
            intent.putExtra(BaseInputFragment.BUNDLE_ARGUMENT_KEY, ProductModel.newInstance(productModel));
            ((Activity)context).startActivityForResult(intent, 0);
        });

        return rowView;

    }
}

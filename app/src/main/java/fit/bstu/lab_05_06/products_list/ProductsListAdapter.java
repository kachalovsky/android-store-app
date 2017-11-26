package fit.bstu.lab_05_06.products_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;

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
    public HashSet<String> selectedItems = new HashSet<>();

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
        ImageView imageChecked = (ImageView) rowView.findViewById(R.id.img_checked);
        ImageView imageSaved = (ImageView) rowView.findViewById(R.id.img_saved);

        ImageButton btnMore = (ImageButton) rowView.findViewById(R.id.btn_more);

        txtTitle.setText(productModel.getName());
        txtPrice.setText(productModel.getPrice().toString() + "$");
        txtCount.setText(productModel.getCount().toString());
        if (productModel.getImgPath() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(productModel.getImgPath()));
        }
        if (!selectedItems.contains(productModel.getIdentifier())) {
            imageChecked.setVisibility(View.INVISIBLE);
        }
        if(!productModel.getSaved()) {
            imageSaved.setVisibility(View.INVISIBLE);
        }

        btnMore.setOnClickListener(v -> {
            PopupMenu popup=new PopupMenu(v.getContext(),v);
            popup.inflate(R.menu.item_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_action_save:
                        productModel.setSaved(!productModel.getSaved());
                        FirebaseDatabase.getInstance().getReference("products").child(productModel.getIdentifier()).setValue(productModel);
                        break;
                    case R.id.item_action_edit:
                        Intent intent = new Intent(context, MainActivityOfChain.class);
                        intent.putExtra(BaseInputFragment.BUNDLE_ARGUMENT_KEY, ProductModel.newInstance(productModel));
                        ((Activity)context).startActivityForResult(intent, 0);
                        break;
                    case R.id.item_action_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setMessage("Do you sure that you want to delete this item(s)? It is PERMANENT operation.")
                                .setTitle("WARNING");

                        builder.setPositiveButton("Cancel", (DialogInterface.OnClickListener) (dialog, id) -> {
                            // User clicked OK button
                        });
                        builder.setNegativeButton("Delete", (DialogInterface.OnClickListener) (dialog, id) -> {
                            FirebaseDatabase.getInstance().getReference("products").child(productModel.getIdentifier()).removeValue();

                        });
                        AlertDialog dialog = builder.create();

                        dialog.show();
                        break;
                }
                return true;
            });
            popup.getMenu().findItem(R.id.item_action_save).setTitle(productModel.getSaved() ? "Unsave" : "Save");
            popup.show();
        });
        return rowView;

    }
}

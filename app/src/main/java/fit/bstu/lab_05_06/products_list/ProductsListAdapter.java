package fit.bstu.lab_05_06.products_list;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fit.bstu.lab_05_06.Product;
import fit.bstu.lab_05_06.R;

/**
 * Created by andre on 12.10.2017.
 */

public class ProductsListAdapter extends ArrayAdapter<Product> {

    private Context context;
    private int itemLayoutResId;

    public ArrayList<Product> listOfProducts = new ArrayList();

    public ProductsListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
        this.itemLayoutResId = resource;
    }

    @Override
    public int getCount() {
        return listOfProducts.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View rowView= inflater.inflate(itemLayoutResId, null, true);
        Product product = listOfProducts.get(position);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.product_list_item_title);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.product_list_item_price);
        TextView txtCount = (TextView) rowView.findViewById(R.id.product_list_item_count);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.product_list_item_img);
        txtTitle.setText(product.getName());
        txtPrice.setText(product.getPrice().toString());
        txtCount.setText(product.getCount().toString());
        imageView.setImageBitmap(BitmapFactory.decodeFile(product.getImagePath()));
        return rowView;

    }
}

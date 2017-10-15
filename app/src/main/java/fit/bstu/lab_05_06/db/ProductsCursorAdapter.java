package fit.bstu.lab_05_06.db;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.models.Product;

/**
 * Created by andre on 15.10.2017.
 */
public class ProductsCursorAdapter extends CursorAdapter {

    private Context context;
    private int itemLayoutResId;

    public ProductsCursorAdapter(Context context, Cursor cursor, @LayoutRes int resource) {
        super(context, cursor, 0);
        this.context = context;
        this.itemLayoutResId = resource;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(itemLayoutResId, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Extract properties from cursor

        TextView txtTitle = (TextView) view.findViewById(R.id.product_list_item_title);
        TextView txtPrice = (TextView) view.findViewById(R.id.product_list_item_price);
        TextView txtCount = (TextView) view.findViewById(R.id.product_list_item_count);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_list_item_img);

        Product product = Product.newInstance(cursor);

        double price = product.getPrice();
        String priceStr = (price % 1 == 0) ? Integer.toString((int)price) : Double.toString(price);

        String imgPath = product.getImagePath();

        txtTitle.setText(product.getName());
        txtPrice.setText(priceStr + "$");
        txtCount.setText(product.getCount().toString());
        if (imgPath != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
        }
    }
}
package fit.bstu.lab_05_06.db.sqllite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.models.Product.ProductModel;

/**
 * Created by andre on 15.10.2017.
 */
public class ProductsCursorAdapter extends CursorAdapter {

    private Context mainContext;
    private int itemLayoutResId;
    private ProductsManager dbManager;

    public ProductsCursorAdapter(Context context, Cursor cursor, @LayoutRes int resource) {
        
        super(context, cursor, 0);
        dbManager = new ProductsManager(context);
        this.mainContext = context;
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
    public void bindView(View view, final Context context, Cursor cursor) {

        // Extract properties from cursor

        TextView txtTitle = (TextView) view.findViewById(R.id.product_list_item_title);
        TextView txtPrice = (TextView) view.findViewById(R.id.product_list_item_price);
        TextView txtCount = (TextView) view.findViewById(R.id.product_list_item_count);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_list_item_img);

//        Button deleteBtn = (Button) view.findViewById(R.id.product_list_item_btn_del);
//        Button editBtn = (Button) view.findViewById(R.id.product_list_item_btn_edit);
//        Button saveBtn = (Button) view.findViewById(R.id.product_list_item_btn_save);

        ProductModel productModel = ProductModel.newInstance(cursor);

//        saveBtn.setText(productModel.getSaved() ? "Unsave" : "Save");

        final ProductsCursorAdapter adapter = this;

//        saveBtn.setOnClickListener(v -> {
//            productModel.setSaved(!productModel.getSaved());
//            dbManager.update(productModel, () -> {
//                ListOfItems activity = (ListOfItems) mainContext;
//                activity.refreshListViewByCurrentOrder((newCursor) -> {
//                    changeCursor(newCursor);
//                    notifyDataSetChanged();
//                });
//            });
//        });
//
//        deleteBtn.setOnClickListener(v -> {
//            dbManager.delete(productModel, () -> {
//                ListOfItems activity = (ListOfItems) mainContext;
//                activity.refreshListViewByCurrentOrder((newCursor) -> {
//                    changeCursor(newCursor);
//                    notifyDataSetChanged();
//                });
//            });
//        });
//
//        editBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(mainContext, MainActivityOfChain.class);
//            intent.putExtra(BaseInputFragment.BUNDLE_ARGUMENT_KEY, productModel);
//            ((Activity)mainContext).startActivityForResult(intent, 0);
//        });

        double price = productModel.getPrice();
        String priceStr = (price % 1 == 0) ? Integer.toString((int)price) : Double.toString(price);

        String imgPath = productModel.getImagePath();
        txtTitle.setText(productModel.getName());
        txtPrice.setText(priceStr + "$");
        txtCount.setText(productModel.getCount().toString());
        String imgBase64 = productModel.getImgPath();
        if (imgBase64 != null) {
            byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }
    }
}
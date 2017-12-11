package fit.bstu.lab_05_06.shared_modules.items_content.fragments.recycler_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.models.Product.ProductFirebase;
import fit.bstu.lab_05_06.shared_modules.items_content.behavior.IItemsContentDelegate;

/**
 * Created by andre on 05.12.2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PersonViewHolder> {

    IItemsContentDelegate delegate;
    int itemViewId;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;
        TextView lblName;
        TextView lblCount;
        TextView lblPrice;
        public PersonViewHolder(View itemView) {
            super(itemView);
            coverImage = (ImageView) itemView.findViewById(R.id.product_cover);
            lblName = (TextView) itemView.findViewById(R.id.product_name);
            lblCount = (TextView) itemView.findViewById(R.id.product_count);
            lblPrice = (TextView) itemView.findViewById(R.id.product_price);
        }
    }

    public RecyclerViewAdapter(List<ProductFirebase> products, IItemsContentDelegate delegate, int itemViewId) {
        this.products = products;
        this.delegate = delegate;
        this.itemViewId = itemViewId;
    }

    public void setProducts(List<ProductFirebase> products) {
        this.products = products;
    }

    private List<ProductFirebase> products;

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemViewId, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        ProductFirebase product = products.get(position);
        holder.lblCount.setText("Count: " + product.getCount().toString());
        holder.lblPrice.setText(product.getPrice().toString() + "$");
        holder.lblName.setText(product.getName());
        if (product.getImgPath() != null) {
            byte[] decodedString = Base64.decode(product.getImgPath(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.coverImage.setImageBitmap(decodedByte);
        }
        holder.itemView.setOnClickListener(v -> {
            delegate.itemDidSelected(product);
        });
    }

    @Override
    public int getItemCount() {
        if (products == null) return 0;
        return products.size();
    }

}

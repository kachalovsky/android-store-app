package fit.bstu.lab_05_06.models.Product;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.count_fragment.ICountInputItem;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.image_fragment.IImageInputItem;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.name_fragment.INameInputItem;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.price_fragment.IPriceInputItem;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainItem;

/**
 * Created by andre on 07.10.2017.
 */

@IgnoreExtraProperties
public class ProductModel implements IChainItem, INameInputItem, IPriceInputItem, ICountInputItem, IImageInputItem, Serializable {

    private String identifier;
    private String name;
    private String userEmail;
    private Double price;
    private Integer count;
    private Boolean isSaved = false;
    private String imgPath;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String getImagePath() {
        return imgPath;
    }

    @Override
    public void setImagePath(String imgPath) {
        this.imgPath = imgPath;
    }

    public static ProductModel newInstance(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        Double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
        String imgPath = cursor.getString(cursor.getColumnIndexOrThrow("img_path"));
       // long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        boolean isSaved = cursor.getInt(cursor.getColumnIndexOrThrow("is_saved")) != 0;

        ProductModel productModel = new ProductModel();
        productModel.setName(name);
        productModel.setImagePath(imgPath);
        productModel.setCount(count);
        productModel.setPrice(price);
       // productModel.setId(id);
        productModel.setSaved(isSaved);

        return productModel;
    }

    public static ProductModel newInstance(ProductFirebase productFirebase) {

        ProductModel productModel = new ProductModel();
        productModel.setName(productFirebase.getName());
        productModel.setImagePath(productFirebase.getImgPath());
        productModel.setCount(productFirebase.getCount());
        productModel.setPrice(productFirebase.getPrice());
        productModel.setSaved(productFirebase.getSaved());
        productModel.setIdentifier(productFirebase.getIdentifier());
        productModel.setUserEmail(productFirebase.getUserEmail());
        productModel.setDescription(productFirebase.getDescription());
      
        return productModel;
    }

    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("userEmail", userEmail);
        values.put("identifier", identifier);
        values.put("price", price);
        values.put("count", count);
        values.put("img_path", imgPath);
        values.put("is_saved", isSaved ? 1 : 0);
        return values;
    }

    public ProductFirebase getFirebaseInstance() {
        return new ProductFirebase(identifier, name, price, count, isSaved, imgPath, description);
    }

}

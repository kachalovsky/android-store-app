package fit.bstu.lab_05_06;

import java.io.Serializable;

import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.count_fragment.ICountInputItem;
import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.image_fragment.IImageInputItem;
import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.name_fragment.INameInputItem;
import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.price_fragment.IPriceInputItem;
import fit.bstu.lab_05_06.chain_of_activities.interfaces.IChainItem;

/**
 * Created by andre on 07.10.2017.
 */

public class Product implements IChainItem, INameInputItem, IPriceInputItem, ICountInputItem, IImageInputItem, Serializable {
    private String name;
    private Double price;
    private Integer count;
    //private String category;
    private String imgPath;

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
}

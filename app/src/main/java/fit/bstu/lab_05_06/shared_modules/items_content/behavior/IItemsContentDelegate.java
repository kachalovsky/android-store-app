package fit.bstu.lab_05_06.shared_modules.items_content.behavior;

import fit.bstu.lab_05_06.models.Product.ProductFirebase;

/**
 * Created by andre on 01.12.2017.
 */

public interface IItemsContentDelegate {
    void itemDidSelected(ProductFirebase product);
}

package fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.count_fragment;

import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainItem;

/**
 * Created by andre on 08.10.2017.
 */

public interface ICountInputItem extends IChainItem {
    Integer getCount();
    void setCount(Integer count);
}

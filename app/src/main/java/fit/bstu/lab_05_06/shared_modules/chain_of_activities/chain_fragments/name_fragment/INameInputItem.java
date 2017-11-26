package fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.name_fragment;

import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainItem;

/**
 * Created by andre on 08.10.2017.
 */

public interface INameInputItem extends IChainItem {
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String name);

}

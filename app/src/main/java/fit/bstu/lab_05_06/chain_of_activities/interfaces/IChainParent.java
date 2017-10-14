package fit.bstu.lab_05_06.chain_of_activities.interfaces;

/**
 * Created by andre on 09.10.2017.
 */

public interface IChainParent<Type extends  IChainItem> {
    public void dataDidChange(Type item);
    public Type passData();
    public boolean isFragmentCanChange(int previousFragment, int nextFragment, int countOfFragments);
}

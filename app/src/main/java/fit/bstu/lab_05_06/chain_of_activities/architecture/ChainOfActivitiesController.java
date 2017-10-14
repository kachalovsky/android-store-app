package fit.bstu.lab_05_06.chain_of_activities.architecture;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.ArrayList;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.chain_of_activities.interfaces.IChainParent;

/**
 * Created by andre on 28.09.2017.
 */

public final class ChainOfActivitiesController {
    private ArrayList<Fragment> chainItems;
    private int currentIndex = 0;
    private Activity mainActivity;
    private int frameContainerId;

    public ChainOfActivitiesController(Activity activity, int frameContainerId) {
        chainItems = new ArrayList<>();
        this.mainActivity = activity;
        this.frameContainerId = frameContainerId;
    }

    public void appendChainItem (Fragment item) {
        chainItems.add(item);
    }

    public void startChain() {
        Fragment chainItem = chainItems.get(currentIndex);
        FragmentManager fragmentManager = mainActivity.getFragmentManager();
        FragmentTransaction transManager = fragmentManager.beginTransaction();
        transManager.add(frameContainerId, chainItem);
        transManager.commit();
    }

    public void nextItem() {
        IChainParent parentActivity = (IChainParent) mainActivity;
        if (!parentActivity.isFragmentCanChange(currentIndex, currentIndex + 1, chainItems.size())) return;
        currentIndex++;
        Fragment chainItem = chainItems.get(currentIndex);
        FragmentTransaction transaction = getTransactionManager();
        setNextAnimation(transaction);
        setChainItem(transaction, chainItem);
    }


    public void previewItem() {
        IChainParent parentActivity = (IChainParent) mainActivity;
        if (!parentActivity.isFragmentCanChange(currentIndex, currentIndex -1, chainItems.size())) return;
        currentIndex--;
        Fragment chainItem = chainItems.get(currentIndex);
        FragmentTransaction transaction = getTransactionManager();
        setBackAnimation(transaction);
        setChainItem(transaction, chainItem);
    }

    private void setChainItem(FragmentTransaction transaction, Fragment item) {
        transaction.replace(frameContainerId, item);
        transaction.commit();
    }

    private FragmentTransaction getTransactionManager() {
        FragmentManager fragmentManager = mainActivity.getFragmentManager();
        return fragmentManager.beginTransaction();
    }
    private FragmentTransaction setNextAnimation (FragmentTransaction transaction) {
        transaction.setCustomAnimations(R.animator.enter_right, R.animator.leave_left);
        return transaction;
    }
    private FragmentTransaction setBackAnimation (FragmentTransaction transaction) {
        transaction.setCustomAnimations(R.animator.enter_left, R.animator.leave_right);
        return transaction;
    }
}

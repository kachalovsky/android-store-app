package fit.bstu.lab_05_06.chain_of_activities.architecture;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import java.util.ArrayList;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.chain_of_activities.interfaces.IChainItem;

/**
 * Created by andre on 28.09.2017.
 */

public final class ChainOfActivitiesController<Type> {
    private ArrayList<ChainInstance> chainItems;
    private int currentIndex = 0;
    private Type generatingInstance;
    private Activity mainActivity;
    private int frameContainerId;

    public ChainOfActivitiesController(Activity activity, int frameContainerId, Type generatingInstance) {
        chainItems = new ArrayList<>();
        this.generatingInstance = generatingInstance;
        this.mainActivity = activity;
        this.frameContainerId = frameContainerId;
    }

    public void appendChainItem (ChainInstance item) {
        chainItems.add(item);
    }

    public void startChain() {
        ChainInstance chainItem = chainItems.get(currentIndex);
        setChainItem(chainItem);
    }

    private void setChainItem(ChainInstance item) {
        FragmentManager fragmentManager = mainActivity.getFragmentManager();
        FragmentTransaction transManager = fragmentManager.beginTransaction();
        transManager.add(frameContainerId, item.getFragment());
        transManager.commit();
    }
}

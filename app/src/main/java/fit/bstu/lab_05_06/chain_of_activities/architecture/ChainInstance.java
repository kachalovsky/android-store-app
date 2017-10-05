package fit.bstu.lab_05_06.chain_of_activities.architecture;

import android.app.Fragment;

/**
 * Created by andre on 04.10.2017.
 */

public class ChainInstance {
    private int resourceId;
    private Fragment fragment;

    public ChainInstance(int resourceId, Fragment fragment) {
        this.resourceId = resourceId;
        this.fragment = fragment;
    }

    public int getResourceId() {
        return resourceId;
    }

    public Fragment getFragment() {
        return fragment;
    }
}

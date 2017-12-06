package fit.bstu.lab_05_06.shared_modules.items_content.fragments;

import android.app.Fragment;

import fit.bstu.lab_05_06.shared_modules.items_content.behavior.IItemsContentDelegate;

/**
 * Created by andre on 06.12.2017.
 */

public class BaseFragment extends Fragment {
    public void setDelegate(IItemsContentDelegate delegate) {
        this.delegate = delegate;
    }
    protected IItemsContentDelegate delegate;
}

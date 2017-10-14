package fit.bstu.lab_05_06.chain_of_activities.chain_fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;

import fit.bstu.lab_05_06.chain_of_activities.interfaces.IChainParent;

/**
 * Created by andre on 08.10.2017.
 */

public abstract class BaseInputFragment <Type extends Serializable, ParentType extends IChainParent> extends Fragment {
    public static final String BUNDLE_ARGUMENT_KEY = "CHAIN_ITEM_INFO";

    public IChainParent getDelegate() {
        return delegate;
    }

    protected void setDelegate(IChainParent delegate) {
        this.delegate = delegate;
    }

    IChainParent delegate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IChainParent) {
            delegate = (IChainParent) context;
        }
    }

    protected static <Type extends Serializable> Bundle prepareBundle(Type generationItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_ARGUMENT_KEY, generationItem);
        return bundle;
    }
}

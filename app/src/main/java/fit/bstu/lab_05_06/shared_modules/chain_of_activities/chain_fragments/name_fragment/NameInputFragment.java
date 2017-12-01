package fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.name_fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainParent;

/**
 * Created by andre on 04.10.2017.
 */

public class NameInputFragment<Type extends INameInputItem> extends BaseInputFragment {

    public static <Type extends INameInputItem> Fragment newInstance(IChainParent<Type> delegate) {
        NameInputFragment<Type> fragment = new NameInputFragment();
        fragment.setDelegate(delegate);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name_item, null);
        setTextBoxLister (view);
        return  view;
    }

    protected void setTextBoxLister (View view) {
        EditText et = (EditText) view.findViewById(R.id.name_edit);
        EditText descEdit = (EditText) view.findViewById(R.id.lbl_description);
        IChainParent delegate = getDelegate();
        if (delegate == null) return;
        et.setText(((INameInputItem) delegate.passData()).getName());
        et.setOnKeyListener((v, keyCode, event) -> {
            IChainParent delegate1 = getDelegate();
            if (delegate1 == null) return false;
            INameInputItem item = (INameInputItem) delegate1.passData();
            String name = ((EditText) v).getText().toString();
            item.setName(name);
            delegate1.dataDidChange(item);
            return false;
        });
        descEdit.setText(((INameInputItem) delegate.passData()).getDescription());
        descEdit.setOnKeyListener((v, keyCode, event) -> {
            IChainParent delegate1 = getDelegate();
            if (delegate1 == null) return false;
            INameInputItem item = (INameInputItem) delegate1.passData();
            String name = ((EditText) v).getText().toString();
            item.setDescription(name);
            delegate1.dataDidChange(item);
            return false;
        });
    }

}

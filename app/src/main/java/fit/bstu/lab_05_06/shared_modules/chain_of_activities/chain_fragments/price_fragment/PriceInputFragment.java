package fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.price_fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class PriceInputFragment<Type extends IPriceInputItem> extends BaseInputFragment {

    public static <Type extends IPriceInputItem> Fragment newInstance(IChainParent<Type> delegate) {
        PriceInputFragment<Type> fragment = new PriceInputFragment();
        fragment.setDelegate(delegate);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.price_item, null);
        prepareForShowing(v);
        return  v;
    }

    private void prepareForShowing(View v) {
        EditText priceInput = (EditText) v.findViewById(R.id.price_edit);
        IPriceInputItem item = ((IPriceInputItem)getDelegate().passData());
        if (item.getPrice() != null) {
            priceInput.setText(item.getPrice().toString());
        }
        priceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                IChainParent delegate = getDelegate();
                IPriceInputItem item = (IPriceInputItem) delegate.passData();
                Double price = (Double.parseDouble(s.toString()));
                item.setPrice(price);
                delegate.dataDidChange(item);
            }
        });
    }

}

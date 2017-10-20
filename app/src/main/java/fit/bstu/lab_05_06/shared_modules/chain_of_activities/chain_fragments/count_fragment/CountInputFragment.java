package fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.count_fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainParent;

/**
 * Created by andre on 04.10.2017.
 */

public class CountInputFragment<Type extends ICountInputItem> extends BaseInputFragment {

    public static <Type extends ICountInputItem> Fragment newInstance(IChainParent<Type> delegate) {
        CountInputFragment<Type> fragment = new CountInputFragment();
        fragment.setDelegate(delegate);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.count_item, null);
        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(9);
        numberPicker.setMinValue(0);
        setPickerListener(numberPicker);
        return   view;
    }

    private void setPickerListener(NumberPicker numberPicker) {
        Integer count = ((ICountInputItem)getDelegate().passData()).getCount();
        if (count != null) {
            numberPicker.setValue(count);
        }
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                IChainParent delegate = getDelegate();
                ICountInputItem item = (ICountInputItem) delegate.passData();
                item.setCount(newVal);
                delegate.dataDidChange(item);
            }
        });
    }
}

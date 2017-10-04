package fit.bstu.lab_05_06.chain_of_activities.chain_fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.bstu.lab_05_06.R;

/**
 * Created by andre on 04.10.2017.
 */

public class NameInputFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.name_item, null);
    }
}

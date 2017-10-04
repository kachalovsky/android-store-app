package fit.bstu.lab_05_06.chain_of_activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.NameInputFragment;

/**
 * Created by andre on 04.10.2017.
 */

public class MainActivityOfChain extends AppCompatActivity {
    public NameInputFragment testFragment = new NameInputFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chain_main);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, testFragment);
        fragmentTransaction.commit();
    }
}

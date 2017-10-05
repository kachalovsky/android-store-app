package fit.bstu.lab_05_06.chain_of_activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.chain_of_activities.architecture.ChainInstance;
import fit.bstu.lab_05_06.chain_of_activities.architecture.ChainOfActivitiesController;
import fit.bstu.lab_05_06.chain_of_activities.chain_fragments.NameInputFragment;

/**
 * Created by andre on 04.10.2017.
 */

public class MainActivityOfChain extends AppCompatActivity {
    private ChainOfActivitiesController<String> chainController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chain_main);
        chainController = new ChainOfActivitiesController(this, R.id.frameLayout, "Hello");
        chainController.startChain();
    }
}

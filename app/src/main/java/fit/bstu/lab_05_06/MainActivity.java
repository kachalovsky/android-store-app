package fit.bstu.lab_05_06;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fit.bstu.lab_05_06.chain_of_activities.MainActivityOfChain;

public class MainActivity extends AppCompatActivity {

    //private ChainOfActivitiesController<String> chain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View v) {
        Intent intent = new Intent(this, MainActivityOfChain.class);
        startActivity(intent);
    }
}

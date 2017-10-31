package fit.bstu.lab_05_06.shared_modules.chain_of_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.models.Product;
import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.architecture.ChainOfActivitiesController;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.count_fragment.CountInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.image_fragment.ImageInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.name_fragment.NameInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.price_fragment.PriceInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.interfaces.IChainParent;

/**
 * Created by andre on 04.10.2017.
 */

public class MainActivityOfChain extends AppCompatActivity implements IChainParent<Product> {

    public static String CREATED_KEY = "RESULT_CREATED";
    public static String UPDATED_KEY = "RESULT_UPDATED";
    private String currentKey = CREATED_KEY;
    private ChainOfActivitiesController chainController;
    private Product product = new Product();
    private boolean isCancelBehavior = true;
    private boolean isFinishBehavior = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chain_main);
        setListeners();
        Product sendedProduct = (Product) getIntent().getSerializableExtra(BaseInputFragment.BUNDLE_ARGUMENT_KEY);
        if (sendedProduct != null) {product = sendedProduct; currentKey = UPDATED_KEY;}
        chainController = new ChainOfActivitiesController(this, R.id.frameLayout);
        chainController.appendChainItem(NameInputFragment.newInstance(this));
        chainController.appendChainItem(PriceInputFragment.newInstance(this));
        chainController.appendChainItem(CountInputFragment.newInstance(this));
        chainController.appendChainItem(ImageInputFragment.newInstance(this));
        chainController.startChain();
    }


    public void setListeners() {
        Button nextBtn = (Button) findViewById(R.id.next_button);
        nextBtn.setOnClickListener(v -> {
            if (!isFinishBehavior) {
                chainController.nextItem();
            } else {finishWithData();}
        });
        Button backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> {
            if(!isCancelBehavior) {
                setResult(RESULT_CANCELED);
                chainController.previewItem();
            } else {cancelActivity();}
        });
    }

    private void finishWithData() {
        Intent intent = new Intent();
        intent.putExtra(currentKey, product);
        setResult(RESULT_OK, intent);
        cancelActivity();
    }

    private void cancelActivity() {
        finish();
    }

    @Override
    public void dataDidChange(Product item) {
        product = item;
    }

    @Override
    public Product passData() { return product; }

    @Override
    public boolean isFragmentCanChange(int previousFragment, int nextFragment, int countOfFragments) {
        Button nextButton = (Button)findViewById(R.id.next_button);
        Button backButton = (Button)findViewById(R.id.back_button);
        if (nextFragment == countOfFragments -1) {
            nextButton.setText("Finish");
            isFinishBehavior = true;
        } else {nextButton.setText("Next");isFinishBehavior = false;}
        if (nextFragment == 0) {
            backButton.setText("Cancel");
            isCancelBehavior = true;
        } else {backButton.setText("Back"); isCancelBehavior = false;}
        return true;
    }
}

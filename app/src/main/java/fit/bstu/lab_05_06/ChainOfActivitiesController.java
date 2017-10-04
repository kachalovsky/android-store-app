package fit.bstu.lab_05_06;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by andre on 28.09.2017.
 */

public final class ChainOfActivitiesController<Type> {
    private ArrayList<IChainItem<Type>> chainItems;
    private int currentIndex = 0;
    private Type generatingInstance;
    private Context mainContext;

    public ChainOfActivitiesController(Context context, IChainItem firstItem, Type generatingInstance) {
        chainItems = new ArrayList<>();
        chainItems.add(firstItem);
        this.generatingInstance = generatingInstance;
        this.mainContext = context;
    }

    public void appendChainItem (IChainItem item) {
        chainItems.add(item);
    }

    public void startChain() {
        IChainItem chainItem = chainItems.get(currentIndex);
        Context itemContext = chainItem.bind(generatingInstance);
       // Intent intent = new Intent(mainContext, NameInputActivity.class);
       // mainContext.startActivity(intent);
    }
}

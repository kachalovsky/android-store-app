package fit.bstu.lab_05_06.chain_of_activities.interfaces;

import android.content.Context;

/**
 * Created by andre on 28.09.2017.
 */

public interface IChainItem<Type> {
    Context bind(Type instance);
}

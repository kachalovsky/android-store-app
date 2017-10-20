package fit.bstu.lab_05_06.shared_modules.order_controller;

import android.util.ArrayMap;

/**
 * Created by andre on 19.10.2017.
 */

public class OrderController<OrderType> {
    public enum States {
        UNSELECT,
        UP,
        DOWN
    }

    ArrayMap<States, String> symbolsMap = new ArrayMap();

    public OrderType getOrderType() {
        return orderType;
    }

    private OrderType orderType;

    public States getState() {
        return statesQueue[currentStateIndex];
    }

    public String getOrderName() {
        return orderName + symbolsMap.get(getState());
    }

    private States[] statesQueue = {States.UNSELECT, States.UP, States.DOWN};
    private int currentStateIndex = 0;
    private String orderName;

    public OrderController(OrderType orderType, String orderName) {
        this.orderName = orderName;
        this.orderType = orderType;
        symbolsMap.put(States.DOWN, "▼");
        symbolsMap.put(States.UP, "▲");
        symbolsMap.put(States.UNSELECT, "");
    }

    public OrderItem nextState() {
        doStep();
        return new OrderItem(getOrderName(), getState());
    }

    private void doStep() {
        if (currentStateIndex == statesQueue.length -1) currentStateIndex = 0;
        else currentStateIndex++;
    }
}

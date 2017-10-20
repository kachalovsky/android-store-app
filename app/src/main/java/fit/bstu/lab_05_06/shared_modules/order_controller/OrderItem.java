package fit.bstu.lab_05_06.shared_modules.order_controller;

/**
 * Created by andre on 19.10.2017.
 */

public class OrderItem {
    public OrderItem(String orderName, OrderController.States orderState) {
        this.orderName = orderName;
        this.orderState = orderState;
    }

    public String getOrderName() {
        return orderName;
    }

    public OrderController.States getOrderState() {
        return orderState;
    }

    String orderName;
    OrderController.States orderState;

}

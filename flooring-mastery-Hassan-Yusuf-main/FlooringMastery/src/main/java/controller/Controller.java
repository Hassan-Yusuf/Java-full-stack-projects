package controller;

import dao.OrderDao;
import dao.OrderPersistenceException;
import dao.ProductPersistenceException;
import dao.TaxPersistenceException;
import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.OrderService;
import service.OrderValidationException;
import view.View;

import java.math.BigDecimal;
import java.util.List;
@Component
public class Controller {

    private View view;
    private OrderService service;

    @Autowired
    public Controller(View view,OrderService service){
        this.view = view;
        this.service = service;
    }
    public boolean checkValidDate(String date) {
        if(service.checkValidDate(date)) return true;
        else {
            view.displayInvalidDate();
            return false;
        }

    }
    public boolean checkValidFutureDate(String date) {
        if(service.checkValidFutureDate(date))return true;
        else {
            view.displayInvalidFutureDate();
            return false;
        }
    }
    public void displayOrders() throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        String date;
        view.displayOrderBanner();
        while(true) {
            date = view.getDate();
            if(checkValidDate(date)) break;
        }
        List<Order> orderList = service.getOrders(date);
        view.displayOrders(orderList);

    }

    public void addOrder() throws OrderPersistenceException, OrderValidationException, TaxPersistenceException, ProductPersistenceException {
        String date,customerName,state,productType;
        view.displayAddOrderBanner();
        while(true) {
            date = view.getDate();
            if(checkValidFutureDate(date)) break;
        }
        Order order = new Order();
        while(true) {
            customerName = view.getCustomerName();
            if(service.checkValidName(customerName)) break;
            view.displayInvalidName();
        }
        view.displayProducts(service.getProducts());
        while(true) {
            state = view.getState();
            if(service.checkValidState(state)) break;
            view.displayInvalidState();
        }

        while(true) {
            productType = view.getProductType();
            if(service.checkValidProductType(productType)) break;
            view.displayInvalidProductType();
        }

        BigDecimal area = view.getArea();
        order.setCustomerName(customerName);
        order.setState(state);
        order.setProductType(productType);
        order.setArea(area);
        Order displayOrder = service.calculateCostsAndTotal(order);
        String inp = view.displayOrderAddSummary(displayOrder);
        if(inp.equalsIgnoreCase("Y"))service.addOrder(date,order);

    }
    public void displayProducts() throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        view.displayProducts(service.getProducts());
    }
    public void editOrder() throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException, OrderValidationException {
        String date;
        view.displayEditOrderBanner();
        while(true) {
            date = view.getDate();
            if(checkValidDate(date)) break;
        }
        int orderNo = Integer.parseInt(view.getOrderId());
        Order order = service.getOrder(date,orderNo);
        order = view.getUpdatedOrderInfo(order);
        Order displayOrder = service.calculateCostsAndTotal(order);
        String inp = view.displayEditOrderSummary(displayOrder);
        if(inp.equalsIgnoreCase("Y"))service.editOrder(date,orderNo,order);
    }
    public void removeOrder() throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        String date;
        view.displayRemoveOrder();
        while(true) {
            date = view.getDate();
            if(checkValidDate(date)) break;
        }
        int orderNo = Integer.parseInt(view.getOrderId());
        String inp = view.displayRemoveOrderSummary(service.getOrder(date,orderNo));
        if(inp.equalsIgnoreCase("Y"))service.removeOrder(date,orderNo);

    }

    private int getMenuSelection(){
        return view.displayMenu();
    }
    public void runnable(){
        boolean keepGoing = true;
        int menuSelection = 0;
        while(keepGoing){
            menuSelection=getMenuSelection();
            switch(menuSelection){
                case 1:
                    try {
                        displayOrders();
                    } catch (OrderPersistenceException | TaxPersistenceException | ProductPersistenceException e) {
                        view.displayErrorMessage(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        addOrder();
                    } catch (OrderPersistenceException | OrderValidationException | TaxPersistenceException |
                             ProductPersistenceException e) {
                        view.displayErrorMessage(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        editOrder();
                    } catch (OrderPersistenceException | TaxPersistenceException | ProductPersistenceException |
                             OrderValidationException e) {
                        view.displayErrorMessage(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        removeOrder();
                    } catch (OrderPersistenceException | ProductPersistenceException | TaxPersistenceException e) {
                        view.displayErrorMessage(e.getMessage());
                    }
                    break;
                case 5:
                    view.displayExportOrderBanner();
                    view.displayFeatureNotSupported();
                    break;
                case 6:
                    view.displayExitBanner();
                    keepGoing=false;
                    break;
                default:
                    view.displayUnknownCommandBanner();
            }
        }
    }
}

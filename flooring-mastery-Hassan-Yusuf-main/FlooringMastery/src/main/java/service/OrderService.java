package service;

import dao.OrderPersistenceException;
import dao.ProductPersistenceException;
import dao.TaxPersistenceException;
import model.Order;
import model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    Order addOrder(String date, Order order) throws OrderPersistenceException, OrderValidationException, OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    Order getOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    List<Order> getOrders(String date) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    Order editOrder(String date, int orderNo,Order order) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException, OrderValidationException;
    Order removeOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    Order calculateCostsAndTotal(Order order) throws ProductPersistenceException, TaxPersistenceException;


    boolean checkValidFutureDate(String date);

    boolean checkValidDate(String date);

    boolean checkValidName(String customerName);

    boolean checkValidState(String state);

    boolean checkValidProductType(String productType);

    boolean checkValidArea(BigDecimal area);
    List<Product> getProducts() throws ProductPersistenceException;
}

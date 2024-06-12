package dao;

import model.Order;

import java.util.List;

public interface OrderDao {

    Order addOrder(String date, Order order) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    Order getOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    List<Order> getOrders(String date) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    Order editOrder(String date,int orderNo, Order order) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;
    Order removeOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException;


}

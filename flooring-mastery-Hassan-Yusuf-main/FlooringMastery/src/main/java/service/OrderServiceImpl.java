package service;

import dao.*;
import dao.OrderPersistenceException;
import model.Order;
import model.Product;
import model.Tax;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OrderServiceImpl implements  OrderService{

    private ProductDao productDao;
    private OrderDao orderDao;
    private TaxDao taxDao;
@Autowired
    public OrderServiceImpl(ProductDao productDao,OrderDao orderDao,TaxDao taxDao){
        this.productDao = productDao;
        this.orderDao = orderDao;
        this.taxDao = taxDao;
    }
    @Override
    public Order addOrder(String date, Order order) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException, OrderValidationException {
        Order orderWithCalculations = calculateCostsAndTotal(order);
        validateOrder(order);
        return orderDao.addOrder(date,orderWithCalculations);
    }

    @Override
    public Order getOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        if(orderDao.getOrder(date,orderNo) == null) {
            throw new OrderPersistenceException("No order found for date " + date + " with order number " + orderNo);
        }
        return orderDao.getOrder(date,orderNo);
    }

    @Override
    public List<Order> getOrders(String date) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        if(orderDao.getOrders(date) == null || orderDao.getOrders(date).isEmpty()) {
            throw new OrderPersistenceException("No orders found for date " + date);
        }
        return orderDao.getOrders(date);
    }

    @Override
    public Order editOrder(String date, int orderNo, Order order) throws TaxPersistenceException, ProductPersistenceException, OrderPersistenceException, OrderValidationException {
        calculateCostsAndTotal(order);
        validateOrder(order);
        return orderDao.editOrder(date,orderNo,order);
    }

    @Override
    public Order removeOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        return orderDao.removeOrder(date,orderNo);
    }
    @Override
    public Order calculateCostsAndTotal(Order order) throws ProductPersistenceException, TaxPersistenceException {
        Product product = productDao.getProduct(order.getProductType());
        Tax tax = taxDao.getTax(order.getState());
        order.setTaxInfo(tax);
        order.setProduct(product);
        order.setMaterialCost(product.getCostPerSquareFoot().multiply(order.getArea()).setScale(2,RoundingMode.HALF_UP));
        order.setLaborCost(product.getLaborCostPerSquareFoot().multiply(order.getArea()).setScale(2,RoundingMode.HALF_UP));
        BigDecimal taxRate = tax.getTaxRate().divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
        order.setTax(taxRate.multiply(order.getMaterialCost().add(order.getLaborCost())).setScale(2,RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()).setScale(2,RoundingMode.HALF_UP));
        return order;
    }

    public void validateOrder(Order order) throws OrderValidationException { //These need to be here to display to the user
        if(order.getCustomerName()==null || order.getCustomerName().trim().isEmpty()
        || order.getState()==null || order.getState().trim().isEmpty()
        || order.getArea()==null || order.getArea().compareTo(BigDecimal.ZERO)==0
        || order.getProductType()==null
        || order.getMaterialCost().compareTo(BigDecimal.ZERO)==0
        || order.getLaborCost().compareTo(BigDecimal.ZERO)==0
        || order.getTax().compareTo(BigDecimal.ZERO)==0
        || order.getTotal().compareTo(BigDecimal.ZERO)==0
        || order.getProduct()==null|| order.getTaxInfo()==null){
            throw new OrderValidationException("Not all data exists for the order. All data is required for the order to save!");
        }
    }
    @Override
    public boolean checkValidDate(String date){ //Goes through all these checks
        if(date==null || date.trim().isEmpty()){
            return false;
        }
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e){
            return false;

        }
        return true;
    }
    @Override
    public boolean checkValidFutureDate(String date){
        if(!checkValidDate(date)){
            return false;
        }
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return localDate.isAfter(LocalDate.now());
    }

    @Override
    public boolean checkValidName(String name){
        if(name==null || name.trim().isEmpty()){
            return false;
        }
        return name.matches("[a-zA-Z\\s.,]+");
    }
    @Override
    public boolean checkValidState(String state){
        if(state==null || state.trim().isEmpty()){
            return false;
        }
        try {
            Tax tax = taxDao.getTax(state);
            return tax!=null;
        } catch (TaxPersistenceException e) {
            return false;
        }
    }
    @Override
    public boolean checkValidProductType(String productType){
        if(productType==null || productType.trim().isEmpty()){
            return false;
        }
        try {
            Product product = productDao.getProduct(productType);
            return product!=null;
        } catch (ProductPersistenceException e) {
            return false;
        }
    }
    @Override
    public boolean checkValidArea(BigDecimal area){
        if(area==null || area.compareTo(BigDecimal.ZERO)<0 || area.compareTo(BigDecimal.valueOf(100))<0){
            return false;
        }
        return true;
    }
    public List<Product> getProducts() throws ProductPersistenceException {
        return productDao.getProducts();
    }





}

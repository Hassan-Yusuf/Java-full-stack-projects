package dao;

import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
@Component
public class OrderDaoImpl implements OrderDao {
    public static int lastOrderNo = 0;

    private Map<String, List<Order>> ordersByDate = new HashMap<>();

    private static final String DELIMITER = ",";
    private TaxDao taxDao = new TaxDaoImpl();
    private ProductDao productDao = new ProductDaoImpl();
    private final String filePath;

    public OrderDaoImpl() {
        filePath="src/main/resources/Orders/Orders_";
    }
    public OrderDaoImpl(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public List<Order> getOrders(String date) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        //if no orders exist in the date then try to load
        if (!ordersByDate.containsKey(date)) {
            loadOrder(date);
        }
        //otherwise return orders
        return ordersByDate.get(date);
    }

    @Override
    public Order addOrder(String date, Order order) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        try{
            loadOrder(date);
        } catch (OrderPersistenceException ignored) {
        }
        List<Order> orders;
        if (ordersByDate.containsKey(date)) {
            orders = getOrders(date);
            order.setOrderNo(getNextOrderNo(date));
        } else {
            orders = new ArrayList<>();
            order.setOrderNo(1);
        }
        // Adds order to list
        orders.add(order);
        ordersByDate.put(date, orders); //or what if we take this map, then for each we add the information we want then write to the file?
        writeOrder(date);
        // Returns that order for confirmation later
        return order;
    }

    @Override
    public Order getOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        loadOrder(date);
        List<Order> orders = getOrders(date);
        //Uses stream to filter orders to orders with the same order number
        return orders.stream()
                .filter(order -> order.getOrderNo() == orderNo)
                //this collects the order we want as itself (can also use findAll, but this sounds better)
                .findFirst()
                .orElse(null);
    }

    private int getNextOrderNo(String date) {
        int nextOrderNo = 1;
        List<Order> orders = ordersByDate.get(date);
        if (orders != null && !orders.isEmpty()) {
            nextOrderNo = orders.stream()
                    .mapToInt(Order::getOrderNo)
                    .max()
                    .orElse(0) + 1;
        }
        return nextOrderNo;
    }

    @Override
    public Order editOrder(String date, int orderNo, Order order) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        loadOrder(date);
        //Check if map contains the required date key
        Order currentOrder = getOrder(date, orderNo);
        if (currentOrder != null) {
            currentOrder.setCustomerName(order.getCustomerName());
            currentOrder.setState(order.getState());
            currentOrder.setProductType(order.getProductType());
            currentOrder.setArea(order.getArea());
        }
        // Update the list of orders for the given date
        List<Order> orders = ordersByDate.get(date);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNo() == orderNo) {
                orders.set(i, currentOrder);
                break;
            }
        }
        ordersByDate.put(date, orders);
        // Write the updated orders back to the file
        writeOrder(date);
        return currentOrder;
    }



    @Override
    public Order removeOrder(String date, int orderNo) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        loadOrder(date);
        Order order = getOrder(date,orderNo);
        List<Order> orders = getOrders(date);
        orders.remove(order);
        ordersByDate.put(date,orders);
        writeOrder(date);
        return order;
    }

    public void loadOrder(String date) throws OrderPersistenceException, TaxPersistenceException, ProductPersistenceException {
        Scanner scanner;
        String filePath2;
        try{
            filePath2 = filePath+date.replaceAll("/","")+".txt";
            scanner = new Scanner(new BufferedReader(new FileReader(filePath2)));
            }
        //Here we are clarifying the exception further
        catch (FileNotFoundException e) {
            throw new OrderPersistenceException("No orders exist for this date yet!");
        }
        // holds each line
        String currentLine;
        Order currentOrder;
        List<Order> orders = new ArrayList<>();
        scanner.nextLine();
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            lastOrderNo = currentOrder.getOrderNo();
            orders.add(currentOrder);
        }
        ordersByDate.put(date,orders);
        scanner.close();

    }

    public void writeOrder(String date) throws OrderPersistenceException {
        PrintWriter out;
        String filePath2;
        try{
            filePath2= filePath + date.replaceAll("/","")+".txt";
            out = new PrintWriter(new FileWriter(filePath2));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not save order.", e);
        }
        String orderAsText;
        List<Order> orders = ordersByDate.get(date);
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        for(Order order: orders){
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }
        out.close();
    }

    public String marshallOrder(Order order){
        String orderAsText = order.getOrderNo() + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getState() + DELIMITER;
        orderAsText += order.getTaxInfo().getTaxRate() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getProduct().getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal();
        return orderAsText;
    }


    public Order unmarshallOrder(String orderLineInFile) throws ProductPersistenceException, TaxPersistenceException {
        String[] orderTokens = orderLineInFile.split(DELIMITER);
        int orderNumber = Integer.parseInt(orderTokens[0]);
        Order orderFromLine = new Order(orderNumber);
        orderFromLine.setCustomerName(orderTokens[1]);
        orderFromLine.setState(orderTokens[2]);
        orderFromLine.setTaxInfo(taxDao.getTax(orderTokens[2]));
        orderFromLine.setProductType(orderTokens[4]);
        orderFromLine.setProduct(productDao.getProduct(orderTokens[4]));
        orderFromLine.setArea(new BigDecimal(orderTokens[5]));
        orderFromLine.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromLine.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromLine.setTax(new BigDecimal(orderTokens[10]));
        orderFromLine.setTotal(new BigDecimal(orderTokens[11]));
        return orderFromLine;
    }

}

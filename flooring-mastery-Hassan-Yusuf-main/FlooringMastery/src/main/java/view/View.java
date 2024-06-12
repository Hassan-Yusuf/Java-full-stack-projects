package view;

import UserIO.UserIO;
import model.Order;
import model.Product;

import java.math.BigDecimal;
import java.util.List;

public class View {
    private UserIO io;
    public View(UserIO io){
        this.io = io;
    }
    public int displayMenu(){
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");
        return io.readInt("Please select from the above choices.", 1, 6);
    }
    public void displayProducts(List<Product> products){
        for (Product currentProduct : products) {
            String productInfo = String.format("%s\tArea: %s\tCost Per Square Foot: %s\n",
                    currentProduct.getProductType(),currentProduct.getLaborCostPerSquareFoot(),currentProduct.getCostPerSquareFoot());
            io.print(productInfo);
        }
    }
    public void displayOrderBanner(){
        io.print("=== Display Order ===");
    }
    public void displayRemoveOrder(){
        io.print("=== Remove Order ===");
    }
    public void displayEditOrderBanner(){
        io.print("=== Edit Order ===");
    }
    public void displayAddOrderBanner(){
        io.print("=== Add Order ===");
    }
    public void displayExportOrderBanner(){
        io.print("=== Exporting Orders ===");
    }
    public void displayExitBanner(){
        io.print("=== Exiting application ===");
    }
    public void displayErrorMessage(String errorMsg) {
        io.print(errorMsg);
    }
    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }
    public void displayOrders(List<Order> orders){
        for (Order currentOrder : orders) {
            String orderInfo = String.format("Order #%s\tCustomer Name: %s\nState: %s\tProduct Type: %s\nArea: %s\t"+
                    "Material Cost: %s\nLabor Cost: %s\tTax: %s\nTotal: %s",
                    currentOrder.getOrderNo(),currentOrder.getCustomerName(),currentOrder.getState(),currentOrder.getProductType(),
                    currentOrder.getArea(),currentOrder.getMaterialCost(),currentOrder.getLaborCost(),currentOrder.getTax(),currentOrder.getTotal());
            io.print(orderInfo);
        }
        io.readString("Please hit enter to continue.");
    }
    public String getOrderId(){
        return io.readString("Please enter the Order ID.");
    }
    public String getDate(){
        return io.readString("Please enter the date. (DD/MM/YYYY)");
    }
    public String getCustomerName(){
        return io.readString("Please enter the customer name.");
    }
    public String getState(){
        return io.readString("Please enter the state.");
    }
    public String getProductType(){
        return io.readString("Please enter the product type.");
    }
    public BigDecimal getArea(){
        return io.readCurrencyValue("Please enter the area.");
    }

    public String displayOrderAddSummary(Order order){
        String orderInfo = String.format("Order No: %s\tCustomer Name: %s\nState: %s\tProduct Type: %s\nArea: %s\t"+
                "Material Cost: %s\nLabor Cost: %s\tTax: %s\nTotal: %s",
                order.getOrderNo(),order.getCustomerName(),order.getState(),order.getProductType(),
                order.getArea(),order.getMaterialCost(),order.getLaborCost(),order.getTax(),order.getTotal());
        io.print(orderInfo);
        while(true) {
            String inp = io.readString("Would you like to confirm? Y/N");
            if(inp.equalsIgnoreCase("Y") || inp.equalsIgnoreCase("N")) return inp;
        }

    }

    public String displayRemoveOrderSummary(Order order) {
        String orderInfo = String.format("Order No: %s\tCustomer Name: %s\nState: %s\tProduct Type: %s\nArea: %s\t"+
                "Material Cost: %s\nLabor Cost: %s\tTax: %s\nTotal: %s",
                order.getOrderNo(),order.getCustomerName(),order.getState(),order.getProductType(),
                order.getArea(),order.getMaterialCost(),order.getLaborCost(),order.getTax(),order.getTotal());
        io.print(orderInfo);
        while(true) {
            String inp = io.readString("Would you like to confirm? Y/N");
            if(inp.equalsIgnoreCase("Y") || inp.equalsIgnoreCase("N")) return inp;
        }
    }

    public String displayEditOrderSummary(Order order) {
        String orderInfo = String.format("Order No: %s\tCustomer Name: %s\nState: %s\tProduct Type: %s\nArea: %s\t"+
                "Material Cost: %s\nLabor Cost: %s\tTax: %s\nTotal: %s",
                order.getOrderNo(),order.getCustomerName(),order.getState(),order.getProductType(),
                order.getArea(),order.getMaterialCost(),order.getLaborCost(),order.getTax(),order.getTotal());
        io.print(orderInfo);
        while(true) {
            String inp = io.readString("Would you like to confirm? Y/N");
            if(inp.equalsIgnoreCase("Y") || inp.equalsIgnoreCase("N")) return inp;
        }
    }

    public Order getUpdatedOrderInfo(Order updatedOrder) {
        String customerName = io.readString("Please enter the customer name.");
        String state = io.readString("Please enter the state abbreviation.");
        String productType = io.readString("Please enter the product type.");
        BigDecimal area = io.readCurrencyValue("Please enter the area.");
        if(!(customerName.isEmpty()))updatedOrder.setCustomerName(customerName);
        if(!(state.isEmpty()))updatedOrder.setState(state);
        if(!(productType.isEmpty()))updatedOrder.setProductType(productType);
        if(!(area.compareTo(BigDecimal.ZERO) == 0))updatedOrder.setArea(area);
        return updatedOrder;
    }

    public void displayInvalidDate() {
        io.print("Invalid date! Please try again.");
    }
    public void displayInvalidState() {
        io.print("This state is not supported yet! Please try again.");
    }
    public void displayInvalidProductType() {
        io.print("This product type is not supported yet! Please try again.");
    }

    public void displayInvalidFutureDate() {
        io.print("This must be a valid date in the future! Please try again.");
    }

    public void displayInvalidName() {
        io.print("Invalid customer name! Names can only include letters, spaces commas and periods. Please try again.");
    }
    public void displayFeatureNotSupported() {
        io.print("This feature is not supported yet! Please try again later.");
    }
}

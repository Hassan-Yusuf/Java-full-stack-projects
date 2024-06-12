package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {
    private int orderNo;
    private String customerName;
    private String state;
    private String productType;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    private Product product;
    private Tax taxInfo;

    public Order(){

    }
    public Order(int orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderNo() {
        return orderNo;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNo == order.orderNo && Objects.equals(customerName, order.customerName) && Objects.equals(state, order.state) && Objects.equals(productType, order.productType) && Objects.equals(area, order.area) && Objects.equals(materialCost, order.materialCost) && Objects.equals(laborCost, order.laborCost) && Objects.equals(tax, order.tax) && Objects.equals(total, order.total) && Objects.equals(product, order.product) && Objects.equals(taxInfo, order.taxInfo);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo=" + orderNo +
                ", customerName='" + customerName + '\'' +
                ", state='" + state + '\'' +
                ", productType='" + productType + '\'' +
                ", area=" + area +
                ", materialCost=" + materialCost +
                ", laborCost=" + laborCost +
                ", tax=" + tax +
                ", total=" + total +
                ", product=" + product +
                ", taxInfo=" + taxInfo +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNo, customerName, state, productType, area, materialCost, laborCost, tax, total, product, taxInfo);
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Tax getTaxInfo() {
        return taxInfo;
    }

    public void setTaxInfo(Tax taxInfo) {
        this.taxInfo = taxInfo;
    }
}

package service;

import dao.*;
import model.Order;
import model.Product;
import model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // Setting this up for each test
        ProductDao productDao = new ProductDaoImpl();
        OrderDao orderDao = new OrderDaoImpl();
        TaxDao taxDao;
        taxDao = new TaxDaoImpl();
        orderService = new OrderServiceImpl(productDao, orderDao, taxDao);
    }

    @Test
    void checkValidDate_ValidDate_ReturnsTrue() {
        assertTrue(orderService.checkValidDate("01/01/2024"));
    }

    @Test
    void checkValidDate_InvalidDate_ReturnsFalse() {
        assertFalse(orderService.checkValidDate("2024/01/01")); // Wrong format
        assertFalse(orderService.checkValidDate("32/01/2024")); // Invalid day
        assertFalse(orderService.checkValidDate("01/13/2024")); // Invalid month
        assertFalse(orderService.checkValidDate("01/01/24")); // Year with two digits
    }

    @Test
    void checkValidFutureDate_FutureDate_ReturnsTrue() {
        assertTrue(orderService.checkValidFutureDate("01/01/2025"));
    }

    @Test
    void checkValidFutureDate_PastDate_ReturnsFalse() {
        assertFalse(orderService.checkValidFutureDate("01/01/2020"));
    }

    @Test
    void checkValidName_ValidName_ReturnsTrue() {
        assertTrue(orderService.checkValidName("John Doe"));
    }

    @Test
    void checkValidName_InvalidName_ReturnsFalse() {
        assertFalse(orderService.checkValidName("123"));
        assertFalse(orderService.checkValidName("!@#$"));
    }

    @Test
    void checkValidState_ValidState_ReturnsTrue() {
        assertTrue(orderService.checkValidState("TX"));
    }

    @Test
    void checkValidState_InvalidState_ReturnsFalse() {
        assertFalse(orderService.checkValidState("ABC")); // Not a valid state code
    }

    @Test
    void checkValidProductType_ValidProductType_ReturnsTrue() {
        assertTrue(orderService.checkValidProductType("Carpet"));
    }

    @Test
    void checkValidProductType_InvalidProductType_ReturnsFalse() {
        assertFalse(orderService.checkValidProductType("InvalidProduct"));
    }

    @Test
    void checkValidArea_ValidArea_ReturnsTrue() {
        assertTrue(orderService.checkValidArea(BigDecimal.valueOf(150)));
    }

    @Test
    void checkValidArea_InvalidArea_ReturnsFalse() {
        assertFalse(orderService.checkValidArea(BigDecimal.valueOf(-10))); // Negative area
        assertFalse(orderService.checkValidArea(BigDecimal.valueOf(50))); // Less than minimum required area
    }
    @Test
    void calculateCosts() throws ProductPersistenceException, TaxPersistenceException {
        // Create a sample order
        Order order = new Order();
        order.setProductType("Carpet");
        order.setArea(BigDecimal.valueOf(100));

        // Retrieve tax information & product information
        Tax tax = new Tax("TX", "Texas", BigDecimal.valueOf(4.45));
        Product product = new Product("Carpet", BigDecimal.valueOf(2.25), BigDecimal.valueOf(2.10));

        // Set tax information in the order
        order.setTaxInfo(tax);
        order.setProduct(product);
        order.setTax(BigDecimal.valueOf(4.45));

        // Calculate costs and total
        Order calculatedOrder = orderService.calculateCostsAndTotal(order);

        // Expected values based on the provided data
        BigDecimal costPerSquareFoot = BigDecimal.valueOf(2.25);
        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(2.10);
        BigDecimal expectedTaxRate = BigDecimal.valueOf(4.45);
        BigDecimal area = BigDecimal.valueOf(100);

        BigDecimal expectedMaterialCost = area.multiply(costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedLaborCost = area.multiply(laborCostPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTax = (expectedMaterialCost.add(expectedLaborCost)).multiply(expectedTaxRate.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedTotal = expectedMaterialCost.add(expectedLaborCost).add(expectedTax).setScale(2, RoundingMode.HALF_UP);

        // Assertions
        assertEquals(expectedMaterialCost, calculatedOrder.getMaterialCost());
        assertEquals(expectedLaborCost, calculatedOrder.getLaborCost());
        assertEquals(expectedTax, calculatedOrder.getTax());
        assertEquals(expectedTotal, calculatedOrder.getTotal());
    }
}
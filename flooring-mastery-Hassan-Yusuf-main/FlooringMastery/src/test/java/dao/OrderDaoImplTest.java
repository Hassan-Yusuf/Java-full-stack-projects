package dao;

import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;

import model.Order;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoImplTest {

    OrderDao testDao;

    public OrderDaoImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        //Arrange
        String testFile = "src/test/resources/Orders/Orders_";
        new FileWriter(testFile + "01012030.txt");
        testDao = new OrderDaoImpl(testFile);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void getOrders() throws Exception {
        // Add some orders for a specific date
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        testDao.addOrder("01012030", order1);
        testDao.addOrder("01012030", order2);

        // Retrieve orders for the same date
        List<Order> orders = testDao.getOrders("01012030");

        // Check the list is not null and contains the added orders
        assertNotNull(orders, "Returned order list should not be null");
        assertEquals(2, orders.size(), "Order list size should be 2");

        // Check if the added orders are present in the retrieved list
        assertTrue(orders.contains(order1), "Order 1 should be present in the list");
        assertTrue(orders.contains(order2), "Order 2 should be present in the list");
    }

    @Test
    void addAndGetOrder() throws Exception {
        //Arrange
        Order order = new Order(1);
        order.setCustomerName("John Doe");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100.00"));
        //Act
        testDao.addOrder("01012030", order);
        Order retrievedOrder = testDao.getOrder("01012030", 1);
        //Assert
        assertEquals(order.getOrderNo(), retrievedOrder.getOrderNo(), "Order numbers should match");
        assertEquals(order.getCustomerName(), retrievedOrder.getCustomerName(), "Customer names should match");
        assertEquals(order.getState(), retrievedOrder.getState(), "States should match");
        assertEquals(order.getProductType(), retrievedOrder.getProductType(), "Product types should match");
        assertEquals(order.getArea(), retrievedOrder.getArea(), "Areas should match");
    }

    @Test
    void editOrder() throws Exception {
        // Arrange
        Order order = new Order(1);
        order.setCustomerName("Jane Doe");
        order.setState("NY");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("200.00"));
        testDao.addOrder("01012030", order);

        // Act
        order.setCustomerName("Alice Smith");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("150.00"));
        testDao.editOrder("01012030", 1, order);

        // Assert
        Order editedOrder = testDao.getOrder("01012030", 1);
        assertNotNull(editedOrder);
        assertEquals("Alice Smith", editedOrder.getCustomerName(), "Customer names should match after editing");
        assertEquals("CA", editedOrder.getState(), "States should match after editing");
        assertEquals("Wood", editedOrder.getProductType(), "Product types should match after editing");
        assertEquals(new BigDecimal("150.00"), editedOrder.getArea(), "Areas should match after editing");
    }

    @Test
    void removeOrder() throws Exception {
        Order order = new Order(1);
        order.setCustomerName("John Doe");
        order.setState("CA");
        order.setProductType("Wood");
        order.setArea(new BigDecimal("100.00"));
        testDao.addOrder("01012030", order);

        // Act
        testDao.removeOrder("01012030", 1);

        // Assert
        assertNull(testDao.getOrder("01012030", 1), "It should say the order does not exist!");
    }

}
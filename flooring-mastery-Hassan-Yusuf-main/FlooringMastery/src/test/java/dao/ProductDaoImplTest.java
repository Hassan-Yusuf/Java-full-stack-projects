package dao;

import model.Product;
import model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoImplTest {

    ProductDao testDao;

    @BeforeEach
    void setUp() {
        testDao = new ProductDaoImpl(); // Actual implementation to be tested
    }

    @Test
    void getProduct() throws Exception{
        Product carpet = testDao.getProduct("Carpet");
        assertEquals("Carpet", carpet.getProductType(), "Product type should be Carpet");
        assertEquals(new BigDecimal("2.25"), carpet.getCostPerSquareFoot(), "Cost per square foot should be 2.25");
        assertEquals(new BigDecimal("2.10"), carpet.getLaborCostPerSquareFoot(), "Labor cost per square foot should be 2.10");
    }

    @Test
    void getProducts() throws Exception{
        List<Product> products = testDao.getProducts();

        assertEquals(4, products.size(), "Number of products should be 4");

        Product wood = products.stream()
                .filter(product -> product.getProductType().equals("Wood"))
                .findFirst()
                .orElse(null);

        assertEquals("Wood", wood.getProductType(), "Product type for Wood should be Wood");
        assertEquals(new BigDecimal("5.15"), wood.getCostPerSquareFoot(), "Cost per square foot for Wood should be 5.15");
        assertEquals(new BigDecimal("4.75"), wood.getLaborCostPerSquareFoot(), "Labor cost per square foot for Wood should be 4.75");
    }
}

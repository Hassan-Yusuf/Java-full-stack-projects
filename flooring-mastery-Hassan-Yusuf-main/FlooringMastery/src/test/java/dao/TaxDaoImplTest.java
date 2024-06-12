package dao;

import model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoImplTest {
    TaxDao testDao;

    @BeforeEach
    void setUp() {
        testDao = new TaxDaoImpl();
    }

    @Test
    void getTax() throws Exception{
        Tax retrievedTax = testDao.getTax("CA");
        assertEquals("California", retrievedTax.getStateName(), "State name should be California");
        assertEquals(new BigDecimal("25.00"), retrievedTax.getTaxRate(), "Tax rate should be 25.00");
    }

    @Test
    void getTaxes() throws Exception {
        List<Tax> taxes = testDao.getTaxes();

        assertEquals(4, taxes.size(), "Number of taxes should be 4");

        Tax californiaTax = taxes.stream()
                .filter(tax -> tax.getStateAbbreviation().equals("CA"))
                .findFirst()
                .orElse(null);

        assertEquals("California", californiaTax.getStateName(), "State name for California should be California");
        assertEquals(new BigDecimal("25.00"), californiaTax.getTaxRate(), "Tax rate for California should be 25.00");
    }
}

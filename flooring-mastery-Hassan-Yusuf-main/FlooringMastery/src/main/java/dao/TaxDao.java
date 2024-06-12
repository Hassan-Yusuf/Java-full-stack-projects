package dao;

import model.Tax;

import java.util.List;

public interface TaxDao {
    Tax getTax(String stateAbbreviation) throws TaxPersistenceException;
    List<Tax> getTaxes() throws TaxPersistenceException;
}

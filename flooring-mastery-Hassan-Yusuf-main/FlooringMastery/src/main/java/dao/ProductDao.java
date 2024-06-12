package dao;

import model.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(String productId) throws ProductPersistenceException;
    List<Product> getProducts() throws ProductPersistenceException;
}

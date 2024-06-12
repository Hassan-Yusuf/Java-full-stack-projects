package dao;

public class ProductPersistenceException extends Exception {

    public ProductPersistenceException(String message) {
        super(message);
    }
    public ProductPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

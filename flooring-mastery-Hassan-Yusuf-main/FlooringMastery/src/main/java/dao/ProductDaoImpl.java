package dao;

import model.Product;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
@Component
public class ProductDaoImpl implements ProductDao {
    Map<String, Product> products = new HashMap<>();
    private static final String DELIMITER = ",";
    private String filePath;

    public ProductDaoImpl() {
        filePath="src/main/resources/Data/products.txt";
    }
    public ProductDaoImpl(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public Product getProduct(String productId) throws ProductPersistenceException {
        loadProducts();
        return products.get(productId);
    }

    @Override
    public List<Product> getProducts() throws ProductPersistenceException {
        loadProducts();
        return new ArrayList(products.values());
    }

    ;

    public void loadProducts() throws ProductPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(filePath)));
        } catch (FileNotFoundException ex) {
            throw new ProductPersistenceException("Could not load products into memory", ex);
        }
        String currentLine;
        Product currentProduct;
        scanner.nextLine();
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }
        scanner.close();
    }
    public Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        Product productInFile = new Product(productTokens[0], new BigDecimal(productTokens[1]), new BigDecimal(productTokens[2]));
        return productInFile;
    }
}




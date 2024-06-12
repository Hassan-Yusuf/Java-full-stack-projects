package dao;

import model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
@Component
public class TaxDaoImpl implements TaxDao{
    Map<String, Tax> taxes = new HashMap<>();
    private static final String DELIMITER = ",";
    private String filePath;

    public TaxDaoImpl() {
        filePath="src/main/resources/Data/Taxes.txt";
    }
    public TaxDaoImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Tax getTax(String stateAbbreviation) throws TaxPersistenceException {
        loadTaxes();
        return taxes.get(stateAbbreviation);
    }

    @Override
    public List<Tax> getTaxes() throws TaxPersistenceException {
        loadTaxes();

        return new ArrayList<>(taxes.values());
    }

    ;

    public void loadTaxes() throws TaxPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(filePath)));
        } catch (FileNotFoundException ex) {
            throw new TaxPersistenceException("Could not load tax file into memory", ex);
        }
        String currentLine;
        Tax currenttax;
        scanner.nextLine();
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currenttax = unmarshalltax(currentLine);
            taxes.put(currenttax.getStateAbbreviation(), currenttax);
        }
        scanner.close();

    }
    public Tax unmarshalltax(String taxAsText) {
        String[] taxTokens = taxAsText.split(DELIMITER);
        Tax taxInFile = new Tax(taxTokens[0], taxTokens[1], new BigDecimal(taxTokens[2]));
        return taxInFile;
    }
}





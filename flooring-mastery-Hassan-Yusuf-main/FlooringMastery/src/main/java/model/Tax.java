package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    private String stateName;
    private BigDecimal taxRate;
    private String stateAbbreviation;

    public Tax(String stateAbbreviation,String stateName, BigDecimal taxRate) {
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = taxRate;
        this.stateName = stateName;
    }
    public Tax(String stateAbbreviation){
        this.stateAbbreviation = stateAbbreviation;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(stateName, tax.stateName) && Objects.equals(taxRate, tax.taxRate) && Objects.equals(stateAbbreviation, tax.stateAbbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateName, taxRate, stateAbbreviation);
    }

    @Override
    public String toString() {
        return "Tax{" +
                "stateName='" + stateName + '\'' +
                ", taxRate=" + taxRate +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                '}';
    }

    public String getStateName() {
        return stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }


}

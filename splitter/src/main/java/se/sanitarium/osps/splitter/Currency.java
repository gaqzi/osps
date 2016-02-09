package se.sanitarium.osps.splitter;

import java.math.BigDecimal;

public class Currency {
  private String name;
  private BigDecimal taxRate;
  private BigDecimal serviceFee;

  public Currency(String name, BigDecimal taxRate, BigDecimal serviceFee) {
    this.name = name;
    this.taxRate = taxRate;
    this.serviceFee = serviceFee;
  }

  public Currency() {
    this.name = "N/A";
    this.taxRate = new BigDecimal("1.0");
    this.serviceFee = new BigDecimal("1.0");
  }

  public BigDecimal getTaxRate() {
    return taxRate;
  }

  public BigDecimal getServiceFee() {
    return serviceFee;
  }

  public String getName() {
    return name;
  }
}

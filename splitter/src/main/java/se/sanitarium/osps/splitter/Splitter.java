package se.sanitarium.osps.splitter;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Splitter {
  private final String input;
  private BigDecimal total;
  private Integer split;
  private Currency currency = new Currency();
  private Extra extra = Extra.NONE;

  public Splitter(String s) {
    this.input = s;
    parse();
  }

  public enum Extra {NONE, TAX, SERVICE_FEE_AND_TAX}

  public BigDecimal split() {
    return getTotal()
        .divide(new BigDecimal(split), BigDecimal.ROUND_CEILING);
  }

  public BigDecimal getTotal() {
    if (extra == Extra.TAX) {
      return total.multiply(currency.getTaxRate());
    } else if (extra == Extra.SERVICE_FEE_AND_TAX) {
      return total
          .multiply(currency.getServiceFee())
          .multiply(currency.getTaxRate());
    }

    return total;
  }

  private void parse() {
    Pattern regex = Pattern.compile(
        "(?<total>\\d+(\\.\\d+)?)" +
            "(?<currency>\\w{2,3})?" +
            "(?<extra>\\+{0,2})?" +
            "/(?<split>\\d{1,2})",
        Pattern.COMMENTS
    );

    Matcher m = regex.matcher(input);
    if (!m.matches()) {
      return;
    }

    this.total = new BigDecimal(m.group("total"));
    this.split = (parseInt(m.group("split")));

    if (m.group("currency") != null) {
      setCurrency(m.group("currency"));
    }

    if (m.group("extra") != null) {
      setExtra(m.group("extra").length());
    }
  }

  private void setCurrency(String currency) {
    if (currency.equals("SGD")) {
      this.currency = new Currency("SGD", new BigDecimal("1.07"), new BigDecimal("1.10"));
    } else {
      this.currency = new Currency();
    }
  }

  private void setExtra(Integer extra) {
    if (extra == 1) {
      this.extra = Extra.TAX;
    } else if (extra == 2) {
      this.extra = Extra.SERVICE_FEE_AND_TAX;
    } else {
      this.extra = Extra.NONE;
    }
  }
}

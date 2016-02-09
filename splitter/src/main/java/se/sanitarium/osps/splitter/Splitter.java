package se.sanitarium.osps.splitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Splitter {
  private final String input;
  private BigDecimal total;
  private Integer split;
  private Currency currency = new Currency();
  private ArrayList<Extra> extra = new ArrayList<>();

  public Splitter(String s) {
    this.input = s;
    parse();
  }

  private enum Extra {
    NONE("1.0"),
    TAX("1.07"),
    SERVICE_FEE_AND_TAX("1.10");

    public final BigDecimal rate;

    Extra(String rate) {
      this.rate = new BigDecimal(rate);
    }
  }

  public BigDecimal split() {
    return getTotal()
        .divide(new BigDecimal(split), BigDecimal.ROUND_CEILING);
  }

  public BigDecimal getTotal() {
    BigDecimal totalWithFees = total;
    for (Extra fee : extra) {
      totalWithFees = totalWithFees.multiply(fee.rate);
    }

    return totalWithFees;
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
    switch (extra) {
      case 2:
        this.extra.add(Extra.SERVICE_FEE_AND_TAX);
        // Fall through
      case 1:
        this.extra.add(Extra.TAX);
        break;
      default:
        this.extra.add(Extra.NONE);
        break;
    }
  }
}

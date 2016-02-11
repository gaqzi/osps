package se.sanitarium.osps.splitter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Splitter extends ResourceSupport {
  private BigDecimal total;
  private BigDecimal totalWithFees;
  private BigDecimal perPerson;
  private ArrayList<Extra> extra = new ArrayList<>();

  @JsonCreator
  public Splitter(BigDecimal total, Integer split, Integer fee) {
    this.total = total;
    this.setExtra(fee);

    setTotalWithFees(total);
    this.perPerson = getTotalWithFees()
        .divide(new BigDecimal(split), BigDecimal.ROUND_CEILING);
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

  public static Splitter parse(String input) {
    Pattern regex = Pattern.compile(
        "(?<total>\\d+(\\.\\d+)?)" +
            "(?<currency>\\w{2,3})?" + // Gobble up if there, but ignore it.
            "(?<extra>\\+{0,2})?" +
            "/(?<split>\\d{1,2})",
        Pattern.COMMENTS
    );

    Matcher m = regex.matcher(input);
    if (!m.matches()) { // TODO: Throw an exception here.
//      throw new IllegalBillString(); // "String '"+ input +"' did not parse properly."
      return new Splitter(new BigDecimal("100"), 4, 0);
    }

    int extras = 0;
    if (m.group("extra") != null) {
      extras = m.group("extra").length();
    }

    return new Splitter(
        new BigDecimal(m.group("total")),
        parseInt(m.group("split")),
        extras
    );
  }

  private void setTotalWithFees(BigDecimal total) {
    this.totalWithFees = total;
    for (Extra fee : extra) {
      this.totalWithFees = totalWithFees.multiply(fee.rate);
    }
  }

  /**
   * All the fees that this bill has. Supports percentage based fees only.
   *
   * @param extra 1 = + (just tax), 2 = ++ (service fee and tax)
   */
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

  @JsonProperty("total")
  public BigDecimal getTotal() {
    return total;
  }

  @JsonProperty("feeAmount")
  public BigDecimal getFees() {
    return getTotalWithFees().subtract(getTotal());
  }

  @JsonProperty("totalWithFees")
  public BigDecimal getTotalWithFees() {
    return totalWithFees;
  }

  @JsonProperty("perPerson")
  public BigDecimal getPerPerson() {
    return perPerson;
  }

  public class IllegalBillString extends Exception {
  }
}

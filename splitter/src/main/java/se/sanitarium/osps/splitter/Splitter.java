package se.sanitarium.osps.splitter;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Splitter {
    private String input;
    private BigDecimal total;
    private Integer split;
    private String currency = "N/A";
    private Integer extra = 0;

    public Splitter(String s) {
        this.input = s;
        parse();
    }

    public BigDecimal split() {
        return getTotal().divide(new BigDecimal(getSplit()), BigDecimal.ROUND_CEILING);
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
        if(m.matches()) {
            this.total = new BigDecimal(m.group("total"));
            setSplit(parseInt(m.group("split")));

            if(m.group("currency") != null) {
                setCurrency(m.group("currency"));
            }

            if(m.group("extra") != null) {
                setExtra(m.group("extra").length());
            }
        }
    }

    public BigDecimal getTotal() {
        if(getExtra() == 1) {
            return total.multiply(new BigDecimal("1.07"));
        } else if(getExtra() == 2) {
            return total
                    .multiply(new BigDecimal("1.10"))
                    .multiply(new BigDecimal("1.07"));
        }

        return total;
    }

    public Integer getSplit() {
        return split;
    }

    private void setSplit(Integer split) {
        this.split = split;
    }

    public String getCurrency() {
        return currency;
    }

    private void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getExtra() {
        return extra;
    }

    private void setExtra(Integer extra) {
        this.extra = extra;
    }
}

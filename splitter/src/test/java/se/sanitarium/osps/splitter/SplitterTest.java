package se.sanitarium.osps.splitter;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.assertThat;


public class SplitterTest {

  @Test
  public void testSplit() {
    assertThat(Splitter.parse("100SGD/5").getPerPerson(),
        is(closeTo(new BigDecimal("20.0"), new BigDecimal("0.1"))));

    assertThat(Splitter.parse("100SGD+/5").getPerPerson(),
        is(closeTo(new BigDecimal("21.4"), new BigDecimal("0.1"))));

    assertThat(Splitter.parse("100SGD++/5").getPerPerson(),
        is(closeTo(new BigDecimal("23.54"), new BigDecimal("0.1"))));
  }

  @Test
  public void testCalculatesTotal() {
    assertThat(
        Splitter.parse("100SGD/4").getTotalWithFees(),
        is(closeTo(new BigDecimal("100.0"), new BigDecimal("0.1")))
    );
  }

  @Test
  public void testConstructorForRest() {
    assertThat(
        new Splitter(new BigDecimal("100"), 4, 0).getPerPerson(),
        is(closeTo(new BigDecimal("25"), new BigDecimal("0.1")))
    );
  }
}

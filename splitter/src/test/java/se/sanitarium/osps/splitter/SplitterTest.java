package se.sanitarium.osps.splitter;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.assertThat;


public class SplitterTest {
    @Test
    public void testSplit() {
        assertThat(new Splitter("100SGD/5").split(),
                is(closeTo(new BigDecimal("20.0"), new BigDecimal("0.1"))));

        assertThat(new Splitter("100SGD+/5").split(),
                is(closeTo(new BigDecimal("21.4"), new BigDecimal("0.1"))));

        assertThat(new Splitter("100SGD++/5").split(),
                is(closeTo(new BigDecimal("23.54"), new BigDecimal("0.1"))));
    }

    @Test
    public void testSetsTotal() {
        Splitter s = new Splitter("100SGD/4");

        assertThat(new BigDecimal("100.0"), is(closeTo(s.getTotal(), new BigDecimal("0.1"))));
    }

    @Test
    public void testSetsSplit() {
        Splitter s = new Splitter("100SGD/4");

        assertThat(s.getSplit(), is(4));
    }

    @Test
    public void testSetsCurrencyWhenAvailable() {
        Splitter s = new Splitter("100SGD/4");

        assertThat(s.getCurrency(), is("SGD"));
    }

    @Test
    public void testDoesNotSetCurrencyWhenNotAvailable() {
        Splitter s = new Splitter("100/4");

        assertThat(s.getCurrency(), is("N/A"));
    }

    @Test
    public void testSetsExtra() {
        assertThat(new Splitter("100SGD/4").getExtra(), is(0));
        assertThat(new Splitter("100SGD+/4").getExtra(), is(1));
        assertThat(new Splitter("100SGD++/4").getExtra(), is(2));
    }
}

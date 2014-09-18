package net.alteridem.tipcalculator.utilites;

import net.alteridem.tipcalculator.AppSettings_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.math.BigDecimal;

/**
 * Calculates tips
 */
@EBean
public class Calculator {
    @Pref
    AppSettings_ _settings;

    /**
     * Calculates the tip on a bill
     * @param bill The amount of the bill. Must be in a double format
     * @param tip The tip 0 to 100
     * @param split The number of people to split the bill between
     */
    public Result calculateTip(String bill, double tip, double split) {
        int precision = precision();

        BigDecimal splitBD = new BigDecimal(split).setScale(precision, BigDecimal.ROUND_HALF_EVEN);
        if (splitBD.intValue() == 0) return null;  // We are not set up yet

        BigDecimal billBD = billAmount(bill, precision);
        BigDecimal percentageBD = new BigDecimal(tip/100f).setScale(precision, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal tipBD = billBD.multiply(percentageBD).setScale(precision, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal tipPerPersonBD = tipBD.divide(splitBD, precision, BigDecimal.ROUND_UP);
        BigDecimal totalBD = billBD.add(tipBD);
        BigDecimal totalPerPersonBD = totalBD.divide(splitBD, precision, BigDecimal.ROUND_UP);

        return new Result(tipBD, tipPerPersonBD, totalBD, totalPerPersonBD);
    }

    public class Result {
        Result(BigDecimal tip, BigDecimal tipPerPerson, BigDecimal total, BigDecimal totalPerPerson) {
            Tip = String.format("%s", tip.toPlainString());
            TipPerPerson = String.format("%s", tipPerPerson.toPlainString());
            Total = String.format("%s", total.toPlainString());
            TotalPerPerson = String.format("%s", totalPerPerson.toPlainString());
        }

        public String Tip;
        public String TipPerPerson;
        public String Total;
        public String TotalPerPerson;
    }

    private int precision() {
        String precStr = _settings.precision().get();
        try {
            return Integer.parseInt(precStr);
        } catch (NumberFormatException nfe) {}
        return 2;
    }

    private BigDecimal billAmount(String bill, int precision) {
        try {
            return new BigDecimal(bill).setScale(precision, BigDecimal.ROUND_UP);
        } catch (NumberFormatException nfe) {}
        return new BigDecimal(0).setScale(precision);
    }
}

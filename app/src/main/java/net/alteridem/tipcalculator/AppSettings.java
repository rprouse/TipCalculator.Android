package net.alteridem.tipcalculator;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Author: Rob Prouse
 * Date: 2014-09-17.
 */
@SharedPref
public interface AppSettings {

    /**
     * The precision in decimals of the currency
     */
    @DefaultString(value = "2", keyRes = R.string.settings_decimal_places_key)
    String precision();

    /**
     * The amount of the bill
     */
    String bill();

    /**
     * The tip percentage
     */
    @DefaultInt(15)
    int tip();

    /**
     * The 0 based number of people. 0 = 1 person.
     */
    int people();
}

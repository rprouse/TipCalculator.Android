package net.alteridem.tipcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Author: Rob.Prouse
 * Date: 2014-09-17.
 */
public class AppSettings {

    private static final String BILL_AMOUNT = "SETTING_BILL_AMOUNT";
    private static final String TIP = "SETTING_TIP";
    private static final String PEOPLE = "SETTING_PEOPLE";

    private SharedPreferences _preferences;
    private Context _context;

    public AppSettings(Context context){
        _context = context;
        _preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * The precision in decimals of the currency
     */
    public int getPrecision(){
        String precStr = _preferences.getString(getKey(R.string.settings_decimal_places_key), "2");
        try {
            return Integer.parseInt(precStr);
        } catch (NumberFormatException nfe) {}
        return 2;
    }

    /**
     * The amount of the bill
     */
    public String getBillAmount() {
        return _preferences.getString(BILL_AMOUNT, "");
    }

    /**
     * The tip percentage
     */
    public int getTipPercent() {
        return _preferences.getInt(TIP, 15);
    }

    /**
     * The 0 based number of people. 0 = 1 person.
     */
    public int getNumberPeople() {
        return _preferences.getInt(PEOPLE, 0);
    }

    /**
     * Saves the data for the TipActivity state
     * @param billAmount The amount of the bill
     * @param tipPercent The tip percentage
     * @param numberPeople The 0 based number of people. 0 = 1 person.
     */
    public void saveState(String billAmount, int tipPercent, int numberPeople) {
        SharedPreferences.Editor edit = _preferences.edit();
        edit.putString(BILL_AMOUNT, billAmount);
        edit.putInt(TIP, tipPercent);
        edit.putInt(PEOPLE, numberPeople);
        edit.apply();
    }

    private String getKey(int stringId) {
        return _context.getString(stringId);
    }
}

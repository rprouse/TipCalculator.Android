package net.alteridem.tipcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.alteridem.tipcalculator.utilites.Calculator;
import net.alteridem.tipcalculator.utilites.PlayStore;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_tip)
@OptionsMenu(R.menu.tip)
public class TipActivity extends Activity {
    private static final String TAG = TipActivity.class.getSimpleName();

    @Pref
    AppSettings_ _settings;

    @Bean
    Calculator _calculator;

    @ViewById(R.id.activity_tip_bill_amount)      TextView _bill;
    @ViewById(R.id.activity_tip_percent)          Spinner _tipSpinner;
    @ViewById(R.id.activity_tip_split)            Spinner _peopleSpinner;
    @ViewById(R.id.activity_tip_tip)              TextView _tip;
    @ViewById(R.id.activity_tip_tip_per_person)   TextView _tipPerPerson;
    @ViewById(R.id.activity_tip_total)            TextView _total;
    @ViewById(R.id.activity_tip_total_per_person) TextView _totalPerPerson;

    @AfterViews
    void initViews() {
        _bill.setText(_settings.bill().get());
        setAdapter(_tipSpinner, createList("%d%%", 0, 25), _settings.tip().get());
        setAdapter(_peopleSpinner, createList("%d", 1, 12), _settings.people().get());
    }

    private List<String> createList(String format, int from, int to) {
        List<String> list = new ArrayList<String>(to-from+1);
        for(int i=from; i<=to; i++) {
            list.add(String.format(format, i));
        }
        return list;
    }

    private void setAdapter(Spinner spinner, List<String> list, int initialSelection) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(initialSelection);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        _settings.edit()
                .bill().put(_bill.getText().toString())
                .tip().put(_tipSpinner.getSelectedItemPosition())
                .people().put(_peopleSpinner.getSelectedItemPosition())
                .apply();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recalculate();
    }

    @OptionsItem(R.id.action_settings)
    void launchSetting() {
        startActivity(new Intent(this, SettingsActivity_.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @OptionsItem(R.id.action_rate)
    void rateApplication() {
        PlayStore.rateApp(this);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @ItemSelect({R.id.activity_tip_percent, R.id.activity_tip_split})
    void onItemSelect(boolean selected, int position) {
        recalculate();
    }

    @AfterTextChange(R.id.activity_tip_bill_amount)
    void recalculate() {
        Calculator.Result result = _calculator.calculateTip(bill(), tip(), split());
        if (result == null) return;

        _tip.setText(result.Tip);
        _tipPerPerson.setText(result.TipPerPerson);
        _total.setText(result.Total);
        _totalPerPerson.setText(result.TotalPerPerson);
    }

    private double tip() { return _tipSpinner.getSelectedItemPosition(); }

    private double split() { return _peopleSpinner.getSelectedItemPosition() + 1; }

    private String bill() { return _bill.getText().toString(); }
}
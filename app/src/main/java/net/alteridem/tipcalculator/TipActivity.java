package net.alteridem.tipcalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.alteridem.tipcalculator.utilites.PlayStore;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_tip)
public class TipActivity extends Activity {
    private static final String TAG = TipActivity.class.getSimpleName();

    private AppSettings _settings;
    private TextView _bill;
    private Spinner _tipPercentSpinner;
    private Spinner _numberPeopleSpinner;
    private TextView _tip;
    private TextView _tipPerPerson;
    private TextView _total;
    private TextView _totalPerPerson;

    @AfterViews
    void initializeLists() {

        _settings = new AppSettings(this);

        String bill = _settings.getBillAmount();
        int tipPercent = _settings.getTipPercent();
        int numberPeople = _settings.getNumberPeople();   // This is the spinner position

        _bill = (TextView)findViewById(R.id.activity_tip_bill_amount);
        _bill.setText(bill);
        _bill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                recalculate();
            }
        });

        List<String> percents_list = new ArrayList<String>(26);
        for(int i=0; i<=25; i++) {
            percents_list.add(String.format("%d%%", i));
        }
        _tipPercentSpinner = getSpinner(R.id.activity_tip_percent, percents_list, tipPercent);

        List<String> split_list = new ArrayList<String>(12);
        for(int i=1; i<=12; i++) {
            split_list.add(String.format("%d", i));
        }
        _numberPeopleSpinner = getSpinner(R.id.activity_tip_split, split_list, numberPeople);

        _tip = (TextView)findViewById(R.id.activity_tip_tip);
        _tipPerPerson = (TextView)findViewById(R.id.activity_tip_tip_per_person);
        _total = (TextView)findViewById(R.id.activity_tip_total);
        _totalPerPerson = (TextView)findViewById(R.id.activity_tip_total_per_person);
    }

    private Spinner getSpinner(int spinnerId, List<String> list, int initialSelection) {
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(initialSelection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recalculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recalculate();
            }
        });
        return spinner;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        _settings.saveState(_bill.getText().toString(),
                _tipPercentSpinner.getSelectedItemPosition(),
                _numberPeopleSpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recalculate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");

        int id = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected");

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_rate:
                PlayStore.rateApp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void recalculate() {
        int precision = _settings.getPrecision();
        BigDecimal bill = billAmount(precision);
        BigDecimal percentage = new BigDecimal(percentage()/100f).setScale(precision, BigDecimal.ROUND_UP);
        BigDecimal split = new BigDecimal(split()).setScale(precision, BigDecimal.ROUND_UP);
        BigDecimal tip = bill.multiply(percentage).setScale(precision, BigDecimal.ROUND_UP);
        BigDecimal tipPerPerson = tip.divide(split, precision, BigDecimal.ROUND_UP);
        BigDecimal total = bill.add(tip);
        BigDecimal totalPerPerson = total.divide(split, precision, BigDecimal.ROUND_UP);

        _tip.setText(String.format("%s", tip.toPlainString()));
        _tipPerPerson.setText(String.format("%s", tipPerPerson.toPlainString()));
        _total.setText(String.format("%s", total.toPlainString()));
        _totalPerPerson.setText(String.format("%s", totalPerPerson.toPlainString()));
    }

    private double percentage() {
        return _tipPercentSpinner.getSelectedItemPosition();
    }

    private double split() {
        return _numberPeopleSpinner.getSelectedItemPosition() + 1;
    }

    private BigDecimal billAmount(int precision) {
        try {
            return new BigDecimal(_bill.getText().toString()).setScale(precision, BigDecimal.ROUND_UP);
        } catch (NumberFormatException nfe) {}
        return new BigDecimal(0).setScale(precision);
    }
}
package net.alteridem.tipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TipActivity extends Activity {

    private TextView _bill;
    private Spinner _percentSpinner;
    private Spinner _splitSpinner;
    private TextView _tip;
    private TextView _tipPerPerson;
    private TextView _total;
    private TextView _totalPerPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        _bill = (TextView)findViewById(R.id.activity_tip_bill_amount);
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
        _percentSpinner = getSpinner(R.id.activity_tip_percent, percents_list, 15);

        List<String> split_list = new ArrayList<String>(12);
        for(int i=1; i<=12; i++) {
            split_list.add(String.format("%d", i));
        }
        _splitSpinner = getSpinner(R.id.activity_tip_split, split_list, 0);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void recalculate() {
        BigDecimal bill = billAmount();
        if (bill == null) {
            _tip.setText("");
            _tipPerPerson.setText("");
            _total.setText("");
            _totalPerPerson.setText("");
            return;
        }

        // TODO: Make the currency precision a setting
        BigDecimal percentage = new BigDecimal(percentage()/100f).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal split = new BigDecimal(split()).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal tip = bill.multiply(percentage).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal tipPerPerson = tip.divide(split, 2, BigDecimal.ROUND_UP);
        BigDecimal total = bill.add(tip);
        BigDecimal totalPerPerson = total.divide(split, 2, BigDecimal.ROUND_UP);

        _tip.setText(String.format("%s", tip.toPlainString()));
        _tipPerPerson.setText(String.format("%s", tipPerPerson.toPlainString()));
        _total.setText(String.format("%s", total.toPlainString()));
        _totalPerPerson.setText(String.format("%s", totalPerPerson.toPlainString()));
    }

    private double percentage() {
        return _percentSpinner.getSelectedItemPosition();
    }

    private double split() {
        return _splitSpinner.getSelectedItemPosition() + 1;
    }

    private BigDecimal billAmount() {
        try {
            BigDecimal amount = new BigDecimal(_bill.getText().toString()).setScale(2, BigDecimal.ROUND_UP);
            return amount;
        } catch (NumberFormatException nfe) {}
        return null;
    }
}
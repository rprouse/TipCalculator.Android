package net.alteridem.tipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class TipActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        List<String> percents_list = new ArrayList<String>(26);
        for(int i=0; i<=25; i++) {
            percents_list.add(String.format("%d%%", i));
        }

        Spinner percents = (Spinner) findViewById(R.id.activity_tip_percent);
        ArrayAdapter<String> percents_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, percents_list);
        percents.setAdapter(percents_adapter);

        List<String> split_list = new ArrayList<String>(12);
        for(int i=1; i<=12; i++) {
            split_list.add(String.format("%d", i));
        }

        Spinner split = (Spinner) findViewById(R.id.activity_tip_split);
        ArrayAdapter<String> split_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, split_list);
        split.setAdapter(split_adapter);
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
}

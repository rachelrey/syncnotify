package com.example.reyhane.syncnotify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;


public class ShowMissedCalls extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ArrayList<Object> tmp = (ArrayList<Object>)intent.getSerializableExtra("missedCalls");
        for (int i = 0; i < tmp.size(); i++){
            String[] newTmp = (String[]) tmp.get(i);
            for (int j = 0; j< 2; j++){
                String text = (newTmp[j]) ;
                TextView textview = new TextView(this);
                textview.setTextSize(40);
                textview.setText(text);
                setContentView(textview);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_missed_calls, menu);
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

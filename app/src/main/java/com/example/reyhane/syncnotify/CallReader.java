package com.example.reyhane.syncnotify;


import android.app.Activity;
import android.database.Cursor;
import android.provider.CallLog;
import android.widget.Toast;
import java.sql.Date;
import java.util.ArrayList;

public class CallReader {


    CallReader(Activity myActivity){
        String[] selection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_FORMATTED_NUMBER ,CallLog.Calls.DATE, CallLog.Calls.TYPE};
        String where = CallLog.Calls.TYPE+"="+CallLog.Calls.MISSED_TYPE;
        String sortOrder = "date DESC";

        myActivity.getBaseContext();
        Cursor c = myActivity.getContentResolver().query(CallLog.Calls.CONTENT_URI, selection,where, null, sortOrder+ " LIMIT 10");
        int name = c.getColumnIndex( CallLog.Calls.CACHED_NAME);
        int number =  c.getColumnIndex( CallLog.Calls.CACHED_FORMATTED_NUMBER);
        long date = c.getColumnIndex(CallLog.Calls.DATE);

        ArrayList<Object> missedCalls = new ArrayList<>();

        if (c != null){
            while (c.moveToNext()){
                String Name = c.getString(name);
                String PhoneNumber = c.getString(number);
                Long newDate = c.getLong((int) date);
                String[] calls = new String[3];
                calls[0]=Name;
                calls[1]=PhoneNumber;
                Date d = new Date(newDate);
                calls[2]= String.valueOf(d);
                missedCalls.add(calls);
            }
        }
        String[] newTmp = (String[]) missedCalls.get(0);

        Toast.makeText(myActivity, "Last missed call from " + newTmp[1]+"at"+ newTmp[2] , Toast.LENGTH_LONG).show();
    }
}

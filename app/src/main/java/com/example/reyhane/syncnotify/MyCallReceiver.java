package com.example.reyhane.syncnotify;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.util.Date;

public class MyCallReceiver extends BroadcastReceiver {

        static boolean isRinging=false;
        static boolean isReceived=false;
        static String callerPhoneNumber;
        static Date callTime;
        static int checked = 0;
        static int checked_2 = 0;

        @Override
        public void onReceive(Context mContext, Intent intent){
            // Get current phone state
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state == null)
                return;
            //phone is ringing
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                isRinging = true;
                //get caller's number
                Bundle bundle = intent.getExtras();
                callerPhoneNumber = bundle.getString("incoming_number");
                callTime = new Date();
            }

            //phone is received
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                isReceived = true;
            }

            // phone is idle
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                // detect missed call
                if (isRinging && !isReceived && checked == 0) {
                    checked = 1;
                    String[] params = new String[2];
                    params[0] = callerPhoneNumber;
                    params[1] = callTime.toString();

                    new HttpAsyncTask(mContext, HttpAsyncTask.TYPE_CALLS_DATA).execute(params);

                    Toast.makeText(mContext, "Got a missed call from : "+callerPhoneNumber + " at " +callTime, Toast.LENGTH_LONG).show();
                }
                if (checked_2 == 1)
                {
                    checked = 0;
                    checked_2 = 0;
                }
            }
        }
    }
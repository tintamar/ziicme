package org.tselex.ziicme;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ZSMSReceiver extends BroadcastReceiver {
    private static final String TAG = "ZSMSReceiver --->";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    public static final String DESTINATION = "android.intent.action.Zvalidnumer";
    ZJSONvalues jsvl;
    ZSECUREziicme zsecure;
    Context context;
    String number = null;
    String simserialref = null;
    String simserial = null;
    TelephonyManager tm;
    public ZSMSReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        simserialref = tm.getSimSerialNumber();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                number = currentMessage.getDisplayOriginatingAddress();
                simserial = currentMessage.getDisplayMessageBody();
            }
            if (simserialref.equals(simserial)) {
                Intent intent2 = new Intent(DESTINATION);
                // add infos for the service which file to download and where to store
                intent2.putExtra(GLOBAL_NUMBER, number);
                //context = context.getApplicationContext();
                context.sendBroadcast(intent2);
            }
        }
    }
}

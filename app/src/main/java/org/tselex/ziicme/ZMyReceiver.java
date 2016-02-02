package org.tselex.ziicme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class ZMyReceiver extends BroadcastReceiver {
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    private static String number;
    public Context context;
    public ZMyReceiver() {
    }

    @Override
    public void onReceive(Context contextr, Intent intentr) {
        context = contextr.getApplicationContext();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        InternetConnectionDetector cd = new InternetConnectionDetector(context);
        if (cd.checkMobileInternetConn()) {
            if (rechsqliteKey(tm.getSimSerialNumber())) {
                Intent intent = new Intent(context, ZMyIntentService.class);
                intent.putExtra(GLOBAL_NUMBER, number);
                context.startService(intent);
            }
        }
    }
    private boolean rechsqliteKey(String key) {
        // recherche sqlite avec SimSerialNumber
        boolean retour = false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(context);
        ZSECUREziicme zsecure = db.selectKeySECUREziicme(key);
        if (!zsecure.getSecretKey().equals("")) {     //indique  trouvé
            number = zsecure.getNumGSM();
            retour =true;
        }
        return retour;
    }
}

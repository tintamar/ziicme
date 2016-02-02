package org.tselex.ziicme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zappa_000 on 18/07/2015.
 */
public class InternetConnectionDetector {

        private Context _context;

        public InternetConnectionDetector(Context context) {
            this._context = context;
        }

        public boolean checkMobileInternetConn() {
            //Create object for ConnectivityManager class which returns network related info
            boolean retour=false;
            ConnectivityManager connectivity = (ConnectivityManager) _context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            //If connectivity object is not null
            if (connectivity != null) {
                //Get network info - WIFI internet access
                NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (info != null) {
                    //Look for whether device is currently connected to WIFI network
                    if (info.isConnected()) {
                        retour= true;
                    }
                    else{
                        info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                        if (info.isConnected()) {
                            retour = true;
                        }
                    }
                }
            }
            return retour;
        }
}


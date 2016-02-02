package org.tselex.ziicme;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ZrechGSMservice extends IntentService {
    private static final String TAG = "Zreceptionservice  ";
    private static final String GLOBAL_ORIGINE = "GLOBAL_ORIGINE";
    private static final String NOMGSM = "NOMGSM";
    private static final String NUM2GSM = "NUM2GSM";
    private static final String NUM3GSM = "NUM3GSM";
    private static final String MAIL1GSM = "MAIL1GSM";
    private static final String MAIL2GSM = "MAIL2GSM";
    private static final String MAIL3GSM = "MAIL3GSM";
    private static final String ADRESSEGSM = "ADRESSEGSM";
    private static final String VILLEGSM = "VILLEGSM";
    private static final String CODPOSTGSM = "CODPOSTGSM";
    private static final String PAYSGSM = "PAYSGSM";
    private static final String PHOTOGSM = "PHOTOGSM";
    private static final String AUDIOGSM = "AUDIOGSM";
    public static final String DESTINATION = "org.tselex.ziicme";
    private static final String urlziicmeGSM = "http://www.tselex.org/ziicme/GestionGSMziicme.php";
    private static String directory;
    private static String host;
    private static String username;
    private static String password;
    private static String operation;
    private static String numberorigine;
    private static String nomorigine;
    private static String num2origine;
    private static String num3origine;
    private static String mail1origine;
    private static String mail2origine;
    private static String mail3origine;
    private static String adresseorigine;
    private static String villeorigine;
    private static String codpostorigine;
    private static String paysorigine;
    private static String photoorigine;
    private static String audioorigine;
    private static Context context;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "org.tselex.ziicme.action.FOO";
    private static final String ACTION_BAZ = "org.tselex.ziicme.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "org.tselex.ziicme.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.tselex.ziicme.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Zreceptionservice.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Zreceptionservice.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ZrechGSMservice() {
        super("Zreceptionservice");
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intento) {
        if (intento != null) {
            context = this.getApplicationContext();
            numberorigine = intento.getStringExtra(GLOBAL_ORIGINE);
            operation = "SELECT";
            if (rechGSMziicme(operation, numberorigine)) {
                if (photoorigine!="") {
                    downloadFTPphototempo(numberorigine);
                    //déchargement fichier photo
                }
                Intent intent = new Intent(DESTINATION);
                // add infos for the service which file to download and where to store
                intent.putExtra(NOMGSM, nomorigine);
                intent.putExtra(NUM2GSM,num2origine);
                intent.putExtra(NUM3GSM,num3origine);
                intent.putExtra(MAIL1GSM,mail1origine);
                intent.putExtra(MAIL2GSM,mail2origine);
                intent.putExtra(MAIL3GSM,mail3origine);
                intent.putExtra(ADRESSEGSM,adresseorigine);
                intent.putExtra(VILLEGSM,villeorigine);
                intent.putExtra(CODPOSTGSM,codpostorigine);
                intent.putExtra(PAYSGSM,paysorigine);
                intent.putExtra(PHOTOGSM,photoorigine);
                intent.putExtra(AUDIOGSM,audioorigine);
                sendBroadcast(intent);
                this.stopSelf();
            }
        }
    }
    private void downloadFTPphototempo(String number){
        host = "ftp.tselex.org";
        username = "tselexorfn";
        password = "xjMxE3G8yaz2";
        directory = "/www/uploads";
        MyFTPClientFunctions ftpclient = new MyFTPClientFunctions();
        boolean status = false;
        status = ftpclient.ftpConnect(host, username, password, 21);
        if (status == true) {
            String msgretour = " 1ere CONNEXION FTP OK ";
            ftpclient.ftpChangeDirectory(directory);
            String fileName1 = number + "photo.bmp";
            String fileName2 = number + "tempophoto.bmp";
            //String storage = Environment.getExternalStorageDirectory().getPath();
            String storage = getFilesDir().getPath();
            File file = new File(storage , fileName2);
            if (file.exists()) file.delete();
            String pathfileName = file.getPath();
            status = ftpclient.ftpDownload(fileName1, pathfileName);
            if (status == true) {
                msgretour += " DOWNLOAD FTP PHOTO OK ";
            } else {
                msgretour += " MAUVAIS DOWNLOAD FTP PHOTO ";
            }
            ftpclient.ftpDisconnect();
        }
    }
    private boolean rechGSMziicme(String operation, String number) {
        boolean retour = false;
        try {
            URL object = new URL(urlziicmeGSM);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject jsonparm = new JSONObject();
            jsonparm.put("operation", operation);
            jsonparm.put("numgsm", number);


            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(jsonparm.toString());
            wr.flush();
            wr.close();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String myJSON = sb.toString();
                JSONObject zonetotale = new JSONObject(myJSON);
                JSONObject zoneoperation = zonetotale.getJSONObject(operation);
                String cdret = zoneoperation.getString("OK");
                if (cdret.equals("1")) {
                    JSONArray zonecorps = zoneoperation.getJSONArray("CORPS");
                    JSONObject zoneinfos = zonecorps.getJSONObject(0);
                    int lignedest = zoneoperation.getInt("ligne");
                    if (lignedest > 0) {
                        retour = true;
                        nomorigine = zoneinfos.getString("nom");
                        num2origine = zoneinfos.getString("num2");
                        num3origine = zoneinfos.getString("num3");
                        mail1origine = zoneinfos.getString("mail1");
                        mail2origine = zoneinfos.getString("mail2");
                        mail3origine = zoneinfos.getString("mail3");
                        adresseorigine = zoneinfos.getString("adresse");
                        codpostorigine = zoneinfos.getString("codpost");
                        villeorigine = zoneinfos.getString("ville");
                        paysorigine = zoneinfos.getString("pays");
                        photoorigine = zoneinfos.getString("photo");
                        audioorigine = zoneinfos.getString("sonnerie");
                    }
                } else {
                    this.stopSelf();
                }
            } else {
                this.stopSelf();
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "0110 UnsupportedEncodingException " + e);
        } catch (JSONException e) {
            Log.e(TAG, "0120 JSONException " + e);
        } catch (IOException e) {
            Log.e(TAG, "0130 IOException " + e);
        }
        return retour;
    }
}




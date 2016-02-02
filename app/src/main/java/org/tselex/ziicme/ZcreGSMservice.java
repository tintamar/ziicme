package org.tselex.ziicme;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
public class ZcreGSMservice extends IntentService {
    private static final String TAG = "ZcreGSMservice";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    private static String operation;
    private static String number;
    private static final String urlziicmeGSM = "http://www.tselex.org/ziicme/GestionGSMziicme.php";

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
        Intent intent = new Intent(context, ZcreGSMservice.class);
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
        Intent intent = new Intent(context, ZcreGSMservice.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ZcreGSMservice() {
        super("ZcreGSMservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            context = this.getApplicationContext();
            number = intent.getStringExtra(GLOBAL_NUMBER);
            operation = "SELECT";
            if (!rechGSMziicme(operation, number)) {
                operation = "INSERT";
                creGSMziicme(operation, number,"",1,"","","","","","","", "", "", "", "");
            }
        }
        this.stopSelf();
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
    private boolean creGSMziicme(String operation,String number,String nom,int autorisation,String num2, String num3,
                                    String mail1,String mail2,String mail3,String adresse,
                                    String ville, String codpost,String pays,String phototitre,
                                    String audiotitre) {
        boolean retour=false;
        try {
            URL object = new URL(urlziicmeGSM);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject jsonparm = new JSONObject();
            jsonparm.put("operation",operation);
            jsonparm.put("numgsm",number);
            jsonparm.put("autorisation",autorisation);
            jsonparm.put("nom", nom);
            jsonparm.put("num2", num2);
            jsonparm.put("num3", num3);
            jsonparm.put("mail1", mail1);
            jsonparm.put("mail2", mail2);
            jsonparm.put("mail3", mail3);
            jsonparm.put("adresse", adresse);
            jsonparm.put("ville", ville);
            jsonparm.put("codpost", codpost);
            jsonparm.put("pays", pays);
            jsonparm.put("photo", phototitre);
            jsonparm.put("sonnerie", audiotitre);

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
                if (cdret.equals("1")){
                    retour=true;
                }else{
                    this.stopSelf();
                }
            }else{
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
}

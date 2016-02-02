package org.tselex.ziicme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;

public class Zreceptionliste extends Activity {
    private static final String TAG = "Zreceptionliste --->";
    ZJSONvalues jsvl;
    ZjsvlCOM1 zcom1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecran_demande_liste);
        jsvl = getIntent().getParcelableExtra("JSVL");
        if(jsvl != null){
            zcom1 = new ZjsvlCOM1();
            zcom1.operation = "SELECT2";
            zcom1.cnumgsmCOM = "" ;
            zcom1.cnumdestCOM = jsvl.GLOBAL_NUMBER;
            zcom1.textdem = "";
            new Comtrt1().execute(zcom1);
        }
    }
    public class Comtrt1 extends AsyncTask<ZjsvlCOM1, Void, String> {
        private String TAG = "Gmstrt1 ---->";
        public Comtrt1() {};
        @Override
        //protected String doInBackground(Void... param)
        protected String doInBackground(ZjsvlCOM1... param) {
            //while(running) {
            ZjsvlCOM1 jsv = param[0];
            String myJSON = "";
            try {
                URL object = new URL(jsv.urlziicmeCOMGSM);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject jsonparm = new JSONObject();
                jsonparm.put("operation", jsv.operation);
                jsonparm.put("numdest", jsv.cnumdestCOM);
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
                    myJSON = sb.toString();
                }
                else{
                    zcom1.textdem = "rechmysqlcom  planté code = "+HttpResult;
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "0110 UnsupportedEncodingException " + e);
            } catch (JSONException e) {
                Log.e(TAG, "0120 JSONException " + e);
            } catch (IOException e) {
                Log.e(TAG, "0130 IOException " + e);
            }
            return myJSON;
        }
        @Override
        protected void onPostExecute(String myJSON) {
            try {
                if (myJSON != null) {
                    zcom1.ligneCOM = 0;
                    JSONObject zonetotale = new JSONObject(myJSON);
                    JSONObject zoneoperation = zonetotale.getJSONObject(zcom1.operation);
                    zcom1.cdret = zoneoperation.getString("OK");
                    if (zcom1.cdret.equals("1")) {
                         switch (zcom1.operation) {
                            case "SELECT2":
                                zcom1.ligneCOM = zoneoperation.getInt("ligne");
                                if (zcom1.ligneCOM > 0) {
                                    JSONArray zonecorps = zoneoperation.getJSONArray("CORPS");

                                    ArrayList<Zcontact> listarticle = new ArrayList<>();

                                    for (int i = 0; i < zonecorps.length(); i++) {
                                        JSONObject zoneinfos = zonecorps.getJSONObject(i);
                                        listarticle.add(new Zcontact(zoneinfos.getString("nom"), zoneinfos.getString("numgsm"),false));

                                    }
                                    ZreceptionAdapter adapter = new ZreceptionAdapter(jsvl.GLOBAL_CONTEXT,listarticle);
                                    //ArrayAdapter<Zcontact> adapter =
                                    //        new ArrayAdapter<Zcontact>(jsvl.GLOBAL_CONTEXT,android.R.layout.simple_list_item_1, listecontact);
                                    // Set The Adapter
                                    ListView demandeList=(ListView)findViewById(R.id.listViewDemande);
                                    demandeList.setAdapter(adapter);
                                    demandeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        //public void onItemClick(AdapterView<?> parent, View view, int position,
                                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                long id) {
                                            TextView num = (TextView)view.findViewById(R.id.numgsm);
                                            jsvl.detdemnumgsm = num.getText().toString();
                                            Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptiondetail.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("JSVL", jsvl);  // tbjs est parcelable donc ok pour mettre dans intent
                                            jsvl.GLOBAL_CONTEXT.startActivity(intent);
                                            finish();
                                            }
                                    });
                                }
                                else{
                                    Log.e(TAG, "0120 ERREUR la table ziicmeCOM ne doit pas être vide");
                                }
                                break;
                            case "INSERT":
                                break;
                            case "UPDATE":
                                break;
                            case "DELETE":
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "0120 JSONException " + e);
            }

        }
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("JSVL", jsvl);
        jsvl.GLOBAL_CONTEXT.startActivity(intent);
        finish();
    }
}

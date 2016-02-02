package org.tselex.ziicme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;

/**
 * Created by zappa_000 on 26/06/2015.
 */
public class MainActivity extends Activity {
    private static final String TAG = "Zmain --->";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    private static final String SIMSERIAL = "SIMSERIAL";
    TelephonyManager tm;
    ZJSONvalues jsvl;
    ZjsvlGMS1 zgms1;
    ZjsvlCOM1 zcom1;
    Context context;
    private boolean CONNon;
    private boolean tant=true;

    //private final ZSECUREziicmeGest db = new ZSECUREziicmeGest(jsvl.GLOBAL_CONTEXT);
    //ZSECUREziicme zsecure = db.selectSECUREziicme(jsvl.GLOBAL_NUMBER,jsvl.GLOBAL_SECRET_KEY);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsvl = getIntent().getParcelableExtra("JSVL");
        if(jsvl != null) {
           if (!rechsqliteKey(jsvl.GLOBAL_SECRET_KEY)) {
                //c'est un bug
                Log.e(TAG, "Etape 0100 ERREUR creation sqlite ");
                finish();
            } else {
                visumenu();
            }
        }
        else{
            initGlobValues();
            if (!rechsqliteKey(jsvl.GLOBAL_SECRET_KEY)) {
                 if (jsvl.GLOBAL_NUMBER.equals("")) {
                     validnumgsm();
                }else {
                    cresqlite(jsvl.GLOBAL_NUMBER, jsvl.GLOBAL_SECRET_KEY, jsvl.GLOBAL_AUTORISATION,
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                    if (jsvl.GLOBAL_CONNEXION){
                        crepreuvechargement();
                    }
                    visumenu();
                }

            }else{
                visumenu();
            }
        }
    }
    private void initGlobValues() {
        jsvl = new ZJSONvalues();
        jsvl.GLOBAL_CONTEXT = getApplicationContext();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        jsvl.GLOBAL_SECRET_KEY = tm.getSimSerialNumber();
        jsvl.GLOBAL_NUMBER = tm.getLine1Number();
        jsvl.GLOBAL_SIM_COUNTRY= tm.getSimCountryIso();
        jsvl.GLOBAL_NOM="";
        InternetConnectionDetector cd = new InternetConnectionDetector(jsvl.GLOBAL_CONTEXT);
        if (!cd.checkMobileInternetConn())jsvl.GLOBAL_CONNEXION=false;
        else jsvl.GLOBAL_CONNEXION=true;
        jsvl.GLOBAL_AUTORISATION=1;

    }

    private void visumenu() {
        if (jsvl.GLOBAL_NOM.equals("")) {
            branchZcartevisite();
        }else{
            foreverturn();
        }


    }
    private boolean rechsqliteKey(String key) {
        // recherche sqlite avec SimSerialNumber
        boolean retour=false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        ZSECUREziicme zsecure = db.selectKeySECUREziicme(key);
        if (!zsecure.getSecretKey().equals("")) {     //indique  trouvé
            jsvl.GLOBAL_NOM = zsecure.getNom();
            jsvl.GLOBAL_NUMBER = zsecure.getNumGSM();
            jsvl.GLOBAL_ID = zsecure.getIdmysql();
            jsvl.audioenvoi = zsecure.getAudioenvoi();
            jsvl.photoenvoi = zsecure.getPhotoenvoi();
            retour =true;
        }
        return retour;
    }

    private boolean  cresqlite(String num,String key,int version,
                               String p1,String p2,String p3,String p4, String p5, String p6, String p7,
                               String p8, String p9, String p10, String p11, String p12, String p13,
                               String p14,String p15,String p16,String p17) {
        boolean retour= false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        retour=db.insertSECUREziicme(num,key,version,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17);
        return retour;
    }

    private void validnumgsm() {
        //Le mobile ne fournit pas son Numero
        //on oblige l'utilisateur à le saisir explicitement
        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zvalidnumero.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("JSVL", jsvl);
        jsvl.GLOBAL_CONTEXT.startActivity(intent);
        finish();
    }
    private void foreverturn(){
        affichecran();
        ImageView[] img = new ImageView[12];
        int j;
        img[0] = (ImageView) findViewById(R.id.imagefr);
        img[1] = (ImageView) findViewById(R.id.imagesu);
        img[2] = (ImageView) findViewById(R.id.imagegb);
        img[3] = (ImageView) findViewById(R.id.imagebe);
        img[4] = (ImageView) findViewById(R.id.imageit);
        img[5] = (ImageView) findViewById(R.id.imagepl);
        img[6] = (ImageView) findViewById(R.id.imagenl);
        img[7] = (ImageView) findViewById(R.id.imagept);
        img[8] = (ImageView) findViewById(R.id.imagede);
        img[9] = (ImageView) findViewById(R.id.imageeu);
        img[10] = (ImageView) findViewById(R.id.imagees);
        img[11] = (ImageView) findViewById(R.id.imagedn);


        j=0;
        img[j].setBackgroundResource(R.drawable.frame_animation_list12);
        AnimationDrawable flag1 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list11);
        AnimationDrawable flag2 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list10);
        AnimationDrawable flag3 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list9);
        AnimationDrawable flag4 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list8);
        AnimationDrawable flag5 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list7);
        AnimationDrawable flag6 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list6);
        AnimationDrawable flag7 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list5);
        AnimationDrawable flag8 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list4);
        AnimationDrawable flag9 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list3);
        AnimationDrawable flag10 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list2);
        AnimationDrawable flag11 = (AnimationDrawable) img[j].getBackground();
        j+=1;
        img[j].setBackgroundResource(R.drawable.frame_animation_list1);
        AnimationDrawable flag12 = (AnimationDrawable) img[j].getBackground();
        flag1.start();
        flag2.start();
        flag3.start();
        flag4.start();
        flag5.start();
        flag6.start();
        flag7.start();
        flag8.start();
        flag9.start();
        flag10.start();
        flag11.start();
        flag12.start();




    }
    private void affichecran(){
        setContentView(R.layout.newecran_menu);
        final Button boutoncv  = (Button) findViewById(R.id.buttoncv);
        final Button boutondem = (Button) findViewById(R.id.buttondem);
        final Button boutondest = (Button) findViewById(R.id.buttondest);
        boutoncv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchZcartevisite();

            }
        });
        boutondem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trtdem();

            }
        });
        boutondest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {trtdest();

            }
        });
    }

    public class Comtrt1 extends AsyncTask<ZjsvlCOM1, Void, String> {
        private String TAG = "Gmstrt1 ---->";
        public Comtrt1() {};
        @Override
        //protected String doInBackground(Void... param)
        protected String doInBackground(ZjsvlCOM1... param) {
            //while(running) {
            ZjsvlCOM1 tempo = param[0];
            String myJSON = "";
            try {
                URL object = new URL(tempo.urlziicmeCOM);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                JSONObject jsonparm = new JSONObject();
                jsonparm.put("operation", tempo.operation);
                jsonparm.put("numdest", tempo.cnumdestCOM);
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
                    Log.e(TAG, "Etape 0170 ERREUR HTTPconnexion ");
                    finish();
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
                       zcom1.ligneCOM = zoneoperation.getInt("ligne");
                        jsvl.detdemnbligne = zcom1.ligneCOM;
                        if (zcom1.ligneCOM == 0) { // pas d'occurence dans ziicmeCOM '
                            String texttoast=getString(R.string.sornothing).toString();
                            ToastAffiche(texttoast);
                        }else{
                            jsvl.nbligne = zcom1.ligneCOM;
                            if (jsvl.nbligne > 1) { //multi occurences dans ziicmeCOM
                                Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptionliste.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("JSVL", jsvl);  // tbjs est parcelable donc ok pour mettre dans intent
                                jsvl.GLOBAL_CONTEXT.startActivity(intent);
                                finish();

                            }else{  // une seule occurence dans ziicmeCOM
                                JSONArray zonecorps = zoneoperation.getJSONArray("CORPS");
                                JSONObject zoneinfos = zonecorps.getJSONObject(0);
                                jsvl.detdemnumgsm = zoneinfos.getString("numgsm");
                                Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptiondetail.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("JSVL", jsvl);  // tbjs est parcelable donc ok pour mettre dans intent
                                jsvl.GLOBAL_CONTEXT.startActivity(intent);
                                finish();
                            }
                        }
                    }else{
                        Log.e(TAG, "Etape 0180 mauvais code retour ");
                        finish();
                    }
                }else{
                    Log.e(TAG, "Etape 0190 ERREUR HTTPconnexion ");
                    finish();
                }
            } catch (JSONException e) {
                Log.e(TAG, "0120 JSONException " + e);
            }
        }
    }
    private void crepreuvechargement() {
        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, ZcreGSMservice.class);
        intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
        jsvl.GLOBAL_CONTEXT.startService(intent);
    }
    private void ToastAffiche(String texttoast) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.ecran_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.texttoast);
        text.setText(texttoast);
        Toast toast = new Toast(getApplicationContext());
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
    private void trtdem(){
        if (jsvl.GLOBAL_NOM.equals("")) {
            String texttoast=getString(R.string.sorfirst).toString();
            ToastAffiche(texttoast);
        } else {
            InternetConnectionDetector cd2 = new InternetConnectionDetector(jsvl.GLOBAL_CONTEXT);
            if (cd2.checkMobileInternetConn()) {
                zcom1 = new ZjsvlCOM1();
                zcom1.operation = jsvl.GLOBAL_SELECT;
                zcom1.cnumdestCOM = jsvl.GLOBAL_NUMBER;
                zcom1.textdem = "";
                new Comtrt1().execute(zcom1);
            }else {
                String texttoast = getString(R.string.sorwifi).toString();
                ToastAffiche(texttoast);
            }
        }
    }
    private void trtdest(){
        if (jsvl.GLOBAL_NOM.equals("")) {
            String texttoast=getString(R.string.sorfirst).toString();
            ToastAffiche(texttoast);
        } else {
            Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zcontactenvoi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("JSVL", jsvl);  // tbjs est parcelable donc ok pour mettre dans intent
            jsvl.GLOBAL_CONTEXT.startActivity(intent);
            finish();
        }
    }
    private void branchZcartevisite(){
        jsvl.travaudiotitre="";
        jsvl.travaudiosrcpath="";
        jsvl.travaudioenvoi="";
        jsvl.travphototitre="";
        jsvl.travphotosrcpath="";
        jsvl.travphotoenvoi="";
        jsvl.travphotobitmap=null;
        jsvl.photobitmap=null;
        jsvl.modifphoto=false;
        jsvl.modifaudio=false;
        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zcartevisite.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("JSVL", jsvl);
        jsvl.GLOBAL_CONTEXT.startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        tant=false;
        jsvl=null;
        finish();
    }

}

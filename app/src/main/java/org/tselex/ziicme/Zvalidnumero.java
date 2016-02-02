package org.tselex.ziicme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class Zvalidnumero extends Activity {
    private static final String TAG = "Zreceptiondetail --->";
    private String nom = "";
    private String num2 = "";
    private String num3 = "";
    private String mail1 = "";
    private String mail2 = "";
    private String mail3 = "";
    private String adresse = "";
    private String codpost = "";
    private String ville = "";
    private String pays = "";
    private String phototitre = "";
    private String photosrcpath = "";
    private String photoenvoi = "";
    private String audiotitre = "";
    private String audiosrcepath = "";
    private String audioenvoi = "";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    private static final String SIMSERIAL = "SIMSERIAL";
    public static final String DESTINATION = "org.tselex.ziicme.Zvalidnumero.class";

    private String globalnumber;
    private static final String CONTENT_SMS = "content://sms";
    private static final int MESSAGE_TYPE_SENT = 2;
    private boolean CONNon;
    private static Bundle bundle;
    private BroadcastReceiver receiver;
    private ProgressDialog myPd;
    private boolean toastaff=true;
    ZJSONvalues jsvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecran_saisie_tel);
        jsvl = getIntent().getParcelableExtra("JSVL");
        if (jsvl != null) {
            Button boutonOK = (Button) findViewById(R.id.buttonok);
            Button boutonOUT = (Button) findViewById(R.id.buttonout);
            final EditText numgsm = (EditText) findViewById(R.id.numgsm);
            boutonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                globalnumber  = numgsm.getText().toString();
                if (!globalnumber.equals("")) {
                    numbergest();
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(globalnumber, null, jsvl.GLOBAL_SECRET_KEY, null, null);
                    myPd=ProgressDialog.show(v.getContext(), "Please wait",
                            "SMS is sending...", true);
                    myPd.setCancelable(false);
                    myPd.show();
                    waitSMS();
                    

                } else {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.ecran_toast,
                                (ViewGroup) findViewById(R.id.toast_layout_root));
                        TextView text = (TextView) layout.findViewById(R.id.texttoast);
                        text.setText(R.string.enternumber);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(layout);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                }
                }
            });
            boutonOUT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentfilter = new IntentFilter("android.intent.action.Zvalidnumer");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (myPd != null) myPd.dismiss();
                toastaff=false;
                jsvl = getIntent().getParcelableExtra("JSVL");
                jsvl.GLOBAL_NUMBER = intent.getStringExtra(GLOBAL_NUMBER);
                cresqlite(jsvl.GLOBAL_NUMBER, jsvl.GLOBAL_SECRET_KEY, jsvl.GLOBAL_AUTORISATION,
                        "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                if (jsvl.GLOBAL_CONNEXION) crepreuvechargement();
                deleteSMS();
                navigation();

            }
        };

        this.registerReceiver(receiver, intentfilter);
    }
        @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    private void waitSMS(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myPd.dismiss();
                if (toastaff) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.ecran_toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));
                    TextView text = (TextView) layout.findViewById(R.id.texttoast);
                    text.setText(R.string.faultnumber);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setView(layout);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        },15000);
    }
    private void deleteSMS(){
        try {
            Uri uriSms = Uri.parse("content://sms");
            Cursor c = jsvl.GLOBAL_CONTEXT.getContentResolver().query(
                    uriSms,
                    new String[] { "_id", "thread_id", "address", "person",
                            "date", "body" }, null, null, null);
            //"date", "body" }, "read=0", null, null);

            if (c != null && c.moveToNext()) {
                do {
                    long id = c.getLong(0);
                    String address = c.getString(2);
                    String body = c.getString(5);
                    ContentValues values = new ContentValues();
                    values.put("read", true);
                    jsvl.GLOBAL_CONTEXT.getContentResolver().update(Uri.parse("content://sms/"),
                            values, "_id=" + id, null);

                    //if (body.equals(jsvl.GLOBAL_SECRET_KEY) && address.equals(jsvl.GLOBAL_NUMBER)) {
                    if (body.equals(jsvl.GLOBAL_SECRET_KEY)) {
                        jsvl.GLOBAL_CONTEXT.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), "date=?",
                                new String[] { c.getString(4) });
                    }
                    else{
                        break;
                    }
                } while (c.moveToNext());
                c.close();
            }
        } catch (Exception e) {
            Log.e("log>>>", e.toString());
        }

    }
    private void crepreuvechargement() {
        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, ZcreGSMservice.class);
        intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
        jsvl.GLOBAL_CONTEXT.startService(intent);
    }
    private void cresqlite(String num, String key, int version, String nom, String num2,
                           String num3, String mail1, String mail2,
                           String mail3, String adresse, String codpost,
                           String ville, String pays, String phototitre, String photosrcpath,
                           String photoenvoi,String audiotitre,
                           String audiosrcpath,String audioenvoi,String idmysql) {
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(jsvl.GLOBAL_CONTEXT);
        if(db.insertSECUREziicme(num, key, version, nom, num2, num3, mail1, mail2, mail3, adresse, codpost,
                ville, pays, phototitre, photosrcpath, photoenvoi,
                audiotitre, audiosrcpath,audioenvoi,idmysql)){
            Log.w(TAG, " insert ok");

        }else{
            Log.w(TAG, " insert planté");
        }
    }


    private void navigation() {
        jsvl.travaudiotitre="";
        jsvl.travaudiosrcpath="";
        jsvl.travaudioenvoi="";
        jsvl.travphototitre="";
        jsvl.travphotosrcpath="";
        jsvl.travphotoenvoi = "";
        jsvl.travphotobitmap=null;
        jsvl.photobitmap=null;
        jsvl.modifphoto=false;
        jsvl.modifaudio=false;
        Intent intento = new Intent(jsvl.GLOBAL_CONTEXT, Zcartevisite.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intento.putExtra("JSVL", jsvl);
        jsvl.GLOBAL_CONTEXT.startActivity(intento);
        finish();
    }
    private void numbergest() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String country = tm.getSimCountryIso();
        Znumerogest zng = new Znumerogest();
        globalnumber= zng.numbergest(country,globalnumber);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}

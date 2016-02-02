package org.tselex.ziicme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class Zreceptiondetail extends Activity {
    private static final String TAG = "Zreceptiondetail --->";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
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
    private static final String SENDBACK = "SENDBACK";
    private static final String NBLIGNE = "NBLIGNE";
    ZJSONvalues jsvl;
    private static String number;
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
    private static Bitmap bitmaporigine;
    private static String sendback;
    private static Bundle bundle;
    private ProgressDialog myPd;



    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            bundle = intent.getExtras();
            // retour tester si on vient de rechGSMservice ou de Zreceptionservice
            if (bundle != null) {
                if (myPd!=null) {
                    myPd.dismiss();
                }
                nomorigine = bundle.getString(NOMGSM);
                num2origine = bundle.getString(NUM2GSM);
                num3origine = bundle.getString(NUM3GSM);
                mail1origine = bundle.getString(MAIL1GSM);
                mail2origine = bundle.getString(MAIL2GSM);
                mail3origine = bundle.getString(MAIL3GSM);
                adresseorigine = bundle.getString(ADRESSEGSM);
                villeorigine = bundle.getString(VILLEGSM);
                codpostorigine = bundle.getString(CODPOSTGSM);
                paysorigine = bundle.getString(PAYSGSM);
                photoorigine = bundle.getString(PHOTOGSM);
                audioorigine = bundle.getString(AUDIOGSM);

                setContentView(R.layout.ecran_demande);
                Button boutonok = (Button) findViewById(R.id.buttonok);
                Button boutonout = (Button) findViewById(R.id.buttonout);
                Button boutonsb = (Button) findViewById(R.id.buttonsb);
                TextView numgsm = (TextView) findViewById(R.id.numgsm);
                TextView nom = (TextView) findViewById(R.id.nom);
                TextView num2 = (TextView) findViewById(R.id.num2);
                TextView num3 = (TextView) findViewById(R.id.num3);
                TextView mail1 = (TextView) findViewById(R.id.mail1);
                TextView mail2 = (TextView) findViewById(R.id.mail2);
                TextView mail3 = (TextView) findViewById(R.id.mail3);
                TextView adresse = (TextView) findViewById(R.id.adresse);
                TextView ville = (TextView) findViewById(R.id.ville);
                TextView codpost = (TextView) findViewById(R.id.codpost);
                TextView pays = (TextView) findViewById(R.id.pays);
                TextView sonnerie = (TextView) findViewById(R.id.sonnerie);
                ImageView portrait = (ImageView) findViewById(R.id.portrait);

                numgsm.setText(getString(R.string.numberaff).toString() + jsvl.detdemnumgsm);
                nom.setText(getString(R.string.nameaff).toString() + nomorigine);
                if (!num2origine.equals("")) {
                    num2.setText(getString(R.string.secnumber).toString() + num2origine);
                }
                if (!num3origine.equals("")) {
                    num3.setText(getString(R.string.thinumber).toString() + num3origine);
                }
                if (!mail1origine.equals("")) {
                    mail1.setText(getString(R.string.mainmail).toString() + mail1origine);
                }
                if (!mail2origine.equals("")) {
                    mail2.setText(getString(R.string.secmail).toString() + mail2origine);
                }
                if (!mail3origine.equals("")) {
                    mail3.setText(getString(R.string.thimail).toString()+ mail3origine);
                }
                if (!adresseorigine.equals("")) {
                    adresse.setText(getString(R.string.address).toString() + adresseorigine);
                }
                if (!villeorigine.equals("")) {
                    ville.setText(getString(R.string.town).toString() + villeorigine);
                }
                if (!codpostorigine.equals("")) {
                    codpost.setText(getString(R.string.codpost).toString() + codpostorigine);
                }
                if (!paysorigine.equals("")) {
                    pays.setText(getString(R.string.country).toString() + paysorigine);
                }
                if (!audioorigine.equals("")) {
                    sonnerie.setText(getString(R.string.ringtone).toString() + audioorigine);
                }
                if (!photoorigine.equals("")) {
                    String fileName = jsvl.detdemnumgsm + "tempophoto.bmp";
                    //String storage = Environment.getExternalStorageDirectory().getPath();
                    String storage = getFilesDir().getPath();
                    File file = new File(storage , fileName);
                    String pathfileName = file.getPath();
                    bitmaporigine = BitmapFactory.decodeFile(pathfileName);
                    portrait.setImageBitmap(bitmaporigine);
                } else {
                    portrait.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.zinterro));
                }
                boutonok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //faire recherche en contact
                        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptionservice.class);
                        intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
                        intent.putExtra(GLOBAL_ORIGINE, jsvl.detdemnumgsm);
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
                        sendback="N";
                        intent.putExtra(SENDBACK,sendback);
                        startService(intent);
                        jsvl.nbligne -= 1;
                        retour(jsvl.nbligne);
                    }
                });
                boutonsb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //faire recherche en contact
                        Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptionservice.class);
                        intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
                        intent.putExtra(GLOBAL_ORIGINE, jsvl.detdemnumgsm);
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
                        sendback="Y";
                        intent.putExtra(SENDBACK,sendback);
                        startService(intent);
                        jsvl.nbligne -= 1;
                        retour(jsvl.nbligne);
                    }
                });
                boutonout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!photoorigine.equals("")) {
                            String fileName = jsvl.detdemnumgsm + "tempophoto.bmp";
                            //String storage = Environment.getExternalStorageDirectory().getPath();
                            String storage = getFilesDir().getPath();
                            File file = new File(storage, fileName);
                            if (file.exists()) file.delete();
                        }
                            Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptionservice.class);
                            intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
                            intent.putExtra(GLOBAL_ORIGINE, jsvl.detdemnumgsm);
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
                            sendback="D";
                            intent.putExtra(SENDBACK,sendback);
                            startService(intent);
                            jsvl.nbligne -= 1;
                            retour(jsvl.nbligne);

                    }
                });
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ZrechGSMservice.DESTINATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsvl = getIntent().getParcelableExtra("JSVL");
        if (jsvl != null) {
            setContentView(R.layout.ecran_attente);
            ImageView imgfr = (ImageView) findViewById(R.id.imagefr);
            ImageView imggb = (ImageView) findViewById(R.id.imagegb);
            ImageView imgde = (ImageView) findViewById(R.id.imagede);
            ImageView imges = (ImageView) findViewById(R.id.imagees);
            ImageView imgpt = (ImageView) findViewById(R.id.imagept);
            ImageView imgpl = (ImageView) findViewById(R.id.imagepl);
            ImageView imgnl = (ImageView) findViewById(R.id.imagenl);
            ImageView imgit = (ImageView) findViewById(R.id.imageit);

            myPd=ProgressDialog.show(imgit.getContext(), "Please wait",

                    "Accessing server...", true);
            myPd.setCancelable(false);
            myPd.show();
            Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, ZrechGSMservice.class);
            intent.putExtra(GLOBAL_ORIGINE, jsvl.detdemnumgsm);
            startService(intent);
        }
    }
    @Override
    public void onBackPressed() {
       retour(jsvl.nbligne);
    }

    private void retour(int nbligne) {
       if (nbligne > 0) {
          Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, Zreceptionliste.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("JSVL", jsvl);  // tbjs est parcelable donc ok pour mettre dans intent
          jsvl.GLOBAL_CONTEXT.startActivity(intent);
          finish();
       } else {
          Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.putExtra("JSVL", jsvl);  // tbjs est parcelable donc ok pour mettre dans intent
          jsvl.GLOBAL_CONTEXT.startActivity(intent);
          finish();
       }
    }
}

package org.tselex.ziicme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Zcartevisite extends Activity {
    private static final String TAG = "Zcartevisite --->";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    ZJSONvalues jsvl;
    private String numgsm = "";
    private String textdest = "";
    private String secretkey = "";
    private int version = 0;
    private static String nom = "";
    private static String num2 = "";
    private static String num3 = "";
    private static String mail1 = "";
    private static String mail2 = "";
    private static String mail3 = "";
    private static String adresse = "";
    private static String codpost = "";
    private static String ville = "";
    private static String pays = "";
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_AUDIO_REQUEST = 2;
    private String phototitre;
    private String photosrcpath;
    private String photoenvoi;
    private int orientation;
    private String audiotitre;
    private String audiosrcpath;
    private String audioenvoi;

    private String temptitre;
    private Bitmap photobitmap;
    private byte[] photobyte;
    private String audioPath;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecran_carte_visite);
        jsvl = getIntent().getParcelableExtra("JSVL");
        if (jsvl != null) {
            Button boutonok = (Button) findViewById(R.id.buttonok);
            Button boutonout = (Button) findViewById(R.id.buttonout);
            Button boutonres = (Button) findViewById(R.id.buttonres);
            Button boutonresring = (Button) findViewById(R.id.buttonresring);
            Button boutonresthumb = (Button) findViewById(R.id.buttonresthumb);
            Button boutonson = (Button) findViewById(R.id.buttonson);
            ImageButton boutonphoto = (ImageButton) findViewById(R.id.portrait);
            boutonphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rechimage();
                }
            });

            boutonres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                initial();
                }
            });
            boutonson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rechson();
                }
            });

            boutonresring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetring();
                }
            });

            boutonresthumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetthumb();
                }
            });

            boutonout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jsvl.travaudiotitre="";
                    jsvl.travaudiosrcpath="";
                    jsvl.travaudioenvoi="";
                    jsvl.travphototitre="";
                    jsvl.travphotosrcpath="";
                    jsvl.travphotoenvoi="";
                    jsvl.travphotobitmap=null;
                    jsvl.photobitmap=null;
                    jsvl.modifaudio=false;
                    jsvl.modifphoto=false;
                    Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("JSVL", jsvl);
                    jsvl.GLOBAL_CONTEXT.startActivity(intent);
                    finish();
                }
            });
            boutonok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validation();
                }
            });

            if (rechsqliteNum(jsvl.GLOBAL_NUMBER)) {
                initial();
            }
        }
    }
    private void initial(){

        //jsvl.modifaudio=false;
        //jsvl.modifphoto=false;
        Button boutonson = (Button) findViewById(R.id.buttonson);
        ImageButton boutonphoto = (ImageButton) findViewById(R.id.portrait);
        TextView enumgsm = (TextView) findViewById(R.id.numgsm);
        final EditText enom = (EditText) findViewById(R.id.nom);

        final EditText enum2 = (EditText) findViewById(R.id.num2);
        final EditText enum3 = (EditText) findViewById(R.id.num3);
        final EditText email1 = (EditText) findViewById(R.id.mail1);
        final EditText email2 = (EditText) findViewById(R.id.mail2);
        final EditText email3 = (EditText) findViewById(R.id.mail3);
        final EditText eadresse = (EditText) findViewById(R.id.adresse);
        final EditText eville = (EditText) findViewById(R.id.ville);
        final EditText ecodpost = (EditText) findViewById(R.id.codpost);
        final EditText epays = (EditText) findViewById(R.id.pays);
        enumgsm.setText(getString(R.string.numberaff)+jsvl.GLOBAL_NUMBER);
        enom.setText(jsvl.nom);
        if(!jsvl.nom.equals("")){
            enom.setText(getString(R.string.nameaff)+jsvl.nom);
        }

        enum2.setText(jsvl.num2);
        enum3.setText(jsvl.num3);
        email1.setText(jsvl.mail1);
        email2.setText(jsvl.mail2);
        email3.setText(jsvl.mail3);
        eadresse.setText(jsvl.adresse);
        eville.setText(jsvl.ville);
        ecodpost.setText(jsvl.codpost);
        epays.setText(jsvl.pays);
        if (jsvl.travaudiotitre.equals("")) {
            if (jsvl.audiotitre.equals("")) {
                boutonson.setText(R.string.ringtoneplay);
            } else {
                boutonson.setText(jsvl.audiotitre + ".mp3");
            }
            jsvl.modifaudio=false;
        }else{
            boutonson.setText(jsvl.travaudiotitre+ ".mp3");
            jsvl.modifaudio=true;
        }
        //sinon c'est le hint qui sera affiché
        if (jsvl.travphototitre.equals("")) {
            if (jsvl.phototitre.equals("")) {
                boutonphoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.zinterro));
            }else{
                //chercher le fichier avec photosrcpath
                //byte array to bitmap

                int h = 400; // width in pixels
                int w = 316; // height in pixels
                //File file = new File(jsvl.photosrcpath);
                jsvl.photobitmap = BitmapFactory.decodeFile(jsvl.photosrcpath);
                //jsvl.photobitmap = Bitmap.createScaledBitmap((jsvl.photobitmap), w, h, true);
                boutonphoto = (ImageButton) findViewById(R.id.portrait);
                boutonphoto.setImageBitmap(jsvl.photobitmap);
            }
            jsvl.modifphoto=false;
        }else{
            boutonphoto = (ImageButton) findViewById(R.id.portrait);
            boutonphoto.setImageBitmap(jsvl.travphotobitmap);
            jsvl.modifphoto=true;
        }
    }
    private void rechimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectpicture)), PICK_IMAGE_REQUEST);
    }

    private void rechson() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectaudio)), PICK_AUDIO_REQUEST);
    }
    private void resetring() {
        Button boutonson = (Button) findViewById(R.id.buttonson);
        //c'est complètement con
        boutonson.setText(R.string.ringtoneplay);
        jsvl.travaudiotitre="";
        jsvl.travaudiosrcpath="";
        jsvl.travaudioenvoi="";

        String testtext=jsvl.audiotitre;
        if(jsvl.audiotitre.equals(jsvl.travaudiotitre)){
            jsvl.modifaudio=false;
        }else{
            jsvl.modifaudio=true;
            jsvl.travaudioenvoi = "D";
        }
    }
    private void resetthumb() {
        ImageButton boutonphoto = (ImageButton) findViewById(R.id.portrait);
        boutonphoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.zinterro));
        jsvl.travphototitre="";
        jsvl.travphotosrcpath="";
        jsvl.travphotoenvoi="";
        jsvl.travphotobitmap=null;
        String testtext=jsvl.phototitre;
        if(jsvl.phototitre.equals(jsvl.travphototitre)){
            jsvl.modifphoto=false;
        }else{
            jsvl.modifphoto=true;
            jsvl.travphotoenvoi = "D";
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            gestionphoto(data);
        } else {
            if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && null != data) {
                gestionaudio(data);
            } else {
                Toast.makeText(this, "You haven't picked Image or audio",
                            Toast.LENGTH_LONG).show();
           }
        }
    }

    private void gestionphoto(Intent data) {

        Uri selectedImage = data.getData();
        //String[] filePathColumn = {(MediaStore.Images.Media.DATA), (MediaStore.Images.Media.TITLE)};
        String[] filePathColumn = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.TITLE};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        jsvl.travphotosrcpath = cursor.getString(columnIndex);
        columnIndex = cursor.getColumnIndex(filePathColumn[1]);
        jsvl.travphototitre = cursor.getString(columnIndex);

        //travail sur l'orientation



        //temptitre = temptitre.replaceAll("[^\\p{Alpha}]", " ");
        if (jsvl.phototitre.equals(jsvl.travphototitre)) {
            jsvl.modifphoto = false;
            cursor.close();
        } //ne pas envoyer
        else{
            jsvl.modifphoto = true;
            jsvl.travphotoenvoi = "N";
            cursor.close();
            int w=0; // width in pixels
            int h=0;

            //File file = new File(jsvl.travphotosrcpath);
            jsvl.travphotobitmap = BitmapFactory.decodeFile(jsvl.travphotosrcpath);
            int origWidth= jsvl.travphotobitmap.getWidth();
            int origHeight = jsvl.travphotobitmap.getHeight();

            double longu= (double)origHeight;
            double large= (double)origWidth;
            double ratio=0;
            if (origHeight > origWidth) {
                ratio = large / longu;
                longu=400;
                w = (int) (longu * ratio);
                h = 400;
            }else{
                if (origWidth >origHeight) {
                    ratio = longu / large;
                    large=400;
                    h=(int)(large*ratio);
                    w = 400;
                }
                else{
                        h=w=400;
                    }
            }

            jsvl.travphotobitmap = Bitmap.createScaledBitmap((jsvl.travphotobitmap), w, h, true);

            ImageButton boutonphoto = (ImageButton) findViewById(R.id.portrait);

            boutonphoto.setImageBitmap(jsvl.travphotobitmap);

        }
    }
    private void gestionaudio(Intent data) {
        Uri selectedaudio = data.getData();
        String[] filePathColumn = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE};

        Cursor cursor = getContentResolver().query(selectedaudio,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        jsvl.travaudiosrcpath = cursor.getString(columnIndex);
        columnIndex = cursor.getColumnIndex(filePathColumn[1]);
        jsvl.travaudiotitre = cursor.getString(columnIndex);


        //nettoyage de audiotitre
        //temptitre = temptitre.replaceAll("[^\\p{Alpha}]", " ");
        if (jsvl.audiotitre.equals(jsvl.travaudiotitre)) {
            jsvl.modifaudio = false;
            cursor.close();
        }
        else{
            jsvl.modifaudio = true;
            jsvl.travaudioenvoi = "N";
            cursor.close();
            Button boutonson = (Button) findViewById(R.id.buttonson);
            boutonson.setText(jsvl.travaudiotitre + ".mp3");
        }
    }


    private void validation() {
        boolean testmodifaudio= jsvl.modifaudio;
        boolean testmodifphoto= jsvl.modifphoto;
        String testtext= jsvl.travaudiotitre;
        testtext=jsvl.travaudiosrcpath;
        testtext=jsvl.travaudioenvoi;
        testtext=jsvl.travphototitre;
        testtext=jsvl.travphotosrcpath;
        testtext=jsvl.travphotoenvoi;
        Bitmap testbitmap = jsvl.travphotobitmap;
        Bitmap testbitmap2 = jsvl.photobitmap;

        EditText enom = (EditText) findViewById(R.id.nom);
        EditText enum2 = (EditText) findViewById(R.id.num2);
        EditText enum3 = (EditText) findViewById(R.id.num3);
        EditText email1 = (EditText) findViewById(R.id.mail1);
        EditText email2 = (EditText) findViewById(R.id.mail2);
        EditText email3 = (EditText) findViewById(R.id.mail3);
        EditText eadresse = (EditText) findViewById(R.id.adresse);
        EditText eville = (EditText) findViewById(R.id.ville);
        EditText ecodpost = (EditText) findViewById(R.id.codpost);
        EditText epays = (EditText) findViewById(R.id.pays);
        nom = enom.getText().toString();
        nom=nom.replace(getString(R.string.NAME),"");
        num2 = enum2.getText().toString();
        num3 = enum3.getText().toString();
        mail1 = email1.getText().toString();
        mail2 = email2.getText().toString();
        mail3 = email3.getText().toString();
        adresse = eadresse.getText().toString();
        ville = eville.getText().toString();
        codpost = ecodpost.getText().toString();
        pays = epays.getText().toString();

        if (!nom.equals("")) {
            jsvl.GLOBAL_NOM =nom;
            if(majsqlite(jsvl.GLOBAL_NUMBER, jsvl.GLOBAL_NOM, num2, num3, mail1, mail2, mail3, adresse, codpost, ville, pays)) {
                if (jsvl.modifaudio || jsvl.modifphoto) {
                    gestionmodif();
                }
                InternetConnectionDetector cd = new InternetConnectionDetector(jsvl.GLOBAL_CONTEXT);
                if (cd.checkMobileInternetConn()) {
                   Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, ZMyIntentService.class);
                   intent.putExtra(GLOBAL_NUMBER, jsvl.GLOBAL_NUMBER);
                   jsvl.GLOBAL_CONTEXT.startService(intent);
                }
                Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("JSVL", jsvl);
                jsvl.GLOBAL_CONTEXT.startActivity(intent);
                finish();
            }
        }
        else{
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.ecran_toast,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            TextView text = (TextView) layout.findViewById(R.id.texttoast);
            text.setText(R.string.validname);
            Toast toast = new Toast(getApplicationContext());
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }
    private void gestionmodif(){
        if (jsvl.modifphoto) {
            if(jsvl.travphotoenvoi.equals("N")) {
                try {
                    jsvl.travphotoenvoi = "N";
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    jsvl.travphotobitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //JPEG et WE
                    photobyte = stream.toByteArray();

                    String fileName = jsvl.GLOBAL_NUMBER + "photo.bmp";
                    File file = new File(getFilesDir().getPath(), fileName);
                    if (file.exists()) file.delete();
                    BufferedOutputStream bufos = new BufferedOutputStream(new FileOutputStream(file));
                    bufos.write(photobyte);
                    bufos.flush();
                    bufos.close();
                    jsvl.travphotosrcpath = file.getAbsolutePath();
                } catch (IOException e) {
                    Log.e(TAG, "IOException : " + e);
                }
            }else{ // ="D"
                jsvl.travphotosrcpath=jsvl.photosrcpath;
                }
            majsqlitephoto(jsvl.GLOBAL_NUMBER, jsvl.travphototitre, jsvl.travphotosrcpath, jsvl.travphotoenvoi);
            jsvl.modifphoto = false;
        }
        if (jsvl.modifaudio) {
            if(jsvl.travaudioenvoi.equals("N")) {
                try {
                    FileInputStream fis = new FileInputStream(new File(jsvl.travaudiosrcpath));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    int i = 0;
                    for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                        bos.write(buf, 0, readNum); //no doubt here is 0
                        if ((i += 1) > 119) break;
                    }
                    byte[] bytes = bos.toByteArray();
                    // attention ici on créer un fichier temporaire en internal storage
                    String fileName = jsvl.GLOBAL_NUMBER + "audio.mp3";
                    File file = new File(getFilesDir().getPath(), fileName);
                    if (file.exists()) file.delete();
                    BufferedOutputStream bufos = new BufferedOutputStream(new FileOutputStream(file));
                    bufos.write(bytes);
                    bufos.flush();
                    bufos.close();

                    jsvl.travaudiosrcpath = file.getAbsolutePath();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{//= "D"
                jsvl.travaudiosrcpath = jsvl.audiosrcpath;
            }
            majsqliteaudio(jsvl.GLOBAL_NUMBER, jsvl.travaudiotitre, jsvl.travaudiosrcpath, jsvl.travaudioenvoi);
            jsvl.modifaudio = false;
        }
    }
    private boolean rechsqliteNum(String num) {
        boolean trouve = false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        ZSECUREziicme zsecure = db.selectNumSECUREziicme(num);
        if (!zsecure.getSecretKey().equals("")) {     //indique pas trouvé
            jsvl.nom = zsecure.getNom();
            jsvl.num2 = zsecure.getNum2();
            jsvl.num3 = zsecure.getNum3();
            jsvl.mail1 = zsecure.getMail1();
            jsvl.mail2 = zsecure.getMail2();
            jsvl.mail3 = zsecure.getMail3();
            jsvl.adresse = zsecure.getAdresse();
            jsvl.codpost = zsecure.getCodpost();
            jsvl.ville = zsecure.getVille();
            jsvl.pays = zsecure.getPays();
            jsvl.phototitre = zsecure.getPhototitre();
            jsvl.photosrcpath = zsecure.getPhotosrcpath();
            jsvl.photoenvoi = zsecure.getPhotoenvoi();
            jsvl.audiotitre = zsecure.getAudiotitre();
            jsvl.audiosrcpath = zsecure.getAudiosrcpath();
            jsvl.audioenvoi = zsecure.getAudioenvoi();
            jsvl.GLOBAL_IDMYSQL= zsecure.getIdmysql();


            trouve = true;
        }
        return trouve;
    }

    private boolean majsqlite(String num, String nom, String num2,
                           String num3,
                           String mail1, String mail2,
                           String mail3, String adresse, String codpost,
                           String ville, String pays) {
        boolean retour;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        return retour= db.updateSECUREziicme(num,
                nom, num2, num3,
                mail1, mail2,
                mail3, adresse, codpost,
                ville, pays);

    }

    private void majsqlitephoto(String num, String phototitre,
                                String photosrcpath,String photoenvoi) {
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        db.updatephotoSECUREziicme(num, phototitre,photosrcpath,photoenvoi);
    }

    private void majsqliteaudio(String num, String audiotitre,
                                String audiosrcpath,String audioenvoi) {
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        db.updatesonSECUREziicme(num, audiotitre, audiosrcpath, audioenvoi);
    }


    @Override
    public void onBackPressed() {
        jsvl.travaudiotitre="";
        jsvl.travaudiosrcpath="";
        jsvl.travaudioenvoi="";
        jsvl.travphototitre="";
        jsvl.travphotosrcpath="";
        jsvl.travphotoenvoi="";
        jsvl.travphotobitmap=null;
        jsvl.photobitmap=null;
        jsvl.modifaudio=false;
        jsvl.modifphoto=false;
        if(!jsvl.GLOBAL_NOM.equals("")) {
            Intent intent = new Intent(jsvl.GLOBAL_CONTEXT, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("JSVL", jsvl);
            jsvl.GLOBAL_CONTEXT.startActivity(intent);
        }
        finish();
    }
}

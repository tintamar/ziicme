package org.tselex.ziicme;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.telephony.SmsManager;
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
import java.util.ArrayList;

public class ZMyIntentService extends IntentService {
    private static final String TAG = "Zmyintentservice  ";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    private static final String urlziicmeGSM = "http://www.tselex.org/ziicme/GestionGSMziicme.php";
    private static final String urlziicmeCOM = "http://www.tselex.org/ziicme/GestionCOMziicme.php";
    private static final String urlziicmeCOMGSM = "http://www.tselex.org/ziicme/GestionCOMGSMziicme.php";
    private static String directory;
    private static String phototitre;
    private static String photosrcpath;
    private static String photoenvoi;
    private static String audiotitre;
    private static String audiosrcpath;
    private static String audioenvoi;
    private static String host;
    private static String username;
    private static String password;

    private static String operation;
    private static String number;
    private static String numdest;
    private static int autorisation;
    private static String nom;
    private static String num2;
    private static String num3;
    private static String mail1;
    private static String mail2;
    private static String mail3;
    private static String adresse;
    private static String ville;
    private static String codpost;
    private static String pays;
    private static String nomdest;
    private static String num2dest;
    private static String num3dest;
    private static String mail1dest;
    private static String mail2dest;
    private static String mail3dest;
    private static String adressedest;
    private static String villedest;
    private static String codpostdest;
    private static String paysdest;
    private static String photodest;
    private static String audiodest;
    private static String uriaudio;
    private static Bitmap bitmapphoto;
    private static String nomSMS;

    public ZMyIntentService() {
        super("ZMyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            number = intent.getStringExtra(GLOBAL_NUMBER);
            if (rechsqliteNum(number)) {//voir si FTP à envoyer ou INFOS à communiquer
                //recherche infos à communiquer dans ZSTOCKsqlite
                operation="2";
                ArrayList<String> zstock = new ArrayList<String>();
                zstock = rechsqliteSTOCKoper2gsm(operation, number);
                operation = "SELECT";
                if (!rechGSMziicme(operation, number))
                {operation = "INSERT";
                }
                else
                {operation = "UPDATE";
                }
                cremajGSMziicme(operation, number, nom, autorisation, num2, num3, mail1, mail2, mail3, adresse,
                        ville, codpost, pays, phototitre, audiotitre);
                boolean action=false;
                if (photoenvoi.equals("N") || audioenvoi.equals("N") || photoenvoi.equals("D") || audioenvoi.equals("D"))
                    action=true;
                else{
                    if (!zstock.isEmpty())
                        action=true;
                    else {
                        operation = "SELECT2";
                        if (rechCOMziicme(operation, number))
                        action = true;
                }   }
                if (action){
                    String travphotoenvoi = photoenvoi;
                    String travaudioenvoi = audioenvoi;
                    if (photoenvoi.equals("D") || audioenvoi.equals("D")) {//le ringtone et/ou le thumbnail ont été supprimés(raz)
                        deleteFTPD(photosrcpath, audiosrcpath); //le FTP a pu déjà être supprimé donc on se soucie pas du status
                        if(photoenvoi.equals("D")){
                           photoenvoi = "O";
                           photosrcpath="";
                        }
                        if(audioenvoi.equals("D")) {
                           audioenvoi = "O";
                           audiosrcpath="";
                        }
                            //ici mettre à jour aussi photosrcpath
                        majsqliteSecureenvoipath(number, photoenvoi,photosrcpath, audioenvoi,audiosrcpath);
                    }
                    if (photoenvoi.equals("N") || audioenvoi.equals("N")) {
                        if (envoiFTP(photoenvoi, photosrcpath, audioenvoi, audiosrcpath)) {
                            photoenvoi = "O";
                            audioenvoi = "O";
                            majsqliteSecureenvoi(number, photoenvoi, audioenvoi);
                    }   }
                    if (!zstock.isEmpty()){
                        for (int i = 0; i < zstock.size(); i++) {
                            numdest = zstock.get(i);
                            operation = "SELECT";
                            if (rechGSMziicme(operation, numdest)) {
                                operation = "INSERT";
                                creCOMziicme(operation, number, numdest);
                                operation = "2";
                                delsqliteSTOCKoper2(operation, number, numdest);
                            }
                            else{

                                //le destinataire a peut être chargé l'appli mais il ne s'est pas connecter à internet donc
                                // on ne sait pas s'il l'a chargée ou non puisque GSM n'est pas encore créé pour lui
                                //solution envoyer SMS data avec analyse du retour
                                //ici envoyer SMS
                                operation = "INSERT";
                                creCOMziicme(operation, number, numdest);
                                operation = "2";
                                delsqliteSTOCKoper2(operation, number, numdest);
                                SMSsend(number, numdest);
                            }
                        }
                    }
                    this.stopSelf();
                }/*
                else{
                    //recherche si autre ZCOM present
                    operation = "SELECT";
                    if (!rechCOMziicme(operation, number)) {
                        //dégonflage ZGSM
                        operation = "UPDATE";
                        if (cremajGSMziicme(operation, number,"",1,"","","","","","","", "", "", "", "")) {
                            if (photosrcpath != "" || audiosrcpath != "") {
                                if (deleteFTP(photosrcpath, audiosrcpath)) {
                                    photoenvoi = "N";
                                    audioenvoi = "N";
                                    majsqliteSecureenvoi(number, photoenvoi, audioenvoi);
                                }
                            }
                        }
                    }
                    this.stopSelf();
                }*/
            }
        }
    }

    private boolean envoiFTP(String photoenvoi,String photosrcpath,String audioenvoi,String audiosrcpath){
        boolean retour=false;
        host = "ftp.cluster011.ovh.net";
        username = "tselexorfn";
        password = "xjMxE3G8yaz2";
        directory = "/www/uploads";
        MyFTPClientFunctions ftpclient = new MyFTPClientFunctions();
        boolean status = false;
        status = ftpclient.ftpConnect(host, username, password, 21);
        String msgretour="";
        if (status == true) {
            ftpclient.ftpChangeDirectory(directory);
            if(photoenvoi.equals("N")){
                String[] splitstring;
                splitstring = photosrcpath.split("/");
                int i = splitstring.length - 1;
                String desFile = splitstring[i];
                File file = new File(photosrcpath);
                String pathfile= file.getAbsolutePath();
                status = ftpclient.ftpUpload(pathfile, desFile, "", null);
                if (status == true) {
                    retour=true;
                    msgretour += " UPLOAD FTP PHOTO OK";
                } else {
                    msgretour += " MAUVAIS UPLOAD PHOTO FTP";
                }
            }
            if(audioenvoi.equals("N")){
                String[] splitstring;
                splitstring = audiosrcpath.split("/");
                int i = splitstring.length - 1;
                String desFile = splitstring[i];
                File file = new File(audiosrcpath);
                String pathfile= file.getAbsolutePath();
                status = ftpclient.ftpUpload(pathfile, desFile, "", null);
                if (status == true) {
                    retour=true;
                    msgretour += " UPLOAD FTP AUDIO OK";
                } else {
                    msgretour += " MAUVAIS UPLOAD AUDIO FTP";
                }
            }
            ftpclient.ftpDisconnect();
        }
        return retour;
    }
    private boolean deleteFTPD(String photosrcpath,String audiosrcpath){
        boolean retour=false;
        host = "ftp.cluster011.ovh.net";
        username = "tselexorfn";
        password = "xjMxE3G8yaz2";
        directory = "/www/uploads/";
        MyFTPClientFunctions ftpclient = new MyFTPClientFunctions();
        boolean status = false;
        status = ftpclient.ftpConnect(host, username, password, 21);
        String msgretour="";
        if (status == true) {
            if(photoenvoi.equals("D")){
                String pathfile= directory +number+"photo.bmp";
                status = ftpclient.ftpRemoveFile(pathfile);
                if (status == true) {
                    retour=true;
                    msgretour += " REMOVE FTP PHOTO OK";
                } else {
                    msgretour += " MAUVAIS REMOVE PHOTO FTP";
                }
            }
            if(audioenvoi.equals("D")){
                String pathfile= directory +number+"audio.mp3";
                status = ftpclient.ftpRemoveFile(pathfile);
                if (status == true) {
                    retour=true;
                    msgretour += " REMOVE FTP AUDIO OK";
                } else {
                    msgretour += " MAUVAIS REMOVE AUDIO FTP";
                }
            }
            ftpclient.ftpDisconnect();
        }
        return retour;
    }
    private boolean deleteFTP(String photosrcpath,String audiosrcpath){
        boolean retour=false;
        host = "ftp.cluster011.ovh.net";
        username = "tselexorfn";
        password = "xjMxE3G8yaz2";
        directory = "/www/uploads/";
        MyFTPClientFunctions ftpclient = new MyFTPClientFunctions();
        boolean status = false;
        status = ftpclient.ftpConnect(host, username, password, 21);
        String msgretour="";
        if (status == true) {
            if(photosrcpath!=""){
                String pathfile= directory +number+"photo.bmp";
                status = ftpclient.ftpRemoveFile(pathfile);
                if (status == true) {
                    retour=true;
                    msgretour += " REMOVE FTP PHOTO OK";
                } else {
                    msgretour += " MAUVAIS REMOVE PHOTO FTP";
                }
            }
            if(audiosrcpath!=""){
                String pathfile= directory +number+"audio.mp3";
                status = ftpclient.ftpRemoveFile(pathfile);
                if (status == true) {
                    retour=true;
                    msgretour += " REMOVE FTP AUDIO OK";
                } else {
                    msgretour += " MAUVAIS REMOVE AUDIO FTP";
                }
            }
            ftpclient.ftpDisconnect();
        }
        return retour;
    }
    private boolean rechGSMziicme(String operation,String numdest){
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
            jsonparm.put("numgsm",numdest);


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
                        nomdest = zoneinfos.getString("nom");
                        num2dest = zoneinfos.getString("num2");
                        num3dest = zoneinfos.getString("num3");
                        mail1dest = zoneinfos.getString("mail1");
                        mail2dest = zoneinfos.getString("mail2");
                        mail3dest = zoneinfos.getString("mail3");
                        adressedest = zoneinfos.getString("adresse");
                        codpostdest = zoneinfos.getString("codpost");
                        villedest = zoneinfos.getString("ville");
                        paysdest = zoneinfos.getString("pays");
                        photodest = zoneinfos.getString("photo");
                        audiodest = zoneinfos.getString("sonnerie");
                    }
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

    private boolean cremajGSMziicme(String operation,String number,String nom,int autorisation,String num2, String num3,
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
    private boolean creCOMziicme(String operation,String number,String numdest){
        boolean retour=false;
        try {
            URL object = new URL(urlziicmeCOM);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject jsonparm = new JSONObject();
            jsonparm.put("operation",operation);
            jsonparm.put("numgsm",number);
            jsonparm.put("numdest", numdest);

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
    private boolean rechCOMziicme(String operation,String number){
        boolean retour=false;
        try {
            URL object = new URL(urlziicmeCOM);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject jsonparm = new JSONObject();
            jsonparm.put("operation",operation);
            jsonparm.put("numgsm",number);


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
                    int ligne = zoneoperation.getInt("ligne");
                    if (ligne >  0)retour=true;
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
    private boolean rechCOMGSMziicme(String operation,String number){
        boolean retour=false;
        try {
            URL object = new URL(urlziicmeCOMGSM);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            JSONObject jsonparm = new JSONObject();
            jsonparm.put("operation",operation);
            jsonparm.put("numgsm",number);


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
                    int ligne = zoneoperation.getInt("ligne");
                    if (ligne > 0) {
                        retour = true;
                        JSONArray zonecorps = zoneoperation.getJSONArray("CORPS");
                        JSONObject zoneinfos = zonecorps.getJSONObject(0);
                        nomSMS = zoneinfos.getString("nom");
                    }
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
    private boolean delsqliteSTOCKoper2(String operation,String number,String numdest) {
        boolean retour=false;
        ZSTOCKziicmeGest db = new ZSTOCKziicmeGest(this);
        retour = db.deleteSTOCKziicmeoper2(operation, number, numdest);
        return retour;
    }

    private ArrayList<String> rechsqliteSTOCKoper2gsm(String operation,String number) {
        ZSTOCKziicmeGest db = new ZSTOCKziicmeGest(this);
        ArrayList<String> zstock= new ArrayList<String>();
        zstock = db.selectSTOCKziicmeOper2gsm(operation, number);
        return zstock;
    }
    private void SMSsend(String number,String numdest){
        operation = "SELECT";
        if(rechCOMGSMziicme(operation, number)) {
            SmsManager sms = SmsManager.getDefault();
            //il faut le nom donc on utilise COMGSM
            String message = nomSMS + " number "+ numdest +
                    " enjoy sharing contacts with ZIICME at : ";

            String Url ="http://www.tselex.org/ziicme/ZIICME.apk";
            int longu = message.length();
            longu= Url.length();
            try {
                sms.sendTextMessage(numdest, null, message+Url, null, null);

            } catch (IllegalArgumentException e) {
                Log.e(TAG,"IllegalArgumentException  "+e);
            }
        }
    }
    private boolean rechsqliteNum(String num) {
        boolean trouve = false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        ZSECUREziicme zsecure = db.selectNumSECUREziicme(num);
        if (!zsecure.getSecretKey().equals("")) {     //indique pas trouvé
            autorisation=zsecure.getAutorisation();
            nom=zsecure.getNom();
            num2=zsecure.getNum2();
            num3=zsecure.getNum3();
            mail1=zsecure.getMail1();
            mail2=zsecure.getMail2();
            mail3=zsecure.getMail3();
            adresse=zsecure.getAdresse();
            ville=zsecure.getVille();
            codpost=zsecure.getCodpost();
            pays=zsecure.getPays();
            phototitre = zsecure.getPhototitre();
            photosrcpath = zsecure.getPhotosrcpath();
            photoenvoi = zsecure.getPhotoenvoi();
            audiotitre = zsecure.getAudiotitre();
            audiosrcpath = zsecure.getAudiosrcpath();
            audioenvoi = zsecure.getAudioenvoi();
            trouve = true;
        }
        return trouve;
    }
    private boolean majsqliteSecureenvoi(String numgsm,String photoenvoi, String sonnerieenvoi) {
        boolean reussite = false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        reussite = db.updateenvoiSECUREziicme(numgsm,photoenvoi,sonnerieenvoi);
        return reussite;
    }
    private boolean majsqliteSecureenvoipath(String numgsm,String photoenvoi,String photosrcpath,
                                             String sonnerieenvoi,String audiosrcpath) {
        boolean reussite = false;
        ZSECUREziicmeGest db = new ZSECUREziicmeGest(this);
        reussite = db.updateenvoiSECUREziicmepath(numgsm,photoenvoi,photosrcpath,sonnerieenvoi,audiosrcpath);
        return reussite;
    }
}

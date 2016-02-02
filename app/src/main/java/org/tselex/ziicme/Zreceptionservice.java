package org.tselex.ziicme;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

public class Zreceptionservice extends IntentService {
    private static final String TAG = "Zreceptionservice  ";
    private static final String GLOBAL_NUMBER = "GLOBAL_NUMBER";
    private static final String GLOBAL_ORIGINE = "GLOBAL_ORIGINE";
    private static final String urlziicmeGSM = "http://www.tselex.org/ziicme/GestionGSMziicme.php";
    private static final String urlziicmeCOM = "http://www.tselex.org/ziicme/GestionCOMziicme.php";
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
    private static String directory;
    private static String host;
    private static String username;
    private static String password;
    private static String operation;
    private static String number;
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
    private static String fileURIaudio;
    private static Bitmap photobitmap;
    private static String sendback;
    private static int nbligne;
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

    public Zreceptionservice() {
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
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            number = intent.getStringExtra(GLOBAL_NUMBER);
            numberorigine = intent.getStringExtra(GLOBAL_ORIGINE);
            nomorigine = intent.getStringExtra(NOMGSM);
            num2origine = intent.getStringExtra(NUM2GSM);
            num3origine = intent.getStringExtra(NUM3GSM);
            mail1origine = intent.getStringExtra(MAIL1GSM);
            mail2origine = intent.getStringExtra(MAIL2GSM);
            mail3origine = intent.getStringExtra(MAIL3GSM);
            adresseorigine = intent.getStringExtra(ADRESSEGSM);
            villeorigine = intent.getStringExtra(VILLEGSM);
            codpostorigine = intent.getStringExtra(CODPOSTGSM);
            paysorigine = intent.getStringExtra(PAYSGSM);
            photoorigine = intent.getStringExtra(PHOTOGSM);
            audioorigine = intent.getStringExtra(AUDIOGSM);
            sendback = intent.getStringExtra(SENDBACK);
            if(sendback.equals("D")){
                operation = "DELETE";
                delCOMziicme(operation, numberorigine, number);
                operation = "2";
                delsqliteSTOCKoper2(operation, numberorigine, number);
                this.stopSelf();
            }

            if (photoorigine!="") {//atention le fichier tempo existe faire juste un rename
                renameFTPphoto(numberorigine);
                //déchargement fichier photo
            }
            if (audioorigine!="") {
                //déchargement fichier audio et  faire fileuri
                downloadFTPaudio(numberorigine);
            }
            gestcontact(nomorigine, numberorigine, num2origine, num3origine,
                    mail1origine, mail2origine, mail3origine,
                    adresseorigine, villeorigine, codpostorigine, paysorigine,
                    photobitmap, fileURIaudio);
            if (fileURIaudio!="")
            gestcontactaudio(numberorigine, fileURIaudio);
            // BLOCAGE DU DELETE POUR TESTS
            operation = "DELETE";
            if(delCOMziicme(operation, numberorigine, number)) {
                //recherche autres ZCOM sur number = numberorigine
                operation = "2";
                delsqliteSTOCKoper2(operation, numberorigine, number);
                operation = "SELECT";
            }
            if(sendback.equals("Y")){
                //creation zcom avec number,numorigine
                operation = "INSERT";
                creCOMziicme(operation,number,numberorigine);
            }/*
            else
            {
                if (!rechCOMziicme(operation, numberorigine)) {    //recherche autres ZCOM sur number = numberorigine
                    //si pas trouvé
                    //dégonflage GSM sur numberorigine
                    operation = "UPDATE";
                    if (majGSMziicme(operation, numberorigine, "", 1, "", "", "", "", "", "", "", "", "", "", "")) {
                        //suppression éventuelle FTP

                        if (photoorigine != "") {
                            deleteFTPphoto();
                        }
                        if (audioorigine != "") {
                            deleteFTPaudio();
                        }
                    }
                }
                //retour à l'appelant
            }*/

            this.stopSelf();
        }
    }



    private void renameFTPphoto(String number){
        //faire rename du tempo.bmp en photo.bmp
        //decoder le fichier dans un bitmap
        String fileName1 = number + "tempophoto.bmp";
        //String storage = Environment.getExternalStorageDirectory().getPath();
        String storage1 = getFilesDir().getPath();
        File file1 = new File(storage1 , fileName1);
        File storage2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storage2.mkdirs();
        String fileName2 = number + "photo.bmp";
        File file2 = new File(storage2+"/" , fileName2);

        try{
            FileInputStream in = new FileInputStream(file1);
            FileOutputStream out = new FileOutputStream(file2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            String pathfileName = file2.getPath();
            photobitmap = BitmapFactory.decodeFile(pathfileName);
            file1.delete();
        }
        catch(IOException e){
            Log.e(TAG,"IOException :  " + e);
        }


    }
    private void downloadFTPaudio(String number){
        //download et créer ensuite un fileuri
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
            String fileName = number + "audio.mp3";
            //String storage = Environment.getExternalStorageDirectory().getPath();
            File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            storage.mkdirs();

            File file = new File(storage+"/" , fileName);
            if (file.exists()) file.delete();
            String pathfileName = file.getAbsolutePath();
            status = ftpclient.ftpDownload(fileName,pathfileName);
            if (status == true) {
                msgretour += " DOWNLOAD FTP AUDIO OK ";
                //fileURIaudio = Uri.decode(Uri.fromFile(file).toString());
                fileURIaudio = Uri.fromFile(file).toString();
            } else {
                msgretour += " MAUVAIS DOWNLOAD FTP AUDIO ";
            }
            ftpclient.ftpDisconnect();
        }
    }
    private boolean delCOMziicme(String operation,String number,String numdest){
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
    private boolean deleteFTPphoto(){
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
            String pathfile= directory +number+"photo.bmp";
            status = ftpclient.ftpRemoveFile(pathfile);
            if (status == true) {
                retour=true;
                msgretour += " REMOVE FTP PHOTO OK";
            } else {
                 msgretour += " MAUVAIS REMOVE PHOTO FTP";
            }
            ftpclient.ftpDisconnect();
        }
        return retour;
    }
    private boolean deleteFTPaudio(){
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
            String pathfile= directory +number+"audio.mp3";
            status = ftpclient.ftpRemoveFile(pathfile);
            if (status == true) {
                retour=true;
                msgretour += " REMOVE FTP PHOTO OK";
            } else {
                msgretour += " MAUVAIS REMOVE PHOTO FTP";
            }
            ftpclient.ftpDisconnect();
        }
        return retour;
    }

    private boolean majGSMziicme(String operation,String number,String nom,int autorisation,String num2, String num3,
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
    private void gestcontact(String nom,String numero,String num2,String num3,
                             String mail1,String mail2,String mail3,
                             String adresse,String ville,String codpost,String pays,
                             Bitmap photo,String fileURI)
    {
        //Uri lkup = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, nom);
        Uri lkup = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero));
        Cursor contactLookup = getContentResolver().query(lkup, null, null, null, null);

        try {
            if (contactLookup == null) {
                return;
            }
            if(contactLookup.moveToFirst()) {
                String lookupKey = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                getContentResolver().delete(uri, null, null);
            }
            crecontact(nom, numero, num2, num3, mail1, mail2, mail3, adresse, ville, codpost, pays, photo, fileURI);
        }
        catch(NullPointerException e) {
            Log.e(TAG,"NullPointerException   "+e);
        }
        catch(CursorIndexOutOfBoundsException e) {
            Log.e(TAG,"CursorIndexOutOfBoundsException   "+e);
        }
        finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

    }
    //ça fout la merde dans le contact
    private void gestcontactaudio(String numero,String fileURI)
    {   final Uri lkup = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(numero));
        final String[] projection = new String[] {Contacts._ID, Contacts.LOOKUP_KEY};
        final Cursor idCursor = getContentResolver().query(lkup, projection, null, null, null);

        try {
            if (idCursor == null) {
                return;
            }
            if(idCursor.moveToFirst()) {
                final long contactId = idCursor.getLong(0);
                final String lookupKey = idCursor.getString(1);
                final Uri contactUri = Contacts.getLookupUri(contactId, lookupKey);
                if (contactUri == null) {
                    return;
                }

                final ContentValues values = new ContentValues(1);
                values.put(Contacts.CUSTOM_RINGTONE, fileURI);
                getContentResolver().update(contactUri, values, null, null);
            }

        }
        catch(NullPointerException e) {
            Log.e(TAG, "NullPointerException   " + e);
        }
        catch(CursorIndexOutOfBoundsException e) {
            Log.e(TAG, "CursorIndexOutOfBoundsException   " + e);
        }
        finally {
            if (idCursor != null) {
                idCursor.close();
            }
        }

    }


    private void crecontact(String nom,String numero,String num2,String num3,
                            String mail1,String mail2,String mail3,
                            String adresse,String ville,String codpost,String pays,
                            Bitmap photo,String fileURI) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactID = ops.size();
        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts

        if(!fileURI.equals("")){
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.CUSTOM_RINGTONE, fileURI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());}
        else{
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
               .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
               .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
               .build());}

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, nom)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numero)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());

        if(!num2.equals("")){
            // Adding insert operation to operations list
            // to  insert Home Phone Number in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, num2)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }
        if(!num3.equals("")) {
            // Adding insert operation to operations list
            // to  insert Home Phone Number in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, num3)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }
        if(!mail1.equals("")){
            // Adding insert operation to operations list
            // to insert Home Email in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, mail1)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }
        if(!mail2.equals("")){
            // Adding insert operation to operations list
            // to insert Home Email in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, mail2)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }
        if(!mail3.equals("")){
            // Adding insert operation to operations list
            // to insert Home Email in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, mail3)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                    .build());
        }
        if(!(adresse.equals("")&&ville.equals("")&&codpost.equals("")&&pays.equals(""))){
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, adresse)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, ville)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, codpost)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, pays)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                    .build());
        }

        if(photo!=null){
            // If an image is selected successfully
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                    .build());

        }


        try{
            // Executing all the insert operations as a single database transaction
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            //Toast.makeText(getBaseContext(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
        }catch (RemoteException e) {
            Log.e(TAG, "RemoteException"+e);
        }catch (OperationApplicationException e) {
            Log.e(TAG, "OperationApplicationException" + e);
        }
        catch (Exception e) {
            Log.e(TAG, "Exception" + e);
        }
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
    private boolean delsqliteSTOCKoper2(String operation,String number,String numdest) {
        boolean retour=false;
        ZSTOCKziicmeGest db = new ZSTOCKziicmeGest(this);
        retour = db.deleteSTOCKziicmeoper2(operation, number, numdest);
        return retour;
    }
 }

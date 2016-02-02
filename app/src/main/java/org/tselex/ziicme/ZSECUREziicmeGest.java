package org.tselex.ziicme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zappa_000 on 26/06/2015.
 */
public class ZSECUREziicmeGest extends SQLiteOpenHelper {

        private static final String DATABASE_ziicme = "DBziicmeSecure";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_SECUREziicme = "SECUREziicme";
        private static final String KEY_ID = "id";
        private static final String KEY_NUMGSM = "numgsm";
        private static final String KEY_NOM = "nom";
        private static final String KEY_SECRETKEY = "secretkey";
        private static final String KEY_AUTORISATION = "autorisation";
        private static final String KEY_NUM2 = "num2";
        private static final String KEY_NUM3 = "num3";
        private static final String KEY_MAIL1 = "mail1";
        private static final String KEY_MAIL2 = "mail2";
        private static final String KEY_MAIL3 = "mail3";
        private static final String KEY_ADRESSE = "adresse";
        private static final String KEY_CODPOST = "codpost";
        private static final String KEY_VILLE = "ville";
        private static final String KEY_PAYS = "pays";
        private static final String KEY_PHOTOTITRE = "phototitre";
        private static final String KEY_PHOTOSRCPATH = "photosrcpath";
        private static final String KEY_PHOTOENVOI = "photoenvoi";
        private static final String KEY_AUDIOTITRE = "audiotitre";
        private static final String KEY_AUDIOSRCPATH = "audiosrcpath";
        private static final String KEY_AUDIOENVOI = "audioenvoi";
        private static final String KEY_IDMYSQL = "idmysql";

    /* clause where */
        String where; /* intégrer l'odre dynamiquement */
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        private static final String TAG = "**** GestionDB **** ";

        public ZSECUREziicmeGest(Context context){
            super(context, DATABASE_ziicme, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, "onCreate");
              /* Instruction de création de la table GSMziicme dans la base de données   */
            //db = this.getWritableDatabase();
            String DATABASE_CREATE = "create table " +
                    TABLE_SECUREziicme + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_NUMGSM + " text unique not null,"
                    + KEY_SECRETKEY + " text not null,"
                    + KEY_AUTORISATION + " integer,"
                    + KEY_NOM + " text not null,"
                    + KEY_NUM2 + " text,"
                    + KEY_NUM3 + " text,"
                    + KEY_MAIL1 + " text,"
                    + KEY_MAIL2 + " text,"
                    + KEY_MAIL3 + " text,"
                    + KEY_ADRESSE + " text,"
                    + KEY_CODPOST + " text,"
                    + KEY_VILLE + " text,"
                    + KEY_PAYS + " text,"
                    + KEY_PHOTOTITRE + " text,"
                    + KEY_PHOTOSRCPATH + " text,"
                    + KEY_PHOTOENVOI + " text,"
                    + KEY_AUDIOTITRE + " text,"
                    + KEY_AUDIOSRCPATH + " text,"
                    + KEY_AUDIOENVOI + " text,"
                    + KEY_IDMYSQL + " text);";


            try{
                db.execSQL(DATABASE_CREATE);

            }
            catch (SQLException sqle ){
                Log.w(TAG, "onCreate    ERREUR" +sqle.toString());
            }
        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {


        }
        /******************************************************/
        /*   SELECT    GSMziicme sur N° numgsm                  ************/
        /******************************************************/
        public ZSECUREziicme selectNumSECUREziicme (String numgsm) {
            Log.w(TAG, "selectSECUREziicme");
            where = KEY_NUMGSM + " = \"" +numgsm+  "\"";
            ZSECUREziicme tb = new ZSECUREziicme();
            /* Valeurs pour sélection  */
            String[] result_colonnes = new String[] {
                    KEY_ID,
                    KEY_NUMGSM,
                    KEY_SECRETKEY,
                    KEY_AUTORISATION,
                    KEY_NOM,
                    KEY_NUM2,
                    KEY_NUM3,
                    KEY_MAIL1,
                    KEY_MAIL2,
                    KEY_MAIL3 ,
                    KEY_ADRESSE,
                    KEY_CODPOST,
                    KEY_VILLE ,
                    KEY_PAYS,
                    KEY_PHOTOTITRE,
                    KEY_PHOTOSRCPATH,
                    KEY_PHOTOENVOI,
                    KEY_AUDIOTITRE,
                    KEY_AUDIOSRCPATH,
                    KEY_AUDIOENVOI,
                    KEY_IDMYSQL};
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                Cursor cursor = db.query(TABLE_SECUREziicme,
                        result_colonnes,
                        where, whereArgs, groupBy, having, order);
                if (cursor.moveToFirst()) {
                    //td.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    /* on devrait faire le même test  colonne par colonne mais un seul test me suffit */
                    tb.setNumGSM(cursor.getString(cursor.getColumnIndex(KEY_NUMGSM)));
                    tb.setSecretKey(cursor.getString(cursor.getColumnIndex(KEY_SECRETKEY)));
                    tb.setAutorisation(cursor.getInt(cursor.getColumnIndex(KEY_AUTORISATION)));
                    tb.setNom(cursor.getString(cursor.getColumnIndex(KEY_NOM)));
                    tb.setNum2(cursor.getString(cursor.getColumnIndex(KEY_NUM2)));
                    tb.setnum3(cursor.getString(cursor.getColumnIndex(KEY_NUM3)));
                    tb.setMail1(cursor.getString(cursor.getColumnIndex(KEY_MAIL1)));
                    tb.setMail2(cursor.getString(cursor.getColumnIndex(KEY_MAIL2)));
                    tb.setMail3(cursor.getString(cursor.getColumnIndex(KEY_MAIL3)));
                    tb.setAdresse(cursor.getString(cursor.getColumnIndex(KEY_ADRESSE)));
                    tb.setCodpost(cursor.getString(cursor.getColumnIndex(KEY_CODPOST)));
                    tb.setVille(cursor.getString(cursor.getColumnIndex(KEY_VILLE)));
                    tb.setPays(cursor.getString(cursor.getColumnIndex(KEY_PAYS)));
                    tb.setPhototitre(cursor.getString(cursor.getColumnIndex(KEY_PHOTOTITRE)));
                    tb.setPhotosrcpath(cursor.getString(cursor.getColumnIndex(KEY_PHOTOSRCPATH)));
                    tb.setPhotoenvoi(cursor.getString(cursor.getColumnIndex(KEY_PHOTOENVOI)));
                    tb.setAudiotitre(cursor.getString(cursor.getColumnIndex(KEY_AUDIOTITRE)));
                    tb.setAudiosrcpath(cursor.getString(cursor.getColumnIndex(KEY_AUDIOSRCPATH)));
                    tb.setAudioenvoi(cursor.getString(cursor.getColumnIndex(KEY_AUDIOENVOI)));
                    tb.setIdmysql(cursor.getString(cursor.getColumnIndex(KEY_IDMYSQL)));
                }
                else{
                    tb.setNumGSM("");
                    tb.setSecretKey("");
                    tb.setAutorisation(0);
                }

            }
            catch (SQLException sqle) {
                 Log.w(TAG, "ERREUR sqlite " + sqle.toString());
            }
            return tb;
        }

    /******************************************************/
        /*   SELECT    GSMziicme sur Key                 ************/
    /******************************************************/
    public ZSECUREziicme selectKeySECUREziicme (String secretkey) {
        Log.w(TAG, "selectKeySECUREziicme");
        where = KEY_SECRETKEY + " = \"" +secretkey+ "\"";

        ZSECUREziicme tb = new ZSECUREziicme();
            /* Valeurs pour sélection  */
        String[] result_colonnes = new String[] {
                KEY_ID,
                KEY_NUMGSM,
                KEY_SECRETKEY,
                KEY_AUTORISATION,
                KEY_NOM,
                KEY_NUM2,
                KEY_NUM3,
                KEY_MAIL1,
                KEY_MAIL2,
                KEY_MAIL3 ,
                KEY_ADRESSE,
                KEY_CODPOST,
                KEY_VILLE ,
                KEY_PAYS,
                KEY_PHOTOTITRE,
                KEY_PHOTOSRCPATH,
                KEY_PHOTOENVOI,
                KEY_AUDIOTITRE,
                KEY_AUDIOSRCPATH,
                KEY_AUDIOENVOI,
                KEY_IDMYSQL};
        SQLiteDatabase db = this.getWritableDatabase();
        try {
             Cursor cursor = db.query(TABLE_SECUREziicme,
                    result_colonnes,
                    where, whereArgs, groupBy, having, order);
            if (cursor.moveToFirst()) {
                //td.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    /* on devrait faire le même test  colonne par colonne mais un seul test me suffit */
                tb.setNumGSM(cursor.getString(cursor.getColumnIndex(KEY_NUMGSM)));
                tb.setSecretKey(cursor.getString(cursor.getColumnIndex(KEY_SECRETKEY)));
                tb.setAutorisation(cursor.getInt(cursor.getColumnIndex(KEY_AUTORISATION)));
                tb.setNom(cursor.getString(cursor.getColumnIndex(KEY_NOM)));
                tb.setNum2(cursor.getString(cursor.getColumnIndex(KEY_NUM2)));
                tb.setnum3(cursor.getString(cursor.getColumnIndex(KEY_NUM3)));
                tb.setMail1(cursor.getString(cursor.getColumnIndex(KEY_MAIL1)));
                tb.setMail2(cursor.getString(cursor.getColumnIndex(KEY_MAIL2)));
                tb.setMail3(cursor.getString(cursor.getColumnIndex(KEY_MAIL3)));
                tb.setAdresse(cursor.getString(cursor.getColumnIndex(KEY_ADRESSE)));
                tb.setCodpost(cursor.getString(cursor.getColumnIndex(KEY_CODPOST)));
                tb.setVille(cursor.getString(cursor.getColumnIndex(KEY_VILLE)));
                tb.setPays(cursor.getString(cursor.getColumnIndex(KEY_PAYS)));
                tb.setPhototitre(cursor.getString(cursor.getColumnIndex(KEY_PHOTOTITRE)));
                tb.setPhotosrcpath(cursor.getString(cursor.getColumnIndex(KEY_PHOTOSRCPATH)));
                tb.setPhotoenvoi(cursor.getString(cursor.getColumnIndex(KEY_PHOTOENVOI)));
                tb.setAudiotitre(cursor.getString(cursor.getColumnIndex(KEY_AUDIOTITRE)));
                tb.setAudiosrcpath(cursor.getString(cursor.getColumnIndex(KEY_AUDIOSRCPATH)));
                tb.setAudioenvoi(cursor.getString(cursor.getColumnIndex(KEY_AUDIOENVOI)));
                tb.setIdmysql(cursor.getString(cursor.getColumnIndex(KEY_IDMYSQL)));
            }
            else{
                tb.setNumGSM("");
                tb.setSecretKey("");
                tb.setAutorisation(0);
            }

        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite " + sqle.toString());
        }
        db.close();
        return tb;
    }
        /******************************************************/
        /*   INSERT                                ************/
        /******************************************************/
        public boolean insertSECUREziicme (String numgsm,String secretkey,int autorisation,
                                        String nom, String num2,String num3,String mail1,
                                        String mail2,String mail3,String adresse,String codpost,
                                        String ville,String pays, String phototitre,
                                        String photosrcpath,String photoenvoi,String audiotitre,
                                        String audiosrcpath,String audioenvoi,String idmysql)
        {
            boolean retour=false;
            Log.w(TAG, "insertKeySECUREziicme");
            ContentValues values = new ContentValues();
            //values.put(KEY_ID, 1 );
            values.put(KEY_NUMGSM, numgsm);
            values.put(KEY_SECRETKEY, secretkey);
            values.put(KEY_AUTORISATION, autorisation);
            values.put(KEY_NOM,nom);
            values.put(KEY_NUM2,num2);
            values.put(KEY_NUM3,num3);
            values.put(KEY_MAIL1,mail1);
            values.put(KEY_MAIL2, mail2);
            values.put(KEY_MAIL3, mail3);
            values.put(KEY_ADRESSE, adresse);
            values.put(KEY_CODPOST, codpost);
            values.put(KEY_VILLE, ville);
            values.put(KEY_PAYS, pays);
            values.put(KEY_PHOTOTITRE, phototitre);
            values.put(KEY_PHOTOSRCPATH, photosrcpath);
            values.put(KEY_PHOTOENVOI, photoenvoi);
            values.put(KEY_AUDIOTITRE, audiotitre);
            values.put(KEY_AUDIOSRCPATH, audiosrcpath);
            values.put(KEY_AUDIOENVOI, audioenvoi);
            values.put(KEY_IDMYSQL, idmysql);

            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                db.insert(TABLE_SECUREziicme,null,values);
                retour= true;
            }
            catch (SQLException sqle) {
                Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
                retour= false;
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return retour;
        }

        /******************************************************/
    /*   UPDATE   GSMziicme                    ************/
        /******************************************************/
        public boolean updateSECUREziicme (String numgsm,
                                           String nom, String num2,String num3,
                                           String mail1,String mail2,
                                           String mail3,String adresse,String codpost,
                                           String ville,String pays)

        {
            Log.w(TAG, "updateSECUREziicm");
            where = KEY_NUMGSM + " =   \"" +numgsm+ "\"" ;
            boolean retour=false;

            ContentValues values = new ContentValues();
          //  values.put(KEY_NUMGSM, numgsm);
            values.put(KEY_NOM,nom);
            values.put(KEY_NUM2,num2);
            values.put(KEY_NUM3,num3);
            values.put(KEY_MAIL1,mail1);
            values.put(KEY_MAIL2, mail2);
            values.put(KEY_MAIL3, mail3);
            values.put(KEY_ADRESSE, adresse);
            values.put(KEY_CODPOST, codpost);
            values.put(KEY_VILLE, ville);
            values.put(KEY_PAYS, pays);

            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                db.update(TABLE_SECUREziicme, values, where, whereArgs);
                retour = true;
            }
            catch (SQLException sqle) {
                Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
                retour = false;
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            return retour;
        }

    /******************************************************/
    /*   UPDATE   GSMziicme photo                   ************/
    /******************************************************/
    public boolean updatephotoSECUREziicme (String numgsm,String phototitre,String photosrcpath,
                                            String photoenvoi)

    {
        Log.w(TAG, "updateSECUREziicm");
        where = KEY_NUMGSM + " =   \"" +numgsm+ "\"" ;
        boolean retour;

        ContentValues values = new ContentValues();

        values.put(KEY_PHOTOTITRE, phototitre);
        values.put(KEY_PHOTOSRCPATH, photosrcpath);
        values.put(KEY_PHOTOENVOI, photoenvoi);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(TABLE_SECUREziicme, values, where, whereArgs);
            retour = true;
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
            retour = false;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return retour;
    }


    /******************************************************/
    /*   UPDATE   GSMziicme                    ************/
    /******************************************************/
    public boolean updatesonSECUREziicme (String numgsm,String audiotitre,
                                          String audiosrcpath,String audioenvoi)

    {
        Log.w(TAG, "updateSECUREziicm");
        where = KEY_NUMGSM + " =   \"" +numgsm+ "\"" ;
        boolean retour;

        ContentValues values = new ContentValues();

        values.put(KEY_AUDIOTITRE, audiotitre);
        values.put(KEY_AUDIOSRCPATH, audiosrcpath);
        values.put(KEY_AUDIOENVOI, audioenvoi);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(TABLE_SECUREziicme, values, where, whereArgs);
            retour = true;
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
            retour = false;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return retour;
    }
    /******************************************************/
    /*   UPDATE   GSMziicme                    ************/
    /******************************************************/
    public boolean updateenvoiSECUREziicme (String numgsm,String photoenvoi,
                                          String audioenvoi)

    {
        Log.w(TAG, "updateSECUREziicm");
        where = KEY_NUMGSM + " =   \"" +numgsm+ "\"" ;
        boolean retour;

        ContentValues values = new ContentValues();


        values.put(KEY_PHOTOENVOI, photoenvoi);
        values.put(KEY_AUDIOENVOI, audioenvoi);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(TABLE_SECUREziicme, values, where, whereArgs);
            retour = true;
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
            retour = false;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return retour;
    }
    public boolean updateenvoiSECUREziicmepath (String numgsm,String photoenvoi,String photosrcpath,
                                            String audioenvoi,String audiosrcpath)

    {
        Log.w(TAG, "updateSECUREziicm");
        where = KEY_NUMGSM + " =   \"" +numgsm+ "\"" ;
        boolean retour;

        ContentValues values = new ContentValues();


        values.put(KEY_PHOTOENVOI, photoenvoi);
        values.put(KEY_PHOTOSRCPATH, photosrcpath);
        values.put(KEY_AUDIOENVOI, audioenvoi);
        values.put(KEY_AUDIOSRCPATH, audiosrcpath);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(TABLE_SECUREziicme, values, where, whereArgs);
            retour = true;
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
            retour = false;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return retour;
    }

    public void deleteSECUREziicme(Context context) {
            Log.w(TAG, "deleteSECUREziicme");
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.delete(TABLE_SECUREziicme,null,null);
            }
            catch (SQLException sqle) {
                Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
            }
        }
}

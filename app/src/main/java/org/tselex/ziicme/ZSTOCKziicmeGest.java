package org.tselex.ziicme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by zappa_000 on 18/07/2015.
 */
public class ZSTOCKziicmeGest extends SQLiteOpenHelper {
 

        private static final String DATABASE_ziicme = "DBziicmeStock";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_STOCKziicme = "STOCKziicme";
        private static final String KEY_ID = "id";
        private static final String KEY_OPERATION = "operation";
        private static final String KEY_NUMGSM = "numgsm";
        private static final String KEY_NOM = "nom";
        private static final String KEY_NUMDEST = "numdest";
        private static final String KEY_AUDIO = "audiofile";
        private static final String KEY_PHOTO = "photofile";
       


        /* clause where */
        String where; /* intégrer l'odre dynamiquement */
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        private static final String TAG = "**** GestionDB **** ";

        public ZSTOCKziicmeGest(Context context){
            super(context, DATABASE_ziicme, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, "onCreate");
              /* Instruction de création de la table GSMziicme dans la base de données   */
            //db = this.getWritableDatabase();
            String DATABASE_CREATE = "create table " +
                    TABLE_STOCKziicme + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_OPERATION + " text not null ,"
                    + KEY_NUMGSM + " text ,"
                    + KEY_NOM + " text ,"
                    + KEY_NUMDEST + " text ,"
                    + KEY_AUDIO + " text ,"
                    + KEY_PHOTO + " text );";

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
        /*   opertion = 1 pour les fichiers en attente d'envoi FTP   ************/
        /******************************************************/
        public ZSTOCKziicme selectSTOCKziicmeOper1 (String oper) {
            Log.w(TAG, "selectSTOCKziicme");
            where = KEY_OPERATION + " = \"" +oper+  "\"";
            ZSTOCKziicme tb = new ZSTOCKziicme();
            /* Valeurs pour sélection  */
            String[] result_colonnes = new String[] {
                    KEY_ID,
                    KEY_AUDIO,
                    KEY_PHOTO};

            SQLiteDatabase db = this.getWritableDatabase();
            try {

                Cursor cursor = db.query(TABLE_STOCKziicme,
                        result_colonnes,
                        where, whereArgs, groupBy, having, order);
                if (cursor.moveToFirst()) {
                    //td.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    /* on devrait faire le même test  colonne par colonne mais un seul test me suffit */
                    tb.setFtpaudioFile(cursor.getString(cursor.getColumnIndex(KEY_AUDIO)));
                    tb.setFtpphotoFile(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));


                }
                else{
                    tb.setFtpphotoFile("");
                    tb.setFtpaudioFile("");

                }

            }
            catch (SQLException sqle) {
                Log.w(TAG, "ERREUR sqlite " + sqle.toString());
            }
            db.close();
            return tb;
        }
    /******************************************************/
        /*   opertion = 2 pour envoi maj contact en attente  ************/
    /******************************************************/
    public ZSTOCKziicme selectSTOCKziicmeOper2unic (String oper,String numgsm,String numdest) {
        Log.w(TAG, "selectSTOCKziicmeOper2unic");
        ZSTOCKziicme tb = new ZSTOCKziicme();
        where = KEY_OPERATION + " = \"" +oper+"\" AND " +KEY_NUMGSM+"=\""+numgsm+"\" AND "+KEY_NUMDEST+"=\""+numdest+"\"";
        String[] result_colonnes = new String[]
                {KEY_NUMGSM,
                KEY_NOM,
                KEY_NUMDEST};
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_STOCKziicme,
                    result_colonnes,
                    where, whereArgs, groupBy, having, order);
            if (cursor.moveToFirst()) {
                tb.setNumgsm(cursor.getString(cursor.getColumnIndex(KEY_NUMGSM)));
                tb.setNom(cursor.getString(cursor.getColumnIndex(KEY_NOM)));
                tb.setNumdest(cursor.getString(cursor.getColumnIndex(KEY_NUMDEST)));
            }
            else{
                tb.setNumgsm("");
                tb.setNom("");
                tb.setNumdest("");
            }
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite " + sqle.toString());
        }
        db.close();
        return tb;

    }

    public ArrayList<String> selectSTOCKziicmeOper2dest (String oper,String numdest) {
        ArrayList<String> listnumgsm = new ArrayList<String>();
        Log.w(TAG, "selectSTOCKziicme");
        where = KEY_OPERATION + " = \"" +oper+  "\" AND " +KEY_NUMDEST + " = \"" + numdest + "\"";
        String[] result_colonnes = new String[] {KEY_NUMGSM};
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_STOCKziicme,
                    result_colonnes,
                    where, whereArgs, groupBy, having, order);
            if (cursor.moveToFirst()) {
                do {
                    listnumgsm.add(cursor.getString(cursor.getColumnIndex(KEY_NUMGSM)));
                }
                while(cursor.moveToNext());
            }

        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite " + sqle.toString());
        }
        db.close();
        return listnumgsm;
    }
    public ArrayList<String> selectSTOCKziicmeOper2gsm (String oper,String numgsm) {
        ArrayList<String> listnumgsm = new ArrayList<String>();
        Log.w(TAG, "selectSTOCKziicme");
        where = KEY_OPERATION + " = \"" +oper+  "\" AND " +KEY_NUMGSM + " = \"" + numgsm + "\"";
        String[] result_colonnes = new String[] {KEY_NUMDEST};
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_STOCKziicme,
                    result_colonnes,
                    where, whereArgs, groupBy, having, order);
            if (cursor.moveToFirst()) {
                do {
                    listnumgsm.add(cursor.getString(cursor.getColumnIndex(KEY_NUMDEST)));
                }
                while(cursor.moveToNext());
            }

        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite " + sqle.toString());
        }
        db.close();
        return listnumgsm;
    }

        /******************************************************/
        /*   INSERT                                ************/
        /******************************************************/
        public void insertSTOCKziicmeOper1 (String oper,String audio,String photo)
            {

            Log.w(TAG, "insertKeySTOCKziicme");
            ContentValues values = new ContentValues();
            //values.put(KEY_ID, 1 );
            values.put(KEY_OPERATION,oper);
            values.put(KEY_AUDIO, audio);
            values.put(KEY_PHOTO, photo);

            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {

                db.insert(TABLE_STOCKziicme,null,values);
            }
            catch (SQLException sqle) {
                Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    public boolean deleteSTOCKziicmeoper2 (String oper,String numgsm,String numdest)
    {
        boolean retour=false;
        Log.w(TAG, "deleteSTOCKziicmeoper2");
        where = KEY_OPERATION + " = \"" +oper+"\" AND " +KEY_NUMGSM+"=\""+numgsm+"\" AND "+KEY_NUMDEST+"=\""+numdest+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            db.delete(TABLE_STOCKziicme, where, null);
            retour=true;
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return retour;
    }
    public void deleteSTOCKziicme (String oper)
    {

        Log.w(TAG, "insertKeySTOCKziicme");
        where = KEY_OPERATION + " = \"" +oper+  "\"";
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            db.delete(TABLE_STOCKziicme,where,null);
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public void insertSTOCKziicmeOper2 (String oper,String numgsm, String numdest)
    {

        Log.w(TAG, "insertKeySTOCKziicme");
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, 1 );
        values.put(KEY_OPERATION,oper);
        values.put(KEY_NUMGSM, numgsm);
        values.put(KEY_NUMDEST,numdest);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            db.insert(TABLE_STOCKziicme,null,values);
        }
        catch (SQLException sqle) {
            Log.w(TAG, "ERREUR sqlite = " + sqle.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

}

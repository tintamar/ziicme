package org.tselex.ziicme;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zappa_000 on 27/06/2015.
 */
public class ZJSONvalues implements Parcelable {
    public static final int GLOBAL_VERSION = 1;
    public static final String GLOBAL_SELECT = "SELECT";
    public static final String GLOBAL_INSERT = "INSERT";
    public static final String GLOBAL_DELETE = "DELETE";
    public static final String GLOBAL_UPDATE = "UPDATE";
    // informations extraites du TELEPHONY_SERVICE
    public  static String GLOBAL_NUMBER;
    public  static String GLOBAL_NOM;
    public  static String GLOBAL_ID;
    public  static String GLOBAL_SECRET_KEY;
    public  static String GLOBAL_SIM_COUNTRY;
    public  static int GLOBAL_AUTORISATION;
    public  static Context GLOBAL_CONTEXT;
    public  static Boolean GLOBAL_CONNEXION;


    public  static String GLOBAL_IDMYSQL;
    public  static String GLOBAL_NUMDESTINATAIRE;
    public  static String GLOBAL_IMEI;
    public  static String GLOBAL_SERIAL_NUMBER = null;
    public  static String GLOBAL_NETWORK_COUNTRY = null;

    public static int nbligne;
    public  static String detdemnumgsm;
    public  static  String numeroenvoi;
    public static int detdemnbligne=0;
    public static String audioenvoi;
    public static String photoenvoi;
    public static Bitmap photobitmap;
    public static boolean modifaudio;
    public static boolean modifphoto;
    public static String nom;
    public static String num2;
    public static String num3;
    public static String mail1;
    public static String mail2;
    public static String mail3;
    public static String adresse;
    public static String codpost;
    public static String ville;
    public static String pays;
    public static String phototitre;
    public static String photosrcpath;

    public static String audiotitre;
    public static String audiosrcpath;
    public static  String travphototitre;
    public static  String travphotosrcpath;
    public static Bitmap travphotobitmap;
    public static  String travphotoenvoi;
    public static  String travaudiotitre;
    public static  String travaudiosrcpath;
    public static  String travaudioenvoi;
    private int mData;






/********************************************************************************************/
/*                   Methodes obligatoires pour parcelable objet    */
    /********************************************************************************************/
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Creator<ZJSONvalues> CREATOR = new Creator<ZJSONvalues>() {
        public ZJSONvalues createFromParcel(Parcel in) {
            return new ZJSONvalues(in);
        }

        public ZJSONvalues[] newArray(int size) {
            return new ZJSONvalues[size];
        }
    };
    private ZJSONvalues(Parcel in) {
        mData = in.readInt();
    }
    public ZJSONvalues() {

    }


}

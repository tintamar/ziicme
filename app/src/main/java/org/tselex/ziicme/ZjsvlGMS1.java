package org.tselex.ziicme;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by zappa_000 on 27/06/2015.
 */
public class ZjsvlGMS1 {
     //infos liees a GSMziicme mysql
    // valeurs d'appel
    public int cidGSM;
    public int cautorisationGSM;
    public String cnumgsmGSM;
    public String cnumdestGSM;
    public String cnomGSM;
    public String cnum2GSM;
    public String cnum3GSM;
    public String cmail1GSM;
    public String cmail2GSM;
    public String cmail3GSM;
    public String cadresseGSM;
    public String ccodpostGSM;
    public String cvilleGSM;
    public String cpaysGSM;
    public String cphotoGSM;
    public String cphototitreGSM;
    public String cphotosrcpathGSM ;
    public String cphotoenvoiGSM;
    public String csonnerietitreGSM;
    public String csonneriesrcpathGSM;
    public String csonnerieenvoiGSM;
    public String csonnerieGSM;
    public Bitmap cbitmapGSM;
    public String cmsgretour;
        // structure table
    public int ligneGSM;

    /***********************/
    //infos liees toutes les tables
    public String operation;
    public String urlziicmeGSM = "http://www.tselex.org/ziicme/GestionGSMziicme.php";
    public String cdret = "";
    public String textdest = "";

    public String host = "";
    public String username ="";
    public String password = "";
    public String srcFilePathphoto ="";
    public String srcFilePathaudio ="";
    public String desFileNamephoto = "";
    public String desFileNameaudio = "";
    public String desFileNameaudioURI = "";
    public String desDirectory1 ="";
    public String directory_path = "";
    public boolean modifaudio = false;
    public boolean modifphoto = false;
    public String FileNamephoto = "";
    public String FileNameaudio = "";
    public Context context;
    }

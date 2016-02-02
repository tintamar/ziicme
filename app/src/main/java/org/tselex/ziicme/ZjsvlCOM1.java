package org.tselex.ziicme;

/**
 * Created by zappa_000 on 27/06/2015.
 */
public class ZjsvlCOM1 {
    //infos liees a GSMziicme mysql
    // valeurs d'appel
    /***********************/
    //infos liees a COMziicme mysql
    /*********************/
    // valeurs d'appel
    public int cidCOM;
    public String cnumgsmCOM;
    public String cnumdestCOM;
    // structure table
    public int ligneCOM;
    public int idCOM[];
    public String numgsmCOM[];
    public String numdestCOM[];
    public String nomGSM[];

    /***********************/
    //infos liees toutes les tables
    public String operation;
    public String urlziicmeCOM = "http://www.tselex.org/ziicme/GestionCOMziicme.php";
    public String urlziicmeCOMGSM = "http://www.tselex.org/ziicme/GestionCOMGSMziicme.php";
    public String cdret = "";
    public String textdem = "";
}

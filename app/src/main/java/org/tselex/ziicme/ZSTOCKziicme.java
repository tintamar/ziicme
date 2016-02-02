package org.tselex.ziicme;

/**
 * Created by zappa_000 on 18/07/2015.
 */
public class ZSTOCKziicme {
    private static String TAG = "======> ZSECUREziicme class =====>    ";

    public String operation;
    public String numgsm;
    public String nom;
    public String numdest;
    public String ftpaudioFile;
    public String ftpphotoFile;



    public ZSTOCKziicme (){
    }
    public String getOperation() {return operation;}
    public String getNumgsm() {return numgsm;}
    public String getNom() {return nom;}
    public String getNumdest() {return numdest;}
    public String getFtpaudioFile() {return ftpaudioFile;}
    public String getFtpphotoFile() {return ftpphotoFile;}



    public void setOperation(String operation) {this.operation = operation;}
    public void setNumgsm(String numgsm) {this.numgsm = numgsm;}
    public void setNom(String nom) {this.nom = nom ;}
    public void setNumdest(String numdest) {this.numdest = numdest ;}
    public void setFtpaudioFile(String ftpaudioFile) {this.ftpaudioFile = ftpaudioFile ;}
    public void setFtpphotoFile(String ftpphotoFile) {this.ftpphotoFile = ftpphotoFile ;}

}

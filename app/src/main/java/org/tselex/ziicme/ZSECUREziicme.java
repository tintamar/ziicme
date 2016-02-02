package org.tselex.ziicme;

/**
 * Created by zappa_000 on 26/06/2015.
 */
public class ZSECUREziicme {
    private static String TAG = "======> ZSECUREziicme class =====>    ";
    public int id;
    public String numgsm;
    public String secretkey;
    public int autorisation;
    public String nom;
    public String num2;
    public String num3;
    public String mail1;
    public String mail2;
    public String mail3;
    public String adresse;
    public String codpost;
    public String ville;
    public String pays;
    public String phototitre;
    public String photosrcpath;
    public String photoenvoi;
    public String sonnerietitre;
    public String sonneriesrcpath;
    public String sonnerieenvoi;
    public String idmysql;


    public ZSECUREziicme (){
    }
    public int getId(){
        return id;
    }
    public String getNumGSM(){
        return numgsm;
    }
    public String getSecretKey(){return secretkey;}
    public int getAutorisation(){
        return autorisation;
    }
    public String getNom() {return nom;}
    public String getNum2() {return num2;}
    public String getNum3() {return num3;}
    public String getMail1() {return mail1;}
    public String getMail2() {return mail2;}
    public String getMail3() {return mail3;}
    public String getAdresse() {return adresse;}
    public String getCodpost() {return codpost;}
    public String getVille() {return ville;}
    public String getPays() {return pays;}
    public String getPhototitre() {return phototitre;}
    public String getPhotosrcpath() {return photosrcpath;}
    public String getPhotoenvoi() {return photoenvoi;}
    public String getAudiotitre() {return sonnerietitre;}
    public String getAudiosrcpath() {return sonneriesrcpath;}
    public String getAudioenvoi() {return sonnerieenvoi;}
    public String getIdmysql() {return idmysql;}


    public void setId(int ident) {
        this.id = ident;
    }
    public void setNumGSM(String numgsm) {
        this.numgsm = numgsm;
    }
    public void setSecretKey(String secretkey) {
        this.secretkey = secretkey;
    }
    public void setAutorisation(int autorisation) {
        this.autorisation= autorisation;
    }
    public void setNom(String nom) {this.nom = nom ;}
    public void setNum2(String num2) {this.num2 = num2 ;}
    public void setnum3(String num3) {this.num3 = num3 ;}
    public void setMail1(String mail1) {this.mail1 = mail1 ;}
    public void setMail2(String mail2) {this.mail2 = mail2 ;}
    public void setMail3(String mail3) {this.mail3 = mail3 ;}
    public void setAdresse(String adresse) {this.adresse = adresse ;}
    public void setCodpost(String codpost) {this.codpost = codpost ;}
    public void setVille(String ville) {this.ville = ville ;}
    public void setPays(String pays) {this.pays = pays ;}
    public void setPhototitre(String phototitre) {this.phototitre = phototitre ;}
    public void setPhotosrcpath(String photosrcpath) {this.photosrcpath = photosrcpath ;}
    public void setPhotoenvoi(String photoeenvoi) {this.photoenvoi = photoeenvoi ;}
    public void setAudiotitre(String sonnerietitre) {this.sonnerietitre = sonnerietitre ;}
    public void setAudiosrcpath(String sonneriesrcpath) {this.sonneriesrcpath = sonneriesrcpath ;}
    public void setAudioenvoi(String sonnerieenvoi) {this.sonnerieenvoi = sonnerieenvoi ;}
    public void setIdmysql(String idmysql) {this.idmysql = idmysql ;}
 }

package org.tselex.ziicme;

/**
 * Created by zappa_000 on 07/07/2015.
 */
public class Zcontact {
    public String nom;
    public String numgsm;
    public boolean selected=false;
    public Zcontact(String nom, String numgsm, boolean selected) {
        this.nom = nom;
        this.numgsm=numgsm;
        this.selected=selected;
    }
    public String getNom() {return nom;}
    public String getNumgsm() {return numgsm;}
    public Boolean isSelected() {return selected;}
    public void setNom(String nom) {
        this.nom=nom;
    }
    public void setNumgsm(String numgsm) {
        this.numgsm=numgsm;
    }
    public void setSelected(boolean selected) {
        this.selected=selected;
    }
}

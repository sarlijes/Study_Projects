package varausjarjestelma;

import java.util.Date;
import java.util.List;

public class VarattuHuone {

    Varaus varaus;
    Huone huone;
    List<VaratunHuoneenLisavaruste> lisavarusteet;

    int id;
    int varaus_id;
    int huone_id;
    Date alku;
    Date loppu;

    public VarattuHuone(int id, int varaus_id, int huone_id, Date alku, Date loppu) {
        this.varaus = varaus;
        this.huone = huone;
        this.lisavarusteet = lisavarusteet;
        this.id = id;
        this.varaus_id = varaus_id;
        this.huone_id = huone_id;
        this.alku = alku;
        this.loppu = loppu;
    }

    public int getId() {
        return id;
    }

    public int getVaraus_id() {
        return varaus_id;
    }

    public int getHuone_id() {
        return huone_id;
    }

    public Date getAlku() {
        return alku;
    }

    public Date getLoppu() {
        return loppu;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVaraus_id(int varaus_id) {
        this.varaus_id = varaus_id;
    }

    public void setHuone_id(int huone_id) {
        this.huone_id = huone_id;
    }

    public void setAlku(Date alku) {
        this.alku = alku;
    }

    public void setLoppu(Date loppu) {
        this.loppu = loppu;
    }


    
    

}

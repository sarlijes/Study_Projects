package varausjarjestelma;

public class VaratunHuoneenLisavaruste {

    VarattuHuone varattuhuone;
    Lisavaruste lisavaruste;

    int id;
    int varattuhuone_id;
    int lisavaruste_id;

    public VaratunHuoneenLisavaruste(VarattuHuone varattuhuone, Lisavaruste lisavaruste, int id, int varattuhuone_id, int lisavaruste_id) {
        this.varattuhuone = varattuhuone;
        this.lisavaruste = lisavaruste;
        this.id = id;
        this.varattuhuone_id = varattuhuone_id;
        this.lisavaruste_id = lisavaruste_id;
    }

    public int getId() {
        return id;
    }

    public int getVarattuhuone_id() {
        return varattuhuone_id;
    }

    public int getLisavaruste_id() {
        return lisavaruste_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVarattuhuone_id(int varattuhuone_id) {
        this.varattuhuone_id = varattuhuone_id;
    }

    public void setLisavaruste_id(int lisavaruste_id) {
        this.lisavaruste_id = lisavaruste_id;
    }
    
    
    
    
}

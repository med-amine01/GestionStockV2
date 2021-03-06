package Classes;

public class Ent {

    private int id;
    private String nomfour;
    private int idpiece;
    private int qte;
    private String date;
    private double montant;
    private int idfour;
    private String confent;


    public Ent(int id, int idpiece, String nomfour, int qte, String date,double montant) {
        this.id = id;
        this.nomfour = nomfour;
        this.idpiece = idpiece;
        this.qte = qte;
        this.date = date;
        this.montant = montant;
    }

    public Ent(int id, int idpiece, String nomfour, int qte, String date,double montant, int idfour) {
        this.id = id;
        this.nomfour = nomfour;
        this.idpiece = idpiece;
        this.qte = qte;
        this.date = date;
        this.montant = montant;
        this.idfour = idfour;
    }

    public Ent(int id, int idpiece, String nomfour, int qte, String date,double montant, String confent) {
        this.id = id;
        this.nomfour = nomfour;
        this.idpiece = idpiece;
        this.qte = qte;
        this.date = date;
        this.montant = montant;
        this.idfour = idfour;
        this.confent = confent;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomfour() {
        return nomfour;
    }

    public void setNomfour(String nomfour) {
        this.nomfour = nomfour;
    }

    public int getIdpiece() {
        return idpiece;
    }

    public void setIdpiece(int idpiece) {
        this.idpiece = idpiece;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getIdfour() {
        return idfour;
    }

    public void setIdfour(int idfour) {
        this.idfour = idfour;
    }

    public String getConfent() {
        return confent;
    }

    public void setConfent(String confent) {
        this.confent = confent;
    }
}

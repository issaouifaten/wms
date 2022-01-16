package com.darryncampbell.dwgettingstartedjava.Model.Colis;

public class PreparationColisLigne {
    String NoDoc;
    String TypeColis;
    String NoColis;
    String PoidsMax;

    public PreparationColisLigne(String noDoc, String typeColis, String noColis, String poidsMax) {
        NoDoc = noDoc;
        TypeColis = typeColis;
        NoColis = noColis;
        PoidsMax = poidsMax;
    }

    public String getPoidsMax() {
        return PoidsMax;
    }

    public void setPoidsMax(String poidsMax) {
        PoidsMax = poidsMax;
    }

    public String getNoColis() {
        return NoColis;
    }

    public void setNoColis(String noColis) {
        NoColis = noColis;
    }



    public String getNoDoc() {
        return NoDoc;
    }

    public void setNoDoc(String noDoc) {
        NoDoc = noDoc;
    }

    public String getTypeColis() {
        return TypeColis;
    }

    public void setTypeColis(String typeColis) {
        TypeColis = typeColis;
    }
}

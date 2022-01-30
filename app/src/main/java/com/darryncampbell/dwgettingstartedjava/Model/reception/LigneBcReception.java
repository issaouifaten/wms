package com.darryncampbell.dwgettingstartedjava.Model.reception;

public class LigneBcReception {
    public String NoDoc;

    public String EAN;


    public String Article;
    public String Piece;
    public String Quantite;
    public String QuantiteScan;
    public String NbrExemplairePaquet;
    public String NbrPaquetCouche;
    public String Epaisseur;

    public String getNbrExemplairePaquet() {
        return NbrExemplairePaquet;
    }

    public void setNbrExemplairePaquet(String nbrExemplairePaquet) {
        NbrExemplairePaquet = nbrExemplairePaquet;
    }

    public String getNbrPaquetCouche() {
        return NbrPaquetCouche;
    }

    public void setNbrPaquetCouche(String nbrPaquetCouche) {
        NbrPaquetCouche = nbrPaquetCouche;
    }

    public String getEpaisseur() {
        return Epaisseur;
    }

    public void setEpaisseur(String epaisseur) {
        Epaisseur = epaisseur;
    }

    public LigneBcReception(String noDoc, String EAN, String article, String piece, String quantite, String quantiteScan, String nbrExemplairePaquet, String nbrPaquetCouche, String epaisseur) {
        NoDoc = noDoc;
        this.EAN = EAN;
        Article = article;
        Piece = piece;
        Quantite = quantite;
        QuantiteScan = quantiteScan;
        NbrExemplairePaquet = nbrExemplairePaquet;
        NbrPaquetCouche = nbrPaquetCouche;
        Epaisseur = epaisseur;
    }

    public String getNoDoc() {
        return NoDoc;
    }

    public void setNoDoc(String noDoc) {
        NoDoc = noDoc;
    }

    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public String getPiece() {
        return Piece;
    }

    public void setPiece(String piece) {
        Piece = piece;
    }

    public String getQuantite() {
        return Quantite;
    }

    public void setQuantite(String quantite) {
        Quantite = quantite;
    }

    public String getQuantiteScan() {
        return QuantiteScan;
    }

    public void setQuantiteScan(String quantiteScan) {
        QuantiteScan = quantiteScan;
    }
}

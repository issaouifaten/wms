package com.darryncampbell.dwgettingstartedjava.LocalBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.darryncampbell.dwgettingstartedjava.Model.Colis.LigneColis;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.LigneColisCreated;
import com.darryncampbell.dwgettingstartedjava.Model.Colis.PreparationColisLigne;
import com.darryncampbell.dwgettingstartedjava.Model.prelevement.LigneBcPrelevement;
import com.darryncampbell.dwgettingstartedjava.Model.prelevement.Value;
import com.darryncampbell.dwgettingstartedjava.Model.reception.LigneBcReception;

import java.util.ArrayList;
import java.util.List;

public class Helper extends SQLiteOpenHelper {

    public Helper(Context context) {
        super(context, "BaseACHATV4", null, 1);
    }

    //LigneBE_Class(String codeArticle, String designationArticle, String quantite)
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE List_Command_Prelevement" +
                "(_id INTEGER PRIMARY KEY,Code TEXT  ,ClientName TEXT, Aux TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Ligne_Command_Prelevement" +
                "(_id INTEGER PRIMARY KEY,noDoc TEXT  ,Article TEXT,EAN TEXT ,Quantite INTEGER, QuantiteScan INTEGER,Piece TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Valide_Command_Prelevement" +
                "(_id INTEGER PRIMARY KEY,noDoc TEXT  ,Code TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE List_Command_Reception" +
                "(_id INTEGER PRIMARY KEY,Code TEXT  ,ClientName TEXT, Aux TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Ligne_Command_Reception" +
                "(_id INTEGER PRIMARY KEY,noDoc TEXT  ,Article TEXT,EAN TEXT ,Quantite INTEGER, QuantiteScan INTEGER,Piece TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Valide_Command_Reception" +
                "(_id INTEGER PRIMARY KEY,noDoc TEXT  ,Code TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE List_Command_Prelevement_Colisage" +
                "(_id INTEGER PRIMARY KEY,NoDoc TEXT  ,TypeColis TEXT, NoColis TEXT,PoidsMax TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Valide_Command_Prelevement_Colisage" +
                "(_id INTEGER PRIMARY KEY,noDoc TEXT  ,Code TEXT)");
        //(String noDoc, String noColis, String article, String quantite, String poidsUnite, String quantiteScan)
        sqLiteDatabase.execSQL("CREATE TABLE Ligne_Colisage_Prelevement" +
                "(_id INTEGER PRIMARY KEY,noDoc TEXT  ,Article TEXT,noColis TEXT ,Quantite INTEGER, QuantiteScan INTEGER,Piece TEXT,poidsUnite TEXT,EAN TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE Ligne_Colis_Created "+
                "(_id INTEGER PRIMARY KEY,NoDoc TEXT,NoCommande TEXT  ,NoColis TEXT ,PoidsMax TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List_Command_Prelevement_Colisage");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Valide_Command_Prelevement_Colisage");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Ligne_Colisage_Prelevement");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List_Command_Prelevement");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Ligne_Command_Prelevement");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Valide_Command_Prelevement");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Ligne_Command_Reception");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List_Command_Reception");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Ligne_Colis_Created");
        onCreate(sqLiteDatabase);
    }

    public void AddBonCommandePrelevement(Value c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Code", c.getNo_());
        cv.put("ClientName", c.getClient());
        cv.put("Aux", c.getAuxiliaryIndex1());

        db.insert("List_Command_Prelevement", null, cv);

    }
    //   "(_id INTEGER PRIMARY KEY,NoDoc TEXT,NoCommande TEXT  ,NoColis TEXT ,PoidsMax TEXT)");
    public void AddLigneColisCreated(LigneColisCreated c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NoDoc", c.getNoDoc());
        cv.put("NoCommande", c.getNoCommande());
        cv.put("NoColis", c.getNoColis());


        db.insert("Ligne_Colis_Created", null, cv);

    }
    public void AddBonCommandePrelevementColisage(PreparationColisLigne c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NoDoc", c.getNoDoc());
        cv.put("TypeColis", c.getTypeColis());
        cv.put("NoColis", c.getNoDoc());
        cv.put("PoidsMax", c.getPoidsMax());

        db.insert("List_Command_Prelevement_Colisage", null, cv);

    }
    public void AddBonCommandeReception(com.darryncampbell.dwgettingstartedjava.Model.reception.Value c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Code", c.getNoDoc());
        cv.put("ClientName", c.getClient());
        cv.put("Aux", c.getAuxiliaryIndex1());

        db.insert("List_Command_Reception", null, cv);

    }
    public Cursor getListCommandPrelevement() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM List_Command_Prelevement  ", null);
        return c;

    }
    public Cursor getListColisCreated() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Ligne_Colis_Created  ", null);
        return c;

    }
    public Cursor getListCommandPrelevementColisage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM List_Command_Prelevement_Colisage  ", null);
        return c;

    }
    public Cursor getListCommandReception() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM List_Command_Reception  ", null);
        return c;

    }
    public void RemoveBonCommandPrelevement(Value c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("List_Command_Prelevement", "Code='" + c.getNo_() + "'", null);
    }
    public void RemoveBonCommandPrelevementColisage(PreparationColisLigne c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("List_Command_Prelevement_Colisage", "NoDoc='" + c.getNoDoc() + "'", null);
    }
    public void RemoveBonCommandReception(com.darryncampbell.dwgettingstartedjava.Model.reception.Value c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("List_Command_Reception", "Code='" + c.getNoDoc() + "'", null);
    }

    public void UpdateListCommand(Value c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Code", c.getNo_());
        cv.put("ClientName", c.getClient());
        cv.put("Aux", c.getAuxiliaryIndex1());


        db.update("List_Command_Prelevement", cv, "Code='" + c.getNo_() + "'", null);


    }
    public void UpdateBonCommandePrelevementColisage(PreparationColisLigne c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NoDoc", c.getNoDoc());
        cv.put("TypeColis", c.getTypeColis());
        cv.put("NoColis", c.getNoColis());
        cv.put("PoidsMax", c.getPoidsMax());
        db.update("List_Command_Prelevement_Colisage", cv, "NoDoc='" + c.getNoDoc() + "'", null);



    }

    public boolean testExistBonCommandPrelevement(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from List_Command_Prelevement where Code='" + code + "'  ", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }
    public boolean testExistBonCommandPrelevementColisage(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from List_Command_Prelevement_Colisage where NoDoc='" + code + "'  ", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }
    public boolean testExistBonCommandReception(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from List_Command_Reception where Code='" + code + "'  ", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }
    ////ligne
    public void AddLigneBonCommandePrelevement(LigneBcPrelevement c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("Piece", c.getPiece());
        cv.put("EAN", c.getEAN());

        db.insert("Ligne_Command_Prelevement", null, cv);

    }
    public void AddLigneBonCommandePrelevementColisage(LigneColis c) {
       // Y,noDoc TEXT  ,Article TEXT,noColis TEXT ,Quantite INTEGER, QuantiteScan INTEGER,Piece TEXT,poidsUnite TEXT
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("poidsUnite", c.getPoidsUnite());
        cv.put("noColis", c.getNoColis());
        cv.put("EAN", c.getEAN());


        db.insert("Ligne_Colisage_Prelevement", null, cv);

    }
    //Ligne_Command_Reception
    public void AddLigneBonCommandeReception(LigneBcReception c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("Piece", c.getPiece());
        cv.put("EAN", c.getEAN());

        db.insert("Ligne_Command_Reception", null, cv);

    }
    public void UpdateLigneBonCommandePrelevement(LigneBcPrelevement c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("Piece", c.getPiece());
        cv.put("EAN", c.getEAN());


        db.update("Ligne_Command_Prelevement", cv, "Article='" + c.getArticle() + "' and noDoc='" + c.getNoDoc() + "' ", null);


    }
    public void UpdateLigneBonCommandePrelevementColisage(LigneColis c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("poidsUnite", c.getPoidsUnite());
        cv.put("noColis", c.getNoColis());
        cv.put("EAN", c.getEAN());


        db.update("Ligne_Colisage_Prelevement", cv, "Article='" + c.getArticle() + "' and noColis='" + c.getNoColis() + "' and  noDoc='"+c.getNoDoc()+"'", null);


    }
    public void UpdateLigneBonCommandeReception(LigneBcReception c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("Piece", c.getPiece());
        cv.put("EAN", c.getEAN());


        db.update("Ligne_Command_Reception", cv, "Article='" + c.getArticle() + "' and noDoc='" + c.getNoDoc() + "' ", null);


    }
    public Cursor getListLigneCommandPrelevement() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Ligne_Command_Prelevement  ", null);
        return c;

    }
    public Cursor getListLigneCommandPrelevementColisage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Ligne_Colisage_Prelevement  ", null);
        return c;

    }
    public Cursor getListLigneCommandReception() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Ligne_Command_Reception  ", null);
        return c;

    }
    public void UpdateLignePrelevement(LigneBcPrelevement c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Article", c.getArticle());
        cv.put("Quantite", c.getQuantite());
        cv.put("QuantiteScan", c.getQuantiteScan());
        cv.put("noDoc", c.getNoDoc());
        cv.put("Piece", c.getPiece());
        cv.put("EAN", c.getEAN());


        db.update("Ligne_Command_Prelevement", cv, "Article='" + c.getArticle() + "'and noDoc ='" + c.getNoDoc() + "'", null);


    }

    public void UpdateLignePrelevementByScan(String ean) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL("UPDATE Ligne_Command_Prelevement SET QuantiteScan=QuantiteScan+1 where EAN='" + ean + "' and QuantiteScan<Quantite");

    }
    public void UpdateLignePrelevementColisageByScan(String ean) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL("UPDATE Ligne_Colisage_Prelevement SET QuantiteScan=QuantiteScan+1 where EAN='" + ean + "' and QuantiteScan<Quantite");

    }
    public void UpdateLigneReceptionByScan(String ean) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.execSQL("UPDATE Ligne_Command_Reception SET QuantiteScan=QuantiteScan+1 where EAN='" + ean + "' and QuantiteScan<Quantite");

    }


    public LigneBcPrelevement getLignePrelevement(String code, String document) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * from Ligne_Command_Prelevement where Code='" + code + "'  and noDoc ='" + document + "'", null);
        List<String> tables = new ArrayList<>();
        LigneBcPrelevement ligneBcPrelevement = null;
        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {

            ligneBcPrelevement.setPiece(c.getString(c.getColumnIndex("Piece")));
            ligneBcPrelevement.setQuantite(c.getString(c.getColumnIndex("Quantite")));
            ligneBcPrelevement.setArticle(c.getString(c.getColumnIndex("Code")));
            ligneBcPrelevement.setNoDoc(c.getString(c.getColumnIndex("noDoc")));
            ligneBcPrelevement.setQuantiteScan(c.getString(c.getColumnIndex("QuantiteScan")));
            ligneBcPrelevement.setEAN(c.getString(c.getColumnIndex("EAN")));
        }
        return ligneBcPrelevement;
    }

    public Cursor getLignePrelevementArticle(String code) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * from Ligne_Command_Prelevement where EAN='" + code + "'", null);
        return c;
    }

    public Cursor getLignePrelevementColisageArticle(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * from Ligne_Colisage_Prelevement where EAN='" + code + "'", null);
        return c;
    }
    public Cursor getLigneReceptionArticle(String code) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * from Ligne_Command_Reception where EAN='" + code + "'", null);
        return c;
    }

    public void AddValideCommandePrelevement(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Code", code);


        db.insert("Valide_Command_Prelevement", null, cv);

    }

    public void AddValideCommandePrelevementColisage(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Code", code);


        db.insert("Valide_Command_Prelevement_Colisage", null, cv);

    }
    public void AddValideCommandeReception(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Code", code);


        db.insert("Valide_Command_Reception", null, cv);

    }
    public boolean testExistValideBonCommandPrelevement() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from Valide_Command_Prelevement ", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }

    public void DeleteValideBonCommandPrelevement() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Valide_Command_Prelevement", "", null);
    }

    public void DeleteValideBonCommandPrelevementColisage() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Valide_Command_Prelevement_Colisage", "", null);
    }

    public void DeleteLigneBonCommandPrelevement() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Ligne_Command_Prelevement", "", null);
    }
    public void DeleteLigneBonCommandPrelevementColisage() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Ligne_Colisage_Prelevement", "", null);
    }

    public void DeleteListBonCommandPrelevement() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("List_Command_Prelevement", "", null);
    }
    public void DeleteListBonCommandPrelevementColisage() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("List_Command_Prelevement_Colisage", "", null);
    }
    public void DeleteValideBonCommandReception() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Valide_Command_Reception", "", null);
    }

    public void DeleteLigneBonCommandReception() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Ligne_Command_Reception", "", null);
    }

    public void DeleteListBonCommandReception() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("List_Command_Reception", "", null);
    }
    public void DeleteLigneColisCreated() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("Ligne_Colis_Created", "", null);
    }
}

package org.cs.amoba.client.modell;

//
// Egy játékost megvalósító osztály
//
public class TJatekos extends Object
{

    TSzin szin;           // a j�t�kos szine
    TLepesForras lf;      // ki m�k�dteti a j�t�kost
    String nev;           // a j�t�kos neve
    public int movecnt;   // eddig ennyit l�pett a j�t�kos

    // Konstruktor
    public TJatekos()
    {
        szin = new TSzin();          // objektumok
        lf = new TLepesForras();   // l�trehoz�sa
    }

    // Beállítja a játékos adatait
    public void Set(TSzin sz, // a színe
            TLepesForras f,   // a lépésforrása
            String n)         // a neve
    {
        szin.Set(sz.Get());
        lf.lepesforras = f.lepesforras;
        nev = n;
        movecnt = 0;
    } // end Set()
} // end TJatekos class


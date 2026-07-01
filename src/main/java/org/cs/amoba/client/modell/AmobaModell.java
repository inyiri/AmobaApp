/**
 * Ez az osztály adja a modell felületet
 *
 */
package org.cs.amoba.client.modell;

import org.cs.amoba.client.*;

public class AmobaModell
{

    public TLepesForras lf1;      // induló paraméter
    public TLepesForras lf2;      // induló paraméter
    public TLepesForras kezdoLF;  // A kezdőjátékos lépésforrása
    public TSzin AKezdoSzine;     // induló paraméter
    public int iTamadasFaktor;    // induló paraméter
    public TAmoba amoba;          // az amőba agya
    public StringBuffer uzenet;   // üzenet a panelre

    public int pirosPontjai;      //
    public int zoldPontjai;       // a játék állása

    public int presentSzin;             // ez a játékos van lépés alatt
    public TLepesForras presentLF;      // a lépés alatti játékos lépésforrása
    public boolean startedByStartBtn;   // A start gomb van nyomva (megy a játék)


    /**
     *
     */
    public AmobaModell()
    {
        lf1 = new TLepesForras();
        lf2 = new TLepesForras();
        AKezdoSzine = new TSzin();
        uzenet = new StringBuffer("");
        iTamadasFaktor = 4;

        amoba = new TAmoba(); // Az amőba engine

        pirosPontjai = 0;
        zoldPontjai  = 0;
        startedByStartBtn = false;

    }

    /**
     *
     * @param lForras1
     * @param lForras2
     * @param kezdoSzine
     * @param tamadasFaktor
     */
    public void setParameters(String lForras1, String lForras2,
            String kezdoSzine,
            String tamadasFaktor)
    {
        lf1 = new TLepesForras();
        lf2 = new TLepesForras();
        AKezdoSzine = new TSzin();
        uzenet = new StringBuffer("");

        // Lépésforrások
        if (lForras1.equals("E"))
        {
            lf1.lepesforras = TLepesForras.EMBER;
            uzenet.append("Ember");
        } else
        {
            lf1.lepesforras = TLepesForras.COMPUTER;
            uzenet.append("Computer");
        }

        if (lForras2.equals("E"))
        {
            lf2.lepesforras = TLepesForras.EMBER;
            uzenet.append("/Ember");
        } else
        {
            lf2.lepesforras = TLepesForras.COMPUTER;
            uzenet.append("/Computer");
        }

        // A kezdőjátékos színe
        uzenet.append(" (kezdő: ");
        if (kezdoSzine.equals("P"))
        {
            AKezdoSzine.szin = TSzin.PIROS;
            presentSzin = TSzin.PIROS;
            kezdoLF = lf1;
            uzenet.append("Piros");
        } else
        {
            AKezdoSzine.szin = TSzin.KEK;
            presentSzin = TSzin.KEK;
            kezdoLF = lf2;
            uzenet.append("Zöld");
        }
        presentLF = kezdoLF;

        // A támadásfaktor beállítása
        try
        {
            iTamadasFaktor = Integer.parseInt(tamadasFaktor);
        } catch (Exception e)
        {
            iTamadasFaktor = 4;
        }

    } // end setParameters method


    //
    // Visszaadja a következő lépés forrását, ha megadjuk, hogy most ki lépett
    //
    public TLepesForras getNextLepesForras(TLepesForras lf)
    {
        return (lf == lf1) ? lf2 : lf1;
    }

    //
    // Visszaadja a következő lépés színét, ha megadjuk, hogy most milyen szin lépett
    //
    public int getNextSzin( int presentSzin )
    {
        return (presentSzin == TSzin.KEK) ? TSzin.PIROS : TSzin.KEK;
    }

    //
    // Visszaadja, hogy most ember lép-e?
    //
    public boolean emberLepMost()
    {
        if ( presentLF.lepesforras == TLepesForras.EMBER )
        {
            return true;
        }
        return false;
    }
  

    //
    // Beállítja az amoba objektumot
    //
    public void setAmobaGame()
    {
        amoba.SetGame(iTamadasFaktor, AKezdoSzine, lf1, lf2);
    }

  
}

package org.cs.amoba.client.modell;

//
// Az amőba játék alakulását kíséri figyelemmel ez az osztály
//
public class TSzinTomb extends Object
{

    TSzin t[][];      // maga a p�lya t�mbje
    int palyameret;   // a p�lya m�rete

    // Konstruktor
    public TSzinTomb(int pm) // pm = p�lyam�ret param�ter
    {
        palyameret = pm;
        int i, j;

        t = new TSzin[palyameret][];
        for (i = 0; i < palyameret; i++)
        {
            t[i] = new TSzin[palyameret];
            for (j = 0; j < palyameret; j++)
            {
                t[i][j] = new TSzin();
                t[i][j].Set(TSzin.URES);
            }
        }
    } // end TSzinTomb() konstruktor

    // Kinulázza a tömböt
    public void Nullaz()
    {
        int i, j;
        for (i = 0; i < palyameret; i++)
        {
            for (j = 0; j < palyameret; j++)
            {
                t[i][j].Set(TSzin.URES);
            }
        }
    } // end Nullaz()

    // �rt�kad�s
    public void Set(int dx, int dy, int sz)
    {
        (t[dx][dy]).Set(sz);
    }

    // Érték lekérdezés
    public int Get(int dx, int dy)
    {
        return ((t[dx][dy]).Get());
    }
} // end TSzinTomb class

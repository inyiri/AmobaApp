package org.cs.amoba.client.modell;

//
// Az amőba játék alakulását kíséri figyelemmel ez az osztály
//
public class TSzinTomb extends Object
{

    // Store color values as int[][] instead of TSzin[][] to avoid
    // GWT 2.13.1 ArrayStoreException with multi-dimensional object arrays
    int[][] t;
    int palyameret;

    // Konstruktor
    public TSzinTomb(int pm)
    {
        palyameret = pm;
        t = new int[palyameret][palyameret];
        Nullaz();
    }

    // Kinulázza a tömböt
    public void Nullaz()
    {
        for (int i = 0; i < palyameret; i++)
        {
            for (int j = 0; j < palyameret; j++)
            {
                t[i][j] = TSzin.URES;
            }
        }
    }

    // Értékadás
    public void Set(int dx, int dy, int sz)
    {
        t[dx][dy] = sz;
    }

    // Érték lekérdezés
    public int Get(int dx, int dy)
    {
        return t[dx][dy];
    }

} // end TSzinTomb class

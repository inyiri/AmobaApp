package org.cs.amoba.client.modell;

//
// Az értékelő függvény egyik segédtömbje
//
public class TValue extends Object
{
    // maga a tömb (3 dimenzi�s)

    int[][][] t;
    int dx, dy, dz; // dimenzi�k

    // Konstruktor
    public TValue(int pdx, int pdy, int pdz) // a pdx, pdy, pdz a dimenziok
    {
        int i, j, k;

        dx = pdx;
        dy = pdy;
        dz = pdz;

        t = new int[dx][][];
        for (i = 0; i < dx; i++)
        {
            t[i] = new int[dx][];
            for (j = 0; j < dy; j++)
            {
                t[i][j] = new int[dz];
                for (k = 0; k < dz; k++)
                {
                    t[i][j][k] = 0;
                }
            }
        }

    } // end TValue() konstruktor

    // A tömb kinullázása
    public void Nullaz()
    {
        int i, j, k;
        for (i = 0; i < dx; i++)
        {
            for (j = 0; j < dy; j++)
            {
                for (k = 0; k < dz; k++)
                {
                    t[i][j][k] = 0;
                }
            }
        }

    }


    // Egy értékadás
    public void Set(int pdx, int pdy, int pdz, int v)
    {
        t[pdx][pdy][pdz] = v;
    }

    // �rt�k lek�rdez�s
    public int Get(int pdx, int pdy, int pdz)
    {
        return (t[pdx][pdy][pdz]);
    }
} // end TValue class

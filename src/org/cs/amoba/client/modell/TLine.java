package org.cs.amoba.client.modell;

//
// Egy-egy irányban van-e már öt azonos jel 
// kiértékelését segítő tömb
//
public class TLine extends Object
{

    // Egy 4 dimenziós tömb
    int[][][][] t;
    int dx, dy, dz, dv; // dimenzi�k

    // Konstruktor
    public TLine(int pdx, int pdy, int pdz, int pdv)
    {
        int i, j, k, l;
        dx = pdx;
        dy = pdy;
        dz = pdz;
        dv = pdv;

        t = new int[dx][][][];
        for (i = 0; i < dx; i++)
        {
            t[i] = new int[dy][][];
            for (j = 0; j < dy; j++)
            {
                t[i][j] = new int[dz][];
                for (k = 0; k < dz; k++)
                {
                    t[i][j][k] = new int[dv];
                    for (l = 0; l < dv; l++)
                    {
                        t[i][j][k][l] = 0;
                    }
                }
            }
        }
    } // end TLine() konstruktor


    // A tömb kinullázása
    public void Nullaz()
    {
        int i, j, k, l;
        for (i = 0; i < dx; i++)
        {
            for (j = 0; j < dy; j++)
            {
                for (k = 0; k < dz; k++)
                {
                    for (l = 0; l < dv; l++)
                    {
                        t[i][j][k][l] = 0;
                    }
                }
            }
        }

    }

    // Értékadás
    public void Set(int pdx, int pdy, int pdz, int pdv, int v)
    {
        t[pdx][pdy][pdz][pdv] = v;
    }

    // Érték lekérdezés
    public int Get(int pdx, int pdy, int pdz, int pdv)
    {
        return (t[pdx][pdy][pdz][pdv]);
    }
} // end TLine class

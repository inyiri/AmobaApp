package org.cs.amoba.client.modell;

//
// Ez az osztály a játékosok és az üres hely színét implementálja
//
public class TSzin extends Object
{

    public final static int PIROS = 0;
    public final static int KEK = 1;
    public final static int URES = 2;
    int szin; 

    public TSzin()
    {
        szin = URES;
    }

    public void Set(int sz)
    {
        szin = sz;
    }

    public int Get()
    {
        return (szin);
    }
    
}

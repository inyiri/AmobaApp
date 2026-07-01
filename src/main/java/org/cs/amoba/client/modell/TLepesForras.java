package org.cs.amoba.client.modell;

//
// A lépésforrásokat reprezentáló osztály
//
public class TLepesForras extends Object
{

    public final static int EMBER = 0;
    public final static int COMPUTER = 1;
    public final static int OTHERCOMPUTER = 2;
    public int lepesforras;

    public TLepesForras()
    {
        lepesforras = TLepesForras.EMBER; // ez a default
    }
}


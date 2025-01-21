package org.cs.amoba.client.modell;

//
// Az jelsorok 4 irányát reprezentáló osztály
//
public class TIrany extends Object
{

    public final static int HORIZ = 0; // vizszintes
    public final static int VERT = 1; // fuggőleges
    public final static int LFTORG = 2; // balról jobbra emelkedő
    public final static int RGTOLF = 3; // jobbról balra emelkedő
    public int irany;

    public TIrany()
    {
        irany = -1; // nem definiált irány
    }

    public String toString()
    {
        switch (irany)
        {
            case HORIZ:
                return new String("Horizontális");
            case VERT:
                return new String("Vertikális");
            case LFTORG:
                return new String("B/J átló");
            case RGTOLF:
                return new String("J/B átló");
        }
        ;
        return new String("HIBA");
    }

    //
    // Visszaadja az x iránynak megfelelő növekményt
    //
    public int DX()
    {
        switch (irany)
        {
            case HORIZ:
                return +1;
            case VERT:
                return 0;
            case LFTORG:
                return +1;
            case RGTOLF:
                return -1;
        }
        return 0;
    }

    //
    // Visszaadja az y iránynak megfelelő növekményt
    //
    public int DY()
    {
        switch (irany)
        {
            case HORIZ:
                return 0;
            case VERT:
                return 1;
            case LFTORG:
                return +1;
            case RGTOLF:
                return +1;
        }
        return 0;
    }
} // end TIrany class

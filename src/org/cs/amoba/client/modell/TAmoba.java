package org.cs.amoba.client.modell;

//
// Az amőba játék agya és központi adminisztrátora
//
public class TAmoba extends Object
{
  public final static int N = 40;  // palyameret

  int        Tamadasfaktor;    // ertekelofuggvenynek kell
  int[]      suly;             // suly az ertekeleshez
  public TSzinTomb  palya;            // az amoba palya
  TJatekos   player1;          // A nagy piros mester
  TJatekos   player2;          // A nagy kek mester
  TSzin	     EzKezdett;        // a kezdojatekos szine
  TSzin      KiFogLepni;       // a kovetkezo lepest o fogja megtenni
  public int        xGyoztes;         // a gyoztes vonal kezdetenek oszlopa
  public int        yGyoztes;         // a gyoztes vonal kezdetenek sora
  public int        xLepes;           // a kovetkezo lepes oszlopa
  public int        yLepes;           // a kovetkezo lepes sora
  TLine      line;             // segit az 5-os vonal felderiteseben
  TValue     value;            // az ert�kelo fuggveny hasznalja
  public volatile boolean    GameOver;      // igaz, ha vege lett a jateknak
  public volatile boolean    GameStop;      // igaz, ha a jatek le lett stoppolva?
  public volatile boolean    AzEmberLepett; // kezeli a mouse esem�ny �s a Lepes() fgv.
  public TIrany     GyoztesVonal;     // a negy irany
  int        TotalLines;       // az osszes vonal szama


   // konstruktor
   public TAmoba()
   {
        Tamadasfaktor = 4;
        TotalLines = 2*2 * ( N*(N-4) + (N-4)*(N-4) );
        GyoztesVonal = new TIrany();
        EzKezdett = new TSzin();
        KiFogLepni = new TSzin();
        suly = new int[7];
	      suly[0] = 0;
        suly[1] = 0;
        suly[2] = 4;
        suly[3] = 20;
        suly[4] = 100;
        suly[5] = 500;
        suly[6] = 0;
        value = new TValue( N+1, N+1, 3 );
        line  = new TLine( 4, N+1, N+1, 3 );
        palya = new TSzinTomb( N+1 );          // ki is nullazodott
        player1 = new TJatekos();
        player2 = new TJatekos();
        GameOver = GameStop = false;
   }


//------------------------------------------------------------------------//
// A fuggveny hatasa: Egy TAmoba peldány alaphelyzetbe allitása           //
//------------------------------------------------------------------------//
public void SetGame( int p_tamadasfaktor, // támadásfaktor az ért.fgv-nek
                     TSzin kpsz,          // kezdőját�kos színe
                     TLepesForras lf1,    // player1 lépésforrása
                     TLepesForras lf2     // player2 lépésforrása
                   )
//------------------------------------------------------------------------//
{
  TSzin    mpsz = new TSzin();

  Tamadasfaktor = p_tamadasfaktor;
  TotalLines = 2*2 * ( N*(N-4) + (N-4)*(N-4) ); // Reset, mert cs�kkenti a j�t�k
  palya.Nullaz();
  line.Nullaz();
  value.Nullaz();

  //--- J�t�kosok adatai
  mpsz.Set( TSzin.PIROS );
  player1.Set( mpsz, lf1, "A PIROS mester");
  mpsz.Set( TSzin.KEK );
  player2.Set(mpsz, lf2, "A KéK mester");

  KiFogLepni.Set( kpsz.Get() );
  EzKezdett.Set( kpsz.Get() );

  player1.movecnt = 0;
  player2.movecnt = 0;

  GameOver = false;
  GameStop = false;
  return;
}

//----------------------------------------------------------//
//  Visszaadja azt, hogy most ember fog-e lépni             //
//----------------------------------------------------------//
synchronized public boolean IsEmberLepMost()
{
  int n;
  // A l�p�sforr�s megszerz�se
  if ( KiFogLepni.Get() == TSzin.PIROS )
  {
     n = player1.lf.lepesforras;
  }
  else
  {
     n = player2.lf.lepesforras;
  }
  // Ember vagy Computer?
  if ( n == TLepesForras.EMBER ) return true; else return false;
} // end IsEmberLepMost()

//------------------------------------------------------------------------//
// A parameterul kapott szamot eggyel megnoveli, GameOver figyel�s
//------------------------------------------------------------------------//
public void Novel( MyInt szam )      // a n�velend� v�ltoz�
//------------------------------------------------------------------------//
{
  szam.i++;
  if ( szam.i == 1 ) TotalLines--;
  if ( szam.i == 5 ) GameOver = true;
  if ( TotalLines <= 0 ) GameOver = true;
  return;
}


//-----------------------------------------------------
// Visszaad 0-4-ig egy veletlenszamot
//-----------------------------------------------------
public int GetRN()
{
  double rn = Math.random();
  rn = 4 * rn;
  return ( (int)Math.round( rn ) );
}

//------------------------------------------------------------------------//
// A f�ggv�ny hat�sa: A MakeMove l�p�sk�nyvel� rutin seg�drutinja         //
//------------------------------------------------------------------------//
void Konyvel( int    opcnt,    // ellenf�l
              int   cnt,       // akit k�nyvel�nk
              MyInt  opval,        // (*opval) ellenf�l
              MyInt  val )         // (*val) akit k�nyvel�nk
//------------------------------------------------------------------------//
{
  if ( opcnt == 0 )
  {
    (val.i) += ( suly[ cnt+1 ] - suly[ cnt ] );
  }
  else if ( cnt == 1 )
  {
    (opval.i) -= suly[ opcnt+1 ];
  }
  return;
}


//------------------------------------------------------------------------//
// A f�ggv�ny hat�sa: A param�terben megadott sz�n� j�t�kos sz�m�ra       //
//                    egy automatikus l�p�skeres�s                        //
//------------------------------------------------------------------------//
public boolean FindMove( TSzin szin )    // ennek keress�k a l�p�st
//------------------------------------------------------------------------//
{
  TSzin opponent = new TSzin();   // ellenf�l
  int i, j;
  int max, ert;

  max = -32000;

  //--- Az oponent szinenek beallitasa ---
  opponent.Set( szin.Get() == TSzin.KEK ? TSzin.PIROS : TSzin.KEK );

  xLepes = yLepes = ( (N + 1) / 2 ); // kezd��rt�k

  if ( palya.Get( xLepes, yLepes) == TSzin.URES ) max = 4;

  for ( i=1; i<=N; i++ )
    for ( j=1; j<=N; j++ )
       if ( palya.Get(i, j) == TSzin.URES )
       {
         ert = value.Get(i, j, szin.Get());
         ert = ert * (16+Tamadasfaktor);
         ert = ert / 16;
         ert += value.Get( i, j, opponent.Get() );
         ert += GetRN(); // random number

         if ( ert > max )
	 {
		  xLepes = i;   // ide hat�rozza meg a l�p�st
		  yLepes = j;   //
		  max    = ert;
	 }
       }
  return( false );
}



//------------------------------------------------------------------------//
// A fuggveny hatasa:  Egy lepes teljes leadminisztralasa. Haszn�lja a    //
//                     Konyvel() f�ggv�nyt.                               //
//------------------------------------------------------------------------//
public void MakeMove( TSzin szin, // ezt a j�t�kost adminisztr�lja
                         int xP,    // a l�p�s ebbenaz oszlopban van
                         int yP )   // a l�p�s ebben a sorban van
//------------------------------------------------------------------------//
{
  TSzin opponent = new TSzin();   // ellenf�l sz�ne
  int   x1, y1;
  int   k, l;
  boolean OT;
  
  int  p1, p2;                // a Konyvel()
  MyInt p3 = new MyInt(0);
  MyInt p4 = new MyInt(0);   // param�terei (csak seg�d!)
  
  MyInt pn = new MyInt(0);   // a Novel() param�tere

  OT = false;

  //--- Az opponent sz�n�nek be�ll�t�sa
  opponent.Set( szin.Get() == TSzin.KEK ? TSzin.PIROS : TSzin.KEK );

  //--- V�zszintes ir�ny kezel�se
  for ( k=0; k<=4; k++ )
  {
	 x1 = xP - k; y1 = yP;
	 if ( 1 <= x1 && x1 <= N-4 )
	 {
            pn.i = line.Get(TIrany.HORIZ, x1, y1, szin.Get());
	    Novel( pn );
            line.Set( TIrany.HORIZ, x1, y1, szin.Get(), pn.i );
            if ( GameOver && !OT )
	    {
		  GyoztesVonal.irany = TIrany.HORIZ;
		  xGyoztes = x1;
		  yGyoztes = y1;
		  OT = true;
	    }
	  for ( l=0; l<=4; l++ )
	  {
            p1 = line.Get(TIrany.HORIZ, x1, y1, opponent.Get() );
            p2 = line.Get(TIrany.HORIZ, x1, y1, szin.Get() );
            p3.i = value.Get( x1+l,  y1, opponent.Get() );
            p4.i = value.Get( x1+l, y1, szin.Get() );
            Konyvel( p1, p2, p3, p4 );
            value.Set( x1+l, y1, opponent.Get(), p3.i );
            value.Set( x1+l, y1, szin.Get(), p4.i );
         }
	} // end if
  } // end for

  //--- Bal als� - jobb fels� ir�ny kezel�se
  for ( k=0; k<=4; k++ )
  {
	 x1 = xP - k; y1 = yP - k;
	 if ( 1 <= x1 && x1 <= N-4 && 1 <= y1 && y1 <= N-4 )
	 {
          pn.i =  line.Get(TIrany.LFTORG, x1, y1, szin.Get() );
	  Novel( pn );
          line.Set(TIrany.LFTORG, x1, y1, szin.Get(), pn.i );
          if ( GameOver && !OT )
	  {
		  GyoztesVonal.irany = TIrany.LFTORG;
		  xGyoztes = x1;
		  yGyoztes = y1;
		  OT = true;
	  }
	  for ( l=0; l<=4; l++ )
	  {
            p1 = line.Get(TIrany.LFTORG, x1, y1, opponent.Get() );
            p2 = line.Get(TIrany.LFTORG, x1, y1, szin.Get() );
            p3.i = value.Get( x1+l, y1+l, opponent.Get() );
            p4.i = value.Get( x1+l, y1+l, szin.Get() );
            Konyvel( p1, p2, p3, p4 );
            value.Set( x1+l, y1+l, opponent.Get(), p3.i );
            value.Set( x1+l, y1+l, szin.Get(), p4.i );
          }
	 } // end if
  } // end for

  //--- Jobb als� - bal fels� ir�ny kezel�se
  for ( k=0; k<=4; k++ )
  {
	 x1 = xP + k; y1 = yP - k;
	 if ( 5 <= x1 && x1 <= N && 1 <= y1 && y1 <= N-4 )
	 {
             pn.i = line.Get(TIrany.RGTOLF, x1, y1, szin.Get() );
	     Novel( pn );
             line.Set(TIrany.RGTOLF, x1, y1, szin.Get(), pn.i );
             if ( GameOver && !OT )
	     {
		  GyoztesVonal.irany = TIrany.RGTOLF;
		  xGyoztes = x1;
		  yGyoztes = y1;
		  OT = true;
	     }
	     for ( l=0; l<=4; l++ )
	     {
               p1 = line.Get(TIrany.RGTOLF, x1, y1, opponent.Get() );
               p2 = line.Get(TIrany.RGTOLF, x1, y1, szin.Get() );
               p3.i = value.Get( x1-l, y1+l, opponent.Get() );
               p4.i = value.Get( x1-l, y1+l, szin.Get() );
               Konyvel( p1, p2, p3, p4 );
               value.Set( x1-l, y1+l, opponent.Get(), p3.i );
               value.Set( x1-l, y1+l, szin.Get(), p4.i );
             }
	 } // end if
  } // end for

  //--- F�gg�leges ir�ny kezel�se
  for ( k=0; k<=4; k++ )
  {
	x1 = xP; y1 = yP - k;
	if ( 1 <= y1 && y1 <= N-4 )
	{
           pn.i = line.Get(TIrany.VERT, x1, y1, szin.Get() );
	   Novel( pn );
           line.Set(TIrany.VERT, x1, y1, szin.Get(), pn.i );
           if ( GameOver && !OT )
	   {
		  GyoztesVonal.irany = TIrany.VERT;
		  xGyoztes = x1;
		  yGyoztes = y1;
		  OT = true;
	   }
	   for ( l=0; l<=4; l++ )
	   {
              p1 = line.Get(TIrany.VERT, x1, y1, opponent.Get() );
              p2 = line.Get(TIrany.VERT, x1, y1, szin.Get() );
              p3.i = value.Get( x1, y1+l, opponent.Get() );
              p4.i = value.Get( x1, y1+l, szin.Get() );
              Konyvel( p1, p2, p3, p4 );
              value.Set( x1, y1+l, opponent.Get(), p3.i );
              value.Set( x1, y1+l, szin.Get(), p4.i );
           }
       } // end if
  } // end for

  //--- A palya p�ly�n is megtessz�k a l�p�st
  palya.Set( xP, yP, szin.Get() );

  //--- A k�vetkez� l�p�s a m�sik j�t�kos�
  KiFogLepni.Set( opponent.Get() );

  //--- A l�p�ssz�m k�nyvel�se
  if ( szin.Get() == player1.szin.Get() )
  {
	  player1.movecnt++;
  }
  else
  {
	  player2.movecnt++;
  }

  return;
}

//---------------------------------------
// Ennek szinkroniz�ltnak kell lennie
//---------------------------------------
synchronized public void SetAzEmberLepett( boolean b )
{
  AzEmberLepett = b;
}

//------------------------------------------------------------------------//
// A f�ggv�ny hat�sa: Egy l�p�s megt�tele                                 //
// Vissza: false, ha v�ge a j�t�knak, egy�bk�nt true                      //
//------------------------------------------------------------------------//
public boolean Lepes()
//------------------------------------------------------------------------//
{
    if ( GameOver || GameStop ) return false;

    if ( IsEmberLepMost() )
    {
       SetAzEmberLepett( false );


//       while ( !AzEmberLepett ) // a mouse click esem�ny �ll�tja
//       {
//         try {
//         //Thread.sleep(100); // imi 2008.12.21
//         } catch (Exception e) { ; }
//         if ( GameStop ) { return false; }
//       }


       // Ezen a ponton az xLepes �s yLepes ki van t�ltve
       MakeMove( KiFogLepni, xLepes, yLepes ); // Adminisztr�ci�
    }
    else // Computer
    {
       FindMove( KiFogLepni ); // L�p�skeres�s
       MakeMove( KiFogLepni, xLepes, yLepes ); // Adminisztr�ci�
    }
    if ( GameOver || GameStop ) return false; else return true;
} // end Lepes()

} // end TAmoba class



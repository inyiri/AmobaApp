import java.awt.*;
import java.awt.event.*;
import java.applet.*;


public class TAmobaApplet extends Applet implements Runnable{
  public int szelesseg;  // az applet ablak szélessége
  public int magassag;   // az applet ablak magassága
  TLepesForras lf1;      // induló paraméter
  TLepesForras lf2;      // induló paraméter
  TSzin AKezdoSzine;     // induló paraméter
  int iTamadasFaktor;    // induló paraméter
  Graphics g;            // az applet grafikus kontextusa
  Thread av = null;      // amõba vezérlõ thread
  TAmoba amoba;          // az amõba agya
  boolean clicktiltas;   // az egér click tiltása/engedése
  Panel panel1 = new Panel();
  Label label1 = new Label();
  Choice redpllf = new Choice();
  Label label2 = new Label();
  Choice bluepllf = new Choice();
  Label label3 = new Label();
  Choice kpl = new Choice();
  TextField textStatus = new TextField();
  Button btnStart = new Button();
  Button btnStop = new Button();
  Label label4 = new Label();
  TextField textPirosPontok = new TextField();
  TextField textKekPontok = new TextField();
  Button btnPontNullazo = new Button();
  Label label5 = new Label();
  TextField textTamFakt = new TextField();
  Label label6 = new Label();

  //
  // A konstruktor
  //
  public TAmobaApplet()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }


  //-----------------------------------------------
  //  Lekérdezi az aktuális panel beállításokat
  //-----------------------------------------------
  public void GetIndPars()
  {
    StringBuffer sb = new StringBuffer(20);
    int i;
    i = redpllf.getSelectedIndex();
    if ( i == 0 )
    {
      lf1.lepesforras = TLepesForras.EMBER;
      sb.append("Ember");
    }
    else
    {
      lf1.lepesforras = TLepesForras.COMPUTER;
      sb.append("Computer");
    }

    i = bluepllf.getSelectedIndex();
    if ( i == 0 )
    {
      lf2.lepesforras = TLepesForras.EMBER;
      sb.append("/Ember");
    }
    else
    {
      lf2.lepesforras = TLepesForras.COMPUTER;
      sb.append("/Computer");
    }

    i = kpl.getSelectedIndex();
    if ( i == 0 )
    {
      AKezdoSzine.Set( TSzin.PIROS );
      sb.append("/PIROS");
    }
    else
    {
      AKezdoSzine.Set( TSzin.KEK );
      sb.append("/KÉK");
    }

    try {
    iTamadasFaktor = Integer.parseInt( textTamFakt.getText() );
    } catch (Exception e) { iTamadasFaktor = 4; }

    textStatus.setText( new String( sb ) );
    return;
  } // end GetIndPars()


  // Az applet inicializálása
  public void init()
  {
     amoba = new TAmoba();

     lf1 = new TLepesForras();      // induló paraméter
     lf2 = new TLepesForras();      // induló paraméter
     AKezdoSzine = new TSzin();     // induló paraméter

     szelesseg = size().width;
     magassag  = size().height;
     g = getGraphics();
     av = new Thread(this);
     redpllf.add("Egy ember");
     redpllf.add("Computer ");
     bluepllf.add("Egy ember");
     bluepllf.add("Computer ");
     kpl.add("A nagy PIROS mester");
     kpl.add("A nagy KEK varazsló");

  } // end init()

  public String getAppletInfo()
  {
    return "Amõba Applet";
  }

  public String[][] getParameterInfo()
  {
    return null;
  }

  //------------------------------------------
  // Kirajzolja az amõba pálya négyzetrácsát
  //------------------------------------------
  public void PalyatRajzol()
  {
    int x1, y1, x2, y2, db;

    //
    // A négyzetrács kirajzolása
    //
    g.setColor( Color.black );
    x1 = 0; x2 = 400;
    for ( db = 1; db <= 41; db++ )
    {
      y1 = 0 + (db-1)*10; y2 = y1;
      g.drawLine(x1, y1, x2, y2);
    }

    y1 = 0; y2 = 400;
    for ( db = 1; db <= 41; db++ )
    {
      x1 = 0 + (db-1)*10; x2 = x1;
      g.drawLine(x1, y1, x2, y2);
    }

    g.setColor( Color.blue );
    g.drawRect(0, 0, 400, 400 );
    g.setColor( Color.black );

    //
    // Az eddigi lépések kirajzolása
    //
    for ( x1 = 1; x1 <= 40; x1++ )
      for ( x2 = 1; x2 <= 40; x2++ )
      {

         if ( amoba.palya.Get( x1, x2 ) != TSzin.URES )
         {
            LepesRajzolas( x1, x2, amoba.palya.Get( x1, x2 ) );
         }

      }


  } // end PalyatRajzol()

  //---------------------
  // Egy lépést kirajzol
  //---------------------
  public void LepesRajzolas( int oszl,  // oszlop
                             int sor,   // sor
                             int sz )   // piros, kék vagy fehér a szin
  {
     int xpoz, ypoz;

     xpoz = (oszl-1)*10 + 5;
     ypoz = (sor-1)*10 + 5;

     if ( sz == TSzin.PIROS )
     {
        g.setColor( Color.red );
        g.fillOval(xpoz-4, ypoz-4, 8, 8);
        g.setColor( Color.black );
     }
     else if ( sz == TSzin.KEK )
     {
        g.setColor( Color.blue );
        g.fillOval(xpoz-4, ypoz-4, 8, 8);
        g.setColor( Color.black );
     }
     else
     {
        g.setColor( Color.white );
        g.fillOval(xpoz-4, ypoz-4, 8, 8);
        g.setColor( Color.black );
     }
  } // end LepesRajzolas()

  //--------------------------
  // Az applet paint metódusa
  //---------------------------
  public void paint( Graphics gc )
  {
    PalyatRajzol();

  } // end paint()

  //-------------------------------------------
  // Ez egy külön szál, ami a játékot vezérli
  //-------------------------------------------
  public void run()
  {

    EgyKisAnimacio();
    while ( true )
    {

      while ( amoba.GameOver || amoba.GameStop )
      {
        ;
      }

      EgyAmobaJatek();

    }
  } // end run()

  //---------------------------
  // At applet start metódusa
  //---------------------------
  public void start()
  {
    av.start();
  } // end start()


  //----------------------------
  //  Egy kis anámációs jelenet
  //----------------------------
  public void EgyKisAnimacio()
  {
    int sor, oszlop, db=0, c;
    double rn;
    c = TSzin.KEK;

    while ( amoba.GameStop )
    {
      rn = Math.random(); rn = 40 * rn;
      oszlop = (int)Math.round( rn );
      rn = Math.random(); rn = 40 * rn;
      sor = (int)Math.round( rn );
      LepesRajzolas(oszlop,  sor, c );
      Var(10);

      if ( db == 1600 )
      {
        db = 0; repaint();
      }
      else
      {
         db++;
         c = ( c==TSzin.KEK ? TSzin.PIROS : TSzin.KEK );
      }
    } // end while

  } // end Egy kis animacio()

  //----------------------
  // Egy játék lejátszása
  //----------------------
  public void EgyAmobaJatek()
  {
    TSzin SaveSzin = new TSzin();
    boolean l; // a lépés kimenetele

    while ( true )
    {
      if ( !amoba.GameStop )
      {
        SaveSzin.Set( amoba.KiFogLepni.Get() );
        SetClickTiltas( false );
        l = amoba.Lepes();
        SetClickTiltas( true );
        AnimaltLepes(amoba.xLepes, amoba.yLepes, SaveSzin.Get() );
      }

      if ( amoba.GameOver && !amoba.GameStop )
      {
        textStatus.setText( amoba.GyoztesVonal.toString() + ": " + amoba.xGyoztes + "/" + amoba.yGyoztes);
        Villogtat();
        break;
      }
    } // end while

  } // EgyAmobaJatek()


  private void jbInit() throws Exception
  {
    this.setBackground(new Color(255, 199, 120));
    this.setSize(new Dimension(579, 400));
    this.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        this_mouseClicked(e);
      }
    });
    panel1.setBackground(Color.blue);
    panel1.setBounds(new Rectangle(412, 0, 167, 400));
    label1.setForeground(Color.red);
    label1.setFont(new Font("Dialog", 1, 12));
    label1.setBackground(Color.yellow);
    label1.setBounds(new Rectangle(6, 9, 155, 23));
    label1.setAlignment(1);
    label1.setText("A nagy PIROS mester");
    redpllf.setBounds(new Rectangle(6, 34, 155, 21));
    label2.setText("A nagy KÉK mester");
    label3.setFont(new Font("Dialog", 1, 12));
    label3.setBackground(Color.yellow);
    label3.setBounds(new Rectangle(7, 138, 152, 23));
    label3.setAlignment(1);
    label3.setText("Kezdõ játékos");
    kpl.setBounds(new Rectangle(6, 163, 155, 21));
    textStatus.setBackground(Color.white);
    textStatus.setBounds(new Rectangle(5, 294, 151, 23));
    textStatus.setText("Helló!");
    textStatus.setEditable(false);
    btnStart.setFont(new Font("Dialog", 2, 14));
    btnStart.setBounds(new Rectangle(11, 194, 67, 35));
    btnStart.setLabel("Start");

    btnStart.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        btnStart_actionPerformed(e);
      }
    });
    btnStop.setLabel("Stop");
    label4.setForeground(Color.white);
    label4.setFont(new Font("Dialog", 1, 12));
    label4.setBounds(new Rectangle(8, 240, 96, 23));
    label4.setText("A játszma állása:");
    textPirosPontok.setBackground(Color.white);
    textPirosPontok.setForeground(Color.red);
    textPirosPontok.setFont(new Font("Dialog", 1, 12));
    textPirosPontok.setBounds(new Rectangle(7, 261, 42, 23));
    textPirosPontok.setText("       0");
    textPirosPontok.setEditable(false);
    textKekPontok.setBackground(Color.white);
    textKekPontok.setForeground(Color.blue);
    textKekPontok.setFont(new Font("Dialog", 1, 12));
    textKekPontok.setBounds(new Rectangle(61, 261, 42, 23));
    textKekPontok.setText("       0");
    textKekPontok.setEditable(false);
    label5.setForeground(Color.white);
    label5.setBounds(new Rectangle(7, 364, 118, 23));
    label5.setText("(C) CreedSoft 2001");
    label6.setForeground(Color.white);
    label6.setFont(new Font("Dialog", 1, 12));
    label6.setBounds(new Rectangle(6, 327, 93, 23));
    label6.setText("Támadásfaktor:");
    textTamFakt.setBackground(Color.white);
    textTamFakt.setBounds(new Rectangle(106, 328, 20, 23));
    textTamFakt.setText("4");
    btnPontNullazo.setBounds(new Rectangle(109, 262, 48, 23));
    btnPontNullazo.setLabel("Nulláz");
    btnPontNullazo.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        btnPontNullazo_actionPerformed(e);
      }
    });

    btnStop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        btnStop_actionPerformed(e);
      }
    });
    btnStop.setEnabled(false);
    btnStop.setFont(new Font("Dialog", 2, 14));
    btnStop.setBounds(new Rectangle(85, 194, 69, 35));
    bluepllf.setBounds(new Rectangle(5, 98, 155, 21));
    label2.setBounds(new Rectangle(5, 73, 156, 23));
    label2.setAlignment(1);
    label2.setBackground(Color.yellow);
    label2.setForeground(Color.blue);
    label2.setFont(new Font("Dialog", 1, 12));
    panel1.setLayout(null);
    this.setLayout(null);
    this.add(panel1, null);
    panel1.add(label2, null);
    panel1.add(label1, null);
    panel1.add(bluepllf, null);
    panel1.add(redpllf, null);
    panel1.add(label3, null);
    panel1.add(kpl, null);
    panel1.add(textStatus, null);
    panel1.add(btnStop, null);
    panel1.add(btnStart, null);
    panel1.add(label4, null);
    panel1.add(textPirosPontok, null);
    panel1.add(textKekPontok, null);
    panel1.add(btnPontNullazo, null);
    panel1.add(label5, null);
    panel1.add(textTamFakt, null);
    panel1.add(label6, null);
  }

  //
  // Ez kezeli a négyzetrács egér click eseményét
  //
  void this_mouseClicked(MouseEvent e)
  {
    if ( clicktiltas ) { return; }

    int xp, yp, o, s;
    boolean MostEmberVan;
    boolean ervenyes = true;
    MostEmberVan = amoba.IsEmberLepMost();
    if ( ! MostEmberVan ) { return; }

    xp = e.getX();
    yp = e.getY();
    if ( xp >= 400 || yp >= 400 ) { ervenyes = false; }

    o = xp / 10; s = yp / 10;
    if ( o*10 != xp ) o++; else ervenyes = false;
    if ( s*10 != yp ) s++; else ervenyes = false;

    if ( amoba.palya.Get( o, s ) != TSzin.URES )
    {
      ervenyes = false;
    }

    if ( ervenyes && !amoba.GameOver && !amoba.GameStop )
    {
       amoba.xLepes = o;
       amoba.yLepes = s;
       amoba.SetAzEmberLepett( true ); // folytatódhat a vezérlõ szál...
       Var(100);
    }
  } // end this_mouseClicked()


  void btnStop_actionPerformed(ActionEvent e)
  {
      amoba.GameStop = true;
      btnStart.enable();
      btnStop.disable();
  }

  void btnStart_actionPerformed(ActionEvent e)
  {
      GetIndPars();
      amoba.SetGame( iTamadasFaktor, AKezdoSzine, lf1, lf2 );
      btnStart.disable();
      btnStop.enable();
      repaint();
  }

  //
  // A kijelzõ növelése
  //
  public void PontotNovel( int sz )
  {
    int i;
    StringBuffer sb = new StringBuffer();
    if ( sz == TSzin.PIROS )
    {
      i = Integer.parseInt( textPirosPontok.getText().trim() );
      i++;
      sb.append( Integer.toString(i) );
      while ( sb.length() < 8 ) sb.insert(0, " " );
      textPirosPontok.setText( new String( sb ) );
    }
    else
    {
      i = Integer.parseInt( textKekPontok.getText().trim() );
      i++;
      sb.append( Integer.toString(i) );
      while ( sb.length() < 8 ) sb.insert(0,  " " );
      textKekPontok.setText( new String( sb ) );
    }
  } // end PontotNovel()

  //-------------------------------
  // A gyõztes vonal villogtatása
  //--------------------------------
  public void Villogtat()
  {
    int i, sz, dx, dy, xKezdo, yKezdo;

    xKezdo = amoba.xGyoztes;
    yKezdo = amoba.yGyoztes;
    sz = amoba.palya.Get( xKezdo, yKezdo );

    PontotNovel( sz );

    dx = amoba.GyoztesVonal.DX();
    dy = amoba.GyoztesVonal.DY();

    while ( !amoba.GameStop )
    {
    for ( i = 1; i <= 5; i++ )
    {
      LepesRajzolas( xKezdo+(i-1)*dx, yKezdo+(i-1)*dy, -1);
    }
    Var(100);
    for ( i = 1; i <= 5; i++ )
    {
      LepesRajzolas( xKezdo+(i-1)*dx, yKezdo+(i-1)*dy,  sz);
    }
    Var(100);
    }

  } // end Villogtat()

  //-------------------------------
  // Egy animált lépés
  //--------------------------------
  public void AnimaltLepes( int x, int y, int szin)
  {
    for ( int i=1; i<=3; i++ )
    {
      LepesRajzolas( x, y, -1);
      Var(100);
      LepesRajzolas( x, y,  szin);
      Var(100);
    }
  } // end AnimaltLepes()


  //------------------------------------------------
  // Egy kis várakozó rutin az önzõ viselkedés ellen
  //------------------------------------------------
  public void Var( long t )
  {
    try { Thread.sleep( t ); } catch (Exception e) { ; }
  }

  synchronized public void SetClickTiltas( boolean b )
  {
    clicktiltas = b;
  }

  void btnPontNullazo_actionPerformed(ActionEvent e)
  {
       textPirosPontok.setText( "       0" );
       textKekPontok.setText( "       0" );
  }

}

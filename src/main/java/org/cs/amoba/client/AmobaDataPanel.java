package org.cs.amoba.client;

//
// A vezérlő osztály
//

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.cs.amoba.client.modell.*;

public class AmobaDataPanel
{
    VerticalPanel dataCtrls = new VerticalPanel();

    Label lblImre = new Label("(C) Nyiri Imre - 1999-2008");
    Label lblTFakt = new Label("Támadásfaktor: ");
    Label label1 = new Label("A nagy piros mester");
    Label label2 = new Label("A nagy zöld mester");
    Label labelKezdo = new Label("Kezdőjátékos:");
    Label labelAllas = new Label("A játék állása:");
    Label labelMsg = new Label("Helló!");

    FlowPanel startStop = new FlowPanel();
    FlowPanel jatekAllas = new FlowPanel();

    Button btnStart = new Button("Start");
    Button btnStop  = new Button("Stop");
    Button btnNullaz  = new Button("Nulláz");

    ListBox lbLFPiros = new ListBox();
    ListBox lbLFZold  = new ListBox();
    ListBox lbKezdo   = new ListBox();

    Label lblAllasPiros = new Label("0");
    Label lblAllasZold = new Label("0");
    Label lblAllasKpont = new Label(" : ");
    Label lblHelytarto = new Label(" ");

    TextBox tamadasFaktor = new TextBox();

    final AmobaModell amobaModell = new AmobaModell();
    final AmobaView   amobaView = new AmobaView(this);

    HTML htmlCim = new HTML("<h1><center>Amőba játék</center></h1><hr>");
    HTML html = new HTML("<img src='" + GWT.getModuleBaseURL() + "imi_fiu.jpg' > <b>Zsazsának és Imrusnak</b>");

    //
    //
    //
    public AmobaDataPanel()
    {
        label1.setStyleName("PirosMester");
        label2.setStyleName("ZoldMester");
        labelKezdo.setStyleName("KezdoJatekos");
        lblTFakt.setStyleName("Szovegek");
        labelAllas.setStyleName("Szovegek");
        lbLFPiros.setStyleName("PirosMesterLista");
        lbLFZold.setStyleName("ZoldMesterLista");
        lbKezdo.setStyleName("KezdoJatekosLista");
        labelMsg.setStyleName("Szovegek");

        lblAllasPiros.setStyleName("JatekPontPiros");
        lblAllasZold.setStyleName("JatekPontZold");
        lblAllasKpont.setStyleName("JatekKettosPont");
        lblAllasPiros.setWidth("40");
        lblAllasZold.setWidth("40");
        lblAllasKpont.setWidth("40");

        dataCtrls.setSpacing(10);
        dataCtrls.setStyleName("AmobaDataPanel");

        amobaView.palyaPanel.add( dataCtrls );

        dataCtrls.add(htmlCim);

        lbLFPiros.addItem("Egy ember", "E");
        lbLFPiros.addItem("A Computer", "C");

        lbLFZold.addItem("Egy ember", "E");
        lbLFZold.addItem("A Computer", "C");

        lbKezdo.addItem("A nagy piros mester", "P");
        lbKezdo.addItem("A nagy zöld mester", "Z");

        dataCtrls.add(label1);
        dataCtrls.add(lbLFPiros);
        dataCtrls.add(label2);
        dataCtrls.add(lbLFZold);

        dataCtrls.add( labelKezdo );
        dataCtrls.add( lbKezdo );

        startStop.add( btnStart );
        btnStop.setEnabled(false);
        startStop.add( btnStop );
        dataCtrls.add( startStop );

        dataCtrls.add( labelAllas );
        dataCtrls.add( jatekAllas );
        jatekAllas.add( lblAllasPiros );
        jatekAllas.add( lblAllasKpont );
        jatekAllas.add( lblAllasZold );
        jatekAllas.add(lblHelytarto);
        jatekAllas.add(btnNullaz);

        dataCtrls.add( lblTFakt );
        tamadasFaktor.setWidth("40");
        tamadasFaktor.setText("4");
        dataCtrls.add( tamadasFaktor );

        dataCtrls.add( labelMsg );
        dataCtrls.add( lblImre );

        dataCtrls.add( html );

        //A modell inicializálása
        amobaModell.setParameters(lbLFPiros.getValue(lbLFPiros.getSelectedIndex()),
                                  lbLFZold.getValue(lbLFZold.getSelectedIndex()),
                                  lbKezdo.getValue(lbKezdo.getSelectedIndex()),
                                  tamadasFaktor.getText() );

        //////////////////
        // Eseménykezelők
        //////////////////

        //
        // Nullázza a tabellát 0 : 0 -ra
        //
        btnNullaz.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                amobaModell.zoldPontjai = amobaModell.pirosPontjai = 0;
                lblAllasZold.setText(  "" + amobaModell.zoldPontjai );
                lblAllasPiros.setText( "" + amobaModell.pirosPontjai );
            }
        });

        //
        // Elkezd egy új Amőba játékot
        //
        btnStart.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                amobaModell.setParameters(lbLFPiros.getValue(lbLFPiros.getSelectedIndex()),
                          lbLFZold.getValue(lbLFZold.getSelectedIndex()),
                          lbKezdo.getValue(lbKezdo.getSelectedIndex()),
                          tamadasFaktor.getText() );

                amobaModell.setAmobaGame();
                amobaModell.amoba.GameOver=false;
                amobaModell.amoba.GameStop=false;
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);
                amobaModell.startedByStartBtn = true;
                labelMsg.setText("Started... ");
                makeComputerSteps();
            }
        });

        //
        //
        //
        btnStop.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                amobaModell.amoba.GameStop = true;
                btnStop.setEnabled(false);
                btnStart.setEnabled(true);
                amobaModell.startedByStartBtn = false;
                resetAmobaView();
            }
        });

    } // end konstruktor

    //
    //
    //
    public void registerAManualStep(String cella)
    {
        if ( !amobaModell.startedByStartBtn ) return;
        if ( amobaModell.amoba.GameOver ) return;
        if ( amobaModell.amoba.GameStop ) return;

        amobaModell.amoba.SetAzEmberLepett(true);

        int pvPos = cella.indexOf(";");
        int o = Integer.parseInt( cella.substring(0, pvPos) );
        int s = Integer.parseInt( cella.substring(pvPos+1, cella.length()) );

        // Ha ide már léptek
        if ( amobaModell.amoba.palya.Get( o, s) != TSzin.URES )
        {
            return;
        }

        if ( amobaModell.presentSzin == TSzin.PIROS )
        {
            labelMsg.setText("Piros " + o + "@" + s);
        }
        else
        {
            labelMsg.setText("Zöld " + o + "@" + s);
        }

        amobaModell.amoba.xLepes = o;
        amobaModell.amoba.yLepes = s;

        boolean tovabb = amobaModell.amoba.Lepes();

        vizualisLepes(o, s, amobaModell.presentSzin);
        amobaModell.presentSzin = amobaModell.getNextSzin( amobaModell.presentSzin );
        amobaModell.presentLF   = amobaModell.getNextLepesForras( amobaModell.presentLF );

        amobaModell.amoba.SetAzEmberLepett(false);

        if ( amobaModell.amoba.GameOver ) vizualisGyoztesVonal();

    }

    //
    // Elvégez egy Computer lépéssorozatot
    //
    public void makeComputerSteps()
    {
        if ( !amobaModell.startedByStartBtn ) return;
        if ( amobaModell.amoba.GameOver ) return;
        if ( amobaModell.amoba.GameStop ) return;

        boolean tovabb=false;
        while ( amobaModell.presentLF.lepesforras == TLepesForras.COMPUTER )
        {
            amobaModell.amoba.SetAzEmberLepett( false );
            tovabb = amobaModell.amoba.Lepes();
            vizualisLepes(amobaModell.amoba.xLepes, amobaModell.amoba.yLepes, amobaModell.presentSzin);
            if ( !tovabb ) break;
            amobaModell.presentSzin = amobaModell.getNextSzin( amobaModell.presentSzin );
            amobaModell.presentLF   = amobaModell.getNextLepesForras( amobaModell.presentLF );
        }

        if ( amobaModell.amoba.GameOver ) vizualisGyoztesVonal();

    }

    //
    // Kitesz egy kört a pályára a megadott színnel
    //
    public void vizualisLepes(int o, int s, int color)
    {
        String base = GWT.getModuleBaseURL();
        if ( TSzin.KEK == color )
        {
            amobaView.cells[s-1][o-1].setUrl(base + "green.jpg");
        }
        else
        {
            amobaView.cells[s-1][o-1].setUrl(base + "red.jpg");
        }
    }

    //
    // A győztes vonal kiemelése
    //
    public void vizualisGyoztesVonal()
    {
        int i, sz, dx, dy, xKezdo, yKezdo;

        xKezdo = amobaModell.amoba.xGyoztes;
        yKezdo = amobaModell.amoba.yGyoztes;

        labelMsg.setText("Győztes:" + xKezdo + "; " + yKezdo);

        dx = amobaModell.amoba.GyoztesVonal.DX();
        dy = amobaModell.amoba.GyoztesVonal.DY();

        String base = GWT.getModuleBaseURL();
        if (amobaModell.amoba.palya.Get(amobaModell.amoba.xGyoztes, amobaModell.amoba.yGyoztes) == TSzin.PIROS)
        {
            lblAllasPiros.setText( "" + ++amobaModell.pirosPontjai );
            for (i = 1; i <= 5; i++)
            {
               amobaView.cells[yKezdo-1 + (i - 1) * dy][xKezdo-1 + (i - 1) * dx].setUrl(base + "red.gif");
            }
        }
        else
        {
            lblAllasZold.setText( "" + ++amobaModell.zoldPontjai );
            for (i = 1; i <= 5; i++)
            {
                amobaView.cells[yKezdo-1 + (i - 1) * dy][xKezdo-1 + (i - 1) * dx].setUrl(base + "green.gif");
            }
        }
    }

    //
    //
    //
    public void resetAmobaView()
    {
        String base = GWT.getModuleBaseURL();
        for (int i = 0; i < amobaView.cells.length; i++)
        {
            for (int j = 0; j < amobaView.cells.length; j++)
            {
                amobaView.cells[i][j].setUrl(base + "ures.png");
            }
        }
    }

} // end class

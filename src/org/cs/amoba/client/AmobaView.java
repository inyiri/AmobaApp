/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cs.amoba.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import org.cs.amoba.client.modell.AmobaModell;

/**
 *
 * @author inyiri
 */
public class AmobaView extends Composite 
{
    // Tagváltozók
    public final Images imgs = (Images) GWT.create(Images.class);

    HorizontalPanel palyaPanel;
    FlexTable flexTable;
    public final Image[][] cells = new Image[40][40];
    AmobaDataPanel amobaDataPanel;

//    Image pirosImage[][] = new Image[40][40];
//    Image zoldImage[][]  = new Image[40][40];
//    Image uresImage[][]  = new Image[40][40];

    //Egy belső statikus osztály
    private static class ClickHandler implements ClickListener
    {

        AmobaDataPanel myAmobaDataPanel;

        public void onClick(Widget sender)
        {

            myAmobaDataPanel.registerAManualStep(((Image) sender).getTitle());
            myAmobaDataPanel.makeComputerSteps();

        }
        
        public void setAmobaDataPanel( AmobaDataPanel adp )
        {
            myAmobaDataPanel = adp;
        }
    } //Egy belső statikus osztály VÉGE

    ClickHandler clickHandler = new ClickHandler();

    /**
     *  Felépíti az Amőba felhasználói felületét
     */
    public AmobaView(AmobaDataPanel dp)
    {
        amobaDataPanel = dp;

        clickHandler.setAmobaDataPanel(dp);

        palyaPanel = new HorizontalPanel();
        flexTable = new FlexTable();
        flexTable.setCellPadding(0);
        flexTable.setCellSpacing(0);
        palyaPanel.add(flexTable);
        RootPanel.get().add(palyaPanel);



        for (int i = 0; i < cells.length; i++)
        {
            for (int j = 0; j < cells.length; j++)
            {
//                uresImage[i][j] = imgs.uresKor().createImage();
//                zoldImage[i][j]  = imgs.greenKor().createImage();
//                pirosImage[i][j] = imgs.redKor().createImage();
                cells[i][j] = new Image( "ures.png" );
                cells[i][j].setPixelSize(20, 20);
               
                cells[i][j].setTitle((j+1) + ";" + (i+1));


//                cells[i][j].addClickListener(new ClickListener()
//                {
//                    public void onClick(Widget sender)
//                    {
//
//                        amobaDataPanel.registerAManualStep( ( (PushButton) sender).getTitle() );
//                        amobaDataPanel.makeComputerSteps();
//
//                    }
//                });
                cells[i][j].addClickListener( clickHandler );

                flexTable.setWidget(i, j, cells[i][j]);
            }

        //

        }


    } // end konstruktor


} // end class

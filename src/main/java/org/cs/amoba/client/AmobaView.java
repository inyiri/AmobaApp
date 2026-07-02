package org.cs.amoba.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class AmobaView extends Composite
{
    HorizontalPanel palyaPanel;
    FlexTable flexTable;
    public final Image[][] cells = new Image[40][40];
    AmobaDataPanel amobaDataPanel;

    private static class CellClickHandler implements ClickHandler
    {
        AmobaDataPanel myAmobaDataPanel;

        @Override
        public void onClick(ClickEvent event)
        {
            myAmobaDataPanel.registerAManualStep(((Image) event.getSource()).getTitle());
            myAmobaDataPanel.makeComputerSteps();
        }

        public void setAmobaDataPanel(AmobaDataPanel adp)
        {
            myAmobaDataPanel = adp;
        }
    }

    CellClickHandler cellClickHandler = new CellClickHandler();

    /**
     * Felépíti az Amőba felhasználói felületét
     */
    public AmobaView(AmobaDataPanel dp)
    {
        amobaDataPanel = dp;
        cellClickHandler.setAmobaDataPanel(dp);

        String base = GWT.getModuleBaseURL();
        palyaPanel = new HorizontalPanel();
        palyaPanel.setStyleName("amobaRoot");
        flexTable = new FlexTable();
        flexTable.setCellPadding(0);
        flexTable.setCellSpacing(0);
        palyaPanel.add(flexTable);
        RootPanel.get().add(palyaPanel);

        for (int i = 0; i < cells.length; i++)
        {
            for (int j = 0; j < cells.length; j++)
            {
                cells[i][j] = new Image(base + "ures.png");
                cells[i][j].setPixelSize(20, 20);
                cells[i][j].setTitle((j+1) + ";" + (i+1));
                cells[i][j].addClickHandler(cellClickHandler);
                flexTable.setWidget(i, j, cells[i][j]);
            }
        }
    }

} // end class

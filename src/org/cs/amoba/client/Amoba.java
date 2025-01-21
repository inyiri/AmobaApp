package org.cs.amoba.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.cs.amoba.client.modell.AmobaModell;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Amoba implements EntryPoint
{

    AmobaDataPanel amobaDataPanel;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {

        amobaDataPanel = new AmobaDataPanel();
        
    }
}

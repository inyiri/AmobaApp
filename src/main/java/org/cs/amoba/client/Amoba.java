package org.cs.amoba.client;

import com.google.gwt.core.client.EntryPoint;

public class Amoba implements EntryPoint
{

    AmobaDataPanel amobaDataPanel;

    public void onModuleLoad()
    {
        amobaDataPanel = new AmobaDataPanel();
    }
}

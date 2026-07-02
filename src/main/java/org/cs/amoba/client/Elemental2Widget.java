package org.cs.amoba.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import jsinterop.base.Js;

/**
 * Bridges a Domino UI (elemental2) DOM element into the classic GWT widget
 * tree so it can be added to widget-based panels like VerticalPanel.
 */
public class Elemental2Widget<T> extends Widget
{
    public Elemental2Widget(T elemental2Element)
    {
        Element element = Js.cast(elemental2Element);
        setElement(element);
    }
}

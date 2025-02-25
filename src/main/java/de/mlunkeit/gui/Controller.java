package de.mlunkeit.gui;

import de.mlunkeit.gui.wrapping.MainFrame;

public abstract class Controller
{
    private MainFrame mainFrame;

    protected void dispatch(Object event, String... views)
    {
        mainFrame.dispatchEvent(event, views);
    }

    public void setMainFrame(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
    }
}

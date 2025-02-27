package de.mlunkeit.gui.views;

import de.mlunkeit.gui.wrapping.MainFrame;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class View
{
    private MainFrame mainFrame;

    public abstract void reset();

    public abstract void render(@NotNull Graphics2D g, int width, int height);

    public abstract void actionPerformed(@NotNull Object action);

    public void setMainFrame(@NotNull MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
    }

    protected void showView(String view)
    {
        mainFrame.showView(view);
    }
}

package de.mlunkeit.gui.views;

import de.mlunkeit.gui.FontManager;
import de.mlunkeit.gui.ResourceLoader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class MenuView extends View
{
    private final Image dirtTexture;
    private final Image grassTexture;

    private int selectedIndex = 0;

    private final int elements = 2;

    public MenuView()
    {
        try
        {
            dirtTexture = ResourceLoader.loadImage("/dirt.png");
            grassTexture = ResourceLoader.loadImage("/grass.png");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void drawTextInCenter(Graphics2D g, String text, int width, int y)
    {
        g.drawString(text, width/2 - g.getFontMetrics().stringWidth(text) / 2, y);
    }

    private void drawBackground(Graphics2D g, int width, int height)
    {
        for(int x = 0; x < width; x += 64)
        {
            for(int y = 0; y < height; y += 64)
            {
                g.drawImage(y == 0 ? grassTexture : dirtTexture, x, y, 64, 64, null);
            }
        }
    }

    @Override
    public void reset()
    {
        selectedIndex = 0;
    }

    @Override
    public void render(@NotNull Graphics2D g, int width, int height)
    {
        drawBackground(g, width, height);

        if (selectedIndex == 0)
            g.setColor(Color.WHITE);
        else
            g.setColor(Color.GRAY);

        g.setFont(FontManager.reformatFont(g.getFont().getName(), Font.BOLD, 20));
        drawTextInCenter(g, selectedIndex == 0 ? "> Start <" : "Start", width, height/2);

        if (selectedIndex == 1)
            g.setColor(Color.WHITE);
        else
            g.setColor(Color.GRAY);

        drawTextInCenter(g, selectedIndex == 1 ? "> Exit <" : "Exit", width, height/2+40);
    }

    @Override
    public void actionPerformed(@NotNull Object action)
    {
        if(action.equals("key-up"))
            selectedIndex = Math.abs(--selectedIndex) % elements;
        else if(action.equals("key-down"))
            selectedIndex = Math.abs(++selectedIndex) % elements;
        else if(action.equals("key-enter"))
        {
            switch(selectedIndex)
            {
                case 0: showView("game"); break;
                case 1: System.exit(0); break;
            }
        }
    }
}

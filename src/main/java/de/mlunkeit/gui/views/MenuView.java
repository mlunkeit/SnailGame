package de.mlunkeit.gui.views;

import de.mlunkeit.gui.View;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MenuView extends View
{

    private int selectedIndex = 0;

    private final int elements = 2;

    private void drawTextInCenter(Graphics2D g, String text, int width, int y)
    {
        g.drawString(text, width/2 - g.getFontMetrics().stringWidth(text) / 2, y);
    }

    @Override
    public void reset()
    {
        selectedIndex = 0;
    }

    @Override
    public void render(@NotNull Graphics2D g, int width, int height)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        if (selectedIndex == 0)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.GRAY);

        g.setFont(new Font(g.getFont().getName(), Font.BOLD, 20));
        drawTextInCenter(g, selectedIndex == 0 ? "> Start <" : "Start", width, height/2);

        if (selectedIndex == 1)
            g.setColor(Color.BLACK);
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
                case 0 -> showView("game");
                case 1 -> System.exit(0);
            }
        }
    }
}

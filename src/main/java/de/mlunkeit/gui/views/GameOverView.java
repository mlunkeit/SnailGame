package de.mlunkeit.gui.views;

import de.mlunkeit.gui.View;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GameOverView extends View
{
    @Override
    public void reset()
    {

    }

    @Override
    public void render(@NotNull Graphics2D g, int width, int height)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);

        Font font = new Font(g.getFont().getName(), Font.BOLD, 40);
        String display = "Game Over!";

        g.setFont(font);
        g.drawString(display, width / 2 - g.getFontMetrics().stringWidth(display) / 2, height / 2 - 20);

        font = new Font(g.getFont().getName(), Font.PLAIN, 18);
        display = "Press ENTER to continue";

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(display, width / 2 - g.getFontMetrics().stringWidth(display) / 2, height / 2 + 30);
    }

    @Override
    public void actionPerformed(@NotNull Object action)
    {
        if(action.equals("key-enter"))
            showView("menu");
    }
}

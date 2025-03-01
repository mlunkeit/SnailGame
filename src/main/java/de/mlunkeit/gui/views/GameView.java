package de.mlunkeit.gui.views;

import de.mlunkeit.gui.FontManager;
import de.mlunkeit.gui.ResourceLoader;
import de.mlunkeit.gui.elements.Character;
import de.mlunkeit.model.SnailGame;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class GameView extends View
{
    private SnailGame game;

    private boolean spacePressed = false;

    private final Image grassTexture;
    private final Image dirtTexture;

    public GameView()
    {
        try
        {
            grassTexture = ResourceLoader.loadImage("/textures/block/grass.png");
            dirtTexture = ResourceLoader.loadImage("/textures/block/dirt.png");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void drawGrassLine(Graphics2D g, int y, int width, int height, int offset)
    {
        offset %= 64;

        for (int x = -offset; x < width; x += 64)
        {
            g.drawImage(grassTexture, x, y, 64, 64, null);

            for (int y2 = y + 64; y2 < height; y2 += 64)
            {
                g.drawImage(dirtTexture, x, y2, 64, 64, null);
            }
        }
    }

    @Override
    public void reset()
    {
        game = new SnailGame(new Character("snail"));
    }

    @Override
    public void render(@NotNull Graphics2D g, int width, int height)
    {
        game.loop(width, height, spacePressed);

        if (game.isGameOver())
            showView("game-over");

        // draw the sky
        g.setColor(new Color(0x85D0FD));
        g.fillRect(0, 0, width, height);

        // draw the floor
        drawGrassLine(g, 3*height/4, width, height, (int) (game.getDistance() % 64));

        int x = game.getCharacter().getX();
        int y = game.getCharacter().getY() - game.getCharacter().getHeight();

        // write score
        g.setColor(Color.BLACK);
        g.setFont(FontManager.reformatFont(g.getFont().getName(), Font.PLAIN, 20));
        String scoreDisplay = String.valueOf(game.getScore());
        g.drawString(scoreDisplay, x - 20 - g.getFontMetrics().stringWidth(scoreDisplay) / 2, y - 60);

        // draw graphical elements
        game.getGraphicalElements().forEach(e -> e.render(g));
    }

    @Override
    public void actionPerformed(@NotNull Object action)
    {
        if (action.equals("key-space"))
        {
            spacePressed = true;
        }
        else if(action.equals("key-release") && spacePressed)
        {
            spacePressed = false;
        }
    }
}

package de.mlunkeit.gui.wrapping;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class MainPanel extends JPanel
{
    private Consumer<Graphics> renderer;

    private final Font font;

    public MainPanel()
    {
        try
        {
            font = Font.createFont(Font.PLAIN, Objects.requireNonNull(getClass().getResourceAsStream("/font.ttf")));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        }
        catch (FontFormatException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void onRender(Consumer<Graphics> renderer)
    {
        this.renderer = renderer;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        long currentTime = System.currentTimeMillis();
        g.setFont(font);
        renderer.accept(g);
        long deltaTime = System.currentTimeMillis() - currentTime;

        try
        {
            if (deltaTime < 20)
                Thread.sleep(20 - deltaTime);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        repaint();
    }
}

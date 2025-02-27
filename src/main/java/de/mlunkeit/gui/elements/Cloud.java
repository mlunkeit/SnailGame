package de.mlunkeit.gui.elements;

import de.mlunkeit.gui.ResourceLoader;

import java.awt.*;
import java.io.IOException;

public class Cloud implements GraphicalElement
{
    private final int size;
    private final Image texture;
    private final float velocityFactor;

    private float x;
    private final int y;

    public Cloud(int size, int x, int y, int type)
    {
        this.size = size;
        this.x = x;
        this.y = y;
        velocityFactor = ((float) size) / 8;

        try
        {
            texture = ResourceLoader.loadImage("/cloud"+type+".png");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Cloud(int size, int x, int y)
    {
        this(size, x, y, 1);
    }

    public void move(int velocity)
    {
        this.x -= ((float) velocity/velocityFactor);
    }

    @Override
    public boolean isVisible()
    {
        return -size <= x;
    }

    @Override
    public void render(Graphics2D g)
    {
        g.drawImage(texture, (int) x, y, size, size, null);
    }
}

package de.mlunkeit.gui.elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Cloud implements GraphicalElement
{
    private final int size;
    private final Image texture;

    private int x;
    private int y;

    public Cloud(int size, int x, int y, int type)
    {
        this.size = size;
        this.x = x;
        this.y = y;

        try
        {
            texture = ImageIO.read(Objects.requireNonNull(getClass().getResource("/cloud"+type+".png")));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Cloud(int size, int x, int y)
    {
        this(size, x, y, 0);
    }

    public void move(int x)
    {
        this.x -= x;
    }

    public boolean visible()
    {
        return -size <= x;
    }

    @Override
    public void render(Graphics2D g)
    {
        System.out.println("Drawing cloud at "+x+" "+y);
        g.drawImage(texture, x, y, size, size, null);
    }
}

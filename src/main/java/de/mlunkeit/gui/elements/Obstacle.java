package de.mlunkeit.gui.elements;

import de.mlunkeit.gui.ResourceLoader;

import java.awt.*;
import java.io.IOException;

public class Obstacle implements GraphicalElement
{
    private boolean scored;

    private final Image texture;

    private int x;
    private final int y;

    private final int drawingSize;

    private final int obstacleWidth;
    private final int obstacleHeight;

    public Obstacle(Type type, int x, int y)
    {
        this.x = x;
        this.y = y;
        drawingSize = type.drawingSize;
        obstacleWidth = type.width;
        obstacleHeight = type.height;

        try
        {
            texture = ResourceLoader.loadImage(type.path);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void move(int x)
    {
        this.x -= x;
    }

    public int getObstacleWidth()
    {
        return obstacleWidth;
    }

    public int getObstacleHeight()
    {
        return obstacleHeight;
    }

    private boolean checkX(int x, int velocity)
    {
        return (this.x + 2 * velocity < x && x - 2 * velocity <= this.x + obstacleWidth);
    }

    public boolean collides(int x, int y, int velocity)
    {
        return checkX(x, velocity) && y > this.y - getObstacleHeight();
    }

    public boolean scores(int x, int y, int velocity)
    {
        if (scored)
            return false;

        scored = checkX(x, velocity) && y < this.y - getObstacleHeight();

        return scored;
    }

    @Override
    public boolean isVisible()
    {
        return -drawingSize <= x;
    }

    @Override
    public void render(Graphics2D g)
    {
        g.drawImage(texture, x, y - drawingSize, drawingSize, drawingSize, null);
    }

    public enum Type
    {
        STEM("/stem.png", 28, 64,64),
        BUSH("/bush.png", 40, 64,64);

        private final String path;
        private final int height;
        private final int width;
        private final int drawingSize;

        Type(String path, int height, int width, int drawingSize)
        {
            this.path = path;
            this.height = height;
            this.width = width;
            this.drawingSize = drawingSize;
        }
    }
}

package de.mlunkeit.gui.elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Obstacle implements GraphicalElement
{
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
            texture = ImageIO.read(Objects.requireNonNull(getClass().getResource(type.path)));
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

    public boolean collides(int x, int y, int velocity)
    {
        return (this.x <= x && x < this.x + this.getObstacleWidth()) && y > this.y - drawingSize;
    }

    public boolean scores(int x, int y, int velocity)
    {
        return (x - velocity < this.x && this.x <= x) && y <= this.y - drawingSize;
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
        STEM("/stem.png", 28, 64,64);

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

package de.mlunkeit.gui.views;

import de.mlunkeit.gui.View;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class GameView extends View
{
    private final Random random = new Random();

    private final Set<Dimension> obstacles = new HashSet<>();

    private long jumpBegin;

    private boolean jumpActive;

    private long begin = System.currentTimeMillis();

    private long lastObstacleSpawned;
    private int spawnNextObstacle = 0;

    private int score = 0;

    private final int maxVelocity = 20;

    private long velocityLastIncreased = 0;
    private final int velocityIncrease = 10000;

    private long distance = 0;

    private final Image grassTexture;
    private final Image dirtTexture;

    public GameView()
    {
        try
        {
            grassTexture = ImageIO.read(Objects.requireNonNull(getClass().getResource("/grass.png")));
            dirtTexture = ImageIO.read(Objects.requireNonNull(getClass().getResource("/dirt.png")));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private int height(long time)
    {
        final float g = 5000f;
        final float v0 = 1.2f;

        return (int) ((-0.5* g *Math.pow(((double) time)/1000, 2)) + (v0 *((double) time)));
    }

    private int velocity(long time)
    {
        final float a = 0.1f;

        return 4 + (int) (a*(((double) time)/1000));
    }

    private void drawClouds(Graphics2D g, int width, int height)
    {

    }

    private void drawGrassLine(Graphics2D g, int y, int width, int height, int offset)
    {
        offset = offset % 64;

        for (int x = -offset; x < width; x += 64)
        {
            g.drawImage(grassTexture, x, y, 64, 64, null);

            for(int y2 = y + 64; y2 < height; y2 += 64)
            {
                g.drawImage(dirtTexture, x, y2, 64, 64, null);
            }
        }
    }

    @Override
    public void reset()
    {
        score = 0;
        lastObstacleSpawned = 0;
        jumpBegin = 0;
        jumpActive = false;
        //velocity = 5;
        begin = System.currentTimeMillis();
        obstacles.clear();
    }

    @Override
    public void render(@NotNull Graphics2D g, int width, int height)
    {
        long time = System.currentTimeMillis() - begin + 1;
        int velocity = velocity(time);

        if(System.currentTimeMillis() - lastObstacleSpawned > spawnNextObstacle)
        {
            obstacles.add(new Dimension(2*width/3, random.nextInt(20, 100)));
            lastObstacleSpawned = System.currentTimeMillis();
            spawnNextObstacle = random.nextInt(5000/velocity, 15000/velocity);
        }

        int deltaY = 0;

        if (jumpActive)
        {
            deltaY = height(System.currentTimeMillis() - jumpBegin);

            if (deltaY < 0)
            {
                deltaY = 0;
                jumpActive = false;
            }
        }

        int x = width / 3;
        int y = 3*height / 4 - deltaY;

        g.setColor(new Color(0xa0d9ef));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));

        g.drawString("Score: " + score, 20, 20);

        g.drawRect(x-10, y-10, 10, 10);

        Set<Dimension> obstaclesToRemove = new HashSet<>();

        for (Dimension obstacle : obstacles)
        {
            g.fillRect(x + obstacle.width, 3*height/4 - obstacle.height, 10, obstacle.height);

            if (-velocity < obstacle.width && obstacle.width <= 0)
            {
                if (obstacle.height > deltaY)
                    showView("game-over");
                else
                    score++;
            }

            obstacle.setSize(obstacle.width - velocity, obstacle.height);

            if (obstacle.width < -width/2)
                obstaclesToRemove.add(obstacle);
        }

        obstaclesToRemove.forEach(obstacles::remove);

        distance += velocity;

        drawGrassLine(g, 3*height/4, width, height, (int) (distance % 64));
    }

    @Override
    public void actionPerformed(@NotNull Object action)
    {
        if(action.equals("key-space") && !jumpActive)
        {
            jumpBegin = System.currentTimeMillis();
            jumpActive = true;
        }
    }
}

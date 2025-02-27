package de.mlunkeit.gui.views;

import de.mlunkeit.gui.FontManager;
import de.mlunkeit.gui.ResourceLoader;
import de.mlunkeit.gui.elements.Cloud;
import de.mlunkeit.gui.elements.Obstacle;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameView extends View
{
    private final Random random = new Random();

    private final Set<Cloud> clouds = new HashSet<>();

    private final Set<Obstacle> obstacles = new HashSet<>();

    private long jumpBegin;

    private boolean jumpActive;

    private long begin = System.currentTimeMillis();

    private long lastObstacleSpawned;
    private int spawnNextObstacle = 0;

    private int score = 0;

    private long distance = 0;

    private final Image grassTexture;
    private final Image dirtTexture;

    private final Image snail1Texture;
    private final Image snail2Texture;
    private final Image snailJumpTexture;

    public GameView()
    {
        try
        {
            grassTexture = ResourceLoader.loadImage("/grass.png");
            dirtTexture = ResourceLoader.loadImage("/dirt.png");
            snail1Texture = ResourceLoader.loadImage("/snail1.png");
            snail2Texture = ResourceLoader.loadImage("/snail2.png");
            snailJumpTexture = ResourceLoader.loadImage("/snail3.png");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private int height(long time)
    {
        final float g = 5000f;
        final float v0 = 1.3f;

        return (int) ((-0.5* g *Math.pow(((double) time)/1000, 2)) + (v0 *((double) time)));
    }

    private int velocity(long time)
    {
        final float a = 0.1f;

        return 7 + (int) (a*(((double) time)/1000));
    }

    private int random(int min, int max)
    {
        return random.nextInt(max - min) + min;
    }

    private void drawClouds(Graphics2D g, int width, int height, int velocity)
    {
        Set<Cloud> removableClouds = new HashSet<>();

        for (Cloud cloud : clouds)
        {
            cloud.render(g);

            cloud.move(velocity/2);

            if (!cloud.isVisible())
                removableClouds.add(cloud);
        }

        if (!removableClouds.isEmpty())
        {
            clouds.add(new Cloud(random(32, 96), width + 96, random(20, height/3)));
        }

        removableClouds.forEach(clouds::remove);
    }

    private void drawObstacles(Graphics2D g, int width, int height, int velocity) {
        Set<Obstacle> removableObstacles = new HashSet<>();

        for (Obstacle obstacle : obstacles) {
            obstacle.render(g);

            obstacle.move(velocity);

            if (!obstacle.isVisible())
                removableObstacles.add(obstacle);
        }

        removableObstacles.forEach(obstacles::remove);
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
        score = 0;
        lastObstacleSpawned = 0;
        jumpBegin = 0;
        jumpActive = false;
        begin = System.currentTimeMillis();
        obstacles.clear();
        clouds.clear();
    }

    @Override
    public void render(@NotNull Graphics2D g, int width, int height)
    {
        long time = System.currentTimeMillis() - begin + 1;
        int velocity = velocity(time);

        if (clouds.isEmpty())
        {
            for (int i = 0; i < 10; i++)
            {
                clouds.add(new Cloud(random(32, 96), i*(width/10), random(20, height/3)));
            }
        }

        if(System.currentTimeMillis() - lastObstacleSpawned > spawnNextObstacle)
        {
            Obstacle.Type type = Obstacle.Type.values()[random.nextInt(Obstacle.Type.values().length)];

            obstacles.add(new Obstacle(type, width, 3*height/4));
            lastObstacleSpawned = System.currentTimeMillis();
            spawnNextObstacle = random(5000/velocity, 15000/velocity);
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

        g.setColor(new Color(0x85D0FD));
        g.fillRect(0, 0, width, height);

        drawClouds(g, width, height, velocity);

        drawObstacles(g, width, height, velocity);

        for (Obstacle obstacle : obstacles)
        {
            if(obstacle.collides(x, y, velocity))
                showView("game-over");
            else if(obstacle.scores(x, y, velocity))
                score++;
        }

        g.setColor(Color.BLACK);
        g.setFont(FontManager.reformatFont(g.getFont().getName(), Font.PLAIN, 20));

        String scoreDisplay = String.valueOf(score);

        g.drawString(scoreDisplay, x - 6 - g.getFontMetrics().stringWidth(scoreDisplay) / 2, y-60);

        boolean useSnail1 = (distance / 200) % 2 == 0;

        //g.fillRect(x-10, y-10, 10, 10);
        Image texture = deltaY > 0 ? snailJumpTexture : (useSnail1 ? snail1Texture : snail2Texture);

        g.drawImage(texture, x-48, y-48, 48, 48, null);

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

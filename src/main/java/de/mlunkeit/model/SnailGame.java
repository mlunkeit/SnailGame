package de.mlunkeit.model;

import de.mlunkeit.gui.elements.Cloud;
import de.mlunkeit.gui.elements.GraphicalElement;
import de.mlunkeit.gui.elements.Obstacle;
import de.mlunkeit.gui.elements.Character;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SnailGame
{
    /**
     * The random number generator for the game.
     */
    private final Random random = new Random();

    /**
     * The set of obstacles.
     */
    private final Set<Obstacle> obstacles = new HashSet<>();

    /**
     * The set of clouds.
     */
    private final Set<Cloud> clouds = new HashSet<>();

    /**
     * The game begin time in milliseconds.
     *
     * <p>This value is used to calculate the current game time.</p>
     */
    private final long begin = System.currentTimeMillis();

    private final Character character;

    private long distance;

    private long lastObstacleSpawned;

    private int spawnNextObstacle;

    private int score = 0;

    private boolean gameOver = false;

    public SnailGame(Character character)
    {
        this.character = character;
    }

    private int velocity(long time)
    {
        //final float a = 0.04f;
        //final int v = (int) (a*(((double) time)/1000));

        if (time < 2000)
            return 7;

        final int v = (int) (Math.log((((double) time)/ 1000)) / Math.log(4));

        return 7 + v;
    }

    private int random(int min, int max)
    {
        return min + random.nextInt(max - min);
    }

    private void updateClouds(int velocity, int width, int height)
    {
        Set<Cloud> removableClouds = new HashSet<>();

        for (Cloud cloud : clouds)
        {
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

    private void updateObstacles(int velocity, int width, int height)
    {
        Set<Obstacle> removableObstacles = new HashSet<>();

        for (Obstacle obstacle : obstacles)
        {
            obstacle.move(velocity);

            if (!obstacle.isVisible())
                removableObstacles.add(obstacle);
        }

        removableObstacles.forEach(obstacles::remove);
    }

    public void loop(int width, int height, boolean jumpPressed)
    {
        long time = System.currentTimeMillis() - begin + 1;
        int velocity = velocity(time);

        if (jumpPressed)
            character.beginJump();

        character.setX(width/3);
        character.setY(3*height/4);

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

        updateClouds(velocity, width, height);
        updateObstacles(velocity, width, height);

        for (Obstacle obstacle : obstacles)
        {
            if (obstacle.collides(character.getX(), character.getY() - character.getHeight(), velocity))
            {
                gameOver = true;
                return;
            }

            if (obstacle.scores(character.getX(), character.getY() - character.getHeight(), velocity))
            {
                score++;
            }
        }

        distance += velocity;
    }

    public Set<Cloud> getClouds()
    {
        return clouds;
    }

    public Set<Obstacle> getObstacles()
    {
        return obstacles;
    }

    public Character getCharacter()
    {
        return character;
    }

    public Set<GraphicalElement> getGraphicalElements()
    {
        Set<GraphicalElement> elements = new HashSet<>();
        elements.addAll(obstacles);
        elements.addAll(clouds);
        elements.add(character);

        return elements;
    }

    public int getScore()
    {
        return score;
    }

    public long getDistance()
    {
        return distance;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }
}

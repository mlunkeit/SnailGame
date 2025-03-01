package de.mlunkeit.gui.elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.mlunkeit.gui.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Character implements GraphicalElement
{
    private final int size;

    private final List<Image> walkingTextures;
    private final List<Image> jumpTextures;

    private long jumpBegin;

    private int x;
    private int y;

    public Character(List<Image> walkingTextures, List<Image> jumpTextures, int size)
    {
        this.walkingTextures = walkingTextures;
        this.jumpTextures = jumpTextures;
        this.size = size;

        jumpBegin = -1;
    }

    public Character(String name)
    {
        String characterPath = "/textures/character/" + name + "/";

        walkingTextures = new ArrayList<>();
        jumpTextures = new ArrayList<>();

        try
        {
            JsonObject characterJson = ResourceLoader.loadJson(characterPath + "character.json");
            JsonObject frames = characterJson.getAsJsonObject("frames");

            JsonArray walkingFrames = frames.getAsJsonArray("walk");
            walkingFrames.forEach(frame -> {
                try
                {
                    walkingTextures.add(ResourceLoader.loadImage(characterPath + frame.getAsString()));
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            });

            JsonArray jumpFrames = frames.getAsJsonArray("jump");
            jumpFrames.forEach(frame -> {
                try
                {
                    jumpTextures.add(ResourceLoader.loadImage(characterPath + frame.getAsString()));
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            });

            size = characterJson.get("size").getAsInt();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void beginJump()
    {
        if (jumpBegin == -1)
            jumpBegin = System.currentTimeMillis();
    }

    private int height(long time)
    {
        final float g = 5000f;
        final float v0 = 1.3f;

        return (int) ((-0.5* g *Math.pow(((double) time)/1000, 2)) + (v0 *((double) time)));
    }

    public int getHeight()
    {
        if (jumpBegin == -1)
            return 0;

        int height = height(System.currentTimeMillis() - jumpBegin);

        if (height < 0)
        {
            jumpBegin = -1;
            return 0;
        }

        return height;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public void render(Graphics2D g)
    {
        int walkFrame = (int) (System.currentTimeMillis() / 400) % walkingTextures.size();
        int jumpFrame = (int) (System.currentTimeMillis() / 400) % jumpTextures.size();

        if (jumpBegin == -1)
            g.drawImage(walkingTextures.get(walkFrame), x - size, y - size - getHeight(), size, size, null);
        else
            g.drawImage(jumpTextures.get(jumpFrame), x - size, y - size - getHeight(), size, size, null);
    }

    @Override
    public boolean isVisible()
    {
        return true;
    }
}

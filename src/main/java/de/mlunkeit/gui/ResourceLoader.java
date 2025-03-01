package de.mlunkeit.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader
{
    private static final Map<String, Image> cachedImages = new HashMap<>();
    private static final Map<String, Font> cachedFonts = new HashMap<>();
    private static final Map<String, JsonObject> cachedJson = new HashMap<>();

    private static InputStream getResourceAsStream(String path)
    {
        return ResourceLoader.class.getResourceAsStream(path);
    }

    public static Image loadImage(String path)
            throws IOException
    {
        if (cachedImages.containsKey(path))
        {
            return cachedImages.get(path);
        }

        Image image = ImageIO.read(getResourceAsStream(path));
        cachedImages.put(path, image);
        return image;
    }

    public static Font loadFont(String path)
            throws IOException, FontFormatException
    {
        if (cachedFonts.containsKey(path))
        {
            return cachedFonts.get(path);
        }

        Font font = Font.createFont(Font.PLAIN, getResourceAsStream(path));
        cachedFonts.put(path, font);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);

        return font;
    }

    public static JsonObject loadJson(String path)
            throws IOException
    {
        if (cachedJson.containsKey(path))
        {
            return cachedJson.get(path);
        }

        JsonObject json;

        try (InputStream inputStream = getResourceAsStream(path))
        {
            json = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
        }

        cachedJson.put(path, json);
        return json;
    }
}

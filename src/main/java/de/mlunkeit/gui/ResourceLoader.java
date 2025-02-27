package de.mlunkeit.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader
{
    private static final Map<String, Image> cachedImages = new HashMap<>();
    private static final Map<String, Font> cachedFonts = new HashMap<>();

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
}

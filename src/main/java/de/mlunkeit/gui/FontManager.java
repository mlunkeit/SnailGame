package de.mlunkeit.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FontManager
{
    private static final Map<String, Font> cachedFonts = new HashMap<>();

    public static Font reformatFont(String name, int style, int size)
    {
        String identifier = name + "_" + size + "_" + style;

        if (cachedFonts.containsKey(identifier))
            return cachedFonts.get(identifier);

        System.out.println("Creating new font " + identifier);

        Font font = new Font(name, style, size);
        cachedFonts.put(identifier, font);

        return font;
    }
}

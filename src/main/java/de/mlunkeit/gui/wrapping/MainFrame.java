package de.mlunkeit.gui.wrapping;

import de.mlunkeit.gui.views.View;
import de.mlunkeit.controller.KeyController;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainFrame extends JFrame
{
    private final Map<String, View> views = new HashMap<>();

    private View currentView = null;

    private String currentViewName = "";

    public MainFrame()
    {
        super("blub");

        try
        {
            Image icon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/textures/character/snail/1.png")));

            setIconImage(icon);
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        setCursor(getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
            "null"));

        setSize(800, 600);
        MainPanel mainPanel = new MainPanel();
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel.onRender(graphics -> {
            if(currentView != null)
            {
                currentView.render((Graphics2D) graphics, getWidth(), getHeight());
            }
        });

        KeyController keyController = new KeyController();
        keyController.setMainFrame(this);
        addKeyListener(keyController);
    }

    public void registerView(String name, @NotNull View view)
    {
        views.put(name, view);
        view.setMainFrame(this);
    }

    public void showView(String name)
    {
        currentView = views.get(name);
        currentView.reset();
        currentViewName = name;
    }

    public void dispatchEvent(Object event, String... views)
    {
        if(views.length != 0 && Arrays.stream(views).noneMatch(v -> v.equals(currentViewName)))
            return;

        currentView.actionPerformed(event);
    }
}

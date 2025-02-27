package de.mlunkeit;

import de.mlunkeit.gui.views.GameOverView;
import de.mlunkeit.gui.views.GameView;
import de.mlunkeit.gui.views.MenuView;
import de.mlunkeit.gui.wrapping.MainFrame;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.registerView("menu", new MenuView());
            mainFrame.registerView("game", new GameView());
            mainFrame.registerView("game-over", new GameOverView());
            mainFrame.showView("menu");
        });
    }
}

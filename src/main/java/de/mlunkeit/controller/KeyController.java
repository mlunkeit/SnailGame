package de.mlunkeit.controller;

import de.mlunkeit.gui.Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController extends Controller implements KeyListener
{

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                dispatch("key-up");
                break;
            case KeyEvent.VK_DOWN:
                dispatch("key-down");
                break;
            case KeyEvent.VK_LEFT:
                dispatch("key-left");
                break;
            case KeyEvent.VK_RIGHT:
                dispatch("key-right");
                break;
            case KeyEvent.VK_SPACE:
                dispatch("key-space");
                break;
            case KeyEvent.VK_ENTER:
                dispatch("key-enter");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        dispatch("key-release");
    }
}

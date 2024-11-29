package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

// this is the game panel that is used to be integrated within the gamewindow class

public class GamePanel extends JPanel {

//    private MouseInputs mouseInputs = new MouseInputs(this);
    private Game game;
    private float mouseX, mouseY; // Mouse position fields

    private boolean isReadyForInput = false;  // Tracks when game is ready for mouse input

    public GamePanel(Game game) {
        this.game = game;
        System.out.println("GamePanel initialized with Game: " + (game != null));

        setPanelSize();
        setFocusable(true);
        addKeyListener(new KeyboardInputs(this));                               // naa ni ang keyboardInputs in another package named keyboardInputs para dili mag yagaw2

        MouseInputs mouseInputs = new MouseInputs(this); // Pass GamePanel to MouseInputs
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    // Method to set mouse input readiness to true after rendering
    public boolean enableMouseInput() {
        return true;  // Enables mouse input
    }

///////// ▼ ▼ ▼ ---------------- GAME PANEL METHODS ---------------- ▼ ▼ ▼ /////////

    // for the window panel
    private void setPanelSize() {
        System.out.println("size " + GAME_WIDTH  + " : " + GAME_HEIGHT);
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void setMouseCoordinates(float x, float y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    // Get mouse position for other classes
    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }


//    public void updateGame() {
//
//    }

    // a magical method that allows us to "paint" stuff
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game != null) {
            try {
                game.render(g);
            } catch (Exception e) {
                System.err.println("Error during rendering: " + e.getMessage());
            }
        } else {
            System.err.println("Error: 'game' is null in GamePanel.paintComponent()");
        }
    }


    public Game getGame() {
        return game;
    }
}

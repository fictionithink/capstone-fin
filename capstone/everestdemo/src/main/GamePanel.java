package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs = new MouseInputs(this);

    // FOR MOVEMENT
    private float xDelta = 100, yDelta = 100;
    private float xDir = 1, yDir = 1;

    // FOR FPS
    private int frames = 0;
    private long lastCheck = 0;

    // FOR RECTANGLE
    private Color color = new Color(123,65,234);
    private Random random;

    public GamePanel() {
        random = new Random();
        addKeyListener(new KeyboardInputs(this));                               // naa ni ang keyboardInputs in another package named keyboardInputs para dili mag yagaw2
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }


///////// ▼ ▼ ▼ ---------------- GAME PANEL METHODS ---------------- ▼ ▼ ▼ /////////

    // for mouse
    public void changeXDelta(int value){
        this.xDelta += value;
    }


    public void setRectPos(int x, int y){
        this.xDelta = x;
        this.yDelta = y;
    }

    // for keyboard
    public void changeYDelta(int value){
        this.yDelta += value;
    }

    // a magical method that allows us to "paint" stuff
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        updateRectangle();
        g.setColor(color);
        g.fillRect((int)xDelta,(int)yDelta, 200, 50);                     // creates a filled rectangle

    }

    private void updateRectangle(){
        xDelta += xDir;
        if(xDelta > 400 || xDelta < 0){
            xDir *= -1;
            color = getRndColor();
        }

        yDelta += yDir;

        if(yDelta > 400 || yDelta < 0){
            yDir *= -1;
            color = getRndColor();
        }
    }

    // color randomizer (we don't need this, but we do need the random implementation)
    private Color getRndColor() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        return new Color(r,g,b);
    }

}

package gamestates;

import entities.Player;
import levels.LevelManager;
import main.Game;
import main.GamePanel;
import ui.PauseOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements Statemethods{

    private Player player;
    private LevelManager levelManager;
    private GamePanel gamePanel;

    private PauseOverlay pauseOverlay;
    private boolean paused = false;


    public Playing(Game game, GamePanel gamePanel) {
        super(game);
        this.gamePanel = gamePanel; // Set GamePanel reference
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), game);
        player.LoadLvlData(levelManager.getCurrentLevel().getLvlData());
        pauseOverlay = new PauseOverlay(this);
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
        } else
            pauseOverlay.update();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        player.render(g);

        if (paused)
            pauseOverlay.draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){                        // reads inputs
            case KeyEvent.VK_A:                         //VK_( A ) key
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:                         //VK_( D ) key
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:                         //VK_( Space bar ) key
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){                        // reads inputs
            case KeyEvent.VK_A:                         //VK_( A ) key
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:                         //VK_( D ) key
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:                         //VK_( Space bar ) key
                player.setJump(false);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttacking(true); // Ensure this is called
    }


    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseDragged(e);

    }

    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverlay.mousePressed(e);

        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) { // Left-click
            player.setAttacking(true); // Ensure this is called
        } else if (button == MouseEvent.BUTTON3) { // Right-click
            getPlayer().shootLaser();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseReleased(e);

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused)
            pauseOverlay.mouseMoved(e);
        else
            gamePanel.setMouseCoordinates(e.getX(), e.getY());

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public void unpauseGame() {
        paused = false;
    }
}

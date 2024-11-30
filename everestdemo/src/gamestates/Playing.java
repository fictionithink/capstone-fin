package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements Statemethods{

    private int xLvlOffset;
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GamePanel gamePanel;
    private boolean paused;


    public Playing(Game game, GamePanel gamePanel) {
        super(game);
        this.gamePanel = gamePanel; // Set GamePanel reference
        if (gamePanel == null) {
            System.err.println("GamePanel is null in Playing constructor! Check where it's being passed.");
        } else {
            System.out.println("GamePanel passed to Playing: " + gamePanel);
        }
        initClasses();
    }

    private void initClasses() {
        if (gamePanel == null) {
            System.err.println("GamePanel is null in Playing.initClasses()! Check the constructor.");
        } else {
            System.out.println("GamePanel is correctly passed to Playing.");
        }



        System.out.println("Initializing LevelManager...");
        levelManager = new LevelManager(game);
        System.out.println("LevelManager initialized: " + (levelManager != null));

        enemyManager = new EnemyManager(this);

        System.out.println("Initializing Player...");
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), game);
        System.out.println("Player initialized: " + (player != null));

        System.out.println("GamePanel reference in Game: " + gamePanel);
        player.LoadLvlData(levelManager.getCurrentLevel().getLvlData());
    }



    @Override
    public void update() {
        levelManager.update();
        player.update();
        enemyManager.update(levelManager.getCurrentLevel().getLvlData(),player);
    }

    @Override
    public void draw(Graphics g) {
        if (levelManager != null) {
            levelManager.draw(g);
        }
        if (player != null) {
            player.render(g,xLvlOffset);
            enemyManager.draw(g,xLvlOffset);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gamePanel == null) {
            System.err.println("GamePanel is null in Playing.mouseDragged()! Ensure it's initialized correctly.");
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true); // Ensure this is called
        }
    }


    public void mouseDragged(MouseEvent e){

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("pressing mouse left");
        if(e.getButton() == MouseEvent.BUTTON1){
            player.setAttacking(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gamePanel == null) {
            System.err.println("GamePanel is null in Playing.mouseMoved()! Ensure it's initialized correctly.");
            return;
        }
        gamePanel.setMouseCoordinates(e.getX(), e.getY());
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){                        // reads inputs
//            case KeyEvent.VK_W:                         //VK_( W ) key
//                player.setUp(true);
//                break;
            case KeyEvent.VK_A:                         //VK_( A ) key
                player.setLeft(true);
                break;
//            case KeyEvent.VK_S:                         //VK_( S ) key
//                player.setDown(true);
//                break;
            case KeyEvent.VK_D:                         //VK_( D ) key
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:                         //VK_( Space bar ) key
                player.setJump(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){                        // reads inputs
//            case KeyEvent.VK_W:                         //VK_( W ) key
//                System.out.println("Pressing W");
//                player.setUp(false);
//                break;
            case KeyEvent.VK_A:                         //VK_( A ) key
//                System.out.println("Pressing A");
                player.setLeft(false);
                break;
//            case KeyEvent.VK_S:                         //VK_( S ) key
//                System.out.println("Pressing S");
//                player.setDown(false);
//                break;
            case KeyEvent.VK_D:                         //VK_( D ) key
//                System.out.println("Pressing D");
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:                         //VK_( Space bar ) key
//                System.out.println("Pressing Space bar");
                player.setJump(false);
                break;
        }
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }
}

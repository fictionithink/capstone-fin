package gamestates;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import main.GamePanel;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constants.Environments.*;
import ui.RoundCompletedOverlay;

public class Playing extends State implements Statemethods{

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GamePanel gamePanel;
    private GameOverOverlay gameOverOverlay;
    private PauseOverlay pauseOverlay;
    private RoundCompletedOverlay roundCompletedOverlay;
    private boolean paused = false;

    private int xLevelOffset;
    private int leftBorder = (int) (0.4 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.6 * Game.GAME_WIDTH);
    private int maxLevelOffsetX;

    private boolean gameOver;
    private boolean roundCompleted = false;

    private BufferedImage backgroundImg, trees, citynear, cityfar, overlayImg;

    public Playing(Game game, GamePanel gamePanel) {
        super(game);
        this.gamePanel = gamePanel;
        initClasses();

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.W1_BACKGROUND);
        trees = LoadSave.getSpriteAtlas(LoadSave.W1_TREES);
        citynear = LoadSave.getSpriteAtlas(LoadSave.W1_CITY_NEAR);
        cityfar = LoadSave.getSpriteAtlas(LoadSave.W1_CITY_FAR);
        overlayImg = LoadSave.getSpriteAtlas(LoadSave.W1_CITY_OVERLAY);

        calcLevelOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        if (!roundCompleted) {
            roundCompleted = true;
            resetAll();
            levelManager.loadNextLevel();
        }
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLevelOffset() {
        maxLevelOffsetX = levelManager.getCurrentLevel().getLevelOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);

        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), game, this);
        player.LoadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        roundCompletedOverlay = new RoundCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (roundCompleted) {
            roundCompletedOverlay.update();

            if (!roundCompletedOverlay.isCountdownActive() && roundCompleted) {
                roundCompleted = false;
                loadNextLevel();
            }
        } else {
            levelManager.update();
            player.update();

            if (player.getLaser() != null) {
                enemyManager.checkLaserHit(player.getLaser());
            }
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLevelOffset;

        if (diff > rightBorder)
            xLevelOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLevelOffset += diff - leftBorder;

        if (xLevelOffset > maxLevelOffsetX)
            xLevelOffset = maxLevelOffsetX;
        else if (xLevelOffset < 0)
            xLevelOffset = 0;

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawCity(g);
        drawTrees(g);

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2d.drawImage(overlayImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g2d.setComposite(originalComposite);

        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.draw(g,xLevelOffset);



        if (paused) {
            g.setColor(new Color(0,0,0, 150));
            g.fillRect(0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }else if(gameOver){
            gameOverOverlay.draw(g);
        } else if (roundCompleted){
            roundCompletedOverlay.draw(g);
        }
    }

    private void drawCity(Graphics g) {
        for(int i = 0; i < 5; i++) {
            g.drawImage(citynear, i * CITYNEAR_WIDTH - (int)(xLevelOffset * 0.1), (int) (75 * Game.SCALE) - 250, CITYNEAR_WIDTH+200, CITYNEAR_HEIGHT+200, null);
        }

        for(int i = 0; i < 5; i++) {
            g.drawImage(cityfar, i * CITYFAR_WIDTH - (int)(xLevelOffset * 0.2), (int) (85 * Game.SCALE)- 250, CITYFAR_WIDTH+200, CITYFAR_HEIGHT+200, null);
        }
    }

    private void drawTrees(Graphics g) {
        for(int i = 0; i < 5; i++) {
            g.drawImage(trees, i * TREES_WIDTH- (int)(xLevelOffset * 0.3), (int) (65 * Game.SCALE) - 200, TREES_WIDTH+200, TREES_HEIGHT+200, null);
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.KeyPressed(e);
        else
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
        if(!gameOver)
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

    public void mouseClicked(MouseEvent e) {
        if(!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true); 
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) return;

        if (paused) {
            pauseOverlay.mousePressed(e);
            return;
        }

        int button = e.getButton();
        if (button == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        } else if (button == MouseEvent.BUTTON3) {
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
        if (paused) {
            pauseOverlay.mouseMoved(e);
        } else if (!gameOver) {
            gamePanel.setMouseCoordinates(e.getX(), e.getY());
        }
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

    public int getXLevelOffset() {
        return xLevelOffset;
    }

    public void resetAll() {
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), game, this);
        player.LoadLvlData(levelManager.getCurrentLevel().getLevelData());
        enemyManager = new EnemyManager(this);
        xLevelOffset = 0;
        paused = false;
        roundCompleted = false;
        gameOver = false;
    }

    public void setGameOVer(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    public void setLevelCompleted(boolean roundCompleted) {
        this.roundCompleted = roundCompleted;
        if (roundCompleted) {
            roundCompletedOverlay.resetCountdown();
        }
    }


    public void setMaxLevelOffset(int levelOffset){
        this.maxLevelOffsetX = levelOffset;
    }
} 

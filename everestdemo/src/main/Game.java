package main;

// this class holds the entirety of the game

import entities.Player;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;

import java.awt.*;

public class Game implements Runnable{

//// Fields
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;

    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 2.0f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int  TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

//// Constructor (Game)
    public Game() {

        System.out.println("Initializing GamePanel...");
        gamePanel = new GamePanel(this);
        System.out.println("GamePanel reference in Game: " + gamePanel);

        System.out.println("Initializing GameWindow...");
        gameWindow = new GameWindow(gamePanel);

        System.out.println("Initializing Classes...");
        initClasses();

        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        System.out.println("Menu initialized: " + (menu != null));

        playing = new Playing(this, gamePanel);
        System.out.println("Playing initialized: " + (playing != null));
    }


    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update() {
        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            default:
                break;
        }
    }

    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU:
                if (menu == null) {
                    System.err.println("Error: 'menu' is null in Game.render()");
                    return;
                }
                menu.draw(g);
                break;
            case PLAYING:
                if (playing == null) {
                    System.err.println("Error: 'playing' is null in Game.render()");
                    return;
                }
                playing.draw(g);
                break;
            default:
                System.err.println("Error: Unknown Gamestate: " + Gamestate.state);
                break;
        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0/FPS_SET;
        double timePerUpdate = 1000000000.0/UPS_SET;
        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while(true){
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }


            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames +  " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }

    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu(){
        return menu;
    }

    public Playing getPlaying(){
        return playing;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }


}

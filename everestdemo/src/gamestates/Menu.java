package gamestates;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.*;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons = new MenuButton[4];
    private BufferedImage backgroundImg, backgroundTitle, backgroundCity,cloudBackground1,cloudBackground2,cloudBackground3,cloudBackground4;
    private int menuX, menuY, menuWidth, menuHeight;
    private double SKYscrollOffset;
    private BufferedImage[] CatPixelated = new BufferedImage[4];
    private double CITYscrollOffset;
    private double CLOUD4scrollOffset, CLOUD3scrollOffset; // Used for dynamic scrolling
    private int currentTitleFrame = 0; // Index of the current frame
    private long lastFrameTime = 0;    // Time when the last frame was updated
    private final long FRAME_INTERVAL = 400;

    public boolean isInitialized() {
        return isInitialized;
    }

    private boolean isInitialized = false;  // Flag to track if menu is initialized

    public Menu(Game game) {
        super(game);
        System.out.println("Menu constructor called.");
       // loadButtons();
        loadCONTENT();
        isInitialized = true;  // Mark as initialized after the menu and buttons are loaded
    }


    private void loadCONTENT(){

        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);

        cloudBackground1 = LoadSave.getSpriteAtlas(LoadSave.CLOUD_1);
        menuWidth = (int) (cloudBackground1.getWidth() * Game.SCALE);
        menuHeight = (int) (cloudBackground1.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);

        cloudBackground2 = LoadSave.getSpriteAtlas(LoadSave.CLOUD_2);
        menuWidth = (int) (cloudBackground2.getWidth() * Game.SCALE);
        menuHeight = (int) (cloudBackground2.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);

        cloudBackground3 = LoadSave.getSpriteAtlas(LoadSave.CLOUD_3);
        menuWidth = (int) (cloudBackground3.getWidth() * Game.SCALE);
        menuHeight = (int) (cloudBackground3.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);

        cloudBackground4 = LoadSave.getSpriteAtlas(LoadSave.CLOUD_4);
        menuWidth = (int) (cloudBackground4.getWidth() * Game.SCALE);
        menuHeight = (int) (cloudBackground4.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);

        backgroundTitle = LoadSave.getSpriteAtlas(LoadSave.MENU_TITLE);

        menuWidth = (int)(backgroundTitle.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundTitle.getHeight() * Game.SCALE);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (-120 * SCALE);

        buttons[0] = new MenuButton(Game.GAME_WIDTH/2,(int)(190*Game.SCALE),0,Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2,(int)(230*Game.SCALE),1,Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH/2,(int)(270*Game.SCALE),2,Gamestate.ABOUT);
        buttons[3] = new MenuButton(Game.GAME_WIDTH/2,(int)(310*Game.SCALE),3,Gamestate.EXIT);

        BufferedImage spriteSheet = LoadSave.getSpriteAtlas(LoadSave.CAT_PIXEL); // Load the sprite sheet
        int spriteWidth = spriteSheet.getWidth() / 8; // Divide the width by 8 (columns)
        int spriteHeight = spriteSheet.getHeight() / 10; // Divide the height by 10 (rows)

        // Directly reference the sprite positions in the sprite sheet
        CatPixelated[0] = spriteSheet.getSubimage(2 * spriteWidth, 0 * spriteHeight, spriteWidth, spriteHeight); // Row 2, Column 0
        CatPixelated[1] = spriteSheet.getSubimage(2 * spriteWidth, 1 * spriteHeight, spriteWidth, spriteHeight); // Row 2, Column 1
        CatPixelated[2] = spriteSheet.getSubimage(2 * spriteWidth, 2 * spriteHeight, spriteWidth, spriteHeight); // Row 2, Column 2
        CatPixelated[3] = spriteSheet.getSubimage(2 * spriteWidth, 3 * spriteHeight, spriteWidth, spriteHeight); // Row 2, Column 3

    }


/*
    private void loadTitle() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);

        backgroundTitle = LoadSave.getSpriteAtlas(LoadSave.MENU_TITLE);

        menuWidth = (int)(backgroundTitle.getWidth() * Game.SCALE);
        menuHeight = (int)(backgroundTitle.getHeight() * Game.SCALE);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (-120 * SCALE);

    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH/2,(int)(190*Game.SCALE),0,Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2,(int)(230*Game.SCALE),1,Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH/2,(int)(270*Game.SCALE),2,Gamestate.ABOUT);
        buttons[3] = new MenuButton(Game.GAME_WIDTH/2,(int)(310*Game.SCALE),3,Gamestate.EXIT);
    }
*/
    @Override
    public void update() {
        if (isInitialized) {  // Only update menu if it is initialized
            for (MenuButton mb : buttons)
                mb.update();
        }

        SKYscrollOffset -= 1.0; // Adjust speed here
        if (SKYscrollOffset <= -Game.GAME_WIDTH) {
            SKYscrollOffset = 0; // Reset when fully scrolled
        }

        CITYscrollOffset -= 0.5; // Adjust speed here
        if (CITYscrollOffset <= -Game.GAME_WIDTH) {
            CITYscrollOffset = 0; // Reset when fully scrolled
        }

        CLOUD4scrollOffset -= 0.2; // Adjust speed here
        if (CLOUD4scrollOffset <= -Game.GAME_WIDTH) {
            CLOUD4scrollOffset = 0; // Reset when fully scrolled
        }

        CLOUD3scrollOffset -= 0.3; // Adjust speed here
        if (CLOUD3scrollOffset <= -Game.GAME_WIDTH) {
            CLOUD3scrollOffset = 0; // Reset when fully scrolled
        }
    }

    @Override
    public void draw(Graphics g) {
        if (isInitialized) {  // Only draw menu if it is initialized
         //   g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
            g.drawImage(cloudBackground1, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            g.drawImage(cloudBackground2, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

            g.drawImage(cloudBackground3, (int) CLOUD3scrollOffset, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            g.drawImage(cloudBackground3, (int) CLOUD3scrollOffset + Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

            g.drawImage(cloudBackground4, (int) CLOUD4scrollOffset, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            g.drawImage(cloudBackground4, (int) CLOUD4scrollOffset + Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

            g.drawImage(backgroundImg, (int) CITYscrollOffset, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            g.drawImage(backgroundImg, (int) CITYscrollOffset + Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

            g.drawImage(backgroundTitle, 0, menuY, GAME_WIDTH, GAME_HEIGHT, null);

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameTime >= FRAME_INTERVAL) {
                currentTitleFrame = (currentTitleFrame + 1) % CatPixelated.length;
                lastFrameTime = currentTime;
            }
            g.drawImage(CatPixelated[currentTitleFrame],  (Game.GAME_WIDTH/2)+240, (Game.GAME_HEIGHT/2)-330, 150, 150, null);

            for (MenuButton mb : buttons)
                mb.draw(g);
        } else {
            System.err.println("Menu is not fully initialized yet.");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {  // Check if the mouse is over a button
                mb.setMousePressed(true);  // Set this button as pressed
                break;  // Exit the loop once the button is pressed
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons) {
            if(isIn(e,mb)){
                if(mb.isMousePressed()) {
                    if (mb.getState() == Gamestate.PLAYING) {
                        game.getAudioPlayer().stopSong(); // Stop the menu music
                        game.getAudioPlayer().setLevelSong(1); // Start playing level music
                    }
                    mb.applyGameState();
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton mb : buttons)
            mb.resetBools();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons) {
            mb.setMouseOver(false);  // Reset the state for each button
        }

        // Check all buttons for hover
        for(MenuButton mb : buttons) {
            if (isIn(e, mb)) {  // Check if mouse is over the button
                mb.setMouseOver(true);  // Set to true for the button the mouse is hovering over
                break;  // Stop the loop once the mouse is over a button
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }
}

package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.SoundButton;
import ui.UrmButton;
import ui.VolumeButton;
import utils.LoadSave;
import static utils.Constants.UI.UrmButtons.*;

public class GameOptions extends State implements Statemethods {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg, skyBackgroundImg, skyBackground2Img;
    private BufferedImage backgroundTitle, backgroundCity,cloudBackground1,cloudBackground2,cloudBackground3,cloudBackground4;
    private int menuX, menuY, menuWidth, menuHeight;
    private int bgX, bgY, bgW, bgH;
    private double SKYscrollOffset;
    private double CITYscrollOffset;
    private double CLOUD4scrollOffset, CLOUD3scrollOffset; // Used for dynamic scrolling


    private int skyX1, skyX2; // Two positions for the sky background to create a looping effect
    private UrmButton menuB;
    private SoundButton muteButton, sfxButton;
    private VolumeButton volumeButton;

    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = new AudioOptions(game,1);
        System.out.println("AudioOptions initialized: " + (audioOptions != null)); // Debugging output
        skyX1 = 0; // Initialize first sky position
        skyX2 = Game.GAME_WIDTH; // Initialize second sky position
    }

    private void loadButton() {
        int menuX = (int) (403 * Game.SCALE);
        int menuY = (int) (300 * Game.SCALE);

        menuB = new UrmButton(menuX, menuY, URM_SIZE_MENU+100, URM_SIZE_MENU+100, 2,6);
        System.out.println("Button Position: (" + menuB.getX() + ", " + menuB.getY() + ")");
        System.out.println("Button Size: (" + menuB.getWidth() + ", " + menuB.getHeight() + ")");
    }

    private void loadImgs() {

        optionsBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.OPTIONS_MENU);

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

        bgW = 800;
        bgH = 500;
        bgX = (Game.GAME_WIDTH / 2 - bgH / 2)-150;
        bgY = (int)(80 * Game.SCALE);

    }

    @Override
    public void update() {

        audioOptions.updateMenu();
        menuB.update();



        // Move both sky images to the left
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
        // Draw the sky backgrounds at the correct positions for the parallax effect


        // Draw the other UI elements like background and options menu

        g.drawImage(cloudBackground1, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(cloudBackground2, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.drawImage(cloudBackground3, (int) CLOUD3scrollOffset, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(cloudBackground3, (int) CLOUD3scrollOffset + Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.drawImage(cloudBackground4, (int) CLOUD4scrollOffset, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(cloudBackground4, (int) CLOUD4scrollOffset + Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.drawImage(backgroundImg, (int) CITYscrollOffset, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, (int) CITYscrollOffset + Game.GAME_WIDTH, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);


        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

        // Draw buttons and other UI elements

        audioOptions.drawMenu(g);
        menuB.drawMenu(g);

    }




    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
        if (audioOptions.getvolumeButtonMenu().isMousePressed()) {
            audioOptions.getvolumeButtonMenu().changeX(e.getX());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed at: " + e.getX() + ", " + e.getY());
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
            System.out.println("UrmButton pressed");
        } else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        } else
            audioOptions.mouseReleased(e);

        menuB.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    private boolean isIn(MouseEvent e, UrmButton b) {
        boolean inside = b.getBounds().contains(e.getX(), e.getY());
        System.out.println("Mouse is inside button: " + inside);
        return inside;
    }
}


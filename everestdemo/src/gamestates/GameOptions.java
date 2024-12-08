package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.UrmButton;
import utils.LoadSave;
import static utils.Constants.UI.UrmButtons.*;

public class GameOptions extends State implements Statemethods {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg, skyBackgroundImg, skyBackground2Img;
    private int bgX, bgY, bgW, bgH;

    private int skyX1, skyX2; // Two positions for the sky background to create a looping effect
    private UrmButton menuB;

    public GameOptions(Game game) {
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions();
        skyX1 = 0; // Initialize first sky position
        skyX2 = Game.GAME_WIDTH; // Initialize second sky position
    }

    private void loadButton() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);

        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs() {
        skyBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);
        skyBackground2Img = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);

        bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuB.update();
      //  audioOptions.update();

        // Move both sky images to the left
        skyX1 -= 0.5;
        skyX2 -= 0.5;

        // If skyX1 goes off the screen, reset it to the right side
        if (skyX1 <= -Game.GAME_WIDTH) {
            skyX1 = Game.GAME_WIDTH;
        }

        // If skyX2 goes off the screen, reset it to the right side
        if (skyX2 <= -Game.GAME_WIDTH) {
            skyX2 = Game.GAME_WIDTH;
        }
    }



    @Override
    public void draw(Graphics g) {
        // Draw the sky backgrounds at the correct positions for the parallax effect
        g.drawImage(skyBackgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        g.drawImage(skyBackground2Img, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        // Draw the other UI elements like background and options menu
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

        // Draw buttons and other UI elements
        menuB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else
            audioOptions.mousePressed(e);
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
        return b.getBounds().contains(e.getX(), e.getY());
    }
}

package gamestates;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class Menu extends State implements Statemethods{

    private MenuButton[] buttons = new MenuButton[4];
    private BufferedImage backgroundImg;

    public boolean isInitialized() {
        return isInitialized;
    }

    private boolean isInitialized = false;  // Flag to track if menu is initialized

    public Menu(Game game) {
        super(game);
        System.out.println("Menu constructor called.");
        loadButtons();
        loadBackground();
        isInitialized = true;  // Mark as initialized after the menu and buttons are loaded
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.MENU_BACKGROUND);

    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH/2,(int)(150*Game.SCALE),0,Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2,(int)(190*Game.SCALE),1,Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH/2,(int)(230*Game.SCALE),2,Gamestate.ABOUT);
        buttons[3] = new MenuButton(Game.GAME_WIDTH/2,(int)(270*Game.SCALE),3,Gamestate.EXIT);
    }

    @Override
    public void update() {
        if (isInitialized) {  // Only update menu if it is initialized
            for (MenuButton mb : buttons)
                mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (isInitialized) {  // Only draw menu if it is initialized
            g.drawImage(backgroundImg, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

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
                if(mb.isMousePressed())
                    mb.applyGameState();
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

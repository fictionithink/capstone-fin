package inputs;

import main.Game;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){                        // reads inputs

            case KeyEvent.VK_W:                         //VK_( W ) key
                System.out.println("pressing W");
                gamePanel.changeYDelta(-5);
                break;
            case KeyEvent.VK_A:                         //VK_( A ) key
                System.out.println("pressing A");
                gamePanel.changeXDelta(-5);
                break;
            case KeyEvent.VK_S:                         //VK_( S ) key
                System.out.println("pressing S");
                gamePanel.changeYDelta(+5);
                break;
            case KeyEvent.VK_D:                         //VK_( D ) key
                System.out.println("pressing D");
                gamePanel.changeXDelta(+5);
                break;
        }
    }


}

package inputs;

import main.Game;
import main.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.Game;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    //MOUSE LISTENER METHODS vvv
    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("mouse clicked!");
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // MOUSE MOTION LISTENER METHODS vvv

    @Override
    public void mouseDragged(MouseEvent e) {
        //.out.println("mouse dragged!");
        //gamePanel.setRectPos(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("mouse moved!");
        //gamePanel.setRectPos(e.getX(), e.getY());
    }
}

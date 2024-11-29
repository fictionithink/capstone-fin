package main;

import javax.swing.JFrame;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();

        jframe.setSize(400,400);                            // sets window's dimensions
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);          // allows us to terminate program when we "close" using exit button
        jframe.add(gamePanel);                                          // by invoking this, we are adding the rectangle we added
        jframe.setLocationRelativeTo(null);                             // puts our frame at the center of the screen
        jframe.setVisible(true);                                        // makes frame visible on computer screen
    }
}

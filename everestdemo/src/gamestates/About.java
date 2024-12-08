    package gamestates;

    import main.Game;
    import ui.UrmButton;
    import utils.LoadSave;

    import java.awt.*;
    import java.awt.event.KeyEvent;
    import java.awt.event.MouseEvent;
    import java.awt.image.BufferedImage;

    //import static utils.Constants.UI.UrmButtons.URM_SIZE_MENU;

    public class About extends State implements Statemethods {

        private UrmButton menuB; // Back button
        private BufferedImage aboutP; // About page image
        private int menuX, menuY, menuWidth, menuHeight;

        public About(Game game) {
            super(game);
            loadBackImg();
            loadButton();
        }

        private void loadBackImg() {
            // Load the about page image
            aboutP = LoadSave.getSpriteAtlas(LoadSave.ABOUT_PAGE);
            menuWidth = (int) (aboutP.getWidth() * Game.SCALE);
            menuHeight = (int) (aboutP.getHeight() * Game.SCALE);
        }

        private void loadButton() {
            // Assign to instance variables, not local variables
            this.menuX = (int) (403 * Game.SCALE);
            this.menuY = (int) (400 * Game.SCALE); // Place it near the bottom
            //menuB = new UrmButton(menuX, menuY, URM_SIZE_MENU , URM_SIZE_MENU , 2, 6);
        }

        @Override
        public void update() {
            // Update the menu button
            menuB.update();
        }

        @Override
        public void draw(Graphics g) {
            // Draw the about page image
            g.drawImage(aboutP, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

            // Draw the menu button
            //menuB.drawMenu(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // No specific functionality here
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("Mouse pressed at: " + e.getX() + ", " + e.getY());
            if (isIn(e, menuB)) {
                menuB.setMousePressed(true);
                System.out.println("UrmButton pressed");

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Handle the menu button release
            if (isIn(e, menuB) && menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU; // Go back to the main menu
            }
            menuB.resetBools();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // Set hover effect for the menu button
            menuB.setMouseOver(false);

            if (isIn(e, menuB))
                menuB.setMouseOver(true);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // Allow ESC key to go back to the main menu
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Gamestate.state = Gamestate.MENU;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // No specific functionality here
        }

        private boolean isIn(MouseEvent e, UrmButton b) {
            Rectangle bounds = b.getBounds();
            System.out.println("Mouse: (" + e.getX() + ", " + e.getY() + ")");
            System.out.println("Button bounds: " + bounds);
            return bounds.contains(e.getX(), e.getY());
        }
    }

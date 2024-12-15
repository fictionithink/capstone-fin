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

        private UrmButton menuB;
        private BufferedImage aboutP;
        private int menuX, menuY, menuWidth, menuHeight;

        public About(Game game) {
            super(game);
            loadBackImg();
            loadButton();
        }

        private void loadBackImg() {
            aboutP = LoadSave.getSpriteAtlas(LoadSave.ABOUT_PAGE);
            menuWidth = (int) (aboutP.getWidth() * Game.SCALE);
            menuHeight = (int) (aboutP.getHeight() * Game.SCALE);
        }

        private void loadButton() {
            this.menuX = (int) (403 * Game.SCALE);
            this.menuY = (int) (400 * Game.SCALE);
            //menuB = new UrmButton(menuX, menuY, URM_SIZE_MENU , URM_SIZE_MENU , 2, 6);
        }

        @Override
        public void update() {
            menuB.update();
        }

        @Override
        public void draw(Graphics g) {
            g.drawImage(aboutP, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
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
        public void mouseDragged(MouseEvent e){

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isIn(e, menuB) && menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
            }
            menuB.resetBools();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            menuB.setMouseOver(false);

            if (isIn(e, menuB))
                menuB.setMouseOver(true);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Gamestate.state = Gamestate.MENU;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        private boolean isIn(MouseEvent e, UrmButton b) {
            Rectangle bounds = b.getBounds();
            System.out.println("Mouse: (" + e.getX() + ", " + e.getY() + ")");
            System.out.println("Button bounds: " + bounds);
            return bounds.contains(e.getX(), e.getY());
        }
    }

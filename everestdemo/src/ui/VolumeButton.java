package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LoadSave;
import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;

    public VolumeButton(int x, int y, int width, int height) {
        super(780 + width / 2, 570, VOLUME_WIDTH, height); // Center button
        bounds.x -= VOLUME_WIDTH / 2; // Adjust bounds to match button's width

        // Set slider's x and y based on red rectangle alignment
        this.x = 724; // Adjust x to position slider horizontally
        this.y = 580; // Adjust y to position slider vertically
        this.width = width;

        // Initialize button position and movement boundaries
        buttonX = this.x + this.width / 2; // Center the button
        minX = this.x + VOLUME_WIDTH / 2;  // Set left boundary
        maxX = this.x + this.width - VOLUME_WIDTH / 2; // Set right boundary

        // Dynamically set hitbox bounds for the button
        bounds.x = buttonX - VOLUME_WIDTH / 2;
        bounds.y = this.y;
        bounds.width = VOLUME_WIDTH; // Match button width
        bounds.height = height;      // Match button height

        loadImgs(); // Load slider and button images
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    public void draw(Graphics g) {
// Draw slider background
        g.drawImage(slider, x, y, width, height, null);

        // Draw draggable button (centered vertically on the slider)
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        // Ensure buttonX stays within the slider's boundaries
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;

        // Update hitbox dynamically to match button position
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}

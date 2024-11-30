package ui;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX , minX, maxX;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;

        bounds = new Rectangle(x, y, VOLUME_WIDTH, height); // Initialize bounds
        bounds.x = x + (width / 2) - (VOLUME_WIDTH / 2); // Align bounds with button sprite

        loadImgs();

        // Initialize button position with a default volume level (e.g., 50%)
        updateButtonPosition(0.5f);
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
        // Calculate the actual position of the slider
        int sliderX = (int) (x + (53 * Game.SCALE)); // Slider's starting X position
        int sliderY = (int) (y + (12 * Game.SCALE)); // Slider's Y position

        // Draw the slider
        g.drawImage(slider, sliderX, sliderY, SLIDER_WIDTH, height, null);

        // Adjust buttonX to position it along the slider based on current volume
        int buttonCenterX = buttonX - (VOLUME_WIDTH / 2);
        g.drawImage(imgs[index], buttonCenterX, sliderY, VOLUME_WIDTH, height, null);

        // Debug: Draw the bounds rectangle
        g.setColor(Color.RED);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

    }

    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;

        // Update the bounds rectangle
        bounds.x = buttonX - (VOLUME_WIDTH / 2);
        bounds.y = y;
    }

    public void updateButtonPosition(float currentVolumeLevel) {
        // Calculate button X position based on volume level
        buttonX = (int) (minX + currentVolumeLevel * (maxX - minX));

        // Update the bounds rectangle to match the new button position
        bounds.x = buttonX - (VOLUME_WIDTH / 2);
        bounds.y = y;
        bounds.width = VOLUME_WIDTH;
        bounds.height = height;
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

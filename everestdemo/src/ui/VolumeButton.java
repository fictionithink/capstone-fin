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
    private float floatValue = 0;

    public VolumeButton(int x, int y, int width, int height) {
        super(780 + width / 2, 570, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;

        this.x = 724;
        this.y = 580;
        this.width = width;

        buttonX = this.x + this.width / 2;
        minX = this.x + VOLUME_WIDTH / 2;
        maxX = this.x + this.width - VOLUME_WIDTH / 2;

        bounds.x = buttonX - VOLUME_WIDTH / 2;
        bounds.y = this.y;
        bounds.width = VOLUME_WIDTH; 
        bounds.height = height;

        loadImgs();
    }

    public VolumeButton(int x, int y, int width, int height, int hi) {
        super(x, y, VOLUME_WIDTH, height, hi);
        bounds.x -= VOLUME_WIDTH / 2;

        this.x = x;
        this.y = y;
        this.width = width;

        buttonX = this.x + this.width / 2;
        minX = this.x + VOLUME_WIDTH / 2;
        maxX = this.x + this.width - VOLUME_WIDTH / 2;

        bounds.x = buttonX - VOLUME_WIDTH / 2;
        bounds.y = this.y;
        bounds.width = VOLUME_WIDTH;
        bounds.height = height;

        loadImgsMenu();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    private void loadImgsMenu() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);

        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;
        updateFloatValue();
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

    public float getFloatValue() {
        return floatValue;
    }
}

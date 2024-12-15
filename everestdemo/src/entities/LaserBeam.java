package entities;

import main.Game;
import static utils.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Line2D;

public class LaserBeam {
    private float startX, startY;
    private float endX, endY;
    private int alpha = 128;
    private float width = 6.0f;
    private long creationTime;
    private boolean faded = false;
    private Line2D.Float hitbox;

    private int[][] levelData;

    public LaserBeam(float startX, float startY, float angle, int[][] levelData) {
        this.startX = startX;
        this.startY = startY;
        this.levelData = levelData;
        calculateEndPosition(angle);
        this.creationTime = System.currentTimeMillis();
        hitbox = new Line2D.Float(startX, startY, endX, endY);
    }

    public void setEndPosition(float x, float y) {
        this.endX = x;
        this.endY = y;
        hitbox.setLine(startX, startY, endX, endY);
    }


    private void calculateEndPosition(float angle) {
        float maxDistance = (float) Math.hypot(Game.GAME_WIDTH, Game.GAME_HEIGHT);

        for (float t = 0; t <= maxDistance; t += 1.0f) {
            float currentX = startX + (float) Math.cos(angle) * t;
            float currentY = startY + (float) Math.sin(angle) * t;

            if (!CanMoveHere(currentX, currentY, width, width, levelData)) {
                endX = currentX;
                endY = currentY;
                return;
            }

            if (currentX < 0 || currentX > Game.GAME_WIDTH || currentY < 0 || currentY > Game.GAME_HEIGHT) {
                endX = currentX;
                endY = currentY;
                return;
            }
        }
    }

    public void update() {
        long elapsedTime = System.currentTimeMillis() - creationTime;
        if (elapsedTime >= 300) {
            alpha = Math.max(0, alpha - 5);
            if (alpha == 0) faded = true;
        }

        hitbox.setLine(startX, startY, endX, endY);
    }

    public boolean isFaded() {
        return faded;
    }

    public void setFaded(boolean faded) {
        this.faded = faded;
    }

    public Line2D.Float getHitbox() {
        return hitbox;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
on
        var originalTransform = g2d.getTransform();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));

        g2d.setColor(Color.BLUE);

        float dx = endX - startX;
        float dy = endY - startY;
        double angle = Math.atan2(dy, dx);

        g2d.translate(startX, startY);
        g2d.rotate(angle);

        g2d.fillRect(0, -(int) (width / 2), (int) Math.hypot(dx, dy), (int) width);

        g2d.setTransform(originalTransform);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}

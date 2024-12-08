package entities;

import main.Game;
import static utils.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Line2D;

public class LaserBeam {
    private float startX, startY; // Starting point of the laser
    private float endX, endY;     // End point of the laser
    private int alpha = 128;      // Opacity (50% transparent, 0-255)
    private float width = 6.0f;   // Width of the laser rectangle
    private long creationTime;    // Time the laser was created
    private boolean faded = false; // Indicates if the laser has faded
    private Line2D.Float hitbox;  // Line for collision detection

    private int[][] levelData;    // Level data for collision checking

    public LaserBeam(float startX, float startY, float angle, int[][] levelData) {
        this.startX = startX;
        this.startY = startY;
        this.levelData = levelData; // Store level data for collision
        calculateEndPosition(angle);
        this.creationTime = System.currentTimeMillis();
        hitbox = new Line2D.Float(startX, startY, endX, endY);
    }

    public void setEndPosition(float x, float y) {
        this.endX = x;
        this.endY = y;
        hitbox.setLine(startX, startY, endX, endY); // Update the hitbox
    }


    private void calculateEndPosition(float angle) {
        float maxDistance = (float) Math.hypot(Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // Incrementally calculate the laser's end point along its path
        for (float t = 0; t <= maxDistance; t += 1.0f) {
            float currentX = startX + (float) Math.cos(angle) * t;
            float currentY = startY + (float) Math.sin(angle) * t;

            // Check for collision with the level
            if (!CanMoveHere(currentX, currentY, width, width, levelData)) {
                endX = currentX;
                endY = currentY;
                return;
            }

            // Stop if the laser reaches the screen boundaries
            if (currentX < 0 || currentX > Game.GAME_WIDTH || currentY < 0 || currentY > Game.GAME_HEIGHT) {
                endX = currentX;
                endY = currentY;
                return;
            }
        }
    }

    public void update() {
        // Gradually reduce the alpha value over time
        long elapsedTime = System.currentTimeMillis() - creationTime;
        if (elapsedTime >= 300) { // Start fading after 300ms
            alpha = Math.max(0, alpha - 5); // Gradually decrease opacity
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

        // Save the current transformation
        var originalTransform = g2d.getTransform();

        // Set transparency for the laser
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f)); // Apply opacity

        // Set the color to blue
        g2d.setColor(Color.BLUE);

        // Translate and rotate to align the rectangle with the laser's path
        float dx = endX - startX;
        float dy = endY - startY;
        double angle = Math.atan2(dy, dx);

        g2d.translate(startX, startY);
        g2d.rotate(angle);

        // Draw the laser as a rectangle
        g2d.fillRect(0, -(int) (width / 2), (int) Math.hypot(dx, dy), (int) width);

        // Restore the original transformation
        g2d.setTransform(originalTransform);

        // Reset transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}

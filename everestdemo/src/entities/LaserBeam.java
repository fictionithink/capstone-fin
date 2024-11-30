package entities;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LaserBeam {
    // Fields for the laser's starting position, direction, length, sprite, and fading
    private float startX, startY; // Starting position of the laser
    private float endX, endY;     // End position of the laser
    private float length;         // Dynamic length of the laser
    private BufferedImage laserSprite;
    private int alpha;            // Alpha value for fading (0-255)
    private long creationTime;    // Time when the laser was created

    public LaserBeam(float startX, float startY, float angle) {
        this.startX = startX;                     // Set the laser's starting x-coordinate
        this.startY = startY;                     // Set the laser's starting y-coordinate
        this.alpha = 255;                        // Start fully opaque

        // Calculate the end position based on the panel dimensions
        calculateDynamicLength(angle);

        // Load the laser sprite
        loadLaserSprite();

        // Record the creation time
        this.creationTime = System.currentTimeMillis();
    }

    private void loadLaserSprite() {
        laserSprite = LoadSave.getSpriteAtlas(LoadSave.LASER_BEAM_SPRITE); // Retrieve the laser sprite
    }

    private void calculateDynamicLength(float angle) {
        float maxDistance = (float) Math.hypot(Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // Incrementally calculate the laser's end point along its path
        for (float t = 0; t <= maxDistance; t += 1.0f) {
            float currentX = startX + (float) Math.cos(angle) * t;
            float currentY = startY + (float) Math.sin(angle) * t;

            // If the laser exceeds the panel's boundaries, stop
            if (currentX < 0 || currentX > Game.GAME_WIDTH || currentY < 0 || currentY > Game.GAME_HEIGHT) {
                this.endX = currentX;
                this.endY = currentY;
                this.length = (float) Math.hypot(currentX - startX, currentY - startY);
                break;
            }
        }
    }

    public void update() {
        // Gradually reduce the alpha value over time
        long elapsedTime = System.currentTimeMillis() - creationTime;
        if (elapsedTime >= 500) { // After 0.5 seconds, start fading
            alpha = Math.max(0, alpha - 5); // Decrease alpha, but ensure it doesn't go below 0
        }
    }

    public boolean isFaded() {
        return alpha <= 0; // Check if the laser is fully invisible
    }

    public void draw(Graphics g) {
        // Calculate the angle of rotation in degrees
        double rotationAngle = Math.toDegrees(Math.atan2(endY - startY, endX - startX));

        // Cast to Graphics2D for transformations
        Graphics2D g2d = (Graphics2D) g;

        // Save the current transformation
        var originalTransform = g2d.getTransform();

        // Translate to the laser's starting point and rotate to match its angle
        g2d.translate(startX, startY);
        g2d.rotate(Math.toRadians(rotationAngle));

        // Set the transparency for fading
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));

        // Stretch the sprite dynamically based on the laser's length
        g2d.drawImage(
                laserSprite, 0,                              // Starting point (translated)
                -(int) (laserSprite.getHeight() / 2 * Game.SCALE), // Center vertically
                (int) length,                   // Scaled width based on length
                (int) (laserSprite.getHeight() * Game.SCALE), // Keep height proportional
                null
        );

        // Restore the original transformation and alpha
        g2d.setTransform(originalTransform);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}

package ui;

import gamestates.Playing;
import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoundCompletedOverlay {

    private Playing playing;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    private long startTime;
    private int countdownSeconds = 10;

    public RoundCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        resetCountdown();
    }

    private void initImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.ROUND_COMPLETE);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void resetCountdown() {
        startTime = System.currentTimeMillis();
        System.out.println("Countdown reset. Start time: " + startTime);
    }

    public void update() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        int remainingTime = countdownSeconds - (int) (elapsedTime / 1000);

        if (remainingTime <= 0) {
            proceedToNextLevel();
        }
    }

    private void proceedToNextLevel() {
        if (playing != null) {
            playing.loadNextLevel();
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img, bgX, bgY, bgW, bgH, null);

        long elapsedTime = System.currentTimeMillis() - startTime;
        int remainingTime = countdownSeconds - (int) (elapsedTime / 1000);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String countdownText = "Next Round in: " + Math.max(remainingTime, 0) + "s";
        int textWidth = g.getFontMetrics().stringWidth(countdownText);
        g.drawString(countdownText, Game.GAME_WIDTH / 2 - textWidth / 2, bgY + bgH + 50);
    }

    public boolean isCountdownActive() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        int remainingTime = countdownSeconds - (int) (elapsedTime / 1000);

        System.out.println("Elapsed time: " + elapsedTime);
        System.out.println("Remaining time: " + remainingTime);

        if (remainingTime <= 0) {
            System.out.println("Countdown completed.");
            return false;
        }

        return true;
    }


}

package entities;

import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] workerArr;
    private ArrayList<Worker> workers = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }



    public void checkLaserHit(LaserBeam laser) {
        for (Worker w : workers) {
            if (w.isActive() && laser != null) {
                if (w.getHitbox().intersectsLine(laser.getHitbox())) {
                    w.hurt(10); // Deal damage to the worker

                    // Stop laser at the enemy's hitbox position
                    laser.setEndPosition(w.getHitbox().x + w.getHitbox().width / 2,
                            w.getHitbox().y + w.getHitbox().height / 2);
                    return; // Exit after the first collision
                }
            }
        }
    }



    public void update(int[][] lvlData,Player player) {
        for (Worker w : workers) {
            if(w.isActive())
                w.update(lvlData,player);

        }
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawWorker(g, xLevelOffset);
    }

    private void drawWorker(Graphics g, int xLevelOffset) {
        for (Worker w : workers) {

            if(w.isActive()) {
                // Add a vertical offset to align the worker sprite properly
                int verticalOffset = (int) (17 * Game.SCALE); // Adjust the value to lift the sprite higher

                g.drawImage(workerArr[w.getEnemyState()][w.getAniIndex()],
                        (int) w.getHitbox().x - xLevelOffset + w.flipX(),
                        (int) w.getHitbox().y - verticalOffset, // Apply the vertical offset
                        WORKER_WIDTH * w.flipW(),
                        WORKER_HEIGHT,
                        null);

                // Draw the worker's hitbox for debugging
                w.drawHitBox(g, xLevelOffset);
                w.drawAttackBox(g, xLevelOffset);
            }
        }
    }


    private void loadEnemyImgs() {
        workerArr = new BufferedImage[5][5];


        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.ENEMY_WORKER_RIGHT);

        for (int j = 0; j < workerArr.length; j++) {
            for (int k = 0; k < workerArr[j].length; k++) {
                workerArr[j][k] = temp.getSubimage(
                        k * WORKER_WIDTH_DEFAULT,
                        j * WORKER_HEIGHT_DEFAULT,
                        WORKER_WIDTH_DEFAULT,
                        WORKER_HEIGHT_DEFAULT
                );
            }
        }
    }

    private void addEnemies() {
        workers = LoadSave.GetWorkers();
        System.out.println("size of enemies: " + workers.size());
    }
}
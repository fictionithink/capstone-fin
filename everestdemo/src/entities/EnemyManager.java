package entities;

import gamestates.Playing;
import levels.Level;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] workerArr;
    private ArrayList<Worker> workers = new ArrayList<>();
    private ArrayList<Muscle> muscles = new ArrayList<>();

    private BufferedImage[][] muscleArr;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        workers = level.getWorkers();
        muscles = level.getMuscles();
    }

    public void checkLaserHit(LaserBeam laser) {
        for (Worker w : workers) {
            if (w.isActive() && laser != null) {
                if (w.getHitbox().intersectsLine(laser.getHitbox())) {
                    w.hurt(10);

                    laser.setEndPosition(w.getHitbox().x + w.getHitbox().width / 2,
                            w.getHitbox().y + w.getHitbox().height / 2);
                    return;
                }
            }
        }

        for (Muscle m : muscles) {
            if (m.isActive() && laser != null) {
                if (m.getHitbox().intersectsLine(laser.getHitbox())) {
                    m.hurt(10);
                    laser.setEndPosition(m.getHitbox().x + m.getHitbox().width / 2,
                            m.getHitbox().y + m.getHitbox().height / 2);
                    return;
                }
            }
        }
    }



    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;

        for (Worker w : workers) {
            if (w.isActive()) {
                w.update(lvlData, player);
                isAnyActive = true;
            }
        }

        for (Muscle m : muscles) {
            if (m.isActive()) {
                m.update(lvlData, player);
                isAnyActive = true;
            }
        }

        if (!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }


    public void draw(Graphics g, int xLevelOffset) {
        drawWorker(g, xLevelOffset);
        drawMuscle(g, xLevelOffset);
    }

    private void drawWorker(Graphics g, int xLevelOffset) {
        for (Worker w : workers) {

            if(w.isActive()) {
                int verticalOffset = (int) (17 * Game.SCALE);

                g.drawImage(workerArr[w.getEnemyState()][w.getAniIndex()],
                        (int) w.getHitbox().x - xLevelOffset + w.flipX(),
                        (int) w.getHitbox().y - verticalOffset,
                        WORKER_WIDTH * w.flipW(),
                        WORKER_HEIGHT,
                        null);

                // worker's hitbox for debugging
                w.drawHitBox(g, xLevelOffset);
                w.drawAttackBox(g, xLevelOffset);
            }
        }
    }

    private void drawMuscle(Graphics g, int xLevelOffset) {
        for (Muscle m : muscles) {

            if(m.isActive()) {
                int verticalOffset = (int) (17 * Game.SCALE);

                g.drawImage(muscleArr[m.getEnemyState()][m.getAniIndex()],
                        (int) m.getHitbox().x - xLevelOffset + m.flipX(),
                        (int) m.getHitbox().y - verticalOffset,
                        MUSCLE_WIDTH * m.flipW(),
                        MUSCLE_HEIGHT,
                        null);

                // worker's hitbox for debugging
                m.drawHitBox(g, xLevelOffset);
                m.drawAttackBox(g, xLevelOffset);
            }
        }
    }

    private void loadEnemyImgs() {
        workerArr = new BufferedImage[5][5];
        muscleArr = new BufferedImage[5][5];

        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.ENEMY_WORKER_RIGHT);
        BufferedImage temp1 = LoadSave.getSpriteAtlas(LoadSave.ENEMY_MUSCLE);

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

        for (int j = 0; j < muscleArr.length; j++) {
            for (int k = 0; k < muscleArr[j].length; k++) {
                muscleArr[j][k] = temp1.getSubimage(
                        k * MUSCLE_WIDTH_DEFAULT,
                        j * MUSCLE_HEIGHT_DEFAULT,
                        MUSCLE_WIDTH_DEFAULT,
                        MUSCLE_HEIGHT_DEFAULT
                );
            }
        }
    }


}

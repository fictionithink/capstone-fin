package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import static utils.Constants.EnemyConstants.*;

import static utils.Constants.EnemyConstants.*;

public class Muscle extends Enemy{
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Muscle(float x, float y) {
        super(x, y, MUSCLE_WIDTH, MUSCLE_HEIGHT, MUSCLE,.3f * Game.SCALE);

        initHitbox(x+30, y + (int)(14 * Game.SCALE), (int)(30 * Game.SCALE), (int)(28.5 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(48*Game.SCALE),(int)(28.5 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 5);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData,player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x=hitbox.x-attackBoxOffsetX;
        attackBox.y= hitbox.y;
    }
    public void drawAttackBox(Graphics g, int xLevelOffset){
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - xLevelOffset) ,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }
    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;

                case RUNNING:
                    if(canSeePlayer(lvlData,player))
                        turnTowardsPlayer(player);
                    if(isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) {
                        attackedChecked = false;
                    }
                    if (aniIndex == 4 && !attackedChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                    break;

                case HURT:
                    break;
            }
        }
    }
}

package entities;

import main.Game;
import main.GamePanel;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

public class Player extends Entity{

    //laser
    private LaserBeam currentLaser;
    private long laserStartTime;
    private boolean canShoot= true;

    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;

    private int playerAction = IDLE;
//    private int playerDir = -1;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1.1f * SCALE;

    private int[][] lvlData;

    private Rectangle2D.Float arm;
    private float gunAngle = 0;
//    private float mouseX, mouseY;

    //hitbox
    private float xDrawOffset = 4 * SCALE;
    private float yDrawOffset = 14 * SCALE;
//    private float currentArmOffsetX = 0;
//    private float currentArmOffsetY = 0;

    // jumping/gravity mechanics
    private float airSpeed = 0f;
    private float gravity = 0.03f * SCALE;
    private float jumpSpeed = -2.25f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;
    private boolean inAir = false;

    private BufferedImage armSprite;

    private GamePanel gamePanel;

    public Player(float x, float y, int width, int height, Game game) {
        super(x, y, width, height);
        this.gamePanel = game.getGamePanel(); // Retrieve GamePanel from Game
        loadAnimations();
        System.out.println("GamePanel reference in Player: " + this.gamePanel);
        initHitbox(x, y, (int)(21 * SCALE), (int)(28.5*SCALE));
        initArm(); // Initialize the arm
        loadArmSprite();

    }

    public void shootLaser() {
        // Starting position of the laser (center of the player's arm)
        if(!canShoot) return;

        float startX = arm.x + arm.width / 2;
        float startY = arm.y + arm.height / 2;

        // Create the laser
        currentLaser = new LaserBeam(startX, startY, gunAngle);
        laserStartTime = System.currentTimeMillis();
        canShoot=false;
    }


    private void initArm() {

        arm = new Rectangle2D.Float(0, 0, 32 * SCALE, 32 * SCALE); // Adjust dimensions as per the sprite
    }

    private void updateArmRotation() {
        if (gamePanel == null) {
            System.err.println("GamePanel is null! Unable to update arm rotation.");
            return;
        }

        // Get mouse coordinates
        float mouseX = gamePanel.getMouseX();
        float mouseY = gamePanel.getMouseY();

        // Calculate distance from arm pivot to mouse
        float xDistance = mouseX - (arm.x + arm.width / 2);
        float yDistance = mouseY - (arm.y + arm.height / 2);

        // Calculate angle in radians
        gunAngle = (float) Math.atan2(yDistance, xDistance);

    }

    private void updateArmPosition() {
        float xOffset = hitbox.width / 2 - arm.width / 2;
        float yOffset = -7 * SCALE;

        switch (playerAction) {
            case IDLE:
                xOffset = -2 * SCALE;
                yOffset = -7 * SCALE;
                break;

            case RUNNING:
                if (aniIndex >= 0) {
                    xOffset += 6 * SCALE;
                    yOffset += 3 * SCALE;
                } else {
                    xOffset += 1 * SCALE;
                }
                break;

            case JUMPING:
                xOffset += 6 * SCALE;
                yOffset += 1 * SCALE;
                break;

            default:
                break;
        }

        arm.x = hitbox.x + xOffset;
        arm.y = hitbox.y + yOffset;

    }


    private void drawArm(Graphics2D g) {
        // Save the original transformation
        AffineTransform originalTransform = g.getTransform();


        // Translate to the arm's pivot point (center of the arm)
        float pivotX = arm.x + arm.width / 2; // Center of the arm
        float pivotY = arm.y + arm.height / 2;
        g.translate(pivotX, pivotY);


        g.rotate(gunAngle);

        g.drawImage(
                armSprite,
                -(int) arm.width / 2,
                -(int) arm.height / 2,
                (int) arm.width,
                (int) arm.height,
                null
        );

        // Restore the original transformation
        g.setTransform(originalTransform);

        // Draw a debug line from the arm's center to show the direction of the mouse
        g.setColor(Color.RED);


        int lineLength = 200; // Length of the line in pixels
        g.drawLine(
                (int) pivotX,
                (int) pivotY,
                (int) (pivotX + Math.cos(gunAngle) * lineLength),
                (int) (pivotY + Math.sin(gunAngle) * lineLength)
        );

        // Optional: Debug line directly to the mouse position
        g.setColor(Color.BLUE);
        g.drawLine(
                (int) pivotX,
                (int) pivotY,
                (int) gamePanel.getMouseX(),
                (int) gamePanel.getMouseY()
        );
    }



    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
        updateArmPosition(); // Update the arm's visual position
        updateArmRotation(); // Update the arm rotation

        if (currentLaser != null) {
            currentLaser.update();
            if (currentLaser.isFaded()) { // 500 milliseconds = 0.5 seconds
                currentLaser = null;
            }
        }

        if(!canShoot){
            long currentTime = System.currentTimeMillis();
            if(currentTime - laserStartTime >=300){
                canShoot=true;
            }
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawArm(g2d);
        g.drawImage(
                animations[playerAction][aniIndex],
                (int)(hitbox.x - xDrawOffset),
                (int)(hitbox.y - yDrawOffset),
                (int)(45 * SCALE),
                (int)(45 * SCALE),
                null
        );

        if (currentLaser != null) {
            currentLaser.draw(g);
        }

    }


    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {

        int startAni = playerAction;

        if(moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if(inAir) {
            playerAction = JUMPING;
        }

        if(attacking)
            playerAction = PUNCHING;

        if(startAni != playerAction)
            resetAniTick();

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if(jump)
            jump();
        if (!left && !right && !inAir)
            return; // No input, no movement

        float xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;
        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);

            }

        } else
            updateXPos(xSpeed);

        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadArmSprite() {
        armSprite = LoadSave.getArmSprite();
    }


    private void loadAnimations() {

            BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

            animations = new BufferedImage[9][8];
            for(int j = 0; j < animations.length; j++){
                for(int i = 0; i < animations[j].length; i++){
                    animations[j][i] = img.getSubimage(i*48, j * 48, 48, 48);
                }
            }
    }

    public void LoadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;

    }

    public void setLeft(boolean left) {
        this.left = left;
    }

//    public boolean isLeft() {
//        return left;
//    }

//    public boolean isUp() {
//        return up;
//    }

//    public boolean isRight() {
//        return right;
//    }

//    public boolean isDown() {
//        return down;
//    }

//public void setDown(boolean down) {
//        this.down = down;
//    }

    public void setUp(boolean up) {
        this.up = up;
    }



    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }



}

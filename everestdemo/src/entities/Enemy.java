    package entities;

    import main.Game;

    import java.awt.geom.Rectangle2D;

    import static utils.Constants.EnemyConstants.*;
    import static utils.HelpMethods.*;
    import static utils.Constants.Directions.*;

    public abstract class Enemy extends Entity {

        protected int walkDir = LEFT;
        protected float walkSpeed = .6f * Game.SCALE;
        protected float gravity = 0.04f * Game.SCALE;
        protected boolean firstUpdate = true;
        protected boolean inAir;
        protected float fallSpeed;
        protected int aniIndex, enemyState = IDLE, enemyType;
        protected int aniTick, aniSpeed = 25;
        protected int tileY;
        protected float attackDistance=Game.TILES_SIZE;
        protected int maxHealth;
        protected int currentHealth;
        protected boolean active = true;
        protected boolean attackedChecked;

        public Enemy(float x, float y, int width, int height, int enemyType) {
            super(x, y, width, height);
            this.enemyType = enemyType;
            initHitbox(x, y, width, height);
            maxHealth = getMaxHealth(enemyType);
            currentHealth=maxHealth;
        }

        public void hurt(int amount){
            if(enemyState == DEAD){
                return;
            }

            currentHealth-=amount;
            if(currentHealth<=0)
                newState(DEAD);
            else
                newState(HURT);
        }

        protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player){
            if(attackBox.intersects(player.hitbox)){
                player.changeHealth(-GetEnemyDMG(enemyType));
                attackedChecked=true;
            }
        }
        protected void firstUpdateCheck(int[][] lvlData) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }

        protected void updateInAir(int[][] lvlData){
            // Handle falling
            if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
                tileY = (int) (hitbox.y / Game.TILES_SIZE);
            }
        }

        protected void move(int[][] lvlData) {
            float xSpeed = (walkDir == LEFT) ? -walkSpeed : walkSpeed;

            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                if (isFloor(hitbox, xSpeed, lvlData)) {
                    hitbox.x += xSpeed;
                } else {
                    changeWalkDir();
                }
            } else {
                changeWalkDir();
            }
        }

        protected void turnTowardsPlayer(Player player){
            if(player.hitbox.x> hitbox.x)
                walkDir = RIGHT;
            else
                walkDir = LEFT;

        }

        protected boolean canSeePlayer(int[][] lvlData, Player player) {
            int playerTileY =(int)player.getHitbox().y/Game.TILES_SIZE;

            if(playerTileY == tileY)
                if(isPlayerInRange(player)){
                    if(IsSightClear(lvlData,hitbox,player.hitbox,tileY))
                        return true;
                }

            return false;
        }

        protected boolean isPlayerInRange(Player player) {
            int absValue=(int)Math.abs(player.hitbox.x - hitbox.x);
            return absValue <= attackDistance * 5;
        }

        protected boolean isPlayerCloseForAttack(Player player){
            int absValue=(int)Math.abs(player.hitbox.x - hitbox.x);
            return absValue <= attackDistance;
        }

        protected void newState(int enemyState){
            this.enemyState=enemyState;
            aniTick=0;
            aniIndex=0;


        }

        protected void updateAnimationTick() {
            aniTick++;
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                    aniIndex = 0;

                    switch (enemyState) {
                        case ATTACK:
                        case HURT:
                            enemyState = IDLE;
                            break;
                        case DEAD:
                            active = false;
                            break;
                    }

                }
            }
        }


        protected void changeWalkDir() {
            walkDir = (walkDir == LEFT) ? RIGHT : LEFT;
        }

        public int getAniIndex() {
            return aniIndex;
        }

        public int getEnemyState() {
            return enemyState;
        }

        public int flipX() {
            if (walkDir == LEFT)
                return (int)hitbox.width;

            return 0;
        }

        public int flipW(){
            if(walkDir == LEFT)
                return -1;
            else
                return 1;

        }
        public boolean isActive(){
            return active;
        }
    }
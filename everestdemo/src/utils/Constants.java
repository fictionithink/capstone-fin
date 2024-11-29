package utils;

public class Constants {

    public static class Directions {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;

    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMPING = 2;
        public static final int HURT = 3;
        public static final int DEATH = 4;
        public static final int PUNCHING = 5;
        public static final int KICKING = 6;
        public static final int RUNNING_ATTACK = 7;
        public static final int HEAVY_ATTACK_1 = 8;
        public static final int HEAVY_ATTACK_2 = 9;

        public static int GetSpriteAmount(int player_action){
            switch (player_action) {
                case HURT:
                    return 2;
                case IDLE:
                case JUMPING:
                    return 4;
                case RUNNING:
                case DEATH:
                case PUNCHING:
                case KICKING:
                case RUNNING_ATTACK:
                    return 6;
                case HEAVY_ATTACK_1:
                case HEAVY_ATTACK_2:
                    return 8;
                default:
                    return 1;
            }
        }

    }

}

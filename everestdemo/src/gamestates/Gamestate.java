package gamestates;

public enum Gamestate {

    PLAYING, MENU, OPTIONS, ABOUT, EXIT; // Ensure these states exist and are properly assigned
    public static Gamestate state = MENU; // Default to MENU

}

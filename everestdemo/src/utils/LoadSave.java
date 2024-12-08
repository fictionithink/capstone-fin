package utils;

import entities.Muscle;
import entities.Worker;
import main.Game;

import static utils.Constants.EnemyConstants.MUSCLE;
import static utils.Constants.EnemyConstants.WORKER;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class LoadSave {

    public static final String PLAYER_ATLAS = "Cyborg_Spritesheet.png";
    public static final String LEVEL_ATLAS = "world1spritesheet.png";
    public static final String CLOUD_1 = "CloudBackground1.png";
    public static final String CLOUD_2 = "CloudBackground2.png";
    public static final String CLOUD_3 = "CloudBackground3.png";
    public static final String CLOUD_4 = "CloudBackground4.png";
    public static final String OPTIONS_MENU = "MenuBackGroundNewPathfinder.png";
    public static final String NEWMENU = "MenuBackGroundNewPathfinder.png";
    public static final String ABOUT_PAGE = "aboutPageOfficial.png";

    public static final String MENU_BACKGROUND_IMG = "PurpleSkyBackground.png";

    public static final String CYBER_ARM = "cyber_arm.png";
    public static final String CAT_PIXEL = "Cat Sprite Sheet.png";
    public static final String MENU_BUTTONS = "menu_atlas.png";

    public static final String ENEMY_WORKER_RIGHT = "man_right_spritesheet.png";
    public static final String ENEMY_MUSCLE = "muscle_enemy.png";

    public static final String MENU_TITLE = "pathfinder.png";

    public static final String PAUSE_BACKGROUND = "pause menu.png";
    public static final String SOUND_BUTTONS = "music atlas.png";
    public static final String URM_BUTTONS = "pause atlas.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String STATUS_BAR_FULL = "full_health_bar.png";
    public static final String MENU_OVERLAY = "menuoverlay.png";
    public static final String ROUND_COMPLETE = "yipee you survived.png";

    public static final String STATUS_BAR_EMPTY = "empty_health_bar.png";

    // world 1 background shenanigans
    public static final String W1_BACKGROUND = "w1bg_1st.png";
    public static final String W1_TREES = "w1bg_2nd.png";
    public static final String W1_CITY_NEAR = "w1bg_3rd.png";
    public static final String W1_CITY_FAR = "w1bg_4th.png";
    public static final String W1_CITY_OVERLAY = "w1bg_5th.png";



    public static BufferedImage getSpriteAtlas(String fileName) {
        BufferedImage img;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try{
            img = ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("image not found?! what the sigma???");
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                System.err.println("InputStream *is* failed to close!");
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/rounds");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for(int i = 0; i < filesSorted.length; i++)
            for(int j = 0; j < files.length; j++){
                if(files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
            }



        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i =0; i <  imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for(File f :files)
            System.out.println("file: " + f.getName());
        for(File f :filesSorted)
            System.out.println("file sorted: " + f.getName());

        return imgs;
    }

    public static BufferedImage getArmSprite() {
        return getSpriteAtlas(CYBER_ARM);
    }


}
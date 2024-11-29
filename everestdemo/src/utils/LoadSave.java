package utils;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

    public static final String PLAYER_ATLAS = "Cyborg_Spritesheet.png";
    public static final String LEVEL_ATLAS = "IndustrialTile_0.png";
    public static final String LEVEL_1_DATA = "level_1_data.png";
    public static final String CYBER_ARM = "cyber_arm.png";
    public static final String MENU_BUTTONS = "menu_atlas.png";
    public static final String LASER_BEAM_SPRITE= "laser_beam.png";
//    public static final String MENU_BACKGROUND = "???????????????????????.png";
//    public static final String PAUSE_BACKGROUND = "pause menu.png";
//    public static final String MENU_BUTTONS = "menu_atlas.png";

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
                System.err.println("InputStream is failed to close!");
            }
        }
        return  img;
    }

    public static int[][] GetLevelData(){
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = getSpriteAtlas(LEVEL_1_DATA);

        for(int j = 0; j < img.getHeight(); j++){
            for(int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if(value >= 12){
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }

    public static BufferedImage getArmSprite() {
        return getSpriteAtlas(CYBER_ARM);
    }

}

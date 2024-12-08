package levels;

import entities.Muscle;
import entities.Worker;
import main.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] levelData;
    private ArrayList<Worker> workers;
    private ArrayList<Muscle> muscles;

    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calcLevelOffsets();
    }

    private void calcLevelOffsets() {
        levelTilesWide = img.getWidth();
        maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = Game.TILES_SIZE;
    }

    private void createEnemies() {
        workers = GetWorkers(img);
        muscles = GetMuscles(img);
    }

    private void createLevelData() {
        levelData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public int getLevelOffset(){
        return maxLevelOffsetX;
    }

    public ArrayList<Worker> getWorkers(){
        return workers;
    }

    public ArrayList<Muscle> getMuscles(){
        return muscles;
    }
}
 

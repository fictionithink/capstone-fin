package audio;

import main.Game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {

    public static int MenuMusic = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;
    public static int RUNNING = 7;
    public static int ENEMY_ATTACK = 8;

    private Clip[] songs, effects;  // diff arrays for diff usages
    public Clip buttonClick;

    // Clip is also considered to be a music player
    // AudioInputStream
    private int currentSongId; // what songs is playing sa background
    private float volume = 1f; // for our volume obviously
    private boolean songMute, effectMute;
    private Random rand = new Random(); // to avoid repetitive audios

    public AudioPlayer(Game game){
        loadSongs();
        loadEffects();
        loadClick();
        playSong(MenuMusic);
    }

    public AudioPlayer(Game game, int extra){
        loadSongs();
        loadEffects();
        loadClick();
    }

    // now we have 3 methods

    private void loadSongs(){
        String[] names = {"MenuMusic","PlayingMusic","PlayingMusic"};
        songs = new Clip[names.length];

        for (int i = 0; i < songs.length; i++){
            songs[i] = getClip(names[i]);
        }
    }

    public void loadClick(){
        String name = "ButtonsClick";
        buttonClick = getClip(name);
    }

    public void playClick(){
        buttonClick.setMicrosecondPosition(0);
        buttonClick.start();
    }

    public void stopSong(int song){
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop();
        }
    }

    public void setLevelSong(int lvlIndex) {
        stopSong(); // Stop the currently playing song
        if (lvlIndex % 2 == 0) {
            playSong(LEVEL_1);
        } else {
            playSong(LEVEL_2);
        }
    }

    public void setVolume(float volume){
        this.volume = volume;
        updateEffectsVolume();
        updateSongVolume();
    }
    private void loadEffects(){

        String[] effectNames = {"PlayerDeath", "JumpGrunt", "PlayerDeath", "PlayerHurt", "PlayerShoot", "PlayerShoot", "Punch", "PlayerRun", "EnemyPunch"};

        effects = new Clip[effectNames.length];

        for (int i = 0; i < effects.length; i++){
            effects[i] = getClip(effectNames[i]);
        }

        updateEffectsVolume();
    }


    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        Clip c = null;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void stopSong(){
        if (songs[currentSongId].isActive()){
            songs[currentSongId].stop();
        }
    }

    public void playAttackSound(){
        int start = 4; // pagdepend sa effectNames array sa taas;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect){
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song){

        stopSong();
        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void lvlCompleted(){
        stopSong();
        playEffect(LVL_COMPLETED);

    }

    public void toggleSongMute(){
        this.songMute = !songMute;
        for (Clip c : songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute(){
        this.effectMute = !effectMute;
        for (Clip c : songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute){
            playEffect(JUMP);
        }
    }

    private void updateSongVolume(){
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range*volume)+gainControl.getMinimum();
        gainControl.setValue(gain);
        //gainControl.setValue(1f);
    }

    private void updateEffectsVolume(){
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    public void stopEffect(int effect) {
        if (effects[effect].isActive()) {
            effects[effect].stop(); // Stop the sound effect
            effects[effect].setMicrosecondPosition(0); // Reset to the beginning
        }
    }
}



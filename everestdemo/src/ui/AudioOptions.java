package ui;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH_MENU;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT_MENU;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;

public class AudioOptions {

    private VolumeButton volumeButton, volumeButtonMenu;
    private SoundButton musicButton, sfxButton;
    private Game game;
    private AudioPlayer audioPlayer = new AudioPlayer(game,1);


    public AudioOptions(Game game) {
        this.game = game;
        System.out.println("Initializing AudioOptions...");
        createSoundButtons();
        createVolumeButtonMenu();
        createSoundButtonsMenu();
        audioPlayer.loadClick();
        createVolumeButton();
        System.out.println("AudioOptions initialized successfully.");
    }

    public AudioOptions(Game game, int hey) {
        this.game = game;
        System.out.println("Initializing AudioOptions...");
        createSoundButtons();
        createVolumeButtonMenu();
        audioPlayer.loadClick();
        createSoundButtonsMenu();
        createVolumeButton();
        System.out.println("AudioOptions initialized successfully.");
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createVolumeButtonMenu() {
        int vX = ((int) (309 * Game.SCALE))-10;
        int vY = 508;
        volumeButtonMenu = new VolumeButton(vX, vY, SLIDER_WIDTH_MENU, VOLUME_HEIGHT_MENU,1);
    }



    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createSoundButtonsMenu() {
        int soundX1 = (int) (390 * Game.SCALE);
        int soundX2 = (int) (510 * Game.SCALE);
        int musicY = (int) (160 * Game.SCALE);
        int sfxY = (int) (160 * Game.SCALE);
        musicButton = new SoundButton(soundX1, musicY, SOUND_SIZE+20, SOUND_SIZE+20);
        sfxButton = new SoundButton(soundX2, sfxY, SOUND_SIZE+20, SOUND_SIZE+20);
    }

    public void update() {
       musicButton.update();
       sfxButton.update();
       volumeButton.update();
        volumeButtonMenu.update();
    }

    public void updateMenu() {
        musicButton.update();
        sfxButton.update();
        volumeButtonMenu.update();
    }



    public void draw(Graphics g) {
        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Volume Button
        volumeButton.draw(g);
        volumeButtonMenu.draw(g);
    }

    public void drawMenu(Graphics g) {
        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Volume Button
        volumeButtonMenu.draw(g);
    }



    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed() || volumeButton != null) {
            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeX(e.getX());
            float valueAfter = volumeButton.getFloatValue();
            if (valueBefore != valueAfter){
                game.getAudioPlayer().setVolume(valueAfter);
                return;
            }
            return;
        }
        if (volumeButtonMenu.isMousePressed()) {
            float valueBefore = volumeButtonMenu.getFloatValue();
            volumeButtonMenu.changeX(e.getX());
            float valueAfter = volumeButtonMenu.getFloatValue();
            if (valueBefore != valueAfter){
                game.getAudioPlayer().setVolume(valueAfter);
                return;
            }
            return;
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
            audioPlayer.playClick();
        }
        else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
            audioPlayer.playClick();
        }
        else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
            audioPlayer.playClick();
        }
        else if (isIn(e, volumeButtonMenu)) {
            volumeButtonMenu.setMousePressed(true);
            audioPlayer.playClick();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
            game.getAudioPlayer().toggleSongMute();

        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
            game.getAudioPlayer().toggleEffectMute();
        }

        musicButton.resetBools();
        sfxButton.resetBools();

        volumeButton.resetBools();
        volumeButtonMenu.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        volumeButton.setMouseOver(false);
        volumeButtonMenu.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
        else if (isIn(e, volumeButtonMenu))
            volumeButtonMenu.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public VolumeButton getvolumeButtonMenu() {
        return volumeButtonMenu;
    }
}
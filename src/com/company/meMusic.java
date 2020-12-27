package com.company;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class meMusic {

    String game_over_path;
    String in_game_path;
    String intro_path;
    File gg;
    File ig;
    File menuSound;
    AudioInputStream audioStream;
    Clip clipGG;
    Clip clipIG;
    Clip clipMenu;

    meMusic() {
        game_over_path = "assets/game_over.wav";
        in_game_path = "assets/in_game.wav";
        intro_path = "assets/intro.wav";
        gg = new File(game_over_path);
        ig = new File(in_game_path);
        menuSound = new File(intro_path);
    }

    public void gameOverSoundStop() {
        clipGG.stop();
        clipGG.close();
    }

    public void inGameSoundStop() {
        clipIG.stop();
        clipIG.close();
    }

    public void introSoundStop() {
        clipMenu.stop();
        clipMenu.close();
    }

    public void gameOverPlay() {
        clipGG.setFramePosition(0);
        clipGG.start();
    }

    public void inGamePlay() {
        clipIG.setFramePosition(0);
        clipIG.start();
    }

    public void menuPlay() {
        clipMenu.setFramePosition(0);
        clipMenu.start();
    }

    public void gameOverPlayLoop() {
        clipGG.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void inGamePlayLoop() {
        clipIG.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void menuPlayLoop() {
        clipMenu.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void gameOverSound() {

        try {
            audioStream = AudioSystem.getAudioInputStream(gg);
            clipGG = AudioSystem.getClip();
            clipGG.open(audioStream);

        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {}
    }

    public void inGameSound() {

        try {
            audioStream = AudioSystem.getAudioInputStream(ig);
            clipIG = AudioSystem.getClip();
            clipIG.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {}
    }

    public void introSound() {
        try {
            audioStream = AudioSystem.getAudioInputStream(menuSound);
            clipMenu = AudioSystem.getClip();
            clipMenu.open(audioStream);

        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {}

    }





}

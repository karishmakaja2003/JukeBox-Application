package dao;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;
    private long pausePosition;

    public void play(String filePath) {
        try {
            File audioFile = new File(filePath);
            System.out.println(audioFile.exists());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void forward(long milliseconds) {
        if (clip != null && clip.isRunning()) {
            long newPosition = clip.getMicrosecondPosition() + (milliseconds * 1000);
            if (newPosition < clip.getMicrosecondLength()) {
                clip.setMicrosecondPosition(newPosition);
            }else {
                clip.setMicrosecondPosition(clip.getMicrosecondLength());
            }
        }
    }

    public void rewind(long milliseconds) {
        if (clip != null && clip.isRunning()) {
            long newPosition = clip.getMicrosecondPosition() - (milliseconds * 1000);
            if (newPosition < 0) {
                clip.setMicrosecondPosition(newPosition);
            }
            else {
                clip.setMicrosecondPosition(0);
            }
        }
    }
}


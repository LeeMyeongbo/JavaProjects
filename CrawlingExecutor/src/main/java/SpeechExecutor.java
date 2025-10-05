import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SpeechExecutor {

    public void playNewsSpeech() {
        if (!synthesize()) {
            throw new RuntimeException("음성 합성이 정상적으로 이루어지지 못했습니다...");
        }
        try (FileInputStream stream = new FileInputStream("NewsTTS/news.mp3")) {
            new Player(stream).play();
        } catch (IOException | JavaLayerException e) {
            throw new RuntimeException("음성 재생 중 오류가 발생하였습니다...", e);
        }
    }

    private boolean synthesize() {
        return System.getProperty("os.name").toLowerCase().contains("win")
            ? synthesize("cmd.exe", "/c", ".venv\\Scripts\\activate && python task.py")
            : synthesize("/bin/bash", "-c", "source .venv/bin/activate && python task.py");
    }

    private boolean synthesize(String cmd1, String cmd2, String cmd3) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File("NewsTTS"));

            builder.command(cmd1, cmd2, cmd3);
            builder.inheritIO();

            return builder.start().waitFor() == 0;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("음성 합성 중 오류가 발생하였습니다...", e);
        }
    }
}

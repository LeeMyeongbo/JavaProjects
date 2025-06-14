import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static javax.sound.sampled.AudioSystem.*;

public class MainService {

    public static class SpeechTask implements Callable<ByteString> {

        private final String text;
        private final VoiceSelectionParams voice;
        private final AudioConfig audioConfig;

        public SpeechTask(String text) {
            this.text = text;
            this.voice = VoiceSelectionParams.newBuilder()
                .setLanguageCode("ko-KR")
                .setName("ko-KR-Chirp3-HD-Leda")
                .build();
            this.audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.LINEAR16)
                .build();
        }

        @Override
        public ByteString call() {
            try (TextToSpeechClient client = TextToSpeechClient.create()) {
                SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
                return client.synthesizeSpeech(input, voice, audioConfig).getAudioContent();
            } catch (IOException e) {
                throw new RuntimeException("음성 합성 클라이언트를 불러올 수 없습니다 : " + e);
            }
        }
    }

    public static void main(String[] args) {
        String article;
        try (
            Scanner sc = new Scanner(System.in);
            BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("Output.txt"))
        ) {
            System.out.print("키워드 입력 : ");
            article = new Crawling().crawl(sc.next());
            bs.write(article.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("검색 완료! 뉴스를 재생합니다..");
        speak(article);
    }

    private static void speak(String wholeText) {
        try (Clip clip = getClip()) {
            AudioInputStream stream = getWholeArticleSpeechStream(getAudioByteStringList(wholeText));
            clip.open(stream);
            clip.start();

            long elapsed = clip.getMicrosecondLength();
            System.out.println("length - " + elapsed / 60000000 + "m " + elapsed % 60000000 / 1000000 + "s");
            clip.drain();
            clip.stop();
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException("음성 재생 중 오류가 발생하였습니다 : " + e);
        }
    }

    private static ArrayList<ByteString> getAudioByteStringList(String wholeText) {
        ArrayList<ByteString> ret = new ArrayList<>();
        ArrayList<SpeechTask> tasks = allocateSpeechTasks(wholeText);
        try (ExecutorService service = Executors.newFixedThreadPool(tasks.size())) {
            List<Future<ByteString>> futureList = service.invokeAll(tasks);
            for (Future<ByteString> future : futureList) {
                ret.add(future.get());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("오디오 합성 중 오류가 발생하였습니다 : " + e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("검색된 뉴스 기사가 없어 음성을 합성할 수 없습니다 : " + e);
        }
        return ret;
    }

    private static ArrayList<SpeechTask> allocateSpeechTasks(String text) {
        int index = 0;
        ArrayList<SpeechTask> taskList = new ArrayList<>();

        while (index < text.length()) {
            taskList.add(new SpeechTask(text.substring(index, Math.min(index + 300, text.length()))));
            index += 300;
        }

        return taskList;
    }

    private static AudioInputStream getWholeArticleSpeechStream(List<ByteString> list) {
        try {
            AudioInputStream stream = getAudioInputStream(new ByteArrayInputStream(list.get(0).toByteArray()));
            for (int i = 1; i < list.size(); i++) {
                AudioInputStream curStream = getAudioInputStream(new ByteArrayInputStream(list.get(i).toByteArray()));
                stream = new AudioInputStream(new SequenceInputStream(stream, curStream), stream.getFormat(),
                    stream.getFrameLength() + curStream.getFrameLength());
            }
            return stream;
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException("오디오 스트림을 불러올 수 없습니다 : " + e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("뉴스 기사가 없습니다 : " + e);
        }
    }
}

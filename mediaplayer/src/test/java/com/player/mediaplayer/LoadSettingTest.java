package com.player.mediaplayer;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import static org.testfx.api.FxAssert.verifyThat;

public class LoadSettingTest extends ControllerTest {
    HashMap<String, Integer> answerData;

    public LoadSettingTest() {
        answerData = new HashMap<>();
        answerData.put("moveTime", 10);
        answerData.put("vanishTime", 3);
        answerData.put("startVolume", 20);
        answerData.put("opacity", 10);
        answerData.put("autoStart", 0);
    }

    @Test
    public void testWhenSettingFileNotExist() {
        File settingFile = new File("C:\\Mediaplayer\\setting.dat");
        settingFile.deleteOnExit();

        clickOn("#appArea");
        clickOn("#settingButton");

        verifySettingDataIsCorrect();
        verifySettingWindowAppearsCorrectly();
    }

    private void verifySettingDataIsCorrect() {
        verifyThat(getController().getData(), new BaseMatcher<>() {

            @Override
            public boolean matches(Object o) {
                HashMap<String, Integer> data = (HashMap<String, Integer>) o;
                if (data == null || answerData.size() != data.size()) {
                    return false;
                }

                int correct = 0;
                Iterator<String> datKeys = data.keySet().iterator();
                Iterator<String> ansKeys = answerData.keySet().iterator();
                while (datKeys.hasNext() && ansKeys.hasNext()) {
                    String datKey = datKeys.next();
                    String ansKey = ansKeys.next();
                    if (datKey.equals(ansKey) && Objects.equals(data.get(datKey), answerData.get(ansKey))) {
                        correct++;
                    }
                }
                return correct == 5;
            }

            @Override
            public void describeTo(Description description) {
            }
        });
    }

    private void verifySettingWindowAppearsCorrectly() {
        verifyMoveTimeBoxAppearsCorrectly();
    }

    private void verifyMoveTimeBoxAppearsCorrectly() {
        verifyThat("#moveTimeBox", new BaseMatcher<Node>() {
            @Override
            public boolean matches(Object o) {
                ChoiceBox<Integer> box = (ChoiceBox<Integer>) o;
                return box.getValue().equals(10);
            }

            @Override
            public void describeTo(Description description) {
            }
        });
    }
}

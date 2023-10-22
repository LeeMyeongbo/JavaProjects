package com.player.mediaplayer;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import static com.player.mediaplayer.SettingDataManager.getEntireData;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class LoadSettingTest {
    private static final HashMap<String, Integer> DATA = new HashMap<>();

    public static class TestWhenSettingFileNotExist extends MainControllerTest {

        public TestWhenSettingFileNotExist() {
            DATA.put("moveTime", 10);
            DATA.put("vanishTime", 3);
            DATA.put("startVolume", 20);
            DATA.put("opacity", 10);
            DATA.put("autoStart", 0);
        }

        @Override
        public void start(Stage stage) {
            File settingFile = new File("C:\\Mediaplayer\\setting.dat");
            settingFile.deleteOnExit();
            super.start(stage);
        }

        @Test
        public void test() {
            clickOn("#settingButton");

            testSettingDataIsCorrect();
            testSettingWindowAppearCorrectly();
        }

        private void testSettingDataIsCorrect() {
            verifyThat(getEntireData(), new BaseMatcher<>() {

                @Override
                public boolean matches(Object o) {
                    HashMap<String, Integer> data = (HashMap<String, Integer>) o;
                    if (data == null || DATA.size() != data.size()) {
                        return false;
                    }

                    int correct = 0;
                    Iterator<String> datKeys = data.keySet().iterator();
                    Iterator<String> ansKeys = DATA.keySet().iterator();
                    while (datKeys.hasNext() && ansKeys.hasNext()) {
                        String datKey = datKeys.next();
                        String ansKey = ansKeys.next();
                        if (datKey.equals(ansKey) && Objects.equals(data.get(datKey), DATA.get(ansKey))) {
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

        private void testSettingWindowAppearCorrectly() {
            testMoveTimeAppearCorrectly();
            testVanishTimeAppearCorrectly();
            testStartVolumeAppearCorrectly();
            testButtonOpacityAppearCorrectly();
            testAutoStartCheckboxAppearCorrectly();
        }

        private void testMoveTimeAppearCorrectly() {
            verifyThat("#moveTimeBox", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    ChoiceBox<Integer> box = (ChoiceBox<Integer>) o;
                    return box.getValue() == 10;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
        }

        private void testVanishTimeAppearCorrectly() {
            verifyThat("#vanishTimeBox", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    ChoiceBox<Integer> box = (ChoiceBox<Integer>) o;
                    return box.getValue() == 3;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
        }

        private void testStartVolumeAppearCorrectly() {
            verifyThat("#startVolumeSlider", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    Slider slider = (Slider) o;
                    return Double.compare(slider.getValue(), 20.0) == 0;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
            verifyThat("#startVolumeLabel", hasText("20"));
        }

        private void testButtonOpacityAppearCorrectly() {
            verifyThat("#opacitySlider", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    Slider slider = (Slider) o;
                    return Double.compare(slider.getValue(), 1.0) == 0;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
            verifyThat("#opacityLabel", hasText("1.0"));
        }

        private void testAutoStartCheckboxAppearCorrectly() {
            verifyThat("#autoStartCheck", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    CheckBox box = (CheckBox) o;
                    return !box.isSelected();
                }

                @Override
                public void describeTo(Description description) {
                }
            });
            verifyThat("#warningLabel", isInvisible());
        }
    }

    public static class TestWhenSettingFileExist extends ApplicationTest {

    }
}

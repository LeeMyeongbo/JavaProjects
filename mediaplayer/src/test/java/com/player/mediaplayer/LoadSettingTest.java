package com.player.mediaplayer;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

import static com.player.mediaplayer.SettingDataManager.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;
import static org.testfx.matcher.control.LabeledMatchers.*;

@RunWith(Enclosed.class)
public class LoadSettingTest {
    private static final HashMap<String, Integer> ANSWER_DATA = new HashMap<>();
    private static final String PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    private static final String NAME = "MediaPlayer";
    private static final Logger LOG = LogManager.getLogger();

    private static class VerificationExecutor {

        private void testSettingDataIsCorrect() {
            verifyThat(getEntireData(), new BaseMatcher<>() {

                @Override
                public boolean matches(Object o) {
                    HashMap<String, Integer> data = (HashMap<String, Integer>) o;
                    if (data == null || ANSWER_DATA.size() != data.size()) {
                        return false;
                    }

                    int correct = 0;
                    Iterator<String> datKeys = data.keySet().iterator();
                    Iterator<String> ansKeys = ANSWER_DATA.keySet().iterator();
                    while (datKeys.hasNext() && ansKeys.hasNext()) {
                        String datKey = datKeys.next();
                        String ansKey = ansKeys.next();
                        if (datKey.equals(ansKey) && Objects.equals(data.get(datKey), ANSWER_DATA.get(ansKey))) {
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

        private void testMoveTimeAppearCorrectly(int val) {
            verifyThat("#moveTimeBox", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    ChoiceBox<Integer> box = (ChoiceBox<Integer>) o;
                    return box.getValue() == val;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
        }

        private void testVanishTimeAppearCorrectly(int val) {
            verifyThat("#vanishTimeBox", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    ChoiceBox<Integer> box = (ChoiceBox<Integer>) o;
                    return box.getValue() == val;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
        }

        private void testStartVolumeAppearCorrectly(double val) {
            verifyThat("#startVolumeSlider", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    Slider slider = (Slider) o;
                    return Double.compare(slider.getValue(), val) == 0;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
            verifyThat("#startVolumeLabel", hasText(String.valueOf((int) val)));
        }

        private void testButtonOpacityAppearCorrectly(double val) {
            verifyThat("#opacitySlider", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    Slider slider = (Slider) o;
                    return Double.compare(slider.getValue(), val) == 0;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
            verifyThat("#opacityLabel", hasText(String.valueOf(val)));
        }

        private void testAutoStartCheckboxAppearCorrectly(boolean isChecked) {
            verifyThat("#autoStartCheck", new BaseMatcher<Node>() {
                @Override
                public boolean matches(Object o) {
                    CheckBox box = (CheckBox) o;
                    return box.isSelected() == isChecked;
                }

                @Override
                public void describeTo(Description description) {
                }
            });
            if (isChecked) {
                verifyThat("#warningLabel", isVisible());
            } else {
                verifyThat("#warningLabel", isInvisible());
            }
        }
    }

    public static class TestWhenSettingFileNotExist extends MainControllerTest {

        @Override
        public void init() throws Exception {
            File settingFile = new File("C:\\Mediaplayer\\setting.dat");
            if (settingFile.delete()) {
                LOG.info("setting.dat removed successfully!");
            }
            setAnswerData();
            super.init();
        }

        private void setAnswerData() {
            ANSWER_DATA.put("moveTime", 10);
            ANSWER_DATA.put("vanishTime", 3);
            ANSWER_DATA.put("startVolume", 20);
            ANSWER_DATA.put("opacity", 10);
            ANSWER_DATA.put("autoStart", 0);
        }

        @Test
        public void test() {
            clickOn("#settingButton");

            VerificationExecutor executor = new VerificationExecutor();
            executor.testSettingDataIsCorrect();
            executor.testMoveTimeAppearCorrectly(10);
            executor.testVanishTimeAppearCorrectly(3);
            executor.testStartVolumeAppearCorrectly(20.0);
            executor.testButtonOpacityAppearCorrectly(1.0);
            executor.testAutoStartCheckboxAppearCorrectly(false);

            assert !Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, PATH, NAME);
        }
    }

    public static class TestWhenSettingFileExist extends MainControllerTest {
        private int randomMoveTime, randomStartVolume, randomVanishTime, randomOpacity;

        @Override
        public void init() throws Exception {
            setRandomData();
            applyToSettingData();
            setAnswerData();
            super.init();
        }

        private void setRandomData() {
            randomMoveTime = (new Random().nextInt(5) + 1) * 5;
            if (randomMoveTime == 25) {
                randomMoveTime = 30;
            }
            randomStartVolume = new Random().nextInt(101);
            randomVanishTime = new Random().nextInt(5) * 5;
            if (randomVanishTime == 0) {
                randomVanishTime = 3;
            }
            randomOpacity = new Random().nextInt(11);
            LOG.info("moveTime : " + randomMoveTime + ", startVolume : " + randomStartVolume + ", vanishTime : " +
                randomVanishTime + ", buttonOpacity : " + randomOpacity + ", autoStart : " + true);
        }

        private void applyToSettingData() {
            putMoveTimeValue(randomMoveTime);
            putVanishTimeValue(randomVanishTime);
            putStartVolumeValue(randomStartVolume);
            putOpacityValue(randomOpacity);
            putAutoStartValue(1);
            saveSettingsOnFile();
        }

        private void setAnswerData() {
            ANSWER_DATA.put("moveTime", randomMoveTime);
            ANSWER_DATA.put("vanishTime", randomVanishTime);
            ANSWER_DATA.put("startVolume", randomStartVolume);
            ANSWER_DATA.put("opacity", randomOpacity);
            ANSWER_DATA.put("autoStart", 1);
        }

        @Test
        public void test() {
            clickOn("#settingButton");

            VerificationExecutor executor = new VerificationExecutor();
            executor.testSettingDataIsCorrect();
            executor.testMoveTimeAppearCorrectly(randomMoveTime);
            executor.testVanishTimeAppearCorrectly(randomVanishTime);
            executor.testStartVolumeAppearCorrectly(randomStartVolume);
            executor.testButtonOpacityAppearCorrectly((double) randomOpacity / 10);
            executor.testAutoStartCheckboxAppearCorrectly(true);
            assert Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, PATH, NAME);

            clickOn("#autoStartCheck").clickOn("#okButton");
            assert !Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, PATH, NAME);
        }
    }
}

package com.player.mediaplayer;

import com.google.common.annotations.VisibleForTesting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;

final public class SettingDataManager {
    private static HashMap<String, Integer> DATA = new HashMap<>();
    private static final Logger LOG = LogManager.getLogger();

    private SettingDataManager() {
    }

    public static void putMoveTimeValue(int val) {
        DATA.put("moveTime", val);
        LOG.info("moveTime set to : " + val);
    }

    public static void putVanishTimeValue(int val) {
        DATA.put("vanishTime", val);
        LOG.info("vanishTime set to : " + val);
    }

    public static void putStartVolumeValue(int val) {
        DATA.put("startVolume", val);
        LOG.info("startVolume set to : " + val);
    }

    public static void putOpacityValue(int val) {
        DATA.put("opacity", val);
        LOG.info("opacity set to : " + val);
    }

    public static void putAutoStartValue(int val) {
        DATA.put("autoStart", val);
        LOG.info("autoStart set to : " + (val == 1));
    }

    public static int getMoveTimeValue() {
        return DATA.get("moveTime");
    }

    public static int getVanishTimeValue() {
        return DATA.get("vanishTime");
    }

    public static int getStartVolumeValue() {
        return DATA.get("startVolume");
    }

    public static int getOpacityValue() {
        return DATA.get("opacity");
    }

    public static int getAutoStartValue() {
        return DATA.get("autoStart");
    }

    public static void loadSettingsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Mediaplayer\\setting.dat"))) {
            DATA = (HashMap<String, Integer>) ois.readObject();
            LOG.info("read setting.dat successfully from C:\\Mediaplayer");
        } catch (FileNotFoundException e) {
            setDefaultSettings();
            saveSettingsOnFile();
        } catch (IOException | ClassNotFoundException e) {
            LOG.fatal("cannot read setting.dat from C:\\Mediaplayer : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void setDefaultSettings() {
        LOG.info("There is no setting.dat in C:\\Mediaplayer, so set to default value");
        putMoveTimeValue(10);
        putVanishTimeValue(3);
        putStartVolumeValue(20);
        putOpacityValue(10);
        putAutoStartValue(0);
    }

    public static void saveSettingsOnFile() {
        File path = new File("C:\\Mediaplayer");
        if (path.mkdir()) {
            LOG.info("MediaPlayer dir created in C:\\");
        }
        String AbsolutePath = new File(path, "setting.dat").getAbsolutePath();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AbsolutePath))) {
            oos.writeObject(DATA);
            oos.flush();
            LOG.info("setting.dat saved successfully in C:\\Mediaplayer");
        } catch (IOException e) {
            LOG.fatal("cannot save setting.dat in C:\\Mediaplayer : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @VisibleForTesting
    static HashMap<String, Integer> getEntireData() {
        return DATA;
    }
}

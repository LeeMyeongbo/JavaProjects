package com.player.mediaplayer;

import com.google.common.annotations.VisibleForTesting;

import java.io.*;
import java.util.HashMap;

final public class SettingDataManager {
    private static HashMap<String, Integer> DATA = new HashMap<>();

    private SettingDataManager() {
    }

    public static void putMoveTimeValue(int val) {
        DATA.put("moveTime", val);
    }

    public static void putVanishTimeValue(int val) {
        DATA.put("vanishTime", val);
    }

    public static void putStartVolumeValue(int val) {
        DATA.put("startVolume", val);
    }

    public static void putOpacityValue(int val) {
        DATA.put("opacity", val);
    }

    public static void putAutoStartValue(int val) {
        DATA.put("autoStart", val);
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
        } catch (FileNotFoundException e) {
            setDefaultSettings();
            saveSettingsOnFile();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setDefaultSettings() {
        DATA.put("moveTime", 10);
        DATA.put("vanishTime", 3);
        DATA.put("startVolume", 20);
        DATA.put("opacity", 10);
        DATA.put("autoStart", 0);
    }

    public static void saveSettingsOnFile() {
        File path = new File("C:\\Mediaplayer");
        if (path.mkdir()) {
            System.out.println("setting file created");
        }
        String AbsolutePath = new File(path, "setting.dat").getAbsolutePath();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(AbsolutePath))) {
            oos.writeObject(DATA);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @VisibleForTesting
    static HashMap<String, Integer> getEntireData() {
        return DATA;
    }
}

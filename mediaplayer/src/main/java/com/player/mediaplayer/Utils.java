package com.player.mediaplayer;

import javafx.application.Application;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class Utils {

    private Utils() {
    }

    public static InputStream getStreamBySource(Class<?> c, String source) {
        return Objects.requireNonNull(c.getResourceAsStream(source));
    }

    public static URL getResource(Class<?> c, String source) {
        return Objects.requireNonNull(c.getResource(source));
    }
}

package com.player.mediaplayer;

import java.io.InputStream;
import java.util.Objects;

public class Utils {

    private Utils() {
    }

    public static InputStream getStreamBySource(Class<?> c, String source) {
        return Objects.requireNonNull(c.getResourceAsStream(source));
    }
}

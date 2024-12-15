module com.player.mediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.web;
    requires com.sun.jna.platform;
    requires com.google.common;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens com.player.mediaplayer to javafx.fxml;
    exports com.player.mediaplayer;
}

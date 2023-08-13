module com.player.mediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.swt;
    requires jna.platform.win32;

    opens com.player.mediaplayer to javafx.fxml;
    exports com.player.mediaplayer;
}

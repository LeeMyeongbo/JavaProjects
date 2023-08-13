import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MP3Button extends JButton {

    public MP3Button(ActionListener listener, ImageIcon icon, Dimension size, Point pos, String toolTipText) {
        super(icon);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setSize(size);
        this.setLocation(pos);
        this.setToolTipText(toolTipText);
        this.addActionListener(listener);
    }
}

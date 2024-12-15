import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class MP3Slider extends JSlider {

    public MP3Slider(int SliderShape, Point p, Dimension d, String toolTipText, MouseListener listener) {
        super(SliderShape);

        this.setOpaque(false);
        this.setLocation(p);
        this.setSize(d);
        this.setBackground(Color.white);
        this.setToolTipText(toolTipText);
        this.addMouseListener(listener);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}

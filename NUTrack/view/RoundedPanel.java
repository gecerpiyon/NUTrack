package view;

import javax.swing.*;
import java.awt.*;

// This class creates a custom JPanel with rounded corners
public class RoundedPanel extends JPanel {
    // radius for the corner roundness
    private int radius;

    // Constructor to set the radius
    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false); // make background transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Use Graphics2D for better drawing quality
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw shadow effect
        g2.setColor(new Color(0, 0, 0, 30)); // semi-transparent black
        g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, radius, radius);

        // Draw the actual background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, radius, radius);

        g2.dispose(); // clean up
        super.paintComponent(g); // paint default components
    }
}

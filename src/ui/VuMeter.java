package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Allows to display the signal level
public class VuMeter extends Canvas {
    // Draws a vertical green/orange/red rectangle depending on the signal level.
    // You may want to use Canvas.getGraphicsContext2D() to write geometrical
    // shapes and fill them with colours.

    // Must be called from the same AnimationTimer as SignalView

    private static final double GREEN_THRESHOLD = -30.0;
    private static final double ORANGE_THRESHOLD = -15.0;
    private static final double RED_THRESHOLD = 0.0;

    // Must be called from the same AnimationTimer as SignalView
    public void updateVuMeter(double dBlevel) {
        GraphicsContext gc = getGraphicsContext2D();

        // Clear the previous content
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Choose color based on the signal level
        Color color;
        if (dBlevel < GREEN_THRESHOLD) {
            color = Color.GREEN;
        } else if (dBlevel < ORANGE_THRESHOLD) {
            color = Color.ORANGE;
        } else {
            color = Color.RED;
        }

        // Draw a vertical rectangle filled with the chosen color
        gc.setFill(color);
        double rectangleHeight = calculateRectangleHeight(dBlevel);
        gc.fillRect(0, getHeight() - rectangleHeight, getWidth(), rectangleHeight);
    }

    /**
     * Calculate the height of the rectangle based on the signal level.
     * @param dBlevel The signal level in decibels.
     * @return The height of the rectangle.
     */
    private double calculateRectangleHeight(double dBlevel) {
        // Map dBlevel to the range [0, getHeight()]
        double normalizedLevel = Math.max(0, Math.min(-dBlevel, getHeight()));
        return normalizedLevel;
    }
}

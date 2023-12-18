package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import audio.AudioSignal;
import math.Complex;
import math.FFT;

import java.util.Arrays;

public class Spectrogram extends Canvas {
    private final int spectrogramWidth = 800; // Set the width of the spectrogram
    private final int spectrogramHeight = 400; // Set the height of the spectrogram

    private final WritableImage spectrogramImage;
    private final PixelWriter pixelWriter;

    public Spectrogram() {
        super();

        // Create a WritableImage for the spectrogram
        spectrogramImage = new WritableImage(spectrogramWidth, spectrogramHeight);
        pixelWriter = spectrogramImage.getPixelWriter();

        // Set the size of the Canvas
        setWidth(spectrogramWidth);
        setHeight(spectrogramHeight);

        // Initialize the image with a default color (e.g., white)
        clearSpectrogram();
    }

    /** Clear the spectrogram by filling it with a default color (e.g., white) */
    public void clearSpectrogram() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, spectrogramWidth, spectrogramHeight);
    }

    /**
     * Update the spectrogram based on the Fourier transform of the given audio signal.
     * @param audioSignal The audio signal to compute the spectrogram from.
     */
    public void updateSpectrogram(AudioSignal audioSignal) {
        // Clear the spectrogram before updating
        clearSpectrogram();

        // Get the sample buffer from the audio signal
        double[] sampleBuffer = audioSignal.getSampleBuffer();

        // Compute the FFT of the audio signal
        Complex[] fftResult = FFT.fft(toComplexArray(sampleBuffer));

        // Display the amplitude (absolute value) of the FFT coefficients as vertical lines
        for (int i = 0; i < spectrogramWidth; i++) {
            double magnitude = fftResult[i].abs();
            drawVerticalLine(i, magnitude);
        }
    }

    /**
     * Draw a vertical line on the spectrogram at the specified x-coordinate with the given magnitude.
     * @param x        The x-coordinate of the vertical line.
     * @param magnitude The magnitude of the line (height).
     */
    private void drawVerticalLine(int x, double magnitude) {
        for (int y = 0; y < magnitude && y < spectrogramHeight; y++) {
            pixelWriter.setColor(x, spectrogramHeight - y - 1, Color.BLACK);
        }
    }

    /**
     * Convert a double array to a Complex array.
     * @param array The input double array.
     * @return The resulting Complex array.
     */
    private Complex[] toComplexArray(double[] array) {
        return Arrays.stream(array).mapToObj(value -> new Complex(value, 0)).toArray(Complex[]::new);
    }
}
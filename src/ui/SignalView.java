package ui;

import audio.AudioSignal;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

/*
You need to have a method updateData() that would update the chart content from an AudioSignal.
This method may be called periodically from a javafx.animation.AnimationTimer, which is
probably the most efficient way as THIS timer knows what is the best update frequency.
*/

// SignalView class extends LineChart<Number,Number> and can display an AudioSignal
public class SignalView extends LineChart<Number, Number> {
    private AudioSignal audioSignal;

    // Define x and y axes
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();

    // Create a series for the LineChart
    private final XYChart.Series<Number, Number> series = new XYChart.Series<>();

    // Constructor
    public SignalView() {
        super(new NumberAxis(), new NumberAxis());
        this.xAxis.setLabel("Time");
        this.yAxis.setLabel("Amplitude");
        this.setTitle("Audio Signal");

        // Add the series to the chart
        this.getData().add(series);

        // Set the axis for the chart
        this.setCreateSymbols(false);
        this.setAnimated(false);  // Disable unnecessary animations
    }

    // Update data method, called periodically from AnimationTimer
    public void updateData() {
        if (audioSignal != null) {
            // Clear previous data
            series.getData().clear();

            // Get sample buffer from AudioSignal
            double[] sampleBuffer = audioSignal.getSampleBuffer();

            // Populate series with sample data
            for (int i = 0; i < sampleBuffer.length; i++) {
                series.getData().add(new XYChart.Data<>(i, sampleBuffer[i]));
            }
        }
    }

    // Setter for AudioSignal
    public void setAudioSignal(AudioSignal audioSignal) {
        this.audioSignal = audioSignal;
    }
}


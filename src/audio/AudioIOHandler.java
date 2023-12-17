package audio;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;

import java.util.List;

public class AudioIOHandler {

    private ComboBox<String> audioInputComboBox;
    private ComboBox<String> audioOutputComboBox;

    public AudioIOHandler(ComboBox<String> audioInputComboBox, ComboBox<String> audioOutputComboBox) {
        this.audioInputComboBox = audioInputComboBox;
        this.audioOutputComboBox = audioOutputComboBox;
    }

    public void populateAudioInputDevices() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Obtain available audio input devices
                List<String> inputDevices = AudioIO.getAvailableInputDevices();

                // Update UI with obtained audio input devices
                Platform.runLater(() -> {
                    audioInputComboBox.getItems().addAll(inputDevices);
                });

                return null;
            }
        };

        // Start the task in a separate thread
        new Thread(task).start();
    }

    public void populateAudioOutputDevices() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Obtain available audio output devices
                List<String> outputDevices = AudioIO.getAvailableOutputDevices();

                // Update UI with obtained audio output devices
                Platform.runLater(() -> {
                    audioOutputComboBox.getItems().addAll(outputDevices);
                });

                return null;
            }
        };

        // Start the task in a separate thread
        new Thread(task).start();
    }

    public void startAudioProcessing() {
        // Perform audio processing logic using selected input and output devices
        String selectedInput = audioInputComboBox.getValue();
        String selectedOutput = audioOutputComboBox.getValue();

        // Simulate audio processing start (replace with actual code)
        System.out.println("Audio processing started with input: " + selectedInput + ", output: " + selectedOutput);

        // Now, you can use the selectedInput and selectedOutput to start audio processing
        // (You can call the relevant methods from the AudioIO class)
    }

    public ComboBox<String> getAudioInputComboBox() {
        return audioInputComboBox;
    }

    public ComboBox<String> getAudioOutputComboBox() {
        return audioOutputComboBox;
    }
}

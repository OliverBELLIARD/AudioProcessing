package ui;

import audio.AudioIO;
import audio.AudioSignal;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import audio.AudioIOHandler;

public class Main extends Application {

    private AudioIOHandler audioIOHandler;
    private boolean audioProcessingOn = false;

    /* Terminal command to run in application configuration (Current Application>Edit Configuration):
    >>> cd src
    >>> --module-path C:\Users\olire\Documents\Java\javafx-libs\javafx-sdk-21.0.1\lib --add-modules ALL-MODULE-PATH

    Note: You must adapt the path of the library to your local library to run this Application.

    Old (runner project):
    --module-path "C:\Users\olire\Documents\Java\javafx-libs\javafx-sdk-21.0.1\lib" --add-modules javafx.controls
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create ComboBoxes for audio input and output devices
            ComboBox<String> audioInputComboBox = new ComboBox<>();
            ComboBox<String> audioOutputComboBox = new ComboBox<>();

            // Initialize AudioIOHandler with ComboBoxes
            audioIOHandler = new AudioIOHandler(audioInputComboBox, audioOutputComboBox);

            // Set up the main window elements (root)
            BorderPane root = new BorderPane();
            root.setTop(createToolbar());
            root.setBottom(createStatusbar());
            root.setCenter(createMainContent());

            // Set up the layout
            Scene scene = new Scene(root,1500,800);

            // Set up the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("The JavaFX audio processor");
            primaryStage.show();

            // Populate audio input and output devices asynchronously
            audioIOHandler.populateAudioInputDevices();
            audioIOHandler.populateAudioOutputDevices();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Returns a populated toolbar.
     * @return Node populated toolbar
     */
    private Node createToolbar() {
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");

        ToolBar toolbar = new ToolBar(startButton, stopButton, new Separator());

        toolbar.getItems().addAll(
                new Label("Input device:"), audioIOHandler.getAudioInputComboBox(),
                new Separator(),
                new Label("Output device:"), audioIOHandler.getAudioOutputComboBox());

        startButton.setOnAction(event -> startAudioProcessing());
        stopButton.setOnAction(event -> stopAudioProcessing());

        return toolbar;
    }

    /**
     * @brief Returns a populated status bar.
     * @return Node populated status bar
     */
    private Node createStatusbar() {
        HBox statusbar = new HBox();
        TextField statusTextField = new TextField(" ");
        statusbar.getChildren().addAll(new Label("Status:"), statusTextField);
        return statusbar;
    }

    /**
     * @brief Starts the audio processing.
     */
    private void startAudioProcessing() {
        audioIOHandler.startAudioProcessing();
        audioProcessingOn = true;

        AudioIO.setAudioInputLine(AudioIO.obtainAudioInput(
                audioIOHandler.getAudioInputComboBox().getSelectionModel().getSelectedItem(),
                44100));

        AudioIO.setAudioOutputLine(AudioIO.obtainAudioOutput(
                audioIOHandler.getAudioOutputComboBox().getSelectionModel().getSelectedItem(),
                44100));

        System.out.println("Audio processing started.");
    }

    /**
     * @brief Stops the audio processing.
     */
    private void stopAudioProcessing() {
        // Stop audio processing using AudioIOHandler (add relevant method in AudioIOHandler)
        // audioIOHandler.stopAudioProcessing();
        audioProcessingOn = false;
        System.out.println("Audio processing stopped.");
    }

    private Node createMainContent() {
        // Create instances of your UI components
        SignalView signalView = new SignalView();
        Spectrogram spectrogram = new Spectrogram();
        VuMeter vuMeter = new VuMeter();

        // Create a layout to arrange the UI components
        HBox mainContent = new HBox(signalView, spectrogram, vuMeter);

        // We prevent the processing to start with null I/O
        audioIOHandler.getAudioInputComboBox().getSelectionModel().selectFirst();
        audioIOHandler.getAudioOutputComboBox().getSelectionModel().selectFirst();

        // We initialize the global audio I/O Lines
        AudioIO.setAudioInputLine(AudioIO.obtainAudioInput(
                audioIOHandler.getAudioInputComboBox().getSelectionModel().getSelectedItem(),
                44100));
        AudioIO.setAudioOutputLine(AudioIO.obtainAudioOutput(
                audioIOHandler.getAudioOutputComboBox().getSelectionModel().getSelectedItem(),
                44100));

        // Attach AnimationTimers to update the views periodically
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (audioProcessingOn) {
                    // Get the current audio signal
                    AudioSignal audioSignal = audioIOHandler.getAudioSignal();

                    // Update the signal view
                    signalView.setAudioSignal(audioSignal);
                    signalView.updateData();

                    // Update the VuMeter
                    vuMeter.updateVuMeter(audioSignal.getdBlevel());

                    // Update the spectrogram
                    spectrogram.updateSpectrogram(audioSignal);
                }
            }
        };

        animationTimer.start(); // Start the animation timer

        return mainContent;
    }

}
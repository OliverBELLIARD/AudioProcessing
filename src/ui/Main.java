package ui;

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
        System.out.println("Audio processing started.");
    }

    /**
     * @brief Stops the audio processing.
     */
    private void stopAudioProcessing() {
        // Stop audio processing using AudioIOHandler (add relevant method in AudioIOHandler)
        // audioIOHandler.stopAudioProcessing();
        System.out.println("Audio processing stopped.");
    }

    private Node createMainContent() {
        Group g = new Group();
        // Using g.getChildren().add(...) is easy to add graphic elements of type Node
        return g;
    }
}
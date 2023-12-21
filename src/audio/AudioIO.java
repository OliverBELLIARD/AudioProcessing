package audio;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** A collection of static utilities related to the audio system. */
public class AudioIO {
    private static final int defaultFramSize = 1024;
    private static TargetDataLine audioInputLine;  // Add this line to store the audio input line
    private static SourceDataLine audioOutputLine; // Add this line to store the audio output line

    /** Displays every audio mixer available on the current system. */
    public static void printAudioMixers() {
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
        .forEach(e -> System.out.println("- name=\"" + e.getName()
            + "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
    }

    /** @return a Mixer.Info whose name matches the given string.
     * Example of use: getMixerInfo("Macbook default output")
     */
    public static Mixer.Info getMixerInfo(String mixerName) {
        // see how the use of streams is much more compact than for() loops!
        return Arrays.stream(AudioSystem.getMixerInfo())
        .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }

    /** Return a line that's appropriate for recording sound from a microphone.
     * Example of use:
     * TargetDataLine line = obtainInputLine("USB Audio Device", 8000);
     * @param mixerName a string that matches one of the available mixers.
     * @see AudioSystem.getMixerInfo() which provides a list of all mixers on your system.
     */
    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) {
        int channels = 2;
        int sampleBytes = Short.SIZE / 8;
        int frameBytes = sampleBytes * channels;
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                Short.SIZE,
                channels,
                frameBytes,
                sampleRate,
                true);
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            Mixer mixer = AudioSystem.getMixer(getMixerInfo(mixerName));
            TargetDataLine line = (TargetDataLine) mixer.getLine(info);
            line.open(format);
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Return a line that's appropriate for playing sound to a loudspeaker. */
    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) {
        int channels = 2;
        int sampleBytes = Short.SIZE / 8;
        int frameBytes = sampleBytes * channels;
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                Short.SIZE,
                channels,
                frameBytes,
                sampleRate,
                true);

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            Mixer mixer = AudioSystem.getMixer(getMixerInfo(mixerName));
            SourceDataLine line = (SourceDataLine) mixer.getLine(info);
            line.open(format);
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Returns a list of available audio input devices.
     * @return List<String> list of available audio input devices.
     */
    public static List<String> getAvailableInputDevices() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        List<String> inputDevices = new ArrayList<>();

        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.getTargetLineInfo().length > 0) {
                inputDevices.add(info.getName());
            }
        }

        return inputDevices;
    }

    /** Returns a list of available audio output devices.
     * @return List<String> list of available audio output devices.
     */
    public static List<String> getAvailableOutputDevices() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        List<String> outputDevices = new ArrayList<>();

        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.getSourceLineInfo().length > 0) {
                outputDevices.add(info.getName());
            }
        }

        return outputDevices;
    }

    /** Returns the current audio signal. */
    public static AudioSignal getCurrentAudioSignal() {
        AudioSignal audioSignal = new AudioSignal(defaultFramSize);

        // Assuming you have an initialized TargetDataLine for recording
        TargetDataLine audioInputLine = getAudioInputLine();

        // Assuming you have initialized the AudioSignal instance
        if (audioInputLine != null) {
            audioSignal.recordFrom(audioInputLine);
        }

        return audioSignal;
    }

    /** Start audio processing using the specified input and output devices. */
    public static void startAudioProcessing(String inputDevice, String outputDevice) {
        // Placeholder implementation; replace with actual logic
        System.out.println("Audio processing started with input: " + inputDevice + ", output: " + outputDevice);
        // Example: Start recording and playing audio
        startRecording(inputDevice, 44100);
        startPlayback(outputDevice, 44100);
    }

    /** Stops audio processing. */
    public static void stopAudioProcessing() {
        // Placeholder implementation; replace with actual logic
        System.out.println("Audio processing stopped.");
        // Example: Stop recording and playing audio
        stopRecording();
        stopPlayback();
    }

    /** Starts recording audio. */
    private static void startRecording(String inputDevice, int sampleRate) {
        audioInputLine = obtainAudioInput(inputDevice, sampleRate);

        if (audioInputLine != null) {
            audioInputLine.start();
            // Placeholder: Add logic to handle the recorded audio data
        }
    }

    /** Stops recording audio. */
    private static void stopRecording() {
        if (audioInputLine != null) {
            audioInputLine.stop();
            audioInputLine.close();
        }
    }

    /** Starts playing back audio. */
    private static void startPlayback(String outputDevice, int sampleRate) {
        audioOutputLine = obtainAudioOutput(outputDevice, sampleRate);

        if (audioOutputLine != null) {
            audioOutputLine.start();
            // Placeholder: Add logic to provide audio data for playback
        }
    }

    /** Stops playing back audio. */
    private static void stopPlayback() {
        if (audioOutputLine != null) {
            audioOutputLine.stop();
            audioOutputLine.close();
        }
    }

    public static TargetDataLine getAudioInputLine() {
        return audioInputLine;
    }

    public static void setAudioInputLine(TargetDataLine audioInputLine) {
        // Check if initialization was successful
        if (audioInputLine == null) {
            System.out.println("Failed to initialize audio input line.");
        }

        AudioIO.audioInputLine = audioInputLine;
    }

    public static SourceDataLine getAudioOutputLine() {
        return audioOutputLine;
    }

    public static void setAudioOutputLine(SourceDataLine audioOutputLine) {
        // Check if initialization was successful
        if (audioOutputLine == null) {
            System.out.println("Failed to initialize audio output line.");
        }

        AudioIO.audioOutputLine = audioOutputLine;
    }

    /** Test client for all class methods.
     * @param args
     */
    public static void main(String[] args) {
        // Print available audio mixers
        printAudioMixers();

        // Obtain audio input line (replace "YourMicMixerName" with the actual mixer name)
        TargetDataLine audioInputLine = obtainAudioInput("YourMicMixerName", 44100);

        if (audioInputLine != null) {
            // Use the obtained audio input line for recording
            // (You can test AudioSignal's recordFrom method with this line)
        }

        // Obtain audio output line (replace "YourSpeakerMixerName" with the actual mixer name)
        SourceDataLine audioOutputLine = obtainAudioOutput("YourSpeakerMixerName", 44100);

        if (audioOutputLine != null) {
            // Use the obtained audio output line for playback
            // (You can test AudioSignal's playTo method with this line)
        }

        // Obtain and print available audio input devices
        List<String> inputDevices = getAvailableInputDevices();
        System.out.println("Available Input Devices: " + inputDevices);

        // Obtain and print available audio output devices
        List<String> outputDevices = getAvailableOutputDevices();
        System.out.println("Available Output Devices: " + outputDevices);

    }
}

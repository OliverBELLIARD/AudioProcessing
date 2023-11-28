package audio;

import javax.sound.sampled.*;
import java.util.Arrays;

/** A collection of static utilities related to the audio system. */
public class AudioIO {

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
    }
}

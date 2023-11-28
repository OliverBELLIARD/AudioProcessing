package audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/** The main audio processing class, implemented as a Runnable so
 * as to be run in a separated execution Thread. */
public class AudioProcessor implements Runnable {

    private AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning; // makes it possible to "terminate" thread

    /** Creates an AudioProcessor that takes input from the given TargetDataLine, and plays back
     * to the given SourceDataLine.
     * @param frameSize the size of the audio buffer. The shorter, the lower the latency. */
    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
        this.inputSignal = new AudioSignal(frameSize);
        this.outputSignal = new AudioSignal(frameSize);
    }

    /** Audio processing thread code. Basically an infinite loop that continuously fills the sample
     * buffer with audio data fed by a TargetDataLine and then applies some audio effect, if any,
     * and finally copies data back to a SourceDataLine.*/
    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning) {
            inputSignal.recordFrom(audioInput);

            // your job: copy inputSignal to outputSignal with some audio effect

            // TODO: Apply your audio processing effect here
            // For example, you might implement a method like applyAudioEffect() in AudioSignal

            // Copy inputSignal to outputSignal (placeholder, replace with actual processing)
            outputSignal.setFrom(inputSignal);

            outputSignal.playTo(audioOutput);
        }
    }

    /** Tells the thread loop to break as soon as possible. This is an asynchronous process. */
    public void terminateAudioThread() {
        isThreadRunning = false;
    }

    // todo here: all getters and setters


    public AudioSignal getInputSignal() {
        return inputSignal;
    }

    public void setInputSignal(AudioSignal inputSignal) {
        this.inputSignal = inputSignal;
    }

    public AudioSignal getOutputSignal() {
        return outputSignal;
    }

    public void setOutputSignal(AudioSignal outputSignal) {
        this.outputSignal = outputSignal;
    }

    public TargetDataLine getAudioInput() {
        return audioInput;
    }

    public void setAudioInput(TargetDataLine audioInput) {
        this.audioInput = audioInput;
    }

    public SourceDataLine getAudioOutput() {
        return audioOutput;
    }

    public void setAudioOutput(SourceDataLine audioOutput) {
        this.audioOutput = audioOutput;
    }

    public boolean isThreadRunning() {
        return isThreadRunning;
    }

    // No setter for (bool) isThreadRunning since it should be set and reset by thread management methods only

    /* an example of a possible test code */
    public static void main(String[] args) {
        TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device", 16000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device", 16000);

        if (inLine != null && outLine != null) {
            try {
                inLine.open();
                inLine.start();
                outLine.open();
                outLine.start();

                AudioProcessor audioProcessor = new AudioProcessor(inLine, outLine, 1024);
                new Thread(audioProcessor).start();
                System.out.println("A new thread has been created!");

                // Sleep for a while to allow the thread to run (this is just for testing purposes)
                Thread.sleep(5000);

                // Terminate the audio thread
                audioProcessor.terminateAudioThread();

                // Close the lines
                inLine.close();
                outLine.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to obtain audio lines.");
        }
    }
}
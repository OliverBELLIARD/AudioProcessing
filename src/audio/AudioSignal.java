package audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Arrays;

/** A container for an audio signal backed by a double buffer so as to allow floating point calculation
 * for signal processing and avoid saturation effects. Samples are 16 bit wide in this implementation. */
public class AudioSignal {

    private double[] sampleBuffer; // floating point representation of audio samples
    private double dBlevel; // current signal level
    private int frameSize;

    public static void main(String[] args) {

    }

    /** Construct an AudioSignal that may contain up to "frameSize" samples.
     * @param frameSize the number of samples in one audio frame */
    public AudioSignal(int frameSize) {
        this.frameSize = frameSize;
    }

    /** Sets the content of this signal from another signal.
     * @param other other.length must not be lower than the length of this signal. */
    public void setFrom(AudioSignal other) {

    }

    /** Fills the buffer content from the given input. Byte's are converted on the fly to double's.
     * @return false if at end of stream */
    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length*2]; // 16 bit samples

        if (audioInput.read(byteBuffer, 0, byteBuffer.length)==-1) return false;

        for (int i=0; i<sampleBuffer.length; i++)
            sampleBuffer[i] = ((byteBuffer[2*i]<<8)+byteBuffer[2*i+1]) / 32768.0; // big endian

        dBlevel = 20*Math.log(Arrays.stream(sampleBuffer).sum()/sampleBuffer.length);
        // ... TODO : dBlevel = update signal level in dB here ...
        return true;
    }

    /** Plays the buffer content to the given output.
     * @return false if at end of stream */
    public boolean playTo(SourceDataLine audioOutput) {
        return true;
    }

    // Getters & Setters
    public double[] getSampleBuffer() {
        return sampleBuffer;
    }

    public void setSampleBuffer(double[] sampleBuffer) {
        this.sampleBuffer = sampleBuffer;
    }

    public double getdBlevel() {
        return dBlevel;
    }

    public void setdBlevel(double dBlevel) {
        this.dBlevel = dBlevel;
    }

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public void setSample(int i, double value) {
        sampleBuffer[i] = value;
    }

    // [x] your job: add getters and setters ...
    // [x] double getSample(int i)
    // [x] void setSample(int i, double value)
    // [x] double getdBLevel()
    // [x] int getFrameSize()
    // [ ] Can be implemented much later: Complex[] computeFFT()
}
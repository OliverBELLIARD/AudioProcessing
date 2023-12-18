package audio;

import math.Complex;
import math.FFT;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.util.Arrays;

/** A container for an audio signal backed by a double buffer so as to allow floating point calculation
 * for signal processing and avoid saturation effects. Samples are 16 bit wide in this implementation. */
public class AudioSignal {
    private double[] sampleBuffer; // floating point representation of audio samples
    private double dBlevel; // current signal level
    private int frameSize;

    public static void main(String[] args) {
        // Define the frame size (number of samples in one audio frame)
        int frameSize = 1024;

        // Create an instance of AudioSignal
        AudioSignal audioSignal = new AudioSignal(frameSize);

        // Test setFrom method by creating another AudioSignal
        AudioSignal otherSignal = new AudioSignal(frameSize);
        // Assume otherSignal has some sample data
        otherSignal.setSampleBuffer(new double[frameSize]); // Initialize with sample data
        audioSignal.setFrom(otherSignal);

        // Populate sampleBuffer with some data (replace this with actual data)
        double[] testData = new double[frameSize];
        for (int i = 0; i < frameSize; i++) {
            testData[i] = Math.sin(2 * Math.PI * i / frameSize);
        }
        audioSignal.setSampleBuffer(testData);

        // Compute FFT
        Complex[] fftResult = audioSignal.computeFFT();

        // Test FFT by displaying the FFT result
        System.out.println("FFT Result:");
        for (Complex c : fftResult) {
            System.out.println(c);
        }
    }


    /** Construct an AudioSignal that may contain up to "frameSize" samples.
     * @param frameSize the number of samples in one audio frame */
    public AudioSignal(int frameSize) {
        this.frameSize = frameSize;
    }

    /** Sets the content of this signal from another signal.
     * @param other other.length must not be lower than the length of this signal. */
    public void setFrom(AudioSignal other) {
        frameSize = other.getFrameSize();
        dBlevel = other.getdBlevel();
        sampleBuffer = Arrays.copyOf(other.getSampleBuffer(), frameSize);
    }

    /** Fills the buffer content from the given input. Byte's are converted on the fly to double's.
     * @return false if at end of stream */
    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length*2]; // 16 bit samples

        if (audioInput.read(byteBuffer, 0, byteBuffer.length)==-1) return false;

        for (int i=0; i<sampleBuffer.length; i++)
            sampleBuffer[i] = ((byteBuffer[2*i]<<8)+byteBuffer[2*i+1]) / 32768.0; // big endian

        dBlevel = 20*Math.log(Arrays.stream(sampleBuffer).sum()/sampleBuffer.length);
        return true;
    }

    /** Plays the buffer content to the given output.
     * @return false if at end of stream */
    public boolean playTo(SourceDataLine audioOutput) {
        if (audioOutput == null) {
            // Handle the case where the SourceDataLine is not available
            return false;
        }

        byte[] byteBuffer = new byte[sampleBuffer.length * 2]; // 16 bit samples

        for (int i = 0; i < sampleBuffer.length; i++) {
            short sampleValue = (short) (sampleBuffer[i] * 32767.0); // Convert double to short
            byteBuffer[2 * i] = (byte) (sampleValue & 0xFF); // Little endian
            byteBuffer[2 * i + 1] = (byte) ((sampleValue >> 8) & 0xFF);
        }

        int bytesWritten = audioOutput.write(byteBuffer, 0, byteBuffer.length);

        return bytesWritten == byteBuffer.length;
    }

    /** Compute the FFT of the audio signal. */
    public Complex[] computeFFT() {
        // Use the FFT class to compute the FFT of the sampleBuffer
        return FFT.fft(Arrays.stream(sampleBuffer)
                .mapToObj(value -> new Complex(value, 0))
                .toArray(Complex[]::new));
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

    public int getFrameSize() { return frameSize;}

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public void setSample(int i, double value) {
        sampleBuffer[i] = value;
    }
}
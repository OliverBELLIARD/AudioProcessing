package ui;

import javafx.scene.canvas.Canvas;

public class Spectrogram extends Canvas {
    /*
    We will use a WritableImage to draw the spectrogram in real time.
    GraphicsContext.drawImage() in effect displays this image on the screen
    (more exactly: inside the Canvas area). Using the getPixelWriter() method,
    you can obtain a tool that allows you to modify the image pixels one by one,
    which, with some clever coding, should get the job done... The idea is to
    draw one vertical line after the other, filling each line with the
    (absolute value of the) coefficients of the Fourier transform of the signal.
     */
}

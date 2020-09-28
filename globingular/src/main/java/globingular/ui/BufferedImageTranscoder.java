package globingular.ui;

import java.awt.image.BufferedImage;

import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

public class BufferedImageTranscoder extends ImageTranscoder {

    /**
     * The current buffered image.
     */
    private BufferedImage img = null;

    /**
     * Create a new buffered image and return it.
     *
     * @param w The width of the image
     * @param h The height of the image
     * @return The created image
     */
    @Override
    public BufferedImage createImage(final int w, final int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Dummy method, required ignoredTO extend ImageTranscoder.
     * @param ignoredImg Ignored
     * @param ignoredTO Ignored
     * @throws UnsupportedOperationException Every time
     */
    @Override
    public void writeImage(final BufferedImage ignoredImg, final TranscoderOutput ignoredTO) {
        throw new UnsupportedOperationException("This is a dummy method!");
    }

    /**
     * Getter for the field img.
     * @return The field img
     */
    public BufferedImage getBufferedImage() {
        return img;
    }

    /**
     * Setter for the field img.
     * @param img The new img
     */
    public void setImg(final BufferedImage img) {
        this.img = img;
    }
}

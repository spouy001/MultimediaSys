package Forms;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
//import com.sun.media.BasicPlayer;
/**
 * Created by spouy001 on 6/7/17.
 */
public class VideoPanel extends JPanel {

    private boolean mantainRatio = true;

    private final ImageComponent imageComponent;

    private int videoWidth = getWidth();
    private int videoHeight = getHeight();

    private BufferedImage currentImage = null;

    private boolean videoStarted = false;

    private BasicPlayer caller = null;

    public VideoPanel(BasicPlayer caller) {
        super();

        this.caller = caller;

        this.setBackground(Color.BLACK);
        this.setOpaque(true);

        imageComponent = new ImageComponent();

        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        this.add(imageComponent);
        this.caller.pack();
        this.setVisible(true);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resize();
            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getClickCount() == 2) swap();
            }
        });
    }

    public boolean isFullScreen() {
        return isFullScreen();
    }

    public void swap() {
        //caller.changeScreen();
    }

    private void resize() {
        if (currentImage == null) {
            return;
        }

        if (mantainRatio == true) {
            double imgW = currentImage.getWidth(this);
            double imgH = currentImage.getHeight(this);

            double ratio1 = (double) getWidth() / imgW;
            double ratio2 = (double) getHeight() / imgH;

            double scaleFactor = Math.min(ratio1, ratio2);

            videoWidth = (int) (currentImage.getWidth(this) * scaleFactor);
            videoHeight = (int) (currentImage.getHeight(this) * scaleFactor);
        } else {
            videoWidth = getWidth();
            videoHeight = getHeight();
        }
    }

    public void setImage(final BufferedImage aImage) {
        currentImage = aImage;

        if (videoStarted == false) {
            resize();
            videoStarted = true;
        }

        imageComponent.setImage(aImage);
    }

    public void setMantainRatio(boolean mantainRatio) {
        this.mantainRatio = mantainRatio;
    }

    public class ImageComponent extends JComponent {

        private Image image;

        public void setImage(Image image) {
            SwingUtilities.invokeLater(new ImageRunnable(image));
        }

        private class ImageRunnable implements Runnable {

            private final Image newImage;

            public ImageRunnable(Image newImage) {
                super();
                this.newImage = newImage;
            }

            public void run() {
                ImageComponent.this.image = newImage;
                repaint();
            }
        }

        public synchronized void paint(Graphics g) {
            if (image != null) {
                g.drawImage(image, (getWidth() - videoWidth)/2, (getHeight() - videoHeight)/2, videoWidth, videoHeight, this);
            }
        }
    }
}

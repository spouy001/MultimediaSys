package Forms;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
/**
 * Created by spouy001 on 6/7/17.
 */
public class VideoPlayer {
    public static void main(String[] args)
    {
        //String sourceUrl = "/Users/spouy001/Downloads/_2QykGw2GWg.mp4";
        String sourceUrl = "/Users/spouy001/Downloads/SampleVideo_1280x720_1mb.mp4";
        VideoPlayer videoPlayer = new VideoPlayer();
        videoPlayer.play(sourceUrl);
    }

    public void play(String sourceUrl)
    {
        IMediaReader reader = ToolFactory.makeReader(sourceUrl);
        reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        final MyVideoFrame frame = new MyVideoFrame();
        frame.setSize(new Dimension(500, 500));

        MediaListenerAdapter adapter = new MediaListenerAdapter()
        {
            @Override
            public void onVideoPicture(IVideoPictureEvent event)
            {
                frame.setImage((BufferedImage) event.getImage());
                try {
                    Thread.sleep((int) reader.getContainer().getStream(event.getStreamIndex()).getFrameRate().getDouble());
                } catch (InterruptedException ex) {}
            }
        };
        reader.addListener(adapter);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (reader.readPacket() == null)
            do {} while(false);
    }

    @SuppressWarnings("serial")
    private class MyVideoFrame extends JFrame
    {
        Image image;

        public void setImage(final Image image)
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    MyVideoFrame.this.image = image;
                    repaint();
                }
            });
        }

        @Override
        public synchronized void paint(Graphics g)
        {
            if (image != null)
            {
                g.drawImage(image, 0, 0, null);
            }
        }
    }
}

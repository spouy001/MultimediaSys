package Forms;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Created by spouy001 on 6/7/17.
 */
public class BasicPlayer extends JFrame {

    private String whatToPlay = "";

    private static SourceDataLine mLine;

    private IContainer container = IContainer.make();
    private VideoPanel videopanel;

    private long startTime = 0;

    private IStreamCoder videoCoder = null;
    private IStreamCoder audioCoder = null;

    private long currentTimestamp = 0;

    private long firstTimestampInStream = Global.NO_PTS;

    private GraphicsDevice device;

    private boolean isFullScreen = false;
    private boolean isMute       = false;
    private boolean isStopped    = false;

    private IVideoPicture picture = null;

    private IPacket packet = null;
    private boolean finish=false;

    public BasicPlayer() {

//       JFileChooser fileChooser = new JFileChooser();
//
//        int response = fileChooser.showOpenDialog (this);
//
//       if (response == JFileChooser.APPROVE_OPTION) {
//            whatToPlay = fileChooser.getSelectedFile().getAbsolutePath();
//        }

        //
        //USED FOR A QUICK TEST
        //whatToPlay = "/Users/spouy001/Downloads/0HWd5-yMXCc.mp4";
        //
        //}
        //public void showForm() {

//        if(whatToPlay.equals("")) {
//            JOptionPane.showMessageDialog(this,"FILE TO PLAY UNKNOWN, EXITING","WARNING",JOptionPane.WARNING_MESSAGE);
//            System.exit(0);
//        }

        initComponents();

        videopanel = new VideoPanel(this);

        panel1.add(videopanel);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        device = devices[0];

        videopanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(isFullScreen) jPanel8.setVisible(false);
            }
        });
        jSlider1.addComponentListener(new ComponentAdapter() {
        });

    }

    private void printBar() {
        System.out.println("-------------------------------------------");
    }

    private void printInfo() {

        System.out.println("INSTALLED CODEC INFO");

        for(ICodec codec : ICodec.getInstalledCodecs()) {
            if(codec.getType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                System.out.print("Codec " + codec.getLongName() + " " + codec.canEncode() + " supports: ");

                if(codec.getNumSupportedVideoFrameRates() > 0) {
                    for(IPixelFormat.Type type : codec.getSupportedVideoPixelFormats()) {
                        System.out.print(type + " - ");
                    }
                } else {
                    System.out.print("unspecified");
                }
                System.out.println("");
            }
        }

        printBar();

        System.out.println("FOUND STREAMS INFO");

        int numStreams = container.getNumStreams();

        System.out.printf("file streams : %d stream%s\n",numStreams,numStreams == 1 ? "" : "s");

        for (int i = 0; i < numStreams; i++) {

            printBar();

            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();

            System.out.printf("stream    : %d\n",    i+1);
            System.out.printf("type      : %s\n",    coder.getCodecType());
            System.out.printf("codec     : %s\n",    coder.getCodecID());
            System.out.printf("frames    : %s\n",    stream.getDuration() == Global.NO_PTS ? "unknown" : "" + stream.getDuration());
            System.out.printf("start time: %s\n",    container.getStartTime() == Global.NO_PTS ? "unknown" : "" + stream.getStartTime());
            System.out.printf("language  : %s\n",   stream.getLanguage() == null ? "unknown" : stream.getLanguage());
            System.out.printf("timebase  : %d/%d\n", stream.getTimeBase().getNumerator(), stream.getTimeBase().getDenominator());
            System.out.printf("coder tb  : %d/%d\n", coder.getTimeBase().getNumerator(), coder.getTimeBase().getDenominator());

            printBar();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                System.out.printf("Audio sample rate: %d\n", coder.getSampleRate());
                System.out.printf("Audio channels   : %d\n", coder.getChannels());
                System.out.printf("Audio format     : %s\n", coder.getSampleFormat());
            } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                System.out.printf("Video width     : %d\n",    coder.getWidth());
                System.out.printf("Video height    : %d\n",    coder.getHeight());
                System.out.printf("Video format    : %s\n",    coder.getPixelType());
                System.out.printf("Video frame-rate: %5.2f\n", coder.getFrameRate().getDouble());
            }
        }

        printBar();

        System.out.println("CONTAINER INFO");

        System.out.printf("Video duration (ms)    : %s\n", container.getDuration() == Global.NO_PTS ? "unknown" : "" + container.getDuration()/1000);
        System.out.printf("Video start time (ms)  : %s\n", container.getStartTime() == Global.NO_PTS ? "unknown" : "" + container.getStartTime()/1000);
        System.out.printf("Video file size (bytes): %d\n", container.getFileSize());
        System.out.printf("Video bit rate         : %d\n", container.getBitRate());

        printBar();

        System.out.println("VIDEO CODER INFO");

        System.out.println("Video timebase  : " + videoCoder.getTimeBase());
        System.out.println("Video tolerance : " + videoCoder.getBitRateTolerance());
        System.out.println("Video channels  : " + videoCoder.getChannels());
        System.out.println("Video codec     : " + videoCoder.getCodec().getName());
        System.out.println("Video bitrate   : " + videoCoder.getBitRate());
        System.out.println("Video samplerate: " + videoCoder.getSampleRate());
        System.out.println("Video framerate : " + videoCoder.getFrameRate());
    }



    // public void SetPath(String path) {whatToPlay=path;}
    public void seekTo(int percent) {

        if(container == null) {
            System.out.println("NULL CONTAINER");
            return;
        }

        long seekByte = ((container.getFileSize()*percent)/100);
        long seekTime = (((container.getDuration()/videoCoder.getFrameRate().getNumerator())*percent)/100);

        System.out.println("Jump to " + percent + "% byte=" + seekByte + " time=" + seekTime);

        for(int i=0;i < container.getNumStreams();i++) {
            container.seekKeyFrame(i, seekByte, seekByte, seekByte, IContainer.SEEK_FLAG_BYTE);
            //container.seekKeyFrame(0, 0, seekByte, container.getDuration(), IContainer.SEEK_FLAG_FRAME);
        }

        firstTimestampInStream = Global.NO_PTS;
    }

    /**
     * Stop the player in parent frame
     * By Samira 07-20-2017
     */
    public void stop() {
        finish= true;
        audioCoder.close();
        audioCoder = null;
        videoCoder.close();
        videoCoder = null;
        container.close();
        container = null;
    }
    public void start(String path) {
        whatToPlay=path;
        if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) {
            throw new RuntimeException("you must install the GPL version  of Xuggler (with IVideoResampler support) for this demo to work");
        }

        if (container.open(whatToPlay, IContainer.Type.READ, null) < 0) {
            //throw new IllegalArgumentException("could not open file: " + whatToPlay);
            JOptionPane.showMessageDialog(new JFrame(), "Could not open file, Please check the path or format of the file", "Dialog", JOptionPane.ERROR_MESSAGE);
            dispose();


        }
        else {

            info.setText(" " + whatToPlay);
            duration.setText("" + convertTime(container.getDuration() / 1000000));

            int numStreams = container.getNumStreams();

            int videoStreamId = -1;
            int audioStreamId = -1;

            for (int i = 0; i < numStreams; i++) {
                IStream stream = container.getStream(i);
                IStreamCoder coder = stream.getStreamCoder();

                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                    videoStreamId = i;
                    videoCoder = coder;
                }
                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                    audioStreamId = i;
                    audioCoder = coder;
                }
            }

            if (videoStreamId == -1 && audioStreamId == -1) {
                throw new RuntimeException("could not find audio or video stream in container: " + whatToPlay);
            }

            if (videoCoder.open() < 0) {
                throw new RuntimeException("could not open video decoder for container: " + whatToPlay);
            }

            if (audioCoder != null) {
                if (audioCoder.open() < 0) {
                    throw new RuntimeException("could not open audio decoder for container: " + whatToPlay);
                }

                try {
                    openJavaSound(audioCoder);
                } catch (LineUnavailableException reason) {
                    throw new RuntimeException("unable to open sound device on your system when playing back container: " + whatToPlay);
                }
            }

            printInfo();

            printBar();

            IVideoResampler resampler = null;
            if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
                resampler = IVideoResampler.make(videoCoder.getWidth(),
                        videoCoder.getHeight(), IPixelFormat.Type.BGR24,
                        videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                if (resampler == null) {
                    throw new RuntimeException("could not create color space resampler for: " + whatToPlay);
                }
            }

            packet = IPacket.make();

            volume.setText("" + jSlider2.getValue() / 2 + "%");

            while (!finish && container.readNextPacket(packet) >= 0) {
                if (isStopped == true) {
                    try {
                        Thread.sleep(500);
                        continue;
                    } catch (InterruptedException ex) {
                    }
                }

                if (packet.getStreamIndex() == audioStreamId) {
                    IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());

                    int offset = 0;

                    while (offset < packet.getSize()) {

                        if (isStopped) {
                            break;
                        }

                        int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                        if (bytesDecoded < 0) {
                            throw new RuntimeException("got error decoding audio in: " + whatToPlay);
                        }
                        offset += bytesDecoded;

                        if (samples.isComplete() && (isMute == false)) {
                            playJavaSound(samples);
                        }
                    }
                } else if (packet.getStreamIndex() == videoStreamId) {

                    picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(), videoCoder.getHeight());

                    int offset = 0;

                    while (offset < packet.getSize()) {

                        if (isStopped) {
                            break;
                        }

                        int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);

                        if (bytesDecoded < 0) {
                            //skipAllPacket -> Seek to...
                            break;
                        }

                        offset += bytesDecoded;

                        if (picture.isComplete()) {
                            IVideoPicture newPic = picture;

                            if (resampler != null) {
                                // we must resample
                                newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), picture.getWidth(), picture.getHeight());
                                if (resampler.resample(newPic, picture) < 0) {
                                    throw new RuntimeException("could not resample video from: " + whatToPlay);
                                }
                            }

                            if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
                                throw new RuntimeException("could not decode video as BGR 24 bit data in: " + whatToPlay);
                            }

                            currentTimestamp = picture.getTimeStamp();

                            delay();

                            updatePanel(Utils.videoPictureToImage(newPic));
                            updateTimeline();
                        }
                    }
                } else {
                    do {
                    } while (false);
                }
            }

            if (videoCoder != null) {
                videoCoder.close();
                videoCoder = null;
            }
            if (audioCoder != null) {
                audioCoder.close();
                audioCoder = null;
            }
            if (container != null) {
                container.close();
                container = null;
            }
        }
    }

    private void openJavaSound(IStreamCoder aAudioCoder) throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(),
                (int) IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()),
                aAudioCoder.getChannels(),
                true,
                false);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        mLine = (SourceDataLine) AudioSystem.getLine(info);

        mLine.open(audioFormat);
        mLine.start();

        setVolume();
    }

    private static void playJavaSound(IAudioSamples aSamples) {
        byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
        mLine.write(rawBytes, 0, aSamples.getSize());
    }

    private static void closeJavaSound() {
        if(mLine != null) {
            mLine.drain();
            mLine.close();
            mLine = null;
        }
    }

    private void delay() {
        if (firstTimestampInStream == Global.NO_PTS) {
            firstTimestampInStream = currentTimestamp;
            startTime = System.currentTimeMillis();
        } else {
            long currentTime                   = System.currentTimeMillis();
            long msClockTimeSinceStartOfVideo  = currentTime - startTime;
            long msStreamTimeSinceStartOfVideo = (currentTimestamp - firstTimestampInStream) / 1000;
            long millisecondsTolerance         = 50;
            long millisecondsToSleep           = (msStreamTimeSinceStartOfVideo - msClockTimeSinceStartOfVideo + millisecondsTolerance);

            if (millisecondsToSleep > 0) {
                try {
                    Thread.sleep(millisecondsToSleep);
                } catch (InterruptedException reason) {
                    return;
                }
            }
        }
    }

    private String convertTime(long total) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String timer = "";
        if(picture == null)
            timer = formatter.format(new Date(total)) + " ";
        else
            timer = formatter.format(new Date(total)) + " ";

        return timer;
    }

    private void updateTimeline() {
        if(videoCoder == null) {
            return;

        }
        if(videoCoder.getStream() == null) {
            return;
        }

        duration.setText(picture.getFormattedTimeStamp() + " [" + (picture.getTimeStamp()/1000) + "] "+ convertTime(container.getDuration()/1000));
    }

    private void updatePanel(BufferedImage javaImage) {
        videopanel.setImage(javaImage);
    }

    public void changeScreen() {
        isFullScreen = !isFullScreen;

        if(isFullScreen) {
            dispose();
            setUndecorated(true);
            setResizable(false);
            device.setFullScreenWindow(this);
            setVisible(true);
        } else {
            dispose();
            setUndecorated(false);
            setResizable(true);
            device.setFullScreenWindow(null);
            setVisible(true);
        }

        jPanel8.setVisible(!isFullScreen);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        muteButton = new javax.swing.JButton();
        fast = new javax.swing.JButton();
        slow = new javax.swing.JButton();
        frameshot = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jSlider2 = new javax.swing.JSlider();
        jPanel4 = new javax.swing.JPanel();
        info = new javax.swing.JLabel();
        volume = new javax.swing.JLabel();
        duration = new javax.swing.JLabel();
        panel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Xuggler-based Media Player");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel6.setPreferredSize(new java.awt.Dimension(700, 600));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setLayout(new java.awt.BorderLayout());

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setMinorTickSpacing(5);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(0);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        jSlider1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSlider1KeyReleased(evt);
            }
        });
        jPanel5.add(jSlider1, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel5);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        playButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        playButton.setText("PAUSE");
        playButton.setPreferredSize(new java.awt.Dimension(70, 30));
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel3.add(playButton, gridBagConstraints);

        muteButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        muteButton.setText("MUTE");
        muteButton.setToolTipText("");
        muteButton.setPreferredSize(new java.awt.Dimension(70, 30));
        muteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muteButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        jPanel3.add(muteButton, gridBagConstraints);

        fast.setText(">>");
        fast.setPreferredSize(new java.awt.Dimension(70, 30));
        fast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        jPanel3.add(fast, gridBagConstraints);

        slow.setText("<<");
        slow.setPreferredSize(new java.awt.Dimension(70, 30));
        slow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slowActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        jPanel3.add(slow, gridBagConstraints);

        frameshot.setText("SHOT");
        frameshot.setPreferredSize(new java.awt.Dimension(70, 30));
        frameshot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                frameshotMouseEntered(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        jPanel3.add(frameshot, gridBagConstraints);

        jPanel7.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jSlider2.setMajorTickSpacing(20);
        jSlider2.setMaximum(200);
        jSlider2.setMinorTickSpacing(10);
        jSlider2.setPaintTicks(true);
        jSlider2.setToolTipText("");
        jSlider2.setValue(100);
        jSlider2.setMinimumSize(new java.awt.Dimension(36, 30));
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
        jPanel9.add(jSlider2);

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel7);

        jPanel2.add(jPanel8);

        jPanel4.setLayout(new java.awt.BorderLayout());

        info.setBackground(new java.awt.Color(51, 51, 255));
        info.setForeground(new java.awt.Color(255, 255, 255));
        info.setText(" info");
        info.setOpaque(true);
        info.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                infoMouseEntered(evt);
            }
        });
        jPanel4.add(info, java.awt.BorderLayout.WEST);

        volume.setBackground(new java.awt.Color(51, 51, 255));
        volume.setForeground(new java.awt.Color(255, 255, 255));
        volume.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        volume.setText("volume");
        volume.setOpaque(true);
        volume.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                volumeMouseEntered(evt);
            }
        });
        jPanel4.add(volume, java.awt.BorderLayout.CENTER);

        duration.setBackground(new java.awt.Color(51, 51, 255));
        duration.setForeground(new java.awt.Color(255, 255, 255));
        duration.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        duration.setText("duration");
        duration.setOpaque(true);
        duration.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                durationMouseEntered(evt);
            }
        });
        jPanel4.add(duration, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel4);

        jPanel6.add(jPanel2, java.awt.BorderLayout.SOUTH);

        panel1.setPreferredSize(new java.awt.Dimension(640, 345));
        panel1.setLayout(new javax.swing.BoxLayout(panel1, javax.swing.BoxLayout.LINE_AXIS));
        jPanel6.add(panel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel6);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        jump();
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jump() {
        seekTo(jSlider1.getValue());
    }

    private void detectKeyEvent(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (isFullScreen) {
                changeScreen();
            } else {
                System.out.println("EXIT");
                System.exit(0);
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            changeScreen();
        }
    }

    private void jSlider1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSlider1KeyReleased
        detectKeyEvent(evt);
        jump();
    }//GEN-LAST:event_jSlider1KeyReleased

    private void muteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_muteButtonActionPerformed
        isMute = !isMute;
    }//GEN-LAST:event_muteButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if(isStopped == true) {
            playButton.setText("PAUSE");
        } else {
            firstTimestampInStream = Global.NO_PTS;
            playButton.setText("PLAY");
        }

        isStopped = !isStopped;
    }//GEN-LAST:event_playButtonActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        updateTimeline();
    }//GEN-LAST:event_formComponentResized

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        setVolume();
    }//GEN-LAST:event_jSlider2StateChanged

    private void slowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slowActionPerformed
        //TODO
    }//GEN-LAST:event_slowActionPerformed

    private void fastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastActionPerformed
        //TODO
    }//GEN-LAST:event_fastActionPerformed

    private void frameshotMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_frameshotMouseEntered
        //TODO
    }//GEN-LAST:event_frameshotMouseEntered

    private void infoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_infoMouseEntered
        if(isFullScreen) jPanel8.setVisible(true);
    }//GEN-LAST:event_infoMouseEntered

    private void volumeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_volumeMouseEntered
        if(isFullScreen) jPanel8.setVisible(true);
    }//GEN-LAST:event_volumeMouseEntered

    private void durationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_durationMouseEntered
        if(isFullScreen) jPanel8.setVisible(true);
    }//GEN-LAST:event_durationMouseEntered

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        detectKeyEvent(evt);
    }//GEN-LAST:event_formKeyPressed

    private void setVolume() {
        if(mLine == null) return;

        FloatControl volctrl=(FloatControl)mLine.getControl(FloatControl.Type.MASTER_GAIN);

        float max_slider_value = 100F;
        float vol = ((float)jSlider2.getValue()/max_slider_value);
        float dB = (float)(Math.log(vol == 0.0F ? 0.0001F : vol)/Math.log(10.0)*20.0);

        volctrl.setValue(dB);

        volume.setText("" + jSlider2.getValue()/2 + "%");
    }





    private javax.swing.JLabel duration;
    private javax.swing.JButton fast;
    private javax.swing.JButton frameshot;
    private javax.swing.JLabel info;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JButton muteButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton slow;
    private javax.swing.JLabel volume;

    public static void main(String args[]) {
        BasicPlayer frame = new BasicPlayer();

        frame.setVisible(true);
        frame.start("/Users/spouy001/Downloads/0HWd5-yMXCc.mp4");
    }
}

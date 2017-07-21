package Forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.Vector;


import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import java.util.Scanner;
import java.io.IOException;


/**
 * Created by Samira Pouyanfar on 5/30/17.
 */
public class VideoFrame extends JFrame{
    private JPanel panelVideo;
    private JRadioButton RBVideo;
    private JRadioButton RBList;
    private JButton KeyframeButton;
    private JButton ShotBoundryButton;
    private JPanel PanelInfo;
    private JLabel LbPath;
    private  JPanel panellist;
    private JButton playButton;
    private  JEditorPane ListEditor;
    private int type=0;
    private String mediaURL;



    ActionListener action = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           new Thread(new VideoRunnable()).start();
        }

        class VideoRunnable implements Runnable {
            @Override
            public void run() {

                BasicPlayer player = new BasicPlayer();
                WindowListener exitListener=new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        int confirm = JOptionPane.showOptionDialog(player,
                                "Are You Sure to Close this Video?",
                                "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, null, null);
                        if (confirm == 0) {
                            player.stop();
                            player.dispose();

                        }
                    }
                };
                player.addWindowListener(exitListener);
                //player.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                player.setVisible(true);
                player.start(mediaURL);

            }
        }
    };
    private VideoFrame() {

        Initialize();
        RBVideo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type=1;
                OpenFile();

            }
        });
        RBList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type=2;
                OpenFile();

            }
        });
        playButton.addActionListener(action);
    }


    private void OpenFile(){
        //Open a file dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(new VideoFrame().panelVideo);
        //Check if the user selects a file or not:
        if (result == JFileChooser.APPROVE_OPTION) {
            // user selects a file
            //File selectedFile = fileChooser.getSelectedFile();
            mediaURL = fileChooser.getSelectedFile().getAbsolutePath();
            if (type == 1) {
                    playButton.setEnabled(true);
                    ShotBoundryButton.setEnabled(true);
                    KeyframeButton.setEnabled(true);
            }
            if (type == 2) {
                    String text;
                    try {
                        text = readFile(mediaURL);
                        ListEditor.setText(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playButton.setEnabled(false);
                    ShotBoundryButton.setEnabled(true);
                    KeyframeButton.setEnabled(true);
                }

            }

            LbPath.setText("Path:"+mediaURL);



    }
    public void showVideo(String mediaURL){

        VideoPlayer videoPlayer = new VideoPlayer();
        videoPlayer.play(mediaURL);
    //Tutorial video=new Tutorial();
    //video.DecodeAndPlayAudioAndVideo(mediaURL);


//        Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
//        try
//        {
//            Player mediaPlayer=Manager.createRealizedPlayer(mediaURL);
//
//            Component video= mediaPlayer.getVisualComponent();
//            Component controls= mediaPlayer.getControlPanelComponent();
//
//            if (video!=null)
//                panelvideoplay.add(video, BorderLayout.CENTER);
//            if (controls !=null)
//                panelvideoplay.add(controls, BorderLayout.SOUTH);
//
//            mediaPlayer.start();
//
//        }
//        catch (NoPlayerException noPlayerException)
//        {
//            System.err.println("No media player found");
//        }
//        catch (CannotRealizeException cannotRealizeException)
//        {
//            System.err.println("Could not realize media player");
//        }
//        catch (IOException iOException)
//        {
//            System.err.println("Error reading from the source");
//        }



//        Canvas canvas = new Canvas();
//        panelvideoplay.add(canvas);
//        panelvideoplay.revalidate();
//        panelvideoplay.repaint();
//        canvas.setVisible(true);
//        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
//        player = mediaPlayerFactory.newEmbeddedMediaPlayer();
//        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
//
//        player.setVideoSurface(videoSurface);
//        player.playMedia(mediaURL);

//        player = new EmbeddedMediaPlayerComponent();
//        Canvas c = new Canvas();
//        c.setBackground(Color.black);
//        c.setVisible(true);
//        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
//        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(c);
//
//        panelvideoplay.setLayout(new BorderLayout());
//        panelvideoplay.add(c, BorderLayout.CENTER);
//        panelvideoplay.revalidate();
//        panelvideoplay.repaint();
//        player.setVisible(true);
//        player.getMediaPlayer().playMedia(mediaURL);
    }
    public void Initialize(){
        ListEditor = new JEditorPane("text/html","No list is selected");
        ListEditor.setAutoscrolls(true);
        panellist.add(new JScrollPane(ListEditor), BorderLayout.CENTER);
        ListEditor.setEnabled(false);
    }
    static String readFile(String path)
            throws IOException
    {
        Scanner scanner = new Scanner( new File(path) );
        String text = scanner.useDelimiter("\r\n").next();
        scanner.close(); // Put this call in a finally block
        return text;
    }


    public static void main(String[] args){
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        JFrame frame = new JFrame("Video Processing");
        frame.setContentPane(new VideoFrame().panelVideo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,500));
        frame.pack();
        frame.setVisible(true);


    }
}

package Forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Utilities;
import java.io.File;

import java.util.Vector;


import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import java.util.Scanner;
import java.io.IOException;

import javax.swing.text.Utilities;
import javax.swing.text.BadLocationException;


/**
 * Created by Samira Pouyanfar on 5/30/17.
 */
public class VideoFrame extends JFrame{
    public JPanel panelVideo;
    private JRadioButton RBVideo;
    private JRadioButton RBList;
    private JButton KeyframeButton;
    private JButton ShotBoundryButton;
    private JPanel PanelInfo;
    private JLabel LbPath;
    private  JPanel panellist;
    private JButton playButton;
    private  JEditorPane ListEditor;
    private JScrollPane scrollpaneList;
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
    public VideoFrame() {

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

        ListEditor.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                try {
                    //System.out.println("row=" + getRow(e.getDot(), (JEditorPane) e.getSource()));
                    int start = e.getDot();
                    int end = e.getMark();
                    if (start> end) {
                        int temp = start;
                        start = end;
                        end = temp;
                    }
                    if (start != end) {
                        String textpan = ListEditor.getText(start, end - start);
                        LbPath.setText("Selected file:" + textpan);

                        //if(textpan.endsWith(".mp4")){
                            playButton.setEnabled(true);
                            mediaURL = textpan;
                        //}
                    }
                }
                catch (BadLocationException e1){
                    e1.printStackTrace();
                }
                //System.out.println("col="+getColumn(e.getDot(), (JTextComponent)e.getSource()));
            }
        });
//        ListEditor.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                //super.mouseClicked(e);
//                try {
//                    int start = Utilities.getRowStart(ListEditor, e.getYOnScreen() -1);
//                    int end = Utilities.getRowEnd(ListEditor, e.getYOnScreen()-1);
//                    String textpan= ListEditor.getText(start, end-start);
//                    LbPath.setText("New Path:"+textpan);
//                    System.out.print(textpan);
//                }
//                catch (Exception e1){
//                    System.out.println(e1);
//                }
//
//            }
//        });
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
                        ListEditor.setEnabled(true);
                        //ListEditor.setSelectionColor(new Color(1.0f, 1.0f, 1.0f,0.0f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playButton.setEnabled(false);
                    ShotBoundryButton.setEnabled(true);
                    KeyframeButton.setEnabled(true);
                }

            }

            LbPath.setText("File Path:"+mediaURL);



    }
    public void showVideo(String mediaURL){

        VideoPlayer videoPlayer = new VideoPlayer();
        videoPlayer.play(mediaURL);

    }
    public void Initialize(){
        ListEditor = new JEditorPane("text/html","No list is selected");
        ListEditor.setAutoscrolls(true);
        panellist.add(new JScrollPane(ListEditor), BorderLayout.CENTER);
        ListEditor.setEnabled(false);
        scrollpaneList.hide();
    }
    private static String readFile(String path)
            throws IOException
    {
        Scanner scanner = new Scanner( new File(path) );
        String text = scanner.useDelimiter("\r\n").next();
        scanner.close(); // Put this call in a finally block
        return text;
    }

    private static int getRow(int pos, JEditorPane editor) {
        int rn = (pos==0) ? 1 : 0;
        try {
            int offs=pos;
            while( offs>0) {
                offs=Utilities.getRowStart(editor, offs)-1;
                rn++;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return rn;
    }



    public static void main(String[] args){
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        JFrame frame = new JFrame("Video Processing");
        frame.setContentPane(new VideoFrame().panelVideo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,400));
        frame.pack();
        frame.setVisible(true);


    }
}

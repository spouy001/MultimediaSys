package Forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * Created by spouy001 on 7/25/17.
 */
public class MainForm extends JFrame {
    private JPanel MainPanel;
    private JButton preprocessButton;
    private JButton annotationsButton;
    private JButton classificationButton;
    private JButton retrievalButton;
    //private JLabel LB_info;
    private JLabel LB_icon;
    private JEditorPane editorPane1;
    private JPanel panel_Icon;
    private JButton featureButton;
    //private JTextArea textArea1;
    private static JMenuBar menuBar;
    public static JFrame frame;

    //private ImageIcon icon = createImage
    public MainForm(){
        initComponents();

        preprocessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame vframe = new JFrame("Video Processing");
                vframe.setContentPane(new VideoFrame().panelVideo);
                vframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                vframe.setPreferredSize(new Dimension(500,400));
                vframe.pack();
                vframe.setVisible(true);
                //new VideoFrame().setVisible(true);
            }
        });
        featureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame fframe = new JFrame("Feature Analaysis");
                fframe.setContentPane(new Features().panelFeatures);
                fframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fframe.setPreferredSize(new Dimension(600,400));
                fframe.pack();
                fframe.setVisible(true);
            }
        });
        classificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Classification");
                frame.setContentPane(new Classification().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(750,600));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    private void initComponents() {

        menuBar = new JMenuBar();
        JMenu Menu_File = new JMenu("Analysis");
        JMenu Menu_File_New = new JMenu("New");
        //ImageIcon icon = createImageIcon("images/middle.gif","this is a caption");
        Menu_File.add(Menu_File_New);
        JMenuItem Menu_File_New_Project = new JMenuItem(new AbstractAction("Preprocessing") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame vframe = new JFrame("Video Processing");
                vframe.setContentPane(new VideoFrame().panelVideo);
                vframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                vframe.setPreferredSize(new Dimension(500,400));
                vframe.pack();
                vframe.setVisible(true);
            }
        });
        JMenuItem Menu_File_New_Burrito = new JMenuItem(new AbstractAction("Feature Analysis") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame fframe = new JFrame("Feature Analaysis");
                fframe.setContentPane(new Features().panelFeatures);
                fframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                fframe.setPreferredSize(new Dimension(600,400));
                fframe.pack();
                fframe.setVisible(true);
            }
        });
        JMenuItem Menu_File_New_Cookie = new JMenuItem("Classification");
        JMenuItem Menu_File_New_Other = new JMenuItem("Retrieval");
        Menu_File_New.add(Menu_File_New_Project);
        Menu_File_New.add(Menu_File_New_Burrito);
        Menu_File_New.add(Menu_File_New_Cookie);
        Menu_File_New.add(Menu_File_New_Other);
        JMenuItem Menu_File_Open = new JMenuItem("Open...");
        JMenuItem Menu_File_Save = new JMenuItem("Save");
        JMenuItem Menu_File_SaveAs = new JMenuItem("Save As...");
        JMenuItem Menu_File_Reload= new JMenuItem("Reload");
        Menu_File.add(Menu_File_Open);
        Menu_File.add(Menu_File_Save);
        Menu_File.add(Menu_File_SaveAs);
        Menu_File.add(Menu_File_Reload);
        Menu_File.addSeparator();
        JMenuItem Menu_File_Exit = new JMenuItem("Exit");
        Menu_File.add(Menu_File_Exit);
        menuBar.add(Menu_File);
        JMenu Menu_Edit = new JMenu("Edit");
        JMenu Menu_Windows = new JMenu("Visualization");
        JMenu Menu_Help = new JMenu("Help");
        menuBar.add(Menu_Edit);
        menuBar.add(Menu_Windows);
        menuBar.add(Menu_Help);
        editorPane1.setText("Distributed Multimedia Information Systems Laboratory\n       School of Computing and Infomation Sciences\n             Florida International University");
        editorPane1.setSize(355,500);
        //editorPane1.setForeground(Color.gray);


    }
    public static void main(String[] args){


        frame = new JFrame("Multimedia System");
        frame.setContentPane(new MainForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(530,400));
        frame.pack();
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);



    }
    //Image Icon
    private void createUIComponents() {
        // TODO: place custom component creation code here
        ImageIcon myicon = new ImageIcon("img/multimedia-icon.jpg");
        Image img = myicon.getImage();
        Image newimg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon newicon = new ImageIcon(newimg);
        LB_icon = new JLabel(newicon);
        //LB_icon.setMaximumSize();
    }
}

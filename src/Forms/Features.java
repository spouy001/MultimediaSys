package Forms;

import de.lmu.dbs.jfeaturelib.features.ExtractCEDD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by spouy001 on 7/21/17.
 */
public class Features extends JFrame {
    public JPanel panelFeatures;
    private JCheckBox chBox_HOG;
    private JCheckBox chBox_Gabor;
    private JCheckBox chBox_Cedd;
    private JCheckBox chBox_color;
    private JCheckBox chBox_alex;
    private JCheckBox chBox_google;
    private JCheckBox chBox_VGG;
    private JCheckBox chBox_ResNet;
    private JButton extractFeaturesButton;
    private JProgressBar pb_cedd;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JTextField tx_cedd;
    private JTextField tx_HOG;
    private JTextField tx_gabor;
    private JTextField tx_color;
    private JCheckBox chBox_Other;
    private JTextField tx_other;
    private JLabel LB_cedd;
    private String imPath="/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/resized_4403_samira/";
    private String imList="image_list.csv";
    static final int MY_MINIMUM = 0;

    static final int MY_MAXIMUM = 100;
    //private Task task;

    public Features() {
        initComponents();
        extractFeaturesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtractCEDD features=new ExtractCEDD();
                if(chBox_Cedd.isSelected() && chBox_HOG.isSelected() && chBox_Gabor.isSelected() && chBox_color.isSelected() && chBox_Other.isSelected()){
                    List<double[]> Results = features.extractAll(imPath,imList);
                    tx_cedd.setText("Done");
                    tx_gabor.setText("Done");
                    tx_HOG.setText("Done");
                    tx_color.setText("Done");
                    tx_other.setText("Done");
                }
                else {
                    if (chBox_Cedd.isSelected()) {

                        //task = new Task();
                        //task.start();
                        //tx_cedd.setText("Extracting ...");
                        List<double[]> ceddResults = features.cedd(imPath, imList);
                        tx_cedd.setText("Done");
                    }
                    if (chBox_HOG.isSelected()) {
                        List<double[]> hogResults = features.hog(imPath, imList);
                        tx_HOG.setText("Done");
                    }
                    if (chBox_Gabor.isSelected()) {
                        List<double[]> gaborResults = features.gabor(imPath, imList);
                        tx_gabor.setText("Done");
                    }
                    if (chBox_color.isSelected()) {
                        List<double[]> colorResults = features.fcth(imPath, imList);
                        tx_color.setText("Done");

                    }
                    if (chBox_Other.isSelected()) {
                        //tamura and moment
                        List<double[]> otherResults = features.momentTamura(imPath, imList);
                        tx_other.setText("Done");

                    }
                }

            }
        });
    }
    private void initComponents() {
        //panel2 = new JPanel();
        pb_cedd = new JProgressBar(0,100);
        pb_cedd.setValue(0);
        pb_cedd.setStringPainted(true);
        add(pb_cedd);
        //pb_cedd.setStringPainted(true);
        //panel2.add(pb_cedd);
        //getContentPane().add(panel2,BorderLayout.SOUTH);

    }

    public void updateBar(JProgressBar pbar, int newValue) {
        pbar.setValue(newValue);
    }
    public static void main(String[] args){

        JFrame frame = new JFrame("Feature Analysis");
        frame.setContentPane(new Features().panelFeatures);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600,400));
        frame.pack();
        frame.setVisible(true);



    }
    /*private class Task extends Thread {
        public Task() {
        }

        public void run() {
            for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
                final int percent = i;
                try {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            pb_cedd.setValue(percent);
                        }
                    });
                    java.lang.Thread.sleep(100);
                } catch (InterruptedException e2) {
                    ;
                }
            }
        }
    }
*/
}


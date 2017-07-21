package Forms;

import javax.swing.*;

/**
 * Created by Samira Pouyanfar on 5/30/17.
 */
public class Features {
    private JPanel PanelFeatures;
    private JCheckBox HOGCheckBox;
    private JCheckBox CEDDCheckBox;
    private JCheckBox YCCBRCheckBox;
    private JCheckBox haarCheckBox;
    private JCheckBox HSVCheckBox;
    private JButton BtnExtFeat;
    private JCheckBox AlexNetCheckBox;
    private JCheckBox GoogleNetCheckBox;
    private JCheckBox VGGCheckBox;
    private JCheckBox ResNetCheckBox;

    public static void main(String[] args){
        JFrame frame = new JFrame("Feature Analysis");
        frame.setContentPane(new Features().PanelFeatures);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setPreferredSize(new Dimension(500,500));
        frame.pack();
        frame.setVisible(true);
    }
}

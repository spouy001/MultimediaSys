package Forms;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import process.Crossvalidation;
import process.RunClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.UnsupervisedFilter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.Filter;
import process.mca.*;


/**
 * Created by spouy001 on 7/25/17.
 */
public class Classification {

    private JPanel panel1;
    public JPanel mainPanel;
    private JRadioButton RBfolds;
    private JComboBox comboBoxClassifiers;
    private JRadioButton RBPercentage;
    private JButton runButton;
    private JButton openAFileButton;
    private JTextField txtPath;
    private JButton openARFFButton;
    private JTextField textFieldSummary;
    private JTextArea textAreaSummary;
    private JPanel jPanleSummary;
    public JEditorPane ListEditorSummary;
    private JPanel panellistSummary;
    private JScrollPane scrollpaneSummary;
    private JPanel panelListResults;
    private JEditorPane ListEditorResults;
    private JScrollPane scrollpaneResults;
    private JTextPane textPane1;
    private JEditorPane editorPane1;
    private JTextArea textArea1;
    private JTextField textFieldFolds;
    private JTextField textFieldPercentage;
    private JButton SaveARFFButton;
    private JPanel panelTest;
    private JTextPane textPaneSummary;
    private String featureURL;
    private Instances inputData;
    String inTrainFile="/Volumes/homes/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group16/train1.arff";
    String inTestFile="/Volumes/homes/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group16/test1.arff";
    //private String[] classifierStrings = { "MCA", "SVM", "J48", "RandomForest", "NaiveBayes", "MLP", "KNN", "AdaBoost", "Bagging"};


    public Classification() {
        Initialize();
        openAFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(new Classification().mainPanel);
                //Check if the user selects a file or not:
                if (result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
                    // user selects a csv file
                    featureURL = fileChooser.getSelectedFile().getAbsolutePath();
                    txtPath.setText(featureURL);
                    //textFieldSummary.setText(Summary(inTrainFile));
                    textFieldSummary.setText(Summary(featureURL));
                    //LbSummary.setText(Summary(featureURL));
                }
                else
                    JOptionPane.showMessageDialog(new JFrame(), "Could not open the csv file, Please check the path or format of the file", "Dialog", JOptionPane.ERROR_MESSAGE);
            }
        });
        openARFFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ListEditorSummary.setText(inputData.toString());
                    //editorPane1.setText(inputData.toString());
                    //textArea1.setText(inputData.toString());
                    System.out.println(inputData.toString());
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        comboBoxClassifiers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText("");
                int fold = 0;
                int percentage = 0;

                    if (comboBoxClassifiers.getSelectedItem() != null) {
                        String classifier = comboBoxClassifiers.getSelectedItem().toString();
                        if (RBfolds.isSelected()) {
                            try {
                                fold = Integer.parseInt(textFieldFolds.getText());
                                if (fold < 2 || fold > 20)
                                    JOptionPane.showMessageDialog(new JFrame(), "Wrong fold value (A number between 2 to 20)", "Dialog", JOptionPane.ERROR_MESSAGE);

                                else {
                                    Crossvalidation CV = new Crossvalidation(fold);
                                    try {
                                        CV.crossValidateModel(inputData, fold, new Random(100));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    for (int i = 0; i < fold; i++) {
                                        textArea1.append("*********** Results of Fold:" + (i + 1) + " *******************\n");
                                        runModel(CV.train[i], CV.test[i], classifier);

                                    }
                                }
                            } catch (NumberFormatException e1) {
                                JOptionPane.showMessageDialog(new JFrame(), "Wrong fold value (A number between 2 to 20)", "Dialog", JOptionPane.ERROR_MESSAGE);
                            }


                        } else if (RBPercentage.isSelected()) {
                            try {
                                percentage = Integer.parseInt(textFieldPercentage.getText());
                                if (percentage <= 0 || percentage > 100)
                                    JOptionPane.showMessageDialog(new JFrame(), "Wrong percentage (A number between 1 to 100)", "Dialog", JOptionPane.ERROR_MESSAGE);
                                else {
                                    Crossvalidation CV = new Crossvalidation(1);
                                    try {
                                        CV.SplitData(inputData, percentage, new Random(100));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

                                    runModel(CV.train[0], CV.test[0], classifier);
                                }
                            } catch (NumberFormatException e1) {
                                JOptionPane.showMessageDialog(new JFrame(), "Wrong percentage (A number between 1 to 100)", "Dialog", JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }
                else

                    JOptionPane.showMessageDialog(new JFrame(), "Please select a classifier", "Dialog", JOptionPane.ERROR_MESSAGE);


            }
        });
        RBPercentage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldPercentage.setEnabled(true);
                textFieldPercentage.setEditable(true);
                textFieldPercentage.setText("");
                textFieldFolds.setEditable(false);
                textFieldFolds.setEnabled(false);
                textFieldFolds.setText("number of folds");
                //textFieldPercentage.setEditable(true);

            }
        });




        RBfolds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldFolds.setEnabled(true);
                textFieldFolds.setEditable(true);
                textFieldFolds.setText("");
                textFieldPercentage.setEditable(false);
                textFieldPercentage.setEnabled(false);
                textFieldPercentage.setText("percentage (e.g. 80)");
            }
        });
        SaveARFFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArffSaver saver = new ArffSaver();
                saver.setInstances(inputData);
                try {
                    saver.setFile(new File("output/"+inputData.relationName()+".arff"));
                    //saver.setDestination(new File(arffFile)); //no need after WEKA version 3.5.3
                    saver.writeBatch();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }
    private String Summary(String fpath){
        int numInstance = 0;
        int numFeat = 0;
        int numClass = 0;
        try {
            // Read all the instances in the file (ARFF, CSV, XRFF, ...)
            CSVLoader loader = new CSVLoader();
            loader.setNoHeaderRowPresent(true);
            loader.setSource(new File(fpath));
            inputData = loader.getDataSet();



            // Make the last attribute be the class
            numInstance = inputData.numInstances();
            numFeat = inputData.numAttributes();
            inputData.setClassIndex(numFeat-1);

            inputData = changeToNominal(inputData,inputData.classIndex()+1);
            numFeat = inputData.numAttributes();
            numClass = inputData.numDistinctValues(numFeat-1);
            inputData = RemoveAtt(inputData,1);
            System.out.println("\nDataset:\n");
            System.out.println(inputData);

        } catch (Exception e){
            e.printStackTrace();
        }
        return "Number of Instances: "+Integer.toString(numInstance)+"   Number of Features:  "+Integer.toString(numFeat)+"   Number of Classes:  "+Integer.toString(numClass);
    }

    private Instances RemoveAtt(Instances ins, int index){

        String[] options = new String[2];
        options[0] = "-R";                                    // "range"
        options[1] = Integer.toString(index);                                     // first attribute
        try {
            Remove remove = new Remove();                         // new instance of filter
            remove.setOptions(options);                           // set options
            remove.setInputFormat(ins);                          // inform filter about dataset **AFTER** setting options
            Instances newIns = Filter.useFilter(ins, remove);
            return newIns;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return ins;
    }
    private Instances changeToNominal(Instances ins, int index){
        NumericToNominal convert= new NumericToNominal();
        String[] options= new String[2];
        options[0]="-R";
        options[1]=Integer.toString(index);;  //range of variables to make nominal

        try {
            convert.setOptions(options);
            convert.setInputFormat(ins);


            Instances newIns = Filter.useFilter(ins, convert);
            return newIns;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return ins;
    }
    public void Initialize(){
        ListEditorSummary = new JEditorPane("text/html","No file is added");
        ListEditorSummary.setAutoscrolls(true);
        panellistSummary.add(new JScrollPane(ListEditorSummary), BorderLayout.CENTER);
        ListEditorSummary.setEnabled(false);
        scrollpaneSummary.hide();

        //ListEditorResults = new JEditorPane("text/html","No results");
        //ListEditorResults.setAutoscrolls(true);
        //panelListResults.add(new JScrollPane(ListEditorResults), BorderLayout.CENTER);
        //ListEditorResults.setEnabled(false);
        //scrollpaneResults.hide();


        comboBoxClassifiers.addItem("MCA");
        comboBoxClassifiers.addItem("SVM");
        comboBoxClassifiers.addItem("J48");
        comboBoxClassifiers.addItem("RandomForest");
        comboBoxClassifiers.addItem("NaiveBayes");
        comboBoxClassifiers.addItem("MLP");
        comboBoxClassifiers.addItem("KNN");
        comboBoxClassifiers.addItem("AdaBoost");
        comboBoxClassifiers.addItem("Bagging");
        comboBoxClassifiers.addItem("Logistic");
        comboBoxClassifiers.addItem("BayesNet");
        comboBoxClassifiers.setSelectedItem(null);



        //editorPane1 = new JEditorPane("text/html","No file is added");
        //editorPane1.setAutoscrolls(true);
        //jpanelTest.add(new JScrollPane(editorPane1), BorderLayout.CENTER);
        //editorPane1.setEnabled(false);
        //scrollpaneSummary.hide();
        //textArea1 = new JTextArea("Hello");
        //textArea1.setAutoscrolls(true);
        //panelTest.add(new JScrollPane(textArea1), BorderLayout.CENTER);
        //textArea1.setEditable(true);
        //textArea1.setLineWrap(true);
        //textArea1.setVisible(true);
        //textPane1 = new JTextPane();
        //textPane1.setAutoscrolls(true);
        //jpanelTest.add(new JScrollPane(textPane1), BorderLayout.CENTER);
        //textPane1.setEnabled(true);


    }
    private String readFile()
            throws IOException
    {
        Scanner scanner = new Scanner(inputData.toString());
        String text = scanner.useDelimiter("\r\n").next();
        scanner.close(); // Put this call in a finally block
        return text;
    }

    private  void runModel(Instances inTrain, Instances inTest, String classifier){
        RunClassifier cls = new RunClassifier();
        String EvaluationRes= "No result";
        try {

            //Instances inTrain = ConverterUtils.DataSource.read(inTrainFile);
            //inTrain.setClassIndex(inTrain.numAttributes() - 1);
            //Instances inTest = ConverterUtils.DataSource.read(inTestFile);
            //inTest.setClassIndex(inTest.numAttributes() - 1);

            //cls.useJ48(inTrain, inTest, trainResultFile, testResultFile);
            //cls.useIG(inTrain);


            switch (classifier) {
                case "MCA": {
                    System.out.println("Run MCA");
                    EvaluationRes = cls.runMCA(inTrain, inTest, true);
                    break;
                }
                case ("SVM"): {
                    System.out.println("Run SVM");
                    EvaluationRes = cls.runSMO(inTrain, inTest);
                    break;
                }
                case "J48": {
                    System.out.println("Run J48");
                    EvaluationRes = cls.runJ48(inTrain, inTest);
                    break;
                }
                case "RandomForest": {
                    System.out.println("Run RandomForest");
                    EvaluationRes = cls.runRandomForest(inTrain, inTest);
                    break;
                }
                case "NaiveBayes": {
                    System.out.println("Run NaiveBayes");
                    EvaluationRes = cls.runNaiveBayes(inTrain, inTest);
                    break;
                }
                case "KNN": {
                    System.out.println("Run KNN");
                    EvaluationRes = cls.runKNN(inTrain, inTest,3);
                    break;
                }
                case "MLP": {
                    System.out.println("Run MLP");
                    EvaluationRes = cls.runMLP(inTrain, inTest);
                    break;
                }
                case "AdaBoost": {
                    System.out.println("Run AdaBoost");
                    EvaluationRes = cls.runAdaBoost(inTrain, inTest);
                    break;
                }
                case "Bagging": {
                    System.out.println("Run Bagging");
                    EvaluationRes = cls.runBagging(inTrain, inTest);
                    break;
                }
                case "Logistic": {
                    System.out.println("Run Logistic");
                    EvaluationRes = cls.runLogistic(inTrain, inTest);
                    break;
                }
                case "BayesNet": {
                    System.out.println("Run Logistic");
                    EvaluationRes = cls.runBayesNet(inTrain, inTest);
                    break;
                }
            }
        }catch (Exception e1) {
            e1.printStackTrace();
        }
        textArea1.append(EvaluationRes);
        //textArea1.setText(EvaluationRes);
        textArea1.setEnabled(true);
    }
    public static void main(String[] args){

        JFrame frame = new JFrame("Classification");
        frame.setContentPane(new Classification().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(750,600));
        frame.pack();
        frame.setVisible(true);


    }
}

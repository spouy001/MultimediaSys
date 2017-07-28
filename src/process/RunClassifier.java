

/**
 * Created by spouy001 on 7/27/17.
 */
package process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.BayesNet;
//import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.Logistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.j48.C45ModelSelection;
import weka.classifiers.trees.j48.C45PruneableClassifierTree;
import weka.classifiers.trees.j48.C45Split;
import weka.classifiers.trees.j48.ClassifierTree;
import weka.classifiers.trees.j48.ModelSelection;
import weka.classifiers.trees.j48.ClassifierSplitModel;
import weka.experiment.AveragingResultProducer;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;

import process.mca.*;

public class RunClassifier {

    private double [] attr=null;
    private double [] infoGain=null;
    private int [] [] depth;
    private int counter=1;
    public static void printProbability(FastVector prob, Instances data, String output){
        String strLine = null;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(output), false));

            for(int i=0; i<prob.size(); i++){
                double[] dist =  (double[]) prob.elementAt(i);
                strLine=dist[1]+","+(int)data.instance(i).classValue();

                bw.write(strLine);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String runNaiveBayes(Instances train, Instances test) {
        String res = "";
        try{

            Classifier naive = new NaiveBayes();
            naive.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(naive, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String runBayesNet(Instances train, Instances test) {
        String res = "";
        try{

            Classifier bayes = new BayesNet();
            bayes.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(bayes, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public String runLogistic(Instances train, Instances test) {
        String res = "";
        try{

            Classifier logistic = new Logistic();
            logistic.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(logistic, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public String runJ48(Instances train, Instances test) {
        String res = "";
        try{

            Classifier j48 = new J48();
            j48.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(j48, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    //samira
    public double average(double[] data) {
        double sum = 0;
        int count=1;
        for(int i=0; i < data.length; i++) {
            if(data[i]>0){
                sum = sum + data[i];
                count++;
            }
        }

        double average = sum / count;
        return average;

    }
    //Samira
    public int [][] GetTreeDepth(){
        return depth;
    }

    public String runAdaBoost(Instances train, Instances test) {
        String res = "";
        try{

            Classifier adaboost = new AdaBoostM1();
            adaboost.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(adaboost, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String runKNN(Instances train, Instances test, int k) {
        String res = "";
        try{

            Classifier knn = new IBk(k);
            knn.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(knn, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public String runRandomForest(Instances train, Instances test) {
        String res = "";
        try{

            Classifier rmf = new RandomForest();
            rmf.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(rmf, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public String runMLP(Instances train, Instances test) {
        String res = "";
        try{

            Classifier mlp = new MultilayerPerceptron();
            mlp.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(mlp, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public String runBagging(Instances train, Instances test) {
        String res = "";
        try{

            Classifier bag = new Bagging();
            bag.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(bag, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public void useSVM(Instances train, Instances test, String trainResultFile, String testResultFile, double gamma) {
        try {
//            LibSVM svm = new LibSVM();
//            svm.setGamma(gamma);
//            svm.setProbabilityEstimates(true);
//            svm.buildClassifier(train);
//
//            FastVector trainProb = new FastVector();
//            for(int i=0; i<train.numInstances(); i++){
//                double[] dist=svm.distributionForInstance(train.instance(i));
//                trainProb.addElement(dist);
//            }
//            printProbability(trainProb, train, trainResultFile);
//
//            FastVector testProb = new FastVector();
//            for(int i=0; i<test.numInstances(); i++){
//                double[] dist=svm.distributionForInstance(test.instance(i));
//                testProb.addElement(dist);
//            }
//            printProbability(testProb, test, testResultFile);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public String runSMO(Instances train, Instances test) {
        String res = "";
        try{

            Classifier smo = new SMO();
            smo.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(smo, test);
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            res = eval.toClassDetailsString();
            res += '\n'+ eval.toMatrixString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }


    //Written by Samira for deep ensemble learning
    //public void useEnsembleSMO(Instances train1, Instances test1, Instances train2, Instances test2, Instances train3, Instances test3, Instances train4, Instances test4, String trainResultFile, String testResultFile) {
    public void useEnsembleSMO(Instances train1, Instances test1, Instances train2, Instances test2, Instances train3, Instances test3, String trainResultFile, String testResultFile) {
        try {
            SMO smo1 = new SMO();
            SMO smo2 = new SMO();
            SMO smo3 = new SMO();
            SMO smo4 = new SMO();
            //smo.setGamma(gamma);
            //smo.setProbabilityEstimates(true);
            System.out.println("alexnet ...");
            smo1.buildClassifier(train1);
            System.out.println("Googlenet ...");
            smo2.buildClassifier(train2);
            System.out.println("Resnet ...");
            smo3.buildClassifier(train3);
            //smo4.buildClassifier(train4);

			/*FastVector trainProb1 = new FastVector();
			for(int i=0; i<train1.numInstances(); i++){
				double[] dist=smo1.distributionForInstance(train1.instance(i));
				trainProb1.addElement(dist);
			}
			printProbability(trainProb, train, trainResultFile);*/

            FastVector testProb1 = new FastVector();
            FastVector testProb2 = new FastVector();
            FastVector testProb3 = new FastVector();
            //FastVector testProb4 = new FastVector();
            double[] testProb=new double[test1.numInstances()];
            //double[] positive={1,0};
            //double[] negative={0,1};

            for(int i=0; i<test1.numInstances(); i++){
                double[] dist1=smo1.distributionForInstance(test1.instance(i));
                double[] dist2=smo2.distributionForInstance(test2.instance(i));
                double[] dist3=smo3.distributionForInstance(test3.instance(i));
                //double[] dist4=smo4.distributionForInstance(test4.instance(i));
                //for disaster
                //double weightedsumP=1.06*dist1[0]+1.06*dist2[0]+1.1*dist3[0]+dist4[0];
                //double weightedsumN=1.06*dist1[1]+1.06*dist2[1]+1.1*dist3[1]+dist4[1];
                //for trecvid

                //if(dist1[0]==1 || dist2[0]==1 || dist3[0]==1)
                //{
                //System.out.println(i);
                //}
                //double weightedsumP=1.2*dist1[0]+0.5*dist2[0]+0.5*dist3[0];
                double weightedsumP=dist1[0]+1.2*dist2[0]+1.06*dist3[0];
                //double weightedsumN=dist1[1]+1.06*dist2[1]+1.2*dist3[1];
                if(weightedsumP>1.1)
                {
                    testProb[i]=0;
                }
                else {
                    testProb[i]=1;
                }


                //	testProb1.addElement(dist1);
                //	testProb2.addElement(dist2);
                //testProb3.addElement(dist3);
                //testProb2.addElement(dist4);
            }

            //String outputTestRank= testResultFile+ "_ensemble.txt";
            //printProbability(testProb, test1, outputTestRank);

			/*String outputTestRank1= testResultFile+ "_model1.txt";
			String outputTestRank2= testResultFile +"_model2.txt";
			String outputTestRank3= testResultFile +"_model3.txt";
			String outputTestRank4= testResultFile +"_model4.txt";*/

            double[] GroundTruth=new double[test1.numInstances()];

            for (int i = 0; i < test1.numInstances(); i++) {
                GroundTruth[i] = test1.instance(i).classValue();
            }
            EvaluateResults(GroundTruth, testProb);


            //printProbability(testProb1, test1, outputTestRank1);
            //printProbability(testProb2, test2, outputTestRank2);
            //printProbability(testProb2, test3, outputTestRank3);
            //printProbability(testProb3, test4, outputTestRank4);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //for ensemble deep learning
    public String EvaluateResults(double[] truth, double[] predict) throws IOException {
        String Result;

        if(truth.length != predict.length)
            throw new IllegalArgumentException("Array length not equal!");

        double TP = 0, TN = 0, FP = 0, FN = 0;
        for(int i = 0; i < truth.length; i++) {
            if(truth[i] == 1 && predict[i] == 1) {
                TP++;
            } else if (truth[i] == 1 && predict[i] == 0) {
                FN++;
            } else if (truth[i] == 0 && predict[i] == 1) {
                FP++;
            } else if (truth[i] == 0 && predict[i] == 0) {
                TN++;
            }
        }

        //System.out.println("TP = " + TN);
        //System.out.println("FP = " + FN);
        //System.out.println("FN = " + FP);
        double precision = TN / (TN + FN);
        //System.out.println("Percision-p = " + precision);
        double recall = TN / (TN + FP);
        //System.out.println("recall-p = " + recall);
        double F1 = 2 * precision * recall / (precision + recall);
        //System.out.println("F1-p = " + F1);
        Result = "*******Result Summary of positive class******" + '\n';
        Result += "TP =" + TN + ", FP =" + FN + ", FN ="+ FP + '\n';
        Result += "Percision ="+ precision + "\n";
        Result += "Recall ="+ recall+ "\n";
        Result += "F1 ="+ F1+ "\n\n";
	/*	Writer writer2 = new BufferedWriter(new FileWriter("U:/mitch-b/dmis-research/Haiman/disaster_6concept_10fold_concept4/final_samira_withoutText_1to1500.csv",true));

		writer2.append(String.valueOf(precision));
		writer2.append(',');
		writer2.append(String.valueOf(recall));
		writer2.append(',');


		writer2.append(String.valueOf(F1));
		writer2.append('\n');
		writer2.flush();
		writer2.close(); */





        precision = TP / (TP + FP);
        recall = TP / (TP + FN);
        //System.out.println("TP = " + TP);
        //System.out.println("FP = " + FP);
        //System.out.println("FN = " + FN);
        //System.out.println("Percision-N = " + precision);
        //System.out.println("recall-N = " + recall);
        F1 = 2 * precision * recall / (precision + recall);
        //System.out.println("F1 = " + F1);
        //System.out.println("F1-p = " + F1);

        Result += "*****Result Summary of negative class*****" + '\n';
        Result += "TP =" + TP + ", FP =" + FP + ", FN ="+ FN + "\n";
        Result += "Percision ="+ precision + "\n";
        Result += "Recall ="+ recall+ "\n";
        Result += "F1 ="+ F1+ "\n";

        return Result;
    }

    public String runMCA(Instances inTrain, Instances inTest, boolean dis){
        String option = "-m";
        String EvaluateRes= "";
        try {
            if (dis) {
                /***********************************************************************************/
                System.out.print("step 1:  Discretize Training data  ...\n ");
                SupervisedDiscretize SD = new SupervisedDiscretize();
                SD.setInTrain(inTrain);
                SD.setInTest(inTest);
                System.out.println("\t # of attribute: " + SD.inTest.numAttributes());
                SD.doDiscretize(true);
                //if(trainDisFile != null && testDisFile != null){
                //  SupervisedDiscretize.save(SD.outTrain, trainDisFile);
                // SupervisedDiscretize.save(SD.outTest,  testDisFile);
                //}
                inTrain = SD.outTrain;
                inTest = SD.outTest;

                /***********************************************************************************/
                System.out.println("step 2:  Check attributes of Training data  ...");
                CheckAttribute CA = new CheckAttribute();
                CA.doCheck(inTrain);
                if (CA.numToDelete > 0) {
                    //						String trainfileNew=outputFolder+"train_"+i+"_"+j+"_new.arff";
                    //						String testfileNew=outputFolder+"test_"+i+"_"+j+"_new.arff";
                    //						String markerFile=outputFolder+"marker_"+i+"_"+j+".txt";
                    CA.removeAttribute(inTrain);
                    CA.removeAttribute(inTest);
                    //if(trainDisFile != null && testDisFile != null){
                    //  CheckAttribute.save(inTrain, trainDisFile);
                    //CheckAttribute.save(inTest, testDisFile);
                    //}
                    //CA.printAttsToDelete(markerFile);
                }
            }

            /***********************************************************************************/
            System.out.println("step 3:  Run MCA on Training data  ...");
            MCA_Indv_MAIN_2 mca = new MCA_Indv_MAIN_2();
            mca.setData(inTrain);
            mca.generateMCAweight();
            //mca.printMCAweight(outputWeight);

            /***********************************************************************************/
            System.out.println("step 4:  Run MCA ranking  ...");
            TransWeight TW = new TransWeight();
            double[][] MCAscoresTrain = TW.calculateTransactionWeight(inTrain, mca.getFeaturePair2weight());
            double[][] MCAscoresTest = TW.calculateTransactionWeight(inTest, mca.getFeaturePair2weight());
            //TW.printTransactionWeight(inTrain, MCAscoresTrain, outputTrainRank);
            //TW.printTransactionWeight(inTest, MCAscoresTest, outputTestRank);

//					Instances dataNewTrain = TW.buildMCAarff(inTrain, mca.getFeaturePair2weight());
//					Instances dataNewTest = TW.buildMCAarff(inTest, mca.getFeaturePair2weight());
//					RunClassifier classifier = new RunClassifier();
//					classifier.useAdaBoost(dataNewTrain, dataNewTest, trainRankFile, testRankFile);
            /*************************************************************************************/
            System.out.println("step 5:  classify results....");
            double bestTH = TW.genTH(inTrain, MCAscoresTrain, mca.getFeaturePair2weight());
            System.out.println("bestTH = " + bestTH);
            double[] TestArray1 = new double[MCAscoresTest.length];
            for (int row = 0; row < MCAscoresTest.length; row++) {
                TestArray1[row] = MCAscoresTest[row][1];
                // TestArray2[row] = MCAscoresTest[row][2];
            }
            double[] Test_predict = TW.classifyInstance(TestArray1, bestTH);
            int dataSize = inTest.numInstances();
            double[][] GroundTruth = new double[dataSize][2];

            for (int i = 0; i < dataSize; i++) {
                GroundTruth[i][0] = MCAscoresTest[i][1];
                GroundTruth[i][1] = inTest.instance(i).classValue();
            }
            EvaluateRes = TW.EvaluateResults(GroundTruth, Test_predict);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(EvaluateRes);
        return EvaluateRes;

    }

    public static void main(String[] args) {

//		   String inTrainFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_train_1.arff";
//		   String inTestFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_test_1.arff";
//		   String trainResultFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_train_1_rank.txt";
//		   String testResultFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_test_1_rank.txt";
//
//		   try {
//				RunClassifier cls = new RunClassifier();
//				Instances inTrain = DataSource.read(inTrainFile);
//				inTrain.setClassIndex(inTrain.numAttributes()-1);
//				Instances inTest = DataSource.read(inTestFile);
//				inTest.setClassIndex(inTest.numAttributes()-1);
//				cls.useSVM(inTrain, inTest, trainResultFile, testResultFile, 0);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

//		   String inTrainFile="C:/Yimin/research/arff/subject_1_train_3.arff";
//		   String inTestFile="C:/Yimin/research/arff/subject_1_test_3.arff";
//		   String trainResultFile="C:/Yimin/research/arff/data_1_train_3_rank.txt";
//		   String testResultFile="C:/Yimin/research/arff/data_1_test_3_rank.txt";

        String inTrainFile="/Volumes/spouy001/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group16/train1.arff";
        String inTestFile="/Volumes/spouy001/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group16/test1.arff";
        String trainResultFile="/Volumes/spouy001/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group6/data_1_train_2_rank.txt";
        String testResultFile="/Volumes/spouy001/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group6/data_1_test_2_rank.txt";

        try {
            RunClassifier cls = new RunClassifier();
            Instances inTrain = DataSource.read(inTrainFile);
            inTrain.setClassIndex(inTrain.numAttributes()-1);
            Instances inTest = DataSource.read(inTestFile);
            inTest.setClassIndex(inTest.numAttributes()-1);
            cls.runRandomForest(inTrain,inTest);
            //cls.useJ48(inTrain, inTest, trainResultFile, testResultFile);
            //cls.useIG(inTrain);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


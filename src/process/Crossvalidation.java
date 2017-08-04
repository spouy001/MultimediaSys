package process;

/**
 * Created by spouy001 on 7/27/17.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;

//import weka.classifiers.functions.LibSVM;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.Random;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation.*;





public class Crossvalidation {
    public Instances [] train;
    public Instances [] test;

    public Crossvalidation(int i) {
            train = new Instances[i];
            test = new Instances[i];
    }
    /**
     * Performs a (stratified if class is nominal) cross-validation
     * for a classifier on a set of instances. Now performs
     * a deep copy of the classifier before each call to
     * buildClassifier() (just in case the classifier is not
     * initialized properly).
     * @param data the data on which the cross-validation is to be
     * performed
     * @param numFolds the number of folds for the cross-validation
     * @param random random number generator for randomization
     * expected to hold a weka.classifiers.evaluation.output.prediction.AbstractOutput
     * object
     * @throws Exception if a classifier could not be generated
     * successfully or the class is not defined
     */

    public void crossValidateModel(Instances data, int numFolds, Random random)
            throws Exception {

        // Make a copy of the data we can reorder
        data = new Instances(data);
//        data.setClassIndex(data.numAttributes()-1);
        data.getClass();
        data.randomize(random);
        if (data.classAttribute().isNominal()) {
            data.stratify(numFolds);
        }

        // We assume that the first element is a
        // weka.classifiers.evaluation.output.prediction.AbstractOutput object
        /*AbstractOutput classificationOutput = null;
        if (forPredictionsPrinting.length > 0) {
            // print the header first
            classificationOutput = (AbstractOutput) forPredictionsPrinting[0];
            classificationOutput.setHeader(data);
            classificationOutput.printHeader();
        }*/

        // Do the folds
        for (int i = 0; i < numFolds; i++) {
            train[i] = data.trainCV(numFolds, i, random);
            //setPriors(train);
            //Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier);
            //copiedClassifier.buildClassifier(train);
            test[i] = data.testCV(numFolds, i);
            //evaluateModel(copiedClassifier, test, forPredictionsPrinting);
        }
        //m_NumFolds = numFolds;

//        if (classificationOutput != null)
//            classificationOutput.printFooter();
    }
    public void SplitData(Instances data, int percentage, Random random)
            throws Exception {

        // Make a copy of the data we can reorder
        data = new Instances(data);
        data.getClass();
        data.randomize(random);
        //if (data.classAttribute().isNominal()) {
        //    data.stratify(1);
       // }


        // Do the percentage split
        int trainSize = data.numInstances() * percentage / 100;
        int testSize = data.numInstances() - trainSize;
        Instances trainTemp  = new Instances(data, 0, trainSize);
        Instances testTemp =  new Instances(data, trainSize, testSize);
        train[0] = (Instances) trainTemp;
        test[0] = (Instances) testTemp;
    }
    public static void main(String[] args){
        String inTrainFile="/Volumes/spouy001/mitch-a/dmis-research/Samira/soccer_goal-detection/minchen_features/group16/train1.arff";


        try {

            Instances inTrain = DataSource.read(inTrainFile);
            inTrain.setClassIndex(inTrain.numAttributes()-1);
            Crossvalidation CV= new Crossvalidation(3);
            CV.crossValidateModel(inTrain, 3, new Random(100));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

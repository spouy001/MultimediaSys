package process;

/**
 * Created by spouy001 on 7/28/17.
 */
import java.io.*;

import weka.core.*;
import weka.filters.Filter;
//import weka.filters.supervised.attribute.Discretize;
import weka.filters.supervised.attribute.Discretize;

/**
 * Shows how to generate compatible train/test sets using the Discretize
 * filter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SupervisedDiscretize {
    Instances     inTrain;
    Instances     inTest;
    Instances     outTrain;
    Instances     outTest;
    Discretize  filter = new Discretize();

    /**
     * loads the given ARFF file and sets the class attribute as the last
     * attribute.
     *
     * @param filename    the file to load
     * @throws Exception  if somethings goes wrong
     */
    protected static Instances load(String filename) throws Exception {
        Instances       result;
        BufferedReader  reader;

        reader = new BufferedReader(new FileReader(filename));
        result = new Instances(reader);
        result.setClassIndex(result.numAttributes() - 1);
        reader.close();

        return result;
    }

    /**
     * saves the data to the specified file
     *
     * @param data        the data to save to a file
     * @param filename    the file to save the data to
     * @throws Exception  if something goes wrong
     */
    protected static void save(Instances data, String filename) throws Exception {
        BufferedWriter  writer;

        writer = new BufferedWriter(new FileWriter(filename));
        writer.write(data.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }

    public void renameAttribute(Instances data)
    {
        for (int i = 0; i < data.numAttributes()-1; i++) {
            Attribute att = data.attribute(i);
            for (int n = 0; n < att.numValues(); n++) {
                data.renameAttributeValue(att, att.value(n), ((i+1)+"_"+n));
            }
        }
    }

    /**set batch to true if need to discretize both training and testing data**/
    public void doDiscretize(boolean batch){
        try {
            //inTrain.setClassIndex(inTrain.numAttributes()-1);
            filter.setInputFormat(inTrain);
            outTrain = Filter.useFilter(inTrain, filter);
            renameAttribute(outTrain);

            if(batch){
                outTest  = Filter.useFilter(inTest,  filter);
                renameAttribute(outTest);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Instances doDiscretize(Instances anotherTest){
        try {
            //inTrain.setClassIndex(inTrain.numAttributes()-1);
            filter.setInputFormat(inTrain);
            outTrain = Filter.useFilter(inTrain, filter);
            outTest  = Filter.useFilter(inTest,  filter);
            anotherTest  = Filter.useFilter(anotherTest,  filter);
            renameAttribute(outTrain);
            renameAttribute(outTest);
            renameAttribute(anotherTest);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return anotherTest;
    }


    public static void main(String[] args) throws Exception {

        String trainFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_train_1.arff";
        String testFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_test_1.arff";
        String trainDisFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_train_1_dis.arff";
        String testDisFile="C:/research/progress/Discretization/CM/data/extend/run1/random/data_1_test_1_dis.arff";

        SupervisedDiscretize DA=new SupervisedDiscretize();
        DA.inTrain = load(trainFile);
        DA.inTest  = load(testFile);
        DA.doDiscretize(true);
        save(DA.outTrain, trainDisFile);
        save(DA.outTest,  testDisFile);

    }


    public Instances getInTrain() {
        return inTrain;
    }

    public void setInTrain(Instances inTrain) {
        this.inTrain = inTrain;
    }

    public Instances getInTest() {
        return inTest;
    }

    public void setInTest(Instances inTest) {
        this.inTest = inTest;
    }

}


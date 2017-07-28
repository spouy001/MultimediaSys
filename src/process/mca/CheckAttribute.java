package process.mca;

/**
 * Created by spouy001 on 7/28/17.
 */

import weka.core.AttributeStats;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;

public class CheckAttribute {
    /**
     * takes an ARFF file as first argument, the number of indices to remove
     * as second and thirdly whether to invert or not (true/false).
     * Dumps the generated data to stdout.
     */
    Instances     inTrain;
    Instances     inTest;
    int [] attsToDelete = null;//mark attribute and remove those with value
    ArrayList<Integer> disCount = new ArrayList<Integer>();	   //int [] attsToDelete={0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    public int numToDelete=0;//count how many features need to remove

    protected static Instances load(String filename) throws Exception {
        Instances       result;
        BufferedReader  reader;

        reader = new BufferedReader(new FileReader(filename));
        result = new Instances(reader);
        result.setClassIndex(result.numAttributes() - 1);
        reader.close();

        return result;
    }

    protected static void save(Instances data, String filename) throws Exception {
        BufferedWriter  writer;

        writer = new BufferedWriter(new FileWriter(filename));
        writer.write(data.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }

    public void doCheck(Instances data){
        int i;
        //System.out.println(inst.numAttributes());
        attsToDelete = new int [data.numAttributes()-1];
        numToDelete = 0;
        for(i=0; i<attsToDelete.length; i++){
            AttributeStats stats = data.attributeStats(i);
            if (stats.distinctCount < 2) {
                attsToDelete[i]=1; //remove attribute set to 1
                numToDelete++;
            }else{
                attsToDelete[i]=0; //keep attribute set to 0
            }
            //System.out.println(marker[i]);
        }
    }

    //samira
    public ArrayList<Integer> doCheck2(Instances data){
        int i;
        //System.out.println(inst.numAttributes());
        attsToDelete = new int [data.numAttributes()-1];
        numToDelete = 0;
        for(i=0; i<attsToDelete.length; i++){
            AttributeStats stats = data.attributeStats(i);
            if (stats.distinctCount < 2) {
                attsToDelete[i]=1; //remove attribute set to 1
                numToDelete++;
            }else{
                attsToDelete[i]=0; //keep attribute set to 0
                disCount.add(stats.distinctCount);
            }
            //samira

        }

        return disCount;
        //System.out.println(marker[i]);
    }






    public void printAttsToDelete(String filename){
        int i;
        PrintWriter out;
        try {
            out = new PrintWriter(filename);
            for(i=0; i<attsToDelete.length; i++){
                if(attsToDelete[i]==1){
                    out.println(i+1);
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void removeAttribute(Instances data){
        int i;
        int count=0;
        for(i=0; i<attsToDelete.length; i++){
            if(attsToDelete[i]==1){
                data.deleteAttributeAt(i-count);
                count++;
            }
        }
    }
    //samira test
    public  void removeAttribute2(Instances data){
        int i;
        int count=0;
        for(i=0; i<attsToDelete.length; i++){
            if(attsToDelete[i]==1){
                data.deleteAttributeAt(i-count);
                count++;
            }
        }

    }
    public static void main(String[] args) throws Exception {
        String trainfile="C:/Users/Qiusha/Desktop/data/train/train (1).arff";
        String trainfileNew="C:/Users/Qiusha/Desktop/data/train/train (1)_new.arff";
        String testfile="C:/Users/Qiusha/Desktop/data/test/test (1).arff";
        String testfileNew="C:/Users/Qiusha/Desktop/data/test/test (1)_new.arff";
        String markerFile="C:/Users/Qiusha/Desktop/data/train/1.txt";

        CheckAttribute CA = new CheckAttribute();
        CA.inTrain=load(trainfile);
        CA.inTest=load(testfile);
        CA.doCheck(CA.inTrain);
        if(CA.numToDelete>0){
            CA.removeAttribute(CA.inTrain);
            save(CA.inTrain, trainfileNew);
            CA.removeAttribute(CA.inTest);
            save(CA.inTest, testfileNew);
            CA.printAttsToDelete(markerFile);
        }
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

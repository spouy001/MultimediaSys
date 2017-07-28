package process.mca;

/**
 * Created by spouy001 on 7/28/17.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class TransWeight {

    /*
     * build arff via replacing feature value pair with its cos value
     * for binary class, cos value can take from the positive class
     * for muli-class cos value can take from the weight average of multiple classes
     */
    public Instances buildMCAarff(Instances data, Hashtable<String, double[]> featurePair2weight){
        int numClasses = data.numClasses();
        int numInstances = data.numInstances();
        int numAttributes= data.numAttributes();
        //System.out.println(numClasses+","+numInstances+","+numAttributes);

        //add feature attribute
        FastVector fvAttr = new FastVector(numAttributes);
        for (int j=0; j<numAttributes-1; j++){
            Attribute featureAttr = new Attribute("f"+(j+1));
            fvAttr.addElement(featureAttr);
        }

        //add class attribute
        FastVector fvClass = new FastVector(numClasses);
        for (int k=0; k<numClasses; k++){
            fvClass.addElement(data.classAttribute().value(k));
        }
        Attribute classAttr = new Attribute("class", fvClass);
        fvAttr.addElement(classAttr);

        Instances dataNew = new Instances("MyRelation", fvAttr, 0);
        dataNew.setClassIndex(numAttributes-1);

        //load data
        Instance inst=null, instNew=null;
        String fVpair=null;
        double[] impactWeight=new double[numClasses];
        double val=0;
        for(int i=0; i<numInstances; i++){
            inst=data.instance(i);
            instNew=new DenseInstance(numAttributes);
            instNew.setDataset(dataNew);
            for(int j=0; j<numAttributes-1; j++){
                fVpair=inst.stringValue(j);
                impactWeight=featurePair2weight.get(fVpair);
                val=0;
                for(int k=0;k<numClasses;k++){
                    val += Math.abs(impactWeight[k]);
                }
                instNew.setValue(j, val/numClasses);
            }

            instNew.setValue(numAttributes-1, inst.classValue());

            dataNew.add(instNew);
        }

        return dataNew;
    }


    /*
     * calculate transaction weight w/o any feature or feature value pair selection
     */
    public double[][] calculateTransactionWeight(Instances data, Hashtable<String, double[]> featurePair2weight) {
        int numClasses=data.numClasses();
        int numAttributes=data.numAttributes();
        int numInstances=data.numInstances();
        double[][] MCAscores=new double[numInstances][numClasses];
        Instance currInst=null;
        String fVpair=null;
        double[] impactWeight=new double[numClasses];
        for(int i=0;i<numInstances;i++){
            currInst=data.instance(i);
            for(int j=0;j<numAttributes-1;j++){

                fVpair=currInst.stringValue(j);
                impactWeight=featurePair2weight.get(fVpair);
                //System.out.println(colInd);
                for(int k=0;k<numClasses;k++){
                    if(!Double.isNaN((impactWeight[k]))){
                        /****************use cos square*****************************************/
                        if(impactWeight[k]>=0){
                            MCAscores[i][k]+=impactWeight[k]*impactWeight[k];
                        }else{
                            MCAscores[i][k]-=impactWeight[k]*impactWeight[k];
                        }

                        /****************use cos***********************************************/
                        //MCAscores[rowInd][colInd]+=impactWeight[colInd];
                    }
                    else{
                        System.out.println("wrong values of the weight");
                    }
                }
            }

            for(int k=0;k<numClasses;k++){

                MCAscores[i][k]=1.0*MCAscores[i][k]/(numAttributes-1);
                //samira
                //MCAscores[i][k]=MCAscores[i][k]/(numAttributes-1)*0.1*data.instance(i).weight();
            }

        }
        return MCAscores;
    }

    /*calculate transaction weight based on selected feature value pairs*/
    public double[][] calculateTransactionWeightTH(Instances data, Hashtable<String, double[]> featurePair2weight, double[] TH) {
        int numClasses=data.numClasses();
        int numAttributes=data.numAttributes();
        int numInstances=data.numInstances();
        double[][] MCAscores=new double[numInstances][numClasses];
        Instance currInst=null;
        String fVpair=null;
        double[] impactWeight=new double[numClasses];
        for(int i=0;i<numInstances;i++){
            currInst=data.instance(i);
            for(int j=0;j<numAttributes-1;j++){

                fVpair=currInst.stringValue(j);
                impactWeight=featurePair2weight.get(fVpair);
                //System.out.println(colInd);
                for(int k=0;k<numClasses;k++){
                    if(!Double.isNaN((impactWeight[k]))){
                        /****************use cos square*****************************************/
                        if(impactWeight[k]>=TH[k]){
                            MCAscores[i][k]+=impactWeight[k]*impactWeight[k];
                        }else if (impactWeight[k]<=TH[k]){
                            MCAscores[i][k]-=impactWeight[k]*impactWeight[k];
                        }else{
                            ;
                        }
                        /****************use cos***********************************************/
                        //MCAscores[rowInd][colInd]+=impactWeight[colInd];
                    }
                    else{
                        System.out.println("wrong values of the weight");
                    }
                }
            }

            for(int k=0;k<numClasses;k++){

                MCAscores[i][k]=1.0*MCAscores[i][k]/(numAttributes-1);
            }

        }
        return MCAscores;
    }

    public void printTransactionWeight(Instances data, double[][] MCAscores, String output){
        try {
            PrintWriter pw = new PrintWriter(output);
            //DecimalFormat df = new DecimalFormat("#.#####");
            int numClasses=data.numClasses();
            int numInstances=data.numInstances();
            //System.out.println(numInstances);

            for(int i=0; i<numInstances; i++){
                for(int j=0; j<numClasses; j++){
                    //pw.print(df.format(MCAscores[i][j])+",");
                    pw.print(MCAscores[i][j]+",");
                }
//				System.out.println(data.instance(i).toString());
//				System.out.println(data.instance(i).numValues());
//				System.out.println(data.instance(i).numAttributes());
//				System.out.println(data.instance(i).numClasses());
//				int classIndex = data.instance(i).numAttributes();
//
                Attribute class_attr = data.classAttribute();
                String attr_value = data.instance(i).stringValue(class_attr);
                pw.print(attr_value + "\n");
//				System.out.println("attr_value = " + attr_value);
//
//				for(int k = 0; k < classIndex; k++) {
//					System.out.println(data.instance(i).value(k));
//				}
//
//				System.out.println("data.instance(" + i + ").classValue() = " + data.instance(i).classValue());

//				pw.print(data.instance(i).classValue()+"\n");
                pw.flush();
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*evaluate the best threshold for selecting feature value pairs based on xinfAP*/
    public double[] evalTH(Instances data, Hashtable<String, double[]> featurePair2weight){
        int numClasses = data.numClasses();
        double[] TH = new double[numClasses];
        double[] bestTH = new double[numClasses];
        double[][] MCAscores;
        double xinfMetric=0, maxXinfMetric=0;
        for(double i=0; i<1.0; i=i+0.1){
            for(double j=0; j<1.0; j=j+0.1){
                TH[0]=i;//threshold of the negative class
                TH[1]=j;//threshold of the positive class
                MCAscores = calculateTransactionWeightTH(data, featurePair2weight, TH);
                xinfMetric = calculateXinfMetric(data, MCAscores);
                if(xinfMetric > maxXinfMetric){
                    maxXinfMetric = xinfMetric;
                    bestTH[0] = TH[0];
                    bestTH[1] = TH[1];
                    System.out.println("TH[0]: "+bestTH[0]+" TH[1]: "+bestTH[1]+" maxXinfAP: "+maxXinfMetric);
                }
            }
        }

        return bestTH;
    }

    /*calculate extended inferred average precision*/
    public double calculateXinfMetric(Instances data, double[][] MCAscores){
        //double infAP = 0, AP10, AP100, AP1000, AP2000;
        int dataSize = data.numInstances();
//		int numClasses = data.numClasses();
//		int classIndex = data.classIndex();
        //final int class2Sort = 1;
        //rank only handle two class situation
        double[][] rank = new double[dataSize][2];

        for(int i=0; i<dataSize; i++){
            rank[i][0]=(MCAscores[i][1]-MCAscores[i][0]+1)*0.5;
            rank[i][1]=data.instance(i).classValue();
        }

        Arrays.sort(rank, new Comparator<double[]>() {
            public int compare(double[] d1, double[] d2) {
                //add - to sort in descending order
                //return -Double.compare(d1[class2Sort], d2[class2Sort]);
                return -Double.compare(d1[0], d2[0]);
            }
        });

        double[] metric10 = calculateAPatK(rank, 10);
        double[] metric100 = calculateAPatK(rank, 100);
        double[] metric1000 = calculateAPatK(rank, 1000);
        double[] metric2000 = calculateAPatK(rank, 2000);

        double xinfRel = 0.25*metric10[0]+0.25*metric100[0]+0.25*metric1000[0]+0.25*metric2000[0];
        double xinfAP = 0.25*metric10[1]+0.25*metric100[1]+0.25*metric1000[1]+0.25*metric2000[1];
        double xinfMetric =xinfRel * xinfAP;
        //System.out.println("rel: "+xinfRel+" ap: "+xinfAP+" metric: "+xinfMetric);
        return xinfMetric;
    }

    /*calculate average precision at position K*/
    public double[] calculateAPatK(double[][] rank, int K){
        double AP=0, P = 0;
        List<Double> listP = new ArrayList<Double>();
        int labelIndex = rank[0].length;
        int pos=0;
        //metric[0] is the number of relevant/positive instance @position K
        //metric[1] is the average precision @position K
        double[] metric = new double[2];
        for(int i=0; i<K; i++){
            if(rank[i][labelIndex-1]==1){
                pos++;
                P=pos*1.0/(i+1);
                listP.add(P);
            }
        }

        int listPSize = listP.size();
        if(listPSize==0){
            metric[0]=0;
            metric[1]=0;
        }else{
            for(int i=0; i<listPSize; i++){
                AP = AP + listP.get(i);
            }
            metric[0]=pos*1.0/K;
            metric[1]=AP/listPSize;
        }

        return metric;
    }


    public static void saveARFF(String arffFile, Instances data){
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        try {
            saver.setFile(new File(arffFile));
            //saver.setDestination(new File(arffFile)); //no need after WEKA version 3.5.3
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void classification(Instances TrainData, Instances TestData){
        // do CV on trainData and select a TH to apply to TestData

    }
    //samira Code // classify test data
    public double[] classifyInstance(double[] transactionWeight, double bestTH) throws Exception {
        double [] class_labels=new double[transactionWeight.length];
        for (int i=0; i < transactionWeight.length; i++) {
            if(transactionWeight[i] > bestTH) {
                class_labels[i]=1;
            } else {
                class_labels[i]=0;
            }

        }
        return class_labels;


    }



    //Yimin Code// calculate TH
    public double genTH(Instances data, double[][] MCAscores, Hashtable<String, double[]> featurePair2weight) {
        //double[][] MCAscores = calculateTransactionWeight(data, featurePair2weight);
        double TH = 0;
        int dataSize = data.numInstances();
        double[][] rank = new double[dataSize][2];

        for (int i = 0; i < dataSize; i++) {
            //rank[i][0] = (MCAscores[i][1] - MCAscores[i][0] + 1) * 0.5;
            rank[i][0] = MCAscores[i][1];
            rank[i][1] = data.instance(i).classValue();
        }

        Arrays.sort(rank, new Comparator<double[]>() {
            public int compare(double[] d1, double[] d2) {
                // add - to sort in descending order
                return Double.compare(d2[0], d1[0]);
            }
        });

        double F1 = 0;
        for(int i = 0; i < dataSize; i++) {
            double[] predict = new double[dataSize];
            Arrays.fill(predict, 0, i, 1);
            double curF1 = calculateF1(rank, predict);
            if(curF1 >= F1) {
                F1 = curF1;
                TH = rank[i][0];
            }
        }
        return TH;
    }
    //Yimin Code -calculate F1
    public double calculateF1(double[][] truth, double[] predict) {
        if(truth.length != predict.length)
            throw new IllegalArgumentException("Array length not equal!");

        double TP = 0, TN = 0, FP = 0, FN = 0;

        for(int i = 0; i < truth.length; i++) {
            if(truth[i][1] == 1 && predict[i] == 1) {
                TP++;
            } else if (truth[i][1] == 1 && predict[i] == 0) {
                FN++;
            } else if (truth[i][1] == 0 && predict[i] == 1) {
                FP++;
            } else if (truth[i][1] == 0 && predict[i] == 0) {
                TN++;
            }
        }
        double precision = TN / (TN + FN);
        double recall = TN / (TN + FP);
        double F1 = 2 * precision * recall / (precision + recall);
        return F1;
    }
    // Samira Code- calculate precision recall F1 (negative and positive are reverse)
    public String EvaluateResults(double[][] truth, double[] predict) throws IOException {
        String Result;
        if(truth.length != predict.length)
            throw new IllegalArgumentException("Array length not equal!");

        double TP = 0, TN = 0, FP = 0, FN = 0;
        for(int i = 0; i < truth.length; i++) {
            if(truth[i][1] == 1 && predict[i] == 1) {
                TP++;
            } else if (truth[i][1] == 1 && predict[i] == 0) {
                FN++;
            } else if (truth[i][1] == 0 && predict[i] == 1) {
                FP++;
            } else if (truth[i][1] == 0 && predict[i] == 0) {
                TN++;
            }
        }
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
/*	public static void writeCSV(int[] Test_label,String CSVname) throws Exception {

	    //create a File class object and give the file the name employees.csv
	    java.io.File courseCSV = new java.io.File(CSVname);

	    //Create a Printwriter text output stream and link it to the CSV File
	    java.io.PrintWriter outfile = new java.io.PrintWriter(courseCSV);

	    //Iterate the elements actually being used
	    for (int i=0; i < Test_label.length ; i++) {
	        outfile.write(Test_label[i].toCSVString());

	    }//end for

	    outfile.close();
	} //end writeCSV()
*/
    //Samira code

    public Instances Weight2Instance(Instances data, double[][] scores) {
        // TODO Auto-generated method stub
        int numClasses=data.numClasses();
        int numInstances=data.numInstances();
        //System.out.println(numInstances);

        // Declare two nominal attribute
//		FastVector fvNominalVal = new FastVector(numInstances);
//		Attribute Attribute1 = new Attribute("att1", fvNominalVal);
//		FastVector fvNominalVal2 = new FastVector(numInstances);
//		Attribute Attribute2 = new Attribute("att2", fvNominalVal2);

        Attribute Attribute1 = new Attribute("firstNumeric");
        Attribute Attribute2 = new Attribute("secondNumeric");
        // Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement("1");
        fvClassVal.addElement("0");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector(3);
        fvWekaAttributes.addElement(Attribute1);
        fvWekaAttributes.addElement(Attribute2);
        fvWekaAttributes.addElement(ClassAttribute);

        // Create an empty training set
        Instances weight=new Instances("Weights",fvWekaAttributes,1000000);
        // Set class index
        weight.setClassIndex(2);

        Attribute class_attr = data.classAttribute();
        // Create the instance
        for(int i=0; i<numInstances; i++){


            Instance iExample = new DenseInstance(3);
            iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), scores[i][0]);
            iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), scores[i][1]);
            iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), data.instance(i).stringValue(class_attr));

            weight.add(iExample);

//			System.out.println(data.instance(i).toString());
//			System.out.println(data.instance(i).numValues());
//			System.out.println(data.instance(i).numAttributes());
//			System.out.println(data.instance(i).numClasses());
//			int classIndex = data.instance(i).numAttributes();
//
            //
            //String attr_value = data.instance(i).stringValue(class_attr);
            //pw.print(attr_value + "\n");
        }

        return weight;
    }



    //Samira code
    public Instances AddWeights(Instances data, double[][] scores)
    {

        int numClasses=data.numClasses();
        int numInstances=data.numInstances();
        Instances newData=new Instances(data);
	/*Add filter;
	newData = new Instances(data);
	filter = new Add();
	filter.setAttributeIndex("last");
	filter.setAttributeName("weight1");
	filter.setInputFormat(newData);
	newData = Filter.useFilter(newData, filter);
	filter = new Add();
    filter.setAttributeIndex("last");
    filter.setAttributeName("weight2");
    filter.setInputFormat(newData);
    newData = Filter.useFilter(newData, filter);*/

        newData.insertAttributeAt(new Attribute("weight1"), newData.numAttributes()-1);
        newData.insertAttributeAt(new Attribute("weight2"), newData.numAttributes()-1);

        for(int i=0; i<numInstances; i++){

            newData.instance(i).setValue(newData.numAttributes() - 3, scores[i][0]);
            newData.instance(i).setValue(newData.numAttributes() - 2, scores[i][1]);

        }



        return newData;


    }
}

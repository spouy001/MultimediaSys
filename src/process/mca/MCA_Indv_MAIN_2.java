package process.mca;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.math.*;

import weka.core.Attribute;
import weka.core.BinarySparseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.DenseInstance;

public class MCA_Indv_MAIN_2 {
	Instances data; int dataSize=0; int attributeSize=0;	
	int classNum=0; static int cnt=0;
	Hashtable<String, double[]> featurePair2weight;
	Hashtable<Integer, String[]> index2featurePair;
	
	  public void loadArffData(String file){
		  try{
		  	data = new Instances(
	              new BufferedReader(
	              new FileReader(file)));
		  	init();
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	  public void init(){
		  	dataSize=data.numInstances();
		  	attributeSize=data.numAttributes();
		  	data.setClassIndex(attributeSize-1);
		  	classNum=data.numClasses();
	  }
	  //Samira add depth as input
/*	 public void generateMCAweight(double [] depth){
		  
		  //Samira calculate IF based on J48 infoGain
		  double [] IF=new double[attributeSize-1];
		 //soccer dataset
		  // double [] IF={1,0.66,0.33,0.8};
		 *//* double temp=17D/(double)(attributeSize-1);
			for (int i=0; i<IF.length; i++)
				if(depth[i]==1)
					IF[i]=(double)temp/0.09;
				else
					IF[i]=(double)temp/(Math.log(depth[i]));
		*//*
		  
		  //disaster dataset
		  for (int i=0; i<IF.length; i++)
			  IF[i]=1;
		 // double [] IF={1,0.66,0.33,0.8};
		  double temp=3D/(double)(attributeSize-1);
			for (int i=0; i<IF.length; i++)
			{
					if(depth[i]==1)
							IF[i]=1+((double)temp/0.09);
						else
							IF[i]=1+((double)temp/(Math.log(depth[i])));
						
			}
				
		  
		  
			featurePair2weight=new Hashtable<String, double[]>();
			index2featurePair=new Hashtable<Integer, String[]>();
			
			MCA_Indv_2 mca=new MCA_Indv_2();	
			setAttributeSize(data.numAttributes());
			for(int attrInd=0;attrInd<attributeSize-1;attrInd++){
				//for(int attrInd=0;attrInd<3;attrInd++){	
				Instances builtData=build(attrInd);
				mca.setData(builtData);
				mca.init();
				mca.MCA();
				double[][] final_MCA_weight=mca.getFinal_MCA_weight();
				//samira 
			for (int i=0; i<final_MCA_weight.length; i++)
				for (int j=1; j<final_MCA_weight[0].length; j++)
					final_MCA_weight[i][j]=final_MCA_weight[i][j]*(IF[attrInd]);
				
				
				
				
				String[] fVpairs=new String[final_MCA_weight.length];
						
				for(int i=0;i<final_MCA_weight.length;i++){//row
					String fVpair = mca.getIndex2feature().get(new Integer(i));
					fVpairs[i] = fVpair;
					double[] curr_fVpair2weight=final_MCA_weight[i];
					featurePair2weight.put(fVpair, curr_fVpair2weight);		
				}
				index2featurePair.put(attrInd, fVpairs);
			}		
		}
	  
	  //samira
	  public void generateMCAweight2(ArrayList<Integer> distcount, double w){
		  //w=1.5;
			featurePair2weight=new Hashtable<String, double[]>();
			index2featurePair=new Hashtable<Integer, String[]>();
			int disSize=distcount.size();
			MCA_Indv_2 mca=new MCA_Indv_2();	
			setAttributeSize(data.numAttributes());
			for(int attrInd=0;attrInd<attributeSize-1;attrInd++){
				//for(int attrInd=0;attrInd<3;attrInd++){	
				Instances builtData=build(attrInd);
				mca.setData(builtData);
				mca.init();
				mca.MCA();
				double[][] final_MCA_weight=mca.getFinal_MCA_weight();
				String[] fVpairs=new String[final_MCA_weight.length];
						
				for(int i=0;i<final_MCA_weight.length;i++){//row
					//samira
						for (int j=1; j<final_MCA_weight[0].length; j++)
							final_MCA_weight[i][j]=final_MCA_weight[i][j];
					
					String fVpair = mca.getIndex2feature().get(new Integer(i));
					fVpairs[i] = fVpair;
					double[] curr_fVpair2weight=final_MCA_weight[i];
					featurePair2weight.put(fVpair, curr_fVpair2weight);		
				}
				index2featurePair.put(attrInd, fVpairs);
			}		
		}*/
	public void generateMCAweight(){
		featurePair2weight=new Hashtable<String, double[]>();
		index2featurePair=new Hashtable<Integer, String[]>();
		
		MCA_Indv_2 mca=new MCA_Indv_2();	
		setAttributeSize(data.numAttributes());
		for(int attrInd=0;attrInd<attributeSize-1;attrInd++){
			//for(int attrInd=0;attrInd<3;attrInd++){	
			Instances builtData=build(attrInd);
			mca.setData(builtData);
			mca.init();
			mca.MCA();
			double[][] final_MCA_weight=mca.getFinal_MCA_weight();
			String[] fVpairs=new String[final_MCA_weight.length];
					
			for(int i=0;i<final_MCA_weight.length;i++){//row
				String fVpair = mca.getIndex2feature().get(new Integer(i));
				fVpairs[i] = fVpair;
				double[] curr_fVpair2weight=final_MCA_weight[i];
				featurePair2weight.put(fVpair, curr_fVpair2weight);		
			}
			index2featurePair.put(attrInd, fVpairs);
		}		
	}
	
	public void printMCAweight(String output){		
		try {

			PrintWriter pw = new PrintWriter(output);	
			DecimalFormat df = new DecimalFormat("#.#####");
			for(int attrInd=0;attrInd<attributeSize-1;attrInd++){
				for(int i=0; i<index2featurePair.get(attrInd).length; i++){

					String fVpair = index2featurePair.get(attrInd)[i];
					//int delim=fVpair.indexOf("_");
					//String ind=fVpair.substring(0,delim);
					//pw.print(ind+",");
					pw.print((attrInd+1)+",");
					pw.print(fVpair);

					for(int j=0; j<featurePair2weight.get(fVpair).length; j++){
						pw.print(","+df.format(featurePair2weight.get(fVpair)[j]));
					}
					pw.print("\n");	
				}
				pw.flush();
			}
			pw.close();
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		    e.printStackTrace();
	   	}
	}
		
	
	private Instances build(int attrInd) {
		// TODO Auto-generated method stub
		FastVector atts=new FastVector();
		Instances newInst = new Instances("MyRelation", atts, 0);
		Attribute attr=data.attribute(attrInd);
		Attribute classAttr=data.attribute(attributeSize-1);
		newInst.insertAttributeAt(attr,0);
		newInst.insertAttributeAt(classAttr,1);
		newInst.setClassIndex(1);
		//load data
		for(int instIndex=0;instIndex<dataSize;instIndex++){
			Instance a=data.instance(instIndex);
			Instance inst=new DenseInstance(2);
//			Instance inst = new BinarySparseInstance(2);	
			inst.setDataset(newInst);
			inst.setValue(0,a.stringValue(attrInd));
//			System.out.println(a.classValue());
			inst.setValue(1,a.classValue());
			newInst.add(inst);
		}
		return newInst;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MCA_Indv_MAIN_2 mca_main=new MCA_Indv_MAIN_2();
		String arffFile="U:/FDOIStorage/storage26/chao/MCA_data/iris_dis.arff";
		//String arffFile=args[0];//"U:/FDOIStorage/storage26/chao/MCA_data/iris_dis.arff";
		String output="U:/FDOIStorage/storage26/chao/MCA_data/iris_dis.txt";
		//String output=args[1];//"U:/FDOIStorage/storage26/chao/MCA_data/weight_iris_dis.txt";
		//1. read in arff files	
		mca_main.loadArffData(arffFile);
		MCA_Indv_2 mca=new MCA_Indv_2();
		mca_main.printMCAweight(output);

	}	
	
	public Instances getData() {
		return data;
	}
	public void setData(Instances data) {
		this.data = data;
		init();
	}
	public int getAttributeSize() {
		return attributeSize;
	}
	public void setAttributeSize(int attributeSize) {
		this.attributeSize = attributeSize;
	}
	public Hashtable<String, double[]> getFeaturePair2weight() {
		return featurePair2weight;
	}
	public void setFeaturePair2weight(Hashtable<String, double[]> featurePair2weight) {
		this.featurePair2weight = featurePair2weight;
	}
	public Hashtable<Integer, String[]> getIndex2featurePair() {
		return index2featurePair;
	}

}

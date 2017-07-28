package process.mca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import Jama.Matrix;
import Jama.SingularValueDecomposition;


import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MCA {
	Instances data; int dataSize=0; int attributeSize=0;
	
	int classNum=0;Hashtable<String, Integer> feature_value;
	Hashtable<Integer, String> index2feature;
	int feature_value_size;
	/**
	 * Function: This function is built to load the data in the input arff file
	 * @param file: input arff file
	 * @author Chao
	 */
	  public void loadArffData(String file){
		  try{
		  	data = new Instances(
	              new BufferedReader(
	              new FileReader(file)));
		  	dataSize=data.numInstances();
		  	attributeSize=data.numAttributes();
		  	data.setClassIndex(attributeSize-1);
		  	classNum=data.numClasses();
		  }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MCA mca=new MCA();
		//String arffFile=args[0];//
		String arffFile="U:/FDOIStorage/storage26/chao/MCA_data/iris_dis_attr_1.arff";
		String output="U:/FDOIStorage/storage26/chao/MCA_data/MCA_Java_angle";
		//String output=args[1];//"U:/FDOIStorage/storage26/chao/MCA_data/angle.txt";
		//String outputA=args[2];
		//String arffFile="U:/FDOIStorage/storage26/chao/Flickr/data/train_dis_combine/Train_1_3_dis.arff";
		//String output="U:/FDOIStorage/storage26/chao/Flickr/merged_data/angle_combine/angle_Train_x.txt";
		//String arffFile="U:/FDOIStorage/storage26/chao/NUS_Scene/merged_data/Train_arff_dis_combine/Train_32_33_merge_dis.arff";
		//String outputA="U:/FDOIStorage/storage26/chao/NUS_Scene/merged_data/Matrix/SVD_32_33.txt";
		//String output="U:/FDOIStorage/storage26/chao/NUS_Scene/merged_data/angle_combine/angle_Train_32_33.txt";
		//1. read in arff files
		mca.loadArffData(arffFile);		
		
		/* Generate input data info
			System.out.println(mca.classNum); 
			System.out.println(mca.dataSize);
			System.out.println(mca.attributeSize);
			
		*/
		System.out.println((new File(arffFile).getName())+" loaded");
		/*
		 * 2. Extract feature-value pair from header and store all   
		 *  key-value pairs into a HashTable the key-value pair can be
		 *  extracted from the header
		 */
	
		mca.getFeatureValuePair();
		System.out.println("Feature-value pair loaded");
		  /* 
		 	System.out.println(mca.feature_value.size());
			Integer n = mca.feature_value.get("0");
		   if (n != null) {
		     System.out.println("0 = " + n);
		   }
		   Integer n1 = mca.feature_value.get("1");
		   if (n1 != null) {
		     System.out.println("1 = " + n1);
		   }
		   Integer n2 = mca.feature_value.get("2");
		   if (n2 != null) {
		     System.out.println("2 = " + n2);
		   }
		  
		   Integer n3 = feature_value.get("3");
		   if (n != null) {
		     System.out.println("3 = " + n3);
		   }
		 */
			
		/*
		 * 3. build an indicator matrix and burt matrix from arff data part
		 * Use Jama to do matrix operation  
		 */
		// 3.1 create Indicator matrix
		double [][] Ind_array=new double[mca.dataSize][mca.feature_value_size];
		String attrValue="";Integer matchInd=new Integer(0);
		//go through every instance, every attribute
		for(int instInd=0;instInd<mca.dataSize;instInd++){
			Instance currInst=mca.data.instance(instInd);
			for(int attrInd=0;attrInd<mca.attributeSize-1;attrInd++){
				attrValue=currInst.stringValue(attrInd);
				matchInd = mca.feature_value.get(attrValue);
				Ind_array[instInd][matchInd]=1.0;
				//System.out.println(matchInd.intValue());
			}
			Attribute class_attr=mca.data.classAttribute();
			String attr_value=currInst.stringValue(class_attr);
			
			matchInd = mca.feature_value.get(attr_value);
			Ind_array[instInd][matchInd]=1.0;
		} 
		Matrix I = new Matrix(Ind_array);
		Matrix IT= I.transpose();
		System.out.println("Indicator matrix created ");
		// 3.2 create Burt matrix
		Matrix B = IT.times(I);		
		int rowD=B.getColumnDimension();
		int colD=B.getRowDimension();		
		System.out.println("Burt matrix created size ["+rowD+","+colD+"]");
		//NumberFormat formatter = new DecimalFormat("0");		
		//B.print(formatter, colD);
		Matrix VB= new Matrix(rowD,1); // column total
		double g=0;double colV;// double[][] rowV=new double[colD][1];
		//colV get summation of columns rowV get summation of rows
		for(int rowInd=0;rowInd<rowD;rowInd++){
			colV=0;
			for(int colInd=0;colInd<colD;colInd++){
				//rowV[colInd][0]+=B.get(rowInd, colInd);
				g+=B.get(rowInd, colInd);
				colV+=B.get(rowInd, colInd); //column total of B
			}
			VB.set(rowInd, 0, colV);			
		}
		
		// 3.3 Create Correspondence matrix
		//get probability matrix Gamma
		Matrix Gamma=B.times(1/g);
		//System.out.println(g);
		System.out.println("Correspondence matrix created");
		//Matrix R=new Matrix(rowV).times(1/g);
		Matrix V=VB.times(1/g);  //column total of Gamma
		Matrix VT=V.transpose();
		//VB.print(colD,1);
		//Note: For MCA, row mass equals to column mass
		//NumberFormat formatter = new DecimalFormat("00");		
		//System.out.println(R.get(0,0));
		//V.print(colD, 1);
		//R.print(colD,1);
		//Derive diagonal matrix D from v
		
		// make column total a diagonal matrix
			Matrix D_n1_2=new Matrix(colD,colD);
			for(int colInd=0;colInd<colD;colInd++){
				D_n1_2.set(colInd, colInd, 1/Math.sqrt(V.get(colInd, 0)));
			}
			NumberFormat formatter = new DecimalFormat("0.00");
			//D_n1_2.print(colD,1);
			Matrix D_n1_2T=D_n1_2.transpose();
			//This is residual matrix
			Matrix A=D_n1_2.times(Gamma.minus(V.times(VT))).times(D_n1_2T);
			//A.print(colD, colD);
		/*
		 * 4. perform MCA and derive cosine value of angle between feature-value
		 * pairs and classes
		 */
			/*
			PrintWriter pw;
			try {
				pw = new PrintWriter(new File(outputA));			
				A.print(pw, colD, colD);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
			*/
			System.out.print("Start MCA ... ");
			SingularValueDecomposition s = A.svd();
			//SingularValueDecomposition s=new SingularValueDecomposition(A);
			//SingularValueDecomposition svd=new SingularValueDecomposition(A);
			System.out.println("Done ");
			double[] eigenvalue=s.getSingularValues();
			Matrix PC=s.getV();
			System.out.print("Get PC ");
			Matrix XY=A.times(PC);
			
			System.out.println("Done ");
			colD=XY.getColumnDimension();
			//get cosine angel between feature value pairs and classes
			Matrix attrXY=XY.getMatrix(0,colD-mca.classNum-1,0,colD-1);
			//Matrix attrXY=XY.getMatrix(0,colD-mca.classNum-1,0,1);//colD-1);
			
			//System.out.println(attrXY.getColumnDimension());
			//System.out.println(attrXY.getRowDimension());
			Matrix classXY=XY.getMatrix(colD-mca.classNum,colD-1,0,colD-1);
			//Matrix classXY=XY.getMatrix(colD-mca.classNum,colD-1,0,1);//colD-1);
			//System.out.println(classXY.getColumnDimension());
			//System.out.println(classXY.getRowDimension());
			//L-2 normalization
			Matrix Attr_columnTotal=mca.getColumnTotal(attrXY);
			Matrix class_columnTotal=mca.getColumnTotal(classXY);
			//Attr_columnTotal.print(formatter,1);
			Matrix classXYT=classXY.transpose();	
			
			Matrix MCA_weight=attrXY.times(classXYT);
			int rowDim=MCA_weight.getRowDimension();
			int colDim=MCA_weight.getColumnDimension();
			double[][] final_MCA_weight=new double[rowDim][colDim];
			for(int i=0;i<rowDim;i++){
				for(int j=0;j<colDim;j++){
					final_MCA_weight[i][j]=1.0*MCA_weight.get(i,j)/(Attr_columnTotal.get(i,0)*class_columnTotal.get(j,0));
				}				
			}
			Matrix final_MCA=new Matrix(final_MCA_weight);
			
			
			final_MCA.print(formatter,colDim);
		/*
		 * 5. Output cosine value inforamtion
		 */			
			System.out.println("Output score to file");
			try {
				PrintWriter out= new PrintWriter(new FileWriter(output));
				for(int rind=0;rind<rowDim;rind++){
					out.print(rind+1+",");
					String fVpair = mca.index2feature.get(new Integer(rind));
					out.print(fVpair);
					for(int cind=0;cind<colDim;cind++){
						//out.print(","+180/Math.PI*Math.acos(final_MCA.get(rind, cind)));		
						out.print(","+final_MCA.get(rind, cind));		
					}
					out.print("\n");
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

	}

	private Matrix getColumnTotal(Matrix classXY) {
		// TODO Auto-generated method stub
		int rowDim=classXY.getRowDimension();
		int colDim=classXY.getColumnDimension();
		double[][] columnTotal=new double[rowDim][1];
		for(int i=0;i<rowDim;i++){
			for(int j=0;j<colDim;j++){
				columnTotal[i][0]+=classXY.get(i,j)*classXY.get(i,j);
			}
			columnTotal[i][0]=Math.sqrt(columnTotal[i][0]);
		}		
		return new Matrix(columnTotal);
	}

	private void getFeatureValuePair() {
		// TODO Auto-generated method stub
		feature_value = new Hashtable<String, Integer>();
		index2feature = new Hashtable<Integer, String>();
		feature_value_size=0;
		// Add regular attribute value
		for(int i=0;i<attributeSize-1;i++){
			Attribute attr=data.attribute(i);
			for(int j=0;j<attr.numValues();j++){
				String attr_value=attr.value(j);
				feature_value.put(attr_value, new Integer(feature_value_size));
				index2feature.put(new Integer(feature_value_size),attr_value);
				feature_value_size++;
				//System.out.println(attr_value);
			}
		}	
		// Add class attribute value
		
		Attribute class_attr=data.classAttribute();
		for(int j=0;j<class_attr.numValues();j++){
			String attr_value=class_attr.value(j);
			feature_value.put(attr_value, new Integer(feature_value_size));			
			index2feature.put(new Integer(feature_value_size),attr_value);
			feature_value_size++;
			//System.out.println(attr_value);
		}		
		
	}

}

package process.mca;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class MCA_Indv_3 {
	Instances data; int dataSize=0; int attributeSize=0;
	
	int classNum=0;Hashtable<String, Integer> feature_value;
	Hashtable<Integer, String> index2feature;
	int feature_value_size;
	double[][] final_MCA_weight;	
	double[][] final_tanimoto_weight;
	/**
	 * Function: This function is built to load the data in the input arff file
	 * @param file: input arff file
	 * @author Chao
	 */
	/*
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
	*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MCA_Indv_3 mca=new MCA_Indv_3();
		/*
		//String arffFile="U:/FDOIStorage/storage26/chao/MCA_data/iris_dis.arff";
		String arffFile="U:/FDOIStorage/storage26/chao/MCA_data/iris_dis.arff";
		//String outputA="U:/FDOIStorage/storage26/chao/NUS_Scene/merged_data/Matrix/SVD_32_33.txt";
		//String output="U:/FDOIStorage/storage26/chao/MCA_data/weight_iris_dis.txt";
		//1. read in arff files		
		//mca.loadArffData(arffFile);
		int numAttr=mca.data.numAttributes(); //number of attributes including class attribute		
		FastVector atts=new FastVector();
		
		
		//for(int attrIndex=0;attrIndex<mca.attributeSize;attrIndex++){
		for(int attrIndex=0;attrIndex<2;attrIndex++){
			Instances newInst = new Instances("MyRelation", atts, 0);
			Attribute attr=mca.data.attribute(attrIndex);
			Attribute classAttr=mca.data.attribute(numAttr-1);
			newInst.insertAttributeAt(attr,0);
			newInst.insertAttributeAt(classAttr,1);
			newInst.setClassIndex(1);
			//load data
			for(int instIndex=0;instIndex<mca.dataSize;instIndex++){
				Instance a=mca.data.instance(instIndex);
				Instance inst=new Instance(2);		
				inst.setDataset(newInst);
				inst.setValue(0,a.stringValue(attrIndex));
				inst.setValue(1,a.classValue());
				newInst.add(inst);
			}
			mca.data=newInst;
			mca.attributeSize=newInst.numAttributes();
			mca.MCA(newInst);
			double[][] finalweight=mca.final_MCA_weight;
			System.out.println("Print2Files");
			*/
			/*
			ArffSaver saver = new ArffSaver();
		    saver.setInstances(newInst);
		    try {
				saver.setFile(new File("U:/FDOIStorage/storage26/chao/NUS_Scene/merged_data/Matrix/arff_1.arff"));			
				//saver.setDestination(new File(args[1]));
				saver.writeBatch();
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		//}
	}
	
	public void MCA(String option){
		//String arffFile=args[0];//"U:/FDOIStorage/storage26/chao/Flickr/data/train_dis_combine";
		//String output=args[1];//"U:/FDOIStorage/storage26/chao/MCA_data/angle.txt";
		//String outputA=args[2];
		//String arffFile="U:/FDOIStorage/storage26/chao/Flickr/data/train_dis_combine/Train_1_3_dis.arff";
		//String output="U:/FDOIStorage/storage26/chao/Flickr/merged_data/angle_combine/angle_Train_x.txt";
	
		
		/* Generate input data info
			System.out.println(mca.classNum); 
			System.out.println(mca.dataSize);
			System.out.println(mca.attributeSize);
			
		*/
		//System.out.println((new File(arffFile).getName())+" loaded");
		/*
		 * 2. Extract feature-value pair from header and store all   
		 *  key-value pairs into a HashTable the key-value pair can be
		 *  extracted from the header
		 */
	
		getFeatureValuePair();
		//System.out.println("Feature-value pair loaded");
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
		double [][] Ind_array=new double[dataSize][feature_value_size];
		String attrValue="";Integer matchInd=new Integer(0);
		//go through every instance, every attribute
		for(int instInd=0;instInd<dataSize;instInd++){
//			if(instInd == 1121)
//				System.out.println("I'm here for testing 1");
			Instance currInst=data.instance(instInd);
			for(int attrInd=0;attrInd<attributeSize-1;attrInd++){
				attrValue=currInst.stringValue(attrInd);
				matchInd = feature_value.get(attrValue);
				Ind_array[instInd][matchInd]=1.0;
				//System.out.println(matchInd.intValue());
			}
			Attribute class_attr=data.classAttribute();
			String attr_value=currInst.stringValue(class_attr);
			
			matchInd =feature_value.get(attr_value);
			Ind_array[instInd][matchInd]=1.0;
		} 
		Matrix I = new Matrix(Ind_array);
		Matrix IT= I.transpose();
		//System.out.println("Indicator matrix created ");
		
		//System.out.print("Start outputing matrix ... ");
		//System.out.println("A matrix created size ["+rowD+","+colD+"]");
		/*
		try {
			PrintWriter pw = new PrintWriter(new File("U:/FDOIStorage/storage26/chao/NUS_Scene/merged_data/Matrix/MCA_data_attr_1.txt"));	
			for(int j=0;j<Ind_array[0].length;j++){
				pw.print(classNum+" ");
			}
			pw.print("\n");
			for(int i=0;i<Ind_array.length;i++){
				for(int j=0;j<Ind_array[0].length;j++){
					pw.print(Ind_array[i][j]+" ");
				}
				pw.print("\n");
			}
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/

		if(option.equalsIgnoreCase("-m")){
			// 3.2 create Burt matrix
			Matrix B = IT.times(I);		
			int rowD=B.getColumnDimension();
			int colD=B.getRowDimension();		
			//System.out.println("Burt matrix created size ["+rowD+","+colD+"]");
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
			//System.out.println("Correspondence matrix created");
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
//					double tmp = V.get(colInd, 0);
//					if(tmp == 0.0){
//						D_n1_2.set(colInd, colInd, 0);					
//					} else{
//						D_n1_2.set(colInd, colInd, 1/Math.sqrt(tmp));
//					}			
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
				//System.out.print("Start MCA ... ");
				SingularValueDecomposition s = A.svd();
				//SingularValueDecomposition s=new SingularValueDecomposition(A);
				//SingularValueDecomposition svd=new SingularValueDecomposition(A);
				//System.out.println("Done ");
				double[] eigenvalue=s.getSingularValues();
				
				int numPC = 0;
				double[] accSum = new double[eigenvalue.length];
				double sum = 0;
				for(int i = 0; i < eigenvalue.length; i++) {
					sum += eigenvalue[i];
					accSum[i] = sum;			
				}
				double TH_ENERGY = 0.95;  // the threshold of energy for selecting PC
				for(int i = 0; i < eigenvalue.length; i++) {
					if ((accSum[i] / accSum[eigenvalue.length-1]) < TH_ENERGY) {
						numPC++;
					}	
				}				

				if(numPC < 2) 
					numPC = 2; // keep as least first two PCs
				
				Matrix PC=s.getV();
				//System.out.print("Get PC ");
				Matrix XY=A.times(PC);				
				colD=XY.getColumnDimension();			
				

				//*********************************** calculate mca weight for each pair of selected PCs
				List<double[][]> mca_pair_weight = new ArrayList<double[][]>();
				int totalPair = numPC*(numPC-1)/2;
				double[] mca_pair_energy = new double[totalPair];
				double sumEner = 0;
				int count = 0;
				for (int i = 0 ; i < numPC-1; i++) {
					for (int j = i+1; j < numPC; j++) {
						Matrix attrXY = XY.getMatrix(0,colD-classNum-1, new int[]{i,j});
						Matrix classXY = XY.getMatrix(colD-classNum,colD-1, new int[]{i,j});						
						//L-2 normalization
						Matrix Attr_columnTotal = getColumnTotal(attrXY);
						Matrix class_columnTotal = getColumnTotal(classXY);				
						Matrix classXYT = classXY.transpose();	
						
						Matrix MCA_weight = attrXY.times(classXYT);
						int rowDim = MCA_weight.getRowDimension();
						int colDim = MCA_weight.getColumnDimension();
						double[][] pair_weight = new double[rowDim][colDim];	
						
						for(int m = 0; m < rowDim; m++){
							for(int n = 0; n < colDim; n++){
								pair_weight[m][n]=1.0*MCA_weight.get(m,n)/(Attr_columnTotal.get(m,0)*class_columnTotal.get(n,0)-MCA_weight.get(m,n)*0.5);
							}				
						}
						mca_pair_weight.add(pair_weight);
						mca_pair_energy[count] = eigenvalue[i] * eigenvalue[j];
						sumEner += mca_pair_energy[count];
						count++;
					}					
				}
				
				final_MCA_weight = new double[colD-classNum][classNum];
				// linear combination of each pair of PCs to obtain the final mca weight
				for(int i = 0; i < totalPair; i++) {
					double[][] pair_weight = mca_pair_weight.get(i);
					double weight = mca_pair_energy[i] / sumEner;
					
					for(int m = 0; m < pair_weight.length; m++){
						for(int n = 0; n < pair_weight[0].length; n++){
							final_MCA_weight[m][n] = final_MCA_weight[m][n] + pair_weight[m][n] * weight;
						}
					}
				}
				
				
//				//********************** original MCA
//				/*
//				 * keep all the principal components
//				 * Matrix attrXY=XY.getMatrix(0,colD-classNum-1,0,colD-1);
//				 * Matrix classXY=XY.getMatrix(colD-classNum,colD-1,0,colD-1);
//				 */
//				
//				//get cosine angel between feature value pairs and classes
//				/* keep only the first two major principal components */
////				Matrix attrXY=XY.getMatrix(0,colD-classNum-1,0,1);
////				Matrix classXY=XY.getMatrix(colD-classNum,colD-1,0,1);
//				Matrix attrXY=XY.getMatrix(0,colD-classNum-1, new int[]{0,1});
//				Matrix classXY=XY.getMatrix(colD-classNum,colD-1, new int[]{0,1});
//
//				//L-2 normalization
//				Matrix Attr_columnTotal=getColumnTotal(attrXY);
//				Matrix class_columnTotal=getColumnTotal(classXY);
//				//Attr_columnTotal.print(formatter,1);
//				Matrix classXYT=classXY.transpose();	
//				
//				Matrix MCA_weight=attrXY.times(classXYT);
//				int rowDim=MCA_weight.getRowDimension();
//				int colDim=MCA_weight.getColumnDimension();
//				final_MCA_weight=new double[rowDim][colDim];
//				for(int i=0;i<rowDim;i++){
//					for(int j=0;j<colDim;j++){
////						//calculate the cosine angle value of each feature value pairs
////						final_MCA_weight[i][j]=1.0*MCA_weight.get(i,j)/(Attr_columnTotal.get(i,0)*class_columnTotal.get(j,0));
//						
//						// calculate weight based on Tanimoto coefficients
////						System.out.println("i = " + i);
////						System.out.println("j = " + j);
////						System.out.println("MCA_weight.get(i,j) = " + MCA_weight.get(i,j));
////						System.out.println("Attr_columnTotal.get(i,0) = " + Attr_columnTotal.get(i,0));
////						System.out.println("class_columnTotal.get(j,0) = " + class_columnTotal.get(j,0));
////						System.out.println("MCA_weight.get(i,j) = " + MCA_weight.get(i,j));		
////						System.out.println("Attr_columnTotal.get(i,0)*class_columnTotal.get(j,0)-MCA_weight.get(i,j) = " + (Attr_columnTotal.get(i,0)*class_columnTotal.get(j,0)-MCA_weight.get(i,j)));//could be zero
//						final_MCA_weight[i][j]=1.0*MCA_weight.get(i,j)/(Attr_columnTotal.get(i,0)*class_columnTotal.get(j,0)-MCA_weight.get(i,j)*0);	
//					}				
//				}
////				System.out.println("done!");
				
		} else if (option.equalsIgnoreCase("-t")){
			//%%%%%%%%%%%% calculate Tanimoto weight based on indicator matrix (Yimin test) %%%%%%%%%%%%
			Matrix attrI = I.getMatrix(0, dataSize-1, 0, feature_value_size-classNum-1);
			Matrix classI = I.getMatrix(0, dataSize-1, feature_value_size-classNum, feature_value_size-1);	
			
			Matrix tanimoto_weight = attrI.transpose().times(classI);			
			Matrix attrColTotal = getColumnTotal(attrI.transpose());
			Matrix classColTotal = getColumnTotal(classI.transpose());
			
			int row = tanimoto_weight.getRowDimension();
			int col = tanimoto_weight.getColumnDimension();
			final_tanimoto_weight = new double[row][col];
			for(int i=0;i<row;i++){
				for(int j=0;j<col;j++){
					// calculate weight based on Tanimoto coefficients
//					final_tanimoto_weight[i][j]=1.0*tanimoto_weight.get(i,j)/(attrColTotal.get(i,0)*classColTotal.get(j,0));
//					final_tanimoto_weight[i][j]=1.0*tanimoto_weight.get(i,j)/(attrColTotal.get(i,0)*classColTotal.get(j,0)-tanimoto_weight.get(i,j));
					final_tanimoto_weight[i][j]=1.0*tanimoto_weight.get(i,j)/(attrColTotal.get(i,0)*classColTotal.get(j,0)-tanimoto_weight.get(i,j)*0.5);
//					final_tanimoto_weight[i][j]=1.0*tanimoto_weight.get(i,j)/(attrColTotal.get(i,0)*attrColTotal.get(i,0) + classColTotal.get(j,0)*classColTotal.get(j,0)-tanimoto_weight.get(i,j));
				}				
			}				
			final_MCA_weight = final_tanimoto_weight;					
		}	
	
			//final_MCA.print(formatter,colDim);
		/*
		 * 5. Output cosine value inforamtion
		 */		
			/*
			System.out.println("Output score to file");
			try {
				PrintWriter out= new PrintWriter(new FileWriter(output));
				for(int rind=0;rind<rowDim;rind++){
					out.print(rind+1+",");
					String fVpair = index2feature.get(new Integer(rind));
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
			*/
			

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
		//System.out.println(attributeSize);
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

	public void init(){
		dataSize=data.numInstances();
		attributeSize=data.numAttributes();
		classNum=data.numClasses();
	}
	public Instances getData() {
		return data;
	}

	public void setData(Instances data) {
		this.data = data;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getAttributeSize() {
		return attributeSize;
	}

	public void setAttributeSize(int attributeSize) {
		this.attributeSize = attributeSize;
	}

	public int getClassNum() {
		return classNum;
	}

	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}

	public double[][] getFinal_MCA_weight() {
		return final_MCA_weight;
	}

	public void setFinal_MCA_weight(double[][] final_MCA_weight) {
		this.final_MCA_weight = final_MCA_weight;
	}

	public Hashtable<Integer, String> getIndex2feature() {
		return index2feature;
	}

	public void setIndex2feature(Hashtable<Integer, String> index2feature) {
		this.index2feature = index2feature;
	}

}

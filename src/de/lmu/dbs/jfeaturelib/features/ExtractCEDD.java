package de.lmu.dbs.jfeaturelib.features;


import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

import javax.imageio.ImageIO;
import javax.swing.plaf.synth.SynthEditorPaneUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;

public class ExtractCEDD {

	/**
	 * @param args inPath: Image directory, ImgList: List of image names in a csv file
	 *             @return output path
	 */
	public static List<double[]> ceddResults;


	public static ArrayList<double[]> cedd(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_CEDD.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults;
		ArrayList<double[]> result = new ArrayList<>(1);
		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			CEDD cedd=new CEDD();
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start CEDD////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				cedd = new CEDD();
				cedd.run(cp);
				tempresults = cedd.getFeatures();
				result.add(tempresults.get(0));
				output.write(Integer.toString(count) + ",");
				int len = tempresults.get(0).length; // size of the hog feature vector
				for (int i = 0; i < len - 1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults.get(0)[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}
	public static ArrayList<double[]> hog(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_hog.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults;
		ArrayList<double[]> result = new ArrayList<>(1);

		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start HOG////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				PHOG hog=new PHOG();
				hog.run(cp);
				tempresults = hog.getFeatures();
				result.add(tempresults.get(0));
				output.write(Integer.toString(count) + ",");
				int len = tempresults.get(0).length; // size of the hog feature vector
				for (int i = 0; i < len - 1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults.get(0)[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}
	public static ArrayList<double[]> fcth(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_fcth.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults = new ArrayList<>(1);
		ArrayList<double[]> result = new ArrayList<>(1);

		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start FCTH////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				FCTH fcth=new FCTH();
				fcth.run(cp);
				tempresults = fcth.getFeatures();
				result.add(tempresults.get(0));
				output.write(Integer.toString(count) + ",");
				int len = tempresults.get(0).length; // size of the cedd feature vector
				for (int i = 0; i < len - 1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults.get(0)[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}
	public static ArrayList<double[]> fuzzy_hist(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_fuzzyhis.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults = new ArrayList<>(1);
		ArrayList<double[]> result = new ArrayList<>(1);

		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start Fuzzy Histogram////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				FuzzyHistogram fh=new FuzzyHistogram();
				fh.run(cp);
				tempresults = fh.getFeatures();
				result.add(tempresults.get(0));
				output.write(Integer.toString(count) + ",");
				int len = tempresults.get(0).length; // size of the cedd feature vector
				for (int i = 0; i < len - 1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults.get(0)[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}
	public static ArrayList<double[]> gabor(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_gabor.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults = new ArrayList<>(1);
		ArrayList<double[]> result = new ArrayList<>(1);

		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start SIFT////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				Gabor gabor=new Gabor();
				gabor.run(cp);
				tempresults = gabor.getFeatures();
				result.add(tempresults.get(0));
				output.write(Integer.toString(count) + ",");
				int len = tempresults.get(0).length; // size of the cedd feature vector
				for (int i = 0; i < len - 1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults.get(0)[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}

	public static ArrayList<double[]> momentTamura(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_moment.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults1 = new ArrayList<>(1);
		List<double[]> tempresults2 = new ArrayList<>(1);
		ArrayList<double[]> result = new ArrayList<>(1);

		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start Moment////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				Moments mm=new Moments();
				mm.run(cp);
				tempresults1 = mm.getFeatures();
				Tamura tm=new Tamura();
				tm.run(cp);
				tempresults2 = tm.getFeatures();
				result.add((double []) ArrayUtils.addAll(tempresults1.get(0),tempresults2.get(0)));
				output.write(Integer.toString(count) + ",");
				int len1 = tempresults1.get(0).length; // size of the moment feature vector
				int len2 = tempresults2.get(0).length; // size of the tamura feature vector
				for (int i = 0; i < len1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults1.get(0)[i]) + ",");
				}
				for (int i = 0; i < len2 -1 ; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults2.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults2.get(0)[len2 - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}
	public static ArrayList<double[]> extractAll(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_all.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		ArrayList<double[]> result = new ArrayList<>(1);
		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start All Features////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				CEDD cedd = new CEDD();
				cedd.run(cp);
				PHOG hog=new PHOG();
				hog.run(cp);
				FCTH fcth=new FCTH();
				fcth.run(cp);
				Gabor gabor=new Gabor();
				gabor.run(cp);
				Moments mm=new Moments();
				mm.run(cp);
				Tamura tm=new Tamura();
				tm.run(cp);
				double[] tempresult1=ArrayUtils.addAll(cedd.getFeatures().get(0),hog.getFeatures().get(0));
				double[] tempresult2=ArrayUtils.addAll(tempresult1,gabor.getFeatures().get(0));
				tempresult1 = ArrayUtils.addAll(tempresult2, fcth.getFeatures().get(0));
				tempresult2 = ArrayUtils.addAll(tempresult1, mm.getFeatures().get(0));
				tempresult1 = ArrayUtils.addAll(tempresult2,tm.getFeatures().get(0));
				result.add(tempresult1);
				output.write(Integer.toString(count) + ",");
				int len = tempresult1.length; // size of the hog feature vector
				for (int i = 0; i < len - 1; i++) {
					output.write(Double.toString(tempresult1[i]) + ",");
				}
				output.write(Double.toString(tempresult1[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}

	public static ArrayList<double[]> tamura(String inpath, String ImgList) {
		// TODO Auto-generated method stub
		String outPath = "output/feature_tamura.csv";
		//String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";
		File file = new File(ImgList);
		String ImgName;
		List<double[]> tempresults = new ArrayList<>(1);
		ArrayList<double[]> result = new ArrayList<>(1);

		try {
			Scanner inputStream = new Scanner(file);
			BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
			int count = 0;
			while (inputStream.hasNext()) {
				count++;
				ImgName = inputStream.next();
				System.out.println("Processing image " + count + ": " + ImgName);
				//////////////////////////////////////////start Tamura////////////////////////
				ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath + ImgName + ".png")));
				Tamura tm=new Tamura();
				tm.run(cp);
				tempresults = tm.getFeatures();
				result.add(tempresults.get(0));
				output.write(Integer.toString(count) + ",");
				int len = tempresults.get(0).length; // size of the cedd feature vector
				for (int i = 0; i < len - 1; i++) {
					//System.out.println(tempresults.get(0)[i]);
					output.write(Double.toString(tempresults.get(0)[i]) + ",");
				}
				output.write(Double.toString(tempresults.get(0)[len - 1]) + "\n");

			}
			output.close();
		}
		catch(IOException e){
			System.out.println(e);
		}

		return result;
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		//ArrayList<double[]> output=cedd("/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/resized_4403_samira/", "/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/image_list2.csv");
		ArrayList<double[]> output=extractAll("/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/resized_4403_samira/", "/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/image_list2.csv");
		System.out.println("Done");


		/*String outlog = "/Volumes/spouy001/MyResearch/CEDD/20170524_CEDD_log.txt";

        BufferedWriter log = new BufferedWriter(new FileWriter(outlog));
        
		//for (int j = 67; j<=71; j++){

			//String inpath = "U:\\FDOIStorage\\storage26\\VACCINE\\1_Data\\Disaster_Pics_Flickr_2012_batch2\\Image\\"+Integer.toString(j)+"\\";
			//String outPath = "U:\\FDOIStorage\\storage26\\VACCINE\\Research_2012\\Flickr_features_2012_batch2_CEDD\\cedd_"+Integer.toString(j)+".csv";
			//String inpath = "U:\\mitch-a\\dmis-research\\Haiman\\Reasearch1_soccer_event_detection\\goal_detection\\info\\";//get number of videos need to process
        	//String inpath = "U:\\mitch-b\\dmis-research\\Haiman\\YoutubeCrawler\\ShotDetection\\";//get number of videos need to process
        //String inpath = "/Volumes/htian005/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/";//output feature
        String inpath="/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/resized_4403_samira/";
        //String inpath=args[0];
        //String outPath = "U:\\Reasearch1_soccer_event_detection\\goal_detection\\zcc\\"+Integer.toString(j)+".csv";
			//String featurePath = "U:\\mitch-a\\dmis-research\\Haiman\\Reasearch1_soccer_event_detection\\goal_detection\\zcc\\";//frame directory
        	//String featurePath="U:\\mitch-b\\dmis-research\\Haiman\\YoutubeCrawler\\CTD_results";
        	//String featurePath="/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/resized_4403_samira/";//images
        String featurePath="/Volumes/spouy001/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/image_list2.csv";
        //String featurePath=args[1];
        
        //String featurePath="/Volumes/spouy001/MyResearch/CEDD/test/";	
        //File dir = new File(inpath);
			*//*
			String[] children = dir.list();
			if (children == null) {
				continue;
			    // Either dir does not exist or is not a directory
			} else {			
			*//*
			String outfileName;
			String fname;
			String name;
				//int catID = j;
			int videoID;
			int shotID;
			int frameID;
				//get file list	
			File file = new File(featurePath);
			Scanner inputStream = new Scanner(file);
//			File folder = new File(featurePath);
//			File[] listofFiles = folder.listFiles(new FilenameFilter(){
//				public boolean accept (File file, String fileName){
//					if (fileName.endsWith(".png")){
//						return true;
//					}
//					return false;
//				}
//			});
			
			//get folder list
			*//*
			File folder = new File(inpath);
			String[] directories = folder.list(new FilenameFilter(){
				public boolean accept (File file, String fileName){
					return new File(file,fileName).isDirectory();
				}
			});*//*
			
			//Arrays.sort(directories);
			//System.out.println(Arrays.toString(directories));
			String outPath = inpath;
			outPath = "feature_CEDD.csv";
		        BufferedWriter output = new BufferedWriter(new FileWriter(outPath));
		        outfileName = featurePath;
		    *//*
			for (int k = 0; k<directories.length; k++){
					//if (listofFiles[k].isFile()){
			if (directories[k].substring(0,3).equals("For_")){
				continue;
			}*//*
			//get File list from csv file
			*//*
			List<String> temps = new ArrayList<String>();
			//File listfile = new File("U:\\mitch-b\\dmis-research\\Haiman\\YoutubeCrawler\\label_2.csv");
			//File listfile = new File("/Volumes/links/mitch-a/dmis-research/Haiman/mitch-b/dmis-research/Haiman/MADIS_video_xiaoyu_CTD_flv/files_all.csv");
			//File listfile = new File("/Volumes/htian005/mitch-b/dmis-research/Haiman/YoutubeCrawler/Video_add2017/70video_labels.csv");//get video list
			File listfile = new File("/Volumes/htian005/mitch-b/dmis-research/Haiman/ACM_MM_Purdue_videos/image_list.csv");//get video list
			FileInputStream fis = new FileInputStream(listfile);
			 
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				temps.add(line);
			}
			br.close();
		    String [] tempsArray = temps.toArray(new String[0]);
		    int videoCount=0;
		    outfileName = featurePath;//image folder
			//feature output
			String outPath = inpath;
			//outPath = outPath+name+"_feature_CEDD.csv";
			outPath = outPath+"ACM_resized_feature_CEDD.csv";
			System.out.println(outPath);

			/*//*************************************************
			
			File csvFile = new File(outPath);
			if(!csvFile.exists()){
				csvFile.createNewFile();
			}
			
			BufferedWriter output = new BufferedWriter(new FileWriter(csvFile.getAbsoluteFile()));
			for (String s : tempsArray){
				videoCount++;
				final String vnum=Integer.toString(videoCount);
				System.out.println(s);
					//if (listofFiles[k].isFile()){
				//name = s.substring(0,11);//.getName();
				name=s;
				//String[] parts=s.split("_");//separate file name by underscore
				//name = parts[0];//video ID
				//fname = name.substring(0, name.length()-9);//get the video name substract _info.mat
					//outfileName = inpath + fname;
				//outfileName = featurePath + fname+"\\Image\\";//get the directory for each video frames
				*//*
				outfileName = featurePath;
				//feature output
				String outPath = inpath;
				//outPath = outPath+name+"_feature_CEDD.csv";
				outPath = outPath+"ACM_feature_CEDD.csv";
				System.out.println(outPath);

				/*//*************************************************
				
				File csvFile = new File(outPath);
				if(!csvFile.exists()){
					csvFile.createNewFile();
				}
				
				BufferedWriter output = new BufferedWriter(new FileWriter(csvFile.getAbsoluteFile()));*//*
				//process each frame
				//final String vidName=name+".png";
				*//*
				File imgs = new File (featurePath);
				File[] listofImgs = imgs.listFiles(new FilenameFilter(){
					public boolean accept (File file, String fileName){
						//if (fileName.endsWith(".png")&&fileName.startsWith(vidName+"_")){
							if (fileName.endsWith(".png")&&fileName.startsWith(vidName)){
							return true;
						}
						return false;
					}
				});*//*
				String Imgname;
				//int CameraTakeID = 0;
				//Arrays.sort(listofImgs);
				//for (int nimg = 0; nimg<listofImgs.length; nimg++){
				//for (int nimg = 0; nimg<listofFiles.length; nimg++){
				int count=0;
				while(inputStream.hasNext()){
					count++;
					//Imgname = listofImgs[nimg].getName();
					Imgname = inputStream.next();
					//name = listofFiles[nimg].getName();
					System.out.println("Processing image " + count + ": " + Imgname);
					//String rmName = name.substring(4);//camaraTakeID_frameID.png
					//String rmName=name;
					//Pattern p = Pattern.compile("\\d+");//("shot\\d+_Frame_\\d.jpg");//video\\d_shot\\d_Frame\\d.jpg
					//Matcher m = p.matcher(rmName);
					//String[] arr = new String[3];
					//String[] arr = new String[1];
					
					//int count = 0;
					//while (m.find()){
						//arr[count] = m.group();
						//count = count+1;
						//System.out.println(m.group());
					//}
					//videoID = Integer.valueOf(arr[0]);
					//shotID = Integer.valueOf(arr[1]);	
					//frameID = Integer.valueOf(arr[2]);	
					//shotID = Integer.valueOf(arr[0]);	
					//frameID = Integer.valueOf(arr[1]);	
					//if(CameraTakeID!=shotID){
						//CameraTakeID = shotID;
						try {
							//////////////////////////////////////////start CEDD////////////////////////
							ColorProcessor cp = new ColorProcessor(ImageIO.read(new File(inpath+Imgname+".png")));
					        CEDD cedd = new CEDD();
					        cedd.run(cp);
					        //output.write(Integer.toString(videoID)+",");
					        output.write(Integer.toString(count)+",");
							//output.write(Integer.toString(shotID)+",");
							//output.write(Integer.toString(frameID)+",");
					        
					        int len = cedd.data.length; // size of the cedd feature vector
					        for (int i=0; i<len-1; i++){
						    	output.write(Double.toString(cedd.data[i])+",");	        	
					        }
					        output.write(Double.toString(cedd.data[len-1])+"\n");		
					        				
						}
						catch (RuntimeException ioe){
							log.write(count);
							continue;
						}	
					//}
				//}
		        
		        
		        
				/*//********append csv files********************
				*//*
				File csvFile = new File(outPath);
				if (csvFile.exists()){
				FileInputStream fisp = new FileInputStream(csvFile);
				 
				//Construct BufferedReader from InputStreamReader
				BufferedReader brp = new BufferedReader(new InputStreamReader(fisp));
			 
				String linep = null;
				while ((linep = brp.readLine()) != null) {
					//System.out.println(linep);
					try{
						File allFile = new File(inpath+"all_cedd.csv");
						if(!allFile.exists()){
							allFile.createNewFile();
						}
						Files.write(Paths.get(inpath+"all_cedd.csv"), (vnum+","+linep+"\n").getBytes(), StandardOpenOption.APPEND);
						
					}catch(IOException e){
						
					}
				}
			 
				brp.close();
				}
		        *//*
			}output.close();	
	log.close();*/
	}		
}

package examplePipeline;

import examplePipeline.tools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main{
	
	//filtered press releases first by date, then by keywords. 

  public static void main (String [] args) throws IOException{
	 	
    search_terms("/home/john/Desktop/REU/Asset" ,mapTFIDF(returnList("/home/john/Desktop/REU/raw")));
   // TEST search_terms("/home/john/Desktop/REU/sampleasset" ,mapTFIDF(returnList("/home/john/Desktop/REU/sampleraw")));
    //                                     sample asset/date                                        sample raw 
  }

  public static ArrayList<String> returnList (String dirName) throws FileNotFoundException{
    //inputs in a directory, outputs arraylist 
    //File dir = new File ("/home/john/Desktop/REU/Asset_and_dates"); // change to raw when I actually do it 
	  File dir = new File(dirName);
  		/*Train my TFIDF with all of the titles
  		 * from the entire press release file
  		 */

  		ArrayList <String> titleList = new ArrayList<>();
  		File files = dir;
  		File [] folder = files.listFiles();
  		if(folder!= null){
  			for(File press_release: folder){
  				try {
  					Scanner press_scan = new Scanner(press_release);
  					String title = tools.extract_title(press_scan);
  					titleList.add(title);
  					
  					//ArrayList filled with titles. This arraylist is
  					// the corpus 
  					press_scan.close();
  					}
  				
  				catch (NoSuchElementException e) {
  					
  					System.out.println("File Not Found" + press_release.getName() 
  					+ press_release.getAbsolutePath() );
  					
  				}
  				
  			}
  			
  		}	
  	
  		
  		else{
  			System.out.println("Folder is empty");
  		}

      return titleList;
  }


  public static HashMap<String, Double>  mapTFIDF (ArrayList<String> titleList){
	  	HashMap<String, Double> map = new HashMap<String, Double>();
		for(String t : titleList){
			/*System.out.println(t);
			System.out.println("####################");
			*/
			String [] title_part = t.split(" ");
			
			double wordCount = 0; //wordCount is the number of times a word appears in a given document
			for(String word: title_part)
			{
				
				if(t.contains(word)){wordCount++;}
				
				
				double tf = wordCount/title_part.length;
				
				//calculates term frequency
				
				//calculating idf
				double docCount = 0; //docCount is the number of documents with a certain term 
				int totalDocCount = titleList.size();
				for(String t1: titleList){
					if(t1.contains(word)){
						docCount++;
					}
				}
				//System.out.println(word + " " + wordCount + " " + docCount);
				double IDF = Math.log10(totalDocCount/(docCount+1.0));
				
				double tfidf = tf*IDF;
				
				if(!map.containsKey(word)){
					map.put(word, tfidf);
				}
				wordCount = 0;
				 
			}
		
		}
		
		
		for (HashMap.Entry<String,Double> entry : map.entrySet()) {
			  String key = entry.getKey();
			  Double value = entry.getValue();
			 // System.out.println(key + " " + " "  +  value);
			}
    
    return map;
  }

  public static void search_terms(String assetLocationName, HashMap <String, Double> map) throws IOException{
	  	File assetLocation = new File (assetLocationName);
	  	//File assetLocation = new File("/home/john/Desktop/REU/sample_releases");
		File files1 = assetLocation;
		File [] folder1 = files1.listFiles();
	
		if(folder1!=null){
		    for(File asset_release: folder1){
			try{
				
				Scanner asset_scan = new Scanner(asset_release);
			    String asset_title = tools.extract_title(asset_scan);
			    String [] title_components = asset_title.split(" ");
			    String search_terms = "";
			    double a = 1.0; 
			    HashMap<String, Double> helperMap = new HashMap<String, Double>();
			    for(String component: title_components) {//navigating each word 
			    	if(map.get(component) != null){
			    		helperMap.put(component, map.get(component));
			    		}
			    	else{
			    		map.put(component, a);
			    		helperMap.put(component, a);
			    		a = a - .001;
			    	}
			    	}
			    for (HashMap.Entry<String,Double> entry : helperMap.entrySet()) {
					  String key = entry.getKey();
					  Double value = entry.getValue();
					  //System.out.println(key + " " + " "  +  value + "helper map info" ) ;
			    }
			    
			    
				for(int x = 0 ;  x< 4; x++){
					 
		    		double maxValueInHelper = Collections.max(helperMap.values());
		    		
		    		Iterator<HashMap.Entry<String, Double>> itr = map.entrySet().iterator();
		    		while(itr.hasNext()){
		    			HashMap.Entry<String, Double> entry = itr.next();
		    			if(entry.getValue().equals(maxValueInHelper) && asset_title.contains(entry.getKey())){
		    				search_terms =   search_terms + " " +  entry.getKey();
		    				helperMap.remove(entry.getKey());
		    			}
		    		}
		    	}
				
				
			    
			SimpleDateFormat format = new SimpleDateFormat("MMddyy");
		    	String name = asset_release.getName();
		    	String date = tools.stripNonDigits(name);
		    	
		    	String fileName = asset_title+".txt";
		    	File newTextFile = new File("/home/john/Desktop/REU/generalreports/"+fileName);
	//test 	    	File newTextFile = new File("/home/john/Desktop/REU/sampletitles/"+fileName);
		    	if(!newTextFile.exists()){
		    		try{
		    			newTextFile.createNewFile();
		    		}
		    		catch (IOException e){
		    			System.out.println(fileName + "not found");
		    		}
		    	}
		    	try{
		    		FileOutputStream fos = new FileOutputStream(newTextFile);
			    	
			    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			    	bw.write(asset_release.getName());
			    	bw.newLine();
			    	bw.write("search terms:" + search_terms);
			    	bw.newLine();
			    	bw.write("date: " + date );
			    	bw.newLine();
			    	bw.close();
		    	}
		    	
		    	catch (FileNotFoundException e){
		    		e.printStackTrace();
		    	}
		    	
			    
			}

			catch (NoSuchElementException e) {
				e.printStackTrace();
				//System.out.println("No such element");
			}
			

		}

		}

		else{
		    System.out.println("Folder is empty");
		}
		
    

    
  }

}

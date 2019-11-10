package examplePipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import examplePipeline.tools;

public class TFIDF {
	
	public static void main(String [] args) throws FileNotFoundException{
		
	}
	
	private static final String press_release_path = "/home/john/Desktop/REU/sample_releases";
	
	//doc name = "ru-ass080111.php.txt"
	
	//method to compute term frequency (Number of times a word appears in a given document)
	
	public static double tfCalc(String term, File filename) throws FileNotFoundException{
		
		double totalCount = 0;
		double termCount = 0;
		String content = new Scanner(filename).useDelimiter("\\Z").next();
		//System.out.println(content);
		System.out.println(tools.Stanford_Tokenizer(content));
		ArrayList<String> tokens = tools.Stanford_Tokenizer(content);
		for(String s: tokens){
			totalCount++;
			String a = tools.getLemma(s).toLowerCase();
			if(term.toLowerCase().equals(a)){
				termCount++;
			}
		}
		return termCount/totalCount;
		
		
	}
	

	
	
	public static double IDF(String term, int totalDocCount) throws FileNotFoundException{
		
		int docCount = 0;
		File files = new File(press_release_path);
		File [] folder = files.listFiles();
		for(File child: folder){
			totalDocCount++;
			Scanner scanner = new Scanner(child);
			while(scanner.hasNextLine() && docCount < 1){
				String nextLine = scanner.nextLine();
				if(nextLine.contains(tools.getLemma(term))){
					docCount++;
				}
			}
		}
		
		double IDF = Math.log10(totalDocCount/docCount);
		return IDF;
		
	}
	
	public static double tf_idf(double tf, double idf){
		return tf*idf;
	}
	
	
	
	
	
	
	
	//method to return the number of documents containing a certain word
	
	//method to compute the inverse document frequency which measures how common a word is among all documents in raw 
	
	
	
	
	
	
	
}
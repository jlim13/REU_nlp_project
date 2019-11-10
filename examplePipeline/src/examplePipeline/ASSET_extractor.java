package examplePipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import edu.stanford.nlp.simple.Sentence;
import examplePipeline.tools;

//You can pull the relevant press releases based on keywords 


public class ASSET_extractor {
	
	public static void main(String [] args) throws IOException{
		
		String path = "/home/john/Desktop/REU/Asset";
		ASSET_extracting(path);
	}
	
	//For every press article in the press release::::
	
	public static void ASSET_extracting(String DirectoryPath) throws IOException {
		
		File dir = new File ("/home/john/Desktop/REU/Asset_and_dates");
		
		
		String [] keywords = {""};
		int count = 0;
		File files = new File(DirectoryPath);
		File [] folder = files.listFiles();
		if(folder!= null){
			for(File child: folder){
				File targetFile = new File(dir, child.getName());
				try{
					Scanner scanner = new Scanner(child);
					while(scanner.hasNext()){
						String nextLine = scanner.nextLine();
						for(int i = 0; i < keywords.length; i++){
							if (nextLine.contains(tools.getLemma(keywords[i]))){
								//add to a new folder
								//Files.move(from, to)
								
								com.google.common.io.Files.move(child, targetFile);
														
							}
						}
					
					}
				}
				catch (FileNotFoundException e){
					System.out.println("File not found" + child.getName());
				}
				
			}
	
		}
		else{
			System.out.println("Folder is empty");
		}
	
	}
	

}

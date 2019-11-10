package examplePipeline;

import java.io.BufferedReader;

//by joshua eisenberg
//jeise003@fiu.edu

//This class loads the texts in a corpus. The corpus object is the data structure
//for accessing documents. 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import examplePipeline.document;

public class corpus {
	
	
	ArrayList<document> documents = new ArrayList<document>();
	ArrayList<Integer> story_pointers = new ArrayList<Integer>();
	ArrayList<Integer> notstory_pointers = new ArrayList<Integer>();
	Boolean single_class = false;

	public corpus(String text_dir) throws IOException 
	{
	
		//load stories
		Scanner story_scanner = new Scanner(new File(text_dir));
		int pointer =0;
		while(story_scanner.hasNextLine())
		{
			documents.add(new document(story_scanner.nextLine(), "story"));
			story_pointers.add(pointer);
			pointer++;
		}
		single_class=true;
		
	}

	public corpus(String story_path, String notstory_path) throws FileNotFoundException 
	{
		//load stories
		Scanner story_scanner = new Scanner(new File(story_path));
		int pointer =0;
		while(story_scanner.hasNextLine())
		{
			documents.add(new document(story_scanner.nextLine(), "story"));
			story_pointers.add(pointer);
			pointer++;
		}
		story_scanner.close();
		
		//load not stories
		Scanner notstory_scanner = new Scanner(new File(notstory_path));
		while(notstory_scanner.hasNextLine())
		{
			documents.add(new document(notstory_scanner.nextLine(), "notstory"));
			notstory_pointers.add(pointer);
			pointer++;
		}
		notstory_scanner.close();
		
		
	}

}
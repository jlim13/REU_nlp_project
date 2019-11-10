package examplePipeline;

//by joshua eisenberg
//jeise003@fiu.edu

//document class
//construct splits sentences, tokenizes

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class document {
	
	String annotation;
	String raw_text;
	ArrayList<String> sentences;
	//tokens
	ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
	//pos tags
	
	//Argument
	
	//argument-entites or characters

	public document(String text, String ann) throws FileNotFoundException 
	{
		
		raw_text = text;
		annotation = ann;
		sentences = tools.Stanford_Sentences(raw_text);
		for(String s : sentences)
		{
			tokens.add(tools.Stanford_Tokenizer(s));
		}
		
		
		//coref replace  + character extraction
		
		//SRL
		
		//find relatedness of adjacent actions
		
		//referntial cohesion
		
	}

}
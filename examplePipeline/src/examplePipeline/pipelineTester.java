package examplePipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import com.jaunt.NotFound;

import edu.stanford.nlp.simple.Sentence;
import examplePipeline.tools;
import examplePipeline.Jaunt_demo;

public class pipelineTester {    

	public static void main(String[] args) throws ClassCastException, ClassNotFoundException, IOException, NotFound, ParseException 
	{
		
		
		String raw_text = "raw_text.txt";
		
		ArrayList<String> sentences;
		ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();
		sentences = tools.Stanford_Sentences(raw_text);
		int cur =0;
		String[] ner;
		for(String s: sentences){
			System.out.println(s);
			tokens.add(tools.Stanford_Tokenizer(s));
			ner = tools.Stanford_NER(tokens.get(cur));
			cur++;
		}
		
		

		
	}

}

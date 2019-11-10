package examplePipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.simple.Sentence;

public class tools {
	
		public static void main(String [] args) throws IOException, ParseException{
			
			Date_filter("/home/john/Desktop/REU/raw");
			
		}
			//returns the title of a press release
		public static String extract_title(Scanner doc){
			
			
			int emailCount = 0;
			String line;
			String email = "";
			String title = ""; 
			while(doc.hasNext()){
				line = doc.nextLine();
				if(line.contains("@") && emailCount == 0) {
					emailCount++;
					email = line;
					title = doc.nextLine();
					
				}
				else
					line = doc.nextLine();
			}
			//doc.close();
			return title; 
		}
		
		public static String stripNonDigits(CharSequence input){
			final StringBuilder sb = new StringBuilder(input.length());
			for(int i = 0; i < input.length(); i++){
				final char c = input.charAt(i);
					if(c > 47 && c < 58){
						sb.append(c);
						}
				}
	    return sb.toString();
			}
		
		public static void Date_filter(String DirectoryPath) throws IOException, ParseException {
			
			File dir = new File ("/home/john/Desktop/REU/Asset");
			
			SimpleDateFormat format = new SimpleDateFormat("MMddyy");
			Date minDate = format.parse("060109");
			Date maxDate = format.parse("123109");
			
			
			File files = new File(DirectoryPath);
			File [] folder = files.listFiles();
			if(folder!= null){
				for(File child: folder){
					File targetFile = new File(dir, child.getName());
					String name = child.getName();
					name = stripNonDigits(name);
					//System.out.println(name);
					try{
						
					
						Date cur = format.parse(name);
						//System.out.println("We are cur" + cur);
						if(cur.compareTo(minDate) >0 && cur.compareTo(maxDate) < 0){
							//System.out.println(minDate);
							System.out.println(name + " Is getting moved");
							com.google.common.io.Files.move(child, targetFile);
						}
						else{
							System.out.println("We did not get moved " + name);
						
						}
						
					}
					catch( ParseException e){
						System.out.println(name + " Was not parsable ");
					}
			}
			}	
				
					
				
			}
		
	
		// http://nlp.stanford.edu/software/tokenizer.shtml
		// Given a string returns an array of sentences
		public static ArrayList<String> Stanford_Sentences(String text)
				throws FileNotFoundException 
		{
			// option #1: By sentence.
			// System.out.println("Sentences:");
			// DocumentPreprocessor dp = new DocumentPreprocessor(new
			// StringReader(text));
			int total = 0;
			for (List<HasWord> sentence : new DocumentPreprocessor(
					new StringReader(text)))
				total++;
//						String[] sents = new String[total];
			ArrayList<String> sents = new ArrayList<String>();
			int i = 0;
			for (List<HasWord> sentence : new DocumentPreprocessor(new StringReader(text))) 
			{
				// System.out.println(sentence);
				sents.add(i, sentence.toString());
//							sents[i] = sentence.toString();
				i++;
			}
			return sents;
		}
		
		// http://nlp.stanford.edu/software/tokenizer.shtml
		// Given a sentence, returns a string of tokens
		public static ArrayList<String> Stanford_Tokenizer(String text) throws FileNotFoundException 
		{
			// option #1: By sentence.
			// System.out.println("Sentences:");
			// DocumentPreprocessor dp = new DocumentPreprocessor(new
			// StringReader(text));
			// for (List<HasWord> sentence : dp) {
			// System.out.println(sentence);
			// }
			// option #2: By token
			// System.out.println("Tokens:");

			PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(
					new StringReader(text), new CoreLabelTokenFactory(), "");

			int total = 0;
			while (ptbt.hasNext()) {
				CoreLabel label = ptbt.next();
				total++;
			}

			String[] tokens = new String[total];
			ptbt = new PTBTokenizer<>(new StringReader(text),
					new CoreLabelTokenFactory(), "");
			int i = 0;

			while (ptbt.hasNext()) {
				CoreLabel label = ptbt.next();
				// System.out.println(label);
				tokens[i] = label.toString();
				i++;
			}

//			String[] clean_tokens = new String[(tokens.length - 2) / 2 + 1];
			ArrayList<String> clean_tokens = new ArrayList<String>();
			int c = 0;
			for (int j = 1; j < tokens.length - 1; j = j + 2) {
				clean_tokens.add(c, tokens[j]);  //tokens[j].toLowerCase();
				c++;
			}
			return clean_tokens;
		}
		
		//http://nlp.stanford.edu/software/ner-example/NERDemo.java
		public static String[] Stanford_NER(ArrayList<String> tokens) throws ClassCastException, ClassNotFoundException, IOException
		{
		   //choose model
		   String serializedClassifier = "english.all.3class.distsim.crf.ser.gz";
		   AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		   String[] ner_tags = new String[tokens.size()];
		   
		   String cur;
		   String garbage; 
		   Scanner s = null;
		   String str;

		   //for (String str : tokens) 
		   for(int i =0; i< tokens.size(); i++)
		   {
			   	str = tokens.get(i);
		        //System.out.println(classifier.classifyToString(str));
			    cur = classifier.classifyToString(str, "tsv", false);
		        System.out.println(cur);
		        s = new Scanner(cur);
		        garbage = s.next();
		        ner_tags[i] = s.next();
		    }
		   s.close();
		   return ner_tags;
		}
		
		public static String getLemma(String word)
		{
			String tokenLemma = new Sentence(word).lemma(0);
			return tokenLemma;
		}

	

}

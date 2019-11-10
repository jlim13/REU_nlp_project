package examplePipeline;

//by joshua eisenberg
//jeise003@fiu.edu

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class povClassifier {

	
	corpus texts;
	String[] books;
	svm_model model;
	int[] pov;
	static double[][] features;
	String[][] sents;
	Boolean single_class = false;
	int[] results;
	
	
	String[] feat_words = {  //first person
			"i", //"i'm", "i'll", "i'd",
			"me", "my", "mine", "myself",
			"we", //"we're", "we'll", "we'd",
			"us", "our", "ours", 
			//second person
			"you", "your", "yours", //"you're"
			//third person
			"he", //"he'll", "he'd", 
			"him","his",
			"she", //"she'll", "she'd",
			"her", "hers",
			"they", //"they'll", "they'd",
			"them", "theirs" 
			};

double[] totals = new double[feat_words.length];
	
	public povClassifier(corpus c) throws IOException 
	{
		texts = c;
		model = svm.svm_load_model("models/pov.model");
		extractFeatures();
		classifyPOV();
	}
	
	public void extractFeatures() throws FileNotFoundException
	{
		pov = new int[texts.documents.size()];
		features = new double[texts.documents.size()][];  //figure out # of features
		sents = new String[texts.documents.size()][];
		String[] tokens;
		books = new String[pov.length];

		for(int i=0; i<pov.length; i++)
		{
			books[i] = texts.documents.get(i).raw_text;
			books[i] = removeQuotedText(books[i]);
			sents[i] = Stanford_Sentences(books[i]);

			//load features into feats
			HashMap<String, Integer> feats = new HashMap<String, Integer>();
			for(int j=0; j<feat_words.length; j++)
				feats.put(feat_words[j], 0);
			
			//find occurences of each pronoun
			for(int j=0;j<sents[i].length;j++)
			{
//				System.out.println(sents[i][j]);
				tokens = Stanford_Tokenizer(sents[i][j]);
				for(int k=0; k<tokens.length; k++)		
				{
					tokens[k].replace(".", "");
					tokens[k].replace("?", "");
					tokens[k].replace("!", "");
					tokens[k].replace(",", "");
					tokens[k].replace(";", "");
					tokens[k].replace(":", "");
					
					tokens[k] = tokens[k].toLowerCase();

					if(feats.containsKey(tokens[k]))
						feats.put(tokens[k], feats.get(tokens[k])+1);
				}
			}
			double[] feat_vals = new double[feat_words.length];
			for(int j=0; j<feat_words.length; j++)
			{
				feat_vals[j] = feats.get(feat_words[j]);
//				System.out.print(feat_words[j] + ": " +feat_vals[j] +"\t");
//				pw.print(feat_words[j] + ": " +feat_vals[j] +"\t");
				
				totals[j] += feat_vals[j];
			}
//			pw.println();
//			System.out.println();
			features[i] = feat_vals;
		}
		
		
		
	}
	
	public void classifyPOV()
	{
		if(!texts.single_class)
		{
			int story1=0, story3=0, notstory1=0, notstory3=0;
			
			for(int i=0; i<texts.story_pointers.size(); i++)
			{
				double predict = evaluate(features[texts.story_pointers.get(i)]);
				if(predict==1)
					story1++;
				else if(predict==3)
					story3++;
			}
			for(int i=0; i<texts.notstory_pointers.size(); i++)
			{
				double predict = evaluate(features[texts.notstory_pointers.get(i)]);
				if(predict==1)
					notstory1++;
				else if(predict==3)
					notstory3++;
			}
			
			System.out.println("1st person stories: " +story1);
			System.out.println("3rd person stories: " +story3);
			System.out.println("1st person not stories: " +notstory1);
			System.out.println("3rd person not stories: " +notstory3);
		}
		else
		{
			results = new int[texts.story_pointers.size()];
			for(int i=0; i<texts.story_pointers.size(); i++)
			{
				double predict = evaluate(features[texts.story_pointers.get(i)]);
				if(predict==1)
					results[i]=1;
				else if(predict==3)
					results[i]=3;
				System.out.println("text "+i+":\t "+results[i]);
			}
		}

		
	}
	
	
	public static String removeQuotedText(String raw)
	{
		Scanner book = new Scanner(raw);
		String cur;
		int innerQ = 0;
		String filtered_txt ="";		
		
		while(book.hasNext())
		{
			cur = book.next();
			
			//single word in quotes
			if((cur.substring(0,1)).equals("\"") && (cur.substring(cur.length()-1,cur.length())).equals("\"")||(cur.substring(0,1)).equals("\'") && (cur.substring(cur.length()-1,cur.length())).equals("\'")) //begin quote
			{	
//				System.out.println(cur); //do nothing
			}
					
			if(innerQ == 0) // currently not in quotes
			{
				if((cur.substring(0,1)).equals("\"")||(cur.substring(0,1)).equals("\'")) //begin quote
				{	
//					System.out.println(cur);
					innerQ++;
				}
				else 
				{
					filtered_txt = filtered_txt + " " + cur;
				}
			}
			else //if(innerQ != 0) // in quotes
			{
				if((cur.substring(0,1)).equals("\"")||(cur.substring(0,1)).equals("\'")) //begin new quote
				{	
//					System.out.println("new " + cur);
					innerQ++;
				}
				else if((cur.substring(cur.length()-1,cur.length())).equals("\"")||(cur.substring(cur.length()-1,cur.length())).equals("\'")) //end
				{	
//					System.out.println(cur);
					innerQ--;
				}
			}
			
		}
		book.close();
		return filtered_txt;
	}
	

	//http://nlp.stanford.edu/software/tokenizer.shtml
	//Given a string returns an array of sentences
	public static String[] Stanford_Sentences(String text) throws FileNotFoundException
	{
		// option #1: By sentence.
		//System.out.println("Sentences:");
		//DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(text));
		int total =0;
		for (List<HasWord> sentence : new DocumentPreprocessor(new StringReader(text))) 
			total++;
		String[] sents = new String[total];
		int i=0;
		for (List<HasWord> sentence :new DocumentPreprocessor(new StringReader(text))) 
		{
		//	System.out.println(sentence);
			sents[i]=sentence.toString();
			i++;
		}
		return sents;	
	}
	
	//http://nlp.stanford.edu/software/tokenizer.shtml
	//Given a sentence, returns a string of tokens
	public static String[] Stanford_Tokenizer(String text) throws FileNotFoundException
	{
		 // option #1: By sentence.
//		 System.out.println("Sentences:");
//	     DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(text));
//	      for (List<HasWord> sentence : dp) {
//	        System.out.println(sentence);
//	      }
	      // option #2: By token
		 // System.out.println("Tokens:");
	      
		  PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new StringReader(text), new CoreLabelTokenFactory(), "");
		  
		  int total=0;
	      while (ptbt.hasNext()) 
	      {
	    	CoreLabel label = ptbt.next();
	        total++;
	      }
	      
		  String[] tokens= new String[total];
	      ptbt = new PTBTokenizer<>(new StringReader(text), new CoreLabelTokenFactory(), "");
	      int i=0;
		  
	      while (ptbt.hasNext()) 
	      {
		        CoreLabel label = ptbt.next();
		        //System.out.println(label);
		        tokens[i] = label.toString();
		        i++;   
		  }
	      return tokens;
	}
	
	public double evaluate(double[] features)
	{
	    svm_node[] nodes = new svm_node[features.length];
	    for(int i=0; i< features.length; i++)
	    {
	    	svm_node node = new svm_node();
	    	node.index =i;
	    	node.value = features[i];
	    	nodes[i] = node;
	    }
	    
	    int totalClasses=2;
	    int[] labels = new int[totalClasses];
	    svm.svm_get_labels(model,labels);
	    
	    double[] prob_estimates = new double[totalClasses];
	    double v = svm.svm_predict_probability(model, nodes, prob_estimates);
	    
//	    for (int i = 0; i < totalClasses; i++)
//	    {
//	        System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
//	    }
	    
//	    System.out.println("(Actual:" + label + " Prediction:" + v + ")"); 

	    return v;
	    
	}

}

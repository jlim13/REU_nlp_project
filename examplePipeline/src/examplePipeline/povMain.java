package examplePipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class povMain {

	public static void main(String[] args) throws IOException {
		String path = "/home/john/Desktop/REU/finalassetreports/povFeed/releases.txt";
		
		corpus texts_to_test = new corpus(path);
		
		povClassifier pov = new povClassifier(texts_to_test);
	}

}

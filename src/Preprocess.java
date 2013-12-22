import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 */

/**
 * @author TYM
 * @version 1.0.0
 * @date 2013-12-22
 */
public class Preprocess {
	public static final String FL_INPUT_PATH = "input/";
	public static final String FL_ORIGINAL_PATH = FL_INPUT_PATH + "original/";
	public static final String FL_IN_QTS = FL_ORIGINAL_PATH + "qts.txt";
	public static final String FL_OUT = FL_INPUT_PATH + "corpus.txt";
	
	public static void qtsFormat() throws FileNotFoundException, IOException {
		File inFile = new File(FL_IN_QTS);
		File outFile = new File(FL_OUT);
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		FileWriter writer = new FileWriter(outFile, true);
		
		String line = reader.readLine();
		
		while (null != line) {
			if (line.matches("\\s.*")) {
				writer.append(line.replaceAll("\\s", "").replace("��", System.getProperty("line.separator")) + System.getProperty("line.separator"));
			}			
			line = reader.readLine();
		}
		
		reader.close();
		writer.close();
	}

}

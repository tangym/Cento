import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author TYM
 * @version 1.0.0
 * @date 2013-12-22
 */
public class Preprocess {
	public static final String FL_INPUT_PATH = "input/";
	public static final String FL_ORIGINAL_PATH = FL_INPUT_PATH + "original/";
	public static final String FL_TXT_PATH = FL_ORIGINAL_PATH + "txt/";
	public static final String FL_IN_QTS = FL_ORIGINAL_PATH + "qts.txt";
	public static final String FL_ALLDATA = FL_ORIGINAL_PATH + "alldata.txt";
	public static final String FL_CORPUS = FL_INPUT_PATH + "corpus.txt";
	
	public static void qtsFormat() throws FileNotFoundException, IOException {
		File inFile = new File(FL_IN_QTS);
		File outFile = new File(FL_CORPUS);
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		FileWriter writer = new FileWriter(outFile, true);
		
		String line = reader.readLine();
		
		while (null != line) {
			if (line.matches("\\s.*")) {
				writer.append(line.replaceAll("\\s", "").replace("。", System.getProperty("line.separator")) + System.getProperty("line.separator"));
			}			
			line = reader.readLine();
		}
		
		reader.close();
		writer.close();
	}

	public static void txtFormat() throws FileNotFoundException, IOException {
		File outFile = new File(FL_CORPUS);
		File inPath = new File(FL_TXT_PATH);
		
		FileWriter writer = new FileWriter(outFile);
		
		File[] inFile = inPath.listFiles();
		for (int i=0; i<inFile.length; i++) {
			if (inFile[i].isFile()) {
				BufferedReader reader = new BufferedReader(
						new FileReader(inFile[i]));
				
				String line = reader.readLine();
				
				while (null != line) {
					if (line.matches(".*http.*") || line.matches(".*诗词全集.*共.*") 
							|| line.matches("《.*》") || line.matches("作者：.*年代.*")) {
					}
					else {
						line = line.replace("。", System.getProperty("line.separator"));
						writer.append(line);
					}
					line = reader.readLine();
				}
			}
			else {
				continue;
			}
		}
		writer.close();
	}

	public static void alldataFormat() throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(FL_ALLDATA)), "utf8" ));		
		FileWriter writer = new FileWriter(new File(FL_CORPUS));
				
		String line = reader.readLine();
		while (null != line) {
			if (line.matches(".*http.*") || line.matches(".*诗词全集.*") 
					|| line.matches("《.*") || line.matches("作.*：.*")) {
			}
			else {
				line = line.replace("。", System.getProperty("line.separator"));
				line = line.replace("？", System.getProperty("line.separator"));
				writer.append(line);
			}
			line = reader.readLine();
		}
		reader.close();
		writer.close();
	}
}

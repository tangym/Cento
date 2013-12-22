import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author TYM
 * @version 1.0.0
 * @date 2013-12-22
 */
public class Util {
	public static final String FL_OUTPUT_PATH = "output/";
	public static final String FL_LEXICON = FL_OUTPUT_PATH + "lexicon.txt";
	
	public static void genLexicon() throws FileNotFoundException,IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_OUT)));
		FileWriter writer = new FileWriter(new File(FL_LEXICON));
		
		ArrayList<Character> lexicon = new ArrayList<>();
		ArrayList<Integer> count = new ArrayList<>();
		
		String line = reader.readLine();
		while (null != line) {
			for (int i=0; i<line.length(); i++) {
				Character c = line.charAt(i);
				if (lexicon.contains(c)) {
					int index = lexicon.indexOf(line.charAt(i));
					count.set(index, count.get(index)+1);
				}
				else {
					lexicon.add(c);
					count.add(1);
				}
			}			
			line = reader.readLine();
		}
		
		for (int i=0; i<lexicon.size(); i++) {
			writer.append(lexicon.get(i) + " " + count.get(i) + System.getProperty("line.separator"));
		}
		
		reader.close();
		writer.close();
	}
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author TYM
 *
 */
public class Generate {
	public final static String FL_COOC = Util.FL_COOC;
	public final static String FL_COOC_PARA = Util.FL_COOC_PARA;
	public final static String FL_LEXICON = Util.FL_LEXICON;
	
	//´ÓcorpusÉú³Élexicon
	public static void genLexicon() throws FileNotFoundException,IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		FileWriter writer = new FileWriter(new File(FL_LEXICON));
		
		ArrayList<Character> lexicon = new ArrayList<>();
		ArrayList<Integer> count = new ArrayList<>();
		
		String line = reader.readLine();
		while (null != line) {
			for (int i=0; i<line.length(); i++) {
				Character c = line.charAt(i);
				if (Character.isWhitespace(c.charValue())) {
					continue;
				}
				
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

	public static void genCooc() throws FileNotFoundException,IOException {
		FileWriter writer = new FileWriter(new File(FL_COOC));
		
		HashMap<Character, HashMap<Character, Integer>> coocMap = Util.countCooc();
		Iterator iterator = coocMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			String line = "";
			Map.Entry entry = (Map.Entry) iterator.next();
			Character keyX = (Character) entry.getKey();
			HashMap<Character, Integer> value = 
					(HashMap<Character, Integer>) entry.getValue(); 
			
			line += keyX;
			
			Iterator valueIterator = value.entrySet().iterator();
			while (valueIterator.hasNext()) {
				Map.Entry valueEntry = (Map.Entry) valueIterator.next();
				Character keyY = (Character) valueEntry.getKey();
				Integer count = (Integer) valueEntry.getValue();
				line += ("#" + keyY + " " + count);
			}
			
			
			writer.append(line + System.getProperty("line.separator"));
		}
		writer.close();
	}	
	
	public static void genCoocPara() throws FileNotFoundException,IOException {
		FileWriter writer = new FileWriter(new File(FL_COOC_PARA));
		
		HashMap<Character, HashMap<Character, Integer>> coocMap = Util.countCoocPara();
		Iterator iterator = coocMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			String line = "";
			Map.Entry entry = (Map.Entry) iterator.next();
			Character keyX = (Character) entry.getKey();
			HashMap<Character, Integer> value = 
					(HashMap<Character, Integer>) entry.getValue(); 
			
			line += keyX;
			
			Iterator valueIterator = value.entrySet().iterator();
			while (valueIterator.hasNext()) {
				Map.Entry valueEntry = (Map.Entry) valueIterator.next();
				Character keyY = (Character) valueEntry.getKey();
				Integer count = (Integer) valueEntry.getValue();
				line += ("#" + keyY + " " + count);
			}
			
			
			writer.append(line + System.getProperty("line.separator"));
		}
		writer.close();
	}

}

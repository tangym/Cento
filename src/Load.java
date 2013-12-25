import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class Load {
	public static HashMap<Character, HashMap<Character, Integer>> loadCooc() 
			throws FileNotFoundException,IOException {
		HashMap<Character, HashMap<Character, Integer>> coocMap = new HashMap<>();
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Util.FL_COOC)));
		String line = reader.readLine();
		
		while (null != line) {
			String[] yList = line.split("#");
			Character x = yList[0].charAt(0);
			HashMap<Character, Integer> map = new HashMap<>();
			
			for (int i=1; i<yList.length; i++) {
				Character y = yList[i].charAt(0);
				Integer count = Integer.valueOf(yList[i].split(" ")[1]);
				map.put(y, count);
			}
			coocMap.put(x, map);
		}
		
		return coocMap;
	}

	public static HashMap<Character, Integer> loadLexicon() 
		throws FileNotFoundException,IOException {
		HashMap<Character, Integer> lexicon = new HashMap<>();
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Util.FL_LEXICON)));
		String line = reader.readLine();
		
		while (null != line) {
			Character key = line.split(" ")[0].charAt(0);
			Integer value = Integer.valueOf(line.split(" ")[1]);
			lexicon.put(key, value);
			line = reader.readLine();
		}
		
		return lexicon;
	}
}

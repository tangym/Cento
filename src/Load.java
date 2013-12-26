import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

/**
 * @author TYM
 *
 */
public class Load {
	public static HashMap<Character, HashMap<Character, Integer>> loadCooc() 
			throws FileNotFoundException,IOException {
		HashMap<Character, HashMap<Character, Integer>> coocMap = new HashMap<>();
		
		File f = new File(Util.FL_COOC);
		List<String> lines = Files.readAllLines(f.toPath(), Charset.defaultCharset());
		
		for (int n=0; n<lines.size(); n++) {
			String[] yList = lines.get(n).split("#");
			Character x = yList[0].charAt(0);
			HashMap<Character, Integer> map = new HashMap<>();
			
			for (int i=1; i<yList.length; i++) {
				Character y = yList[i].charAt(0);
				Integer count = Integer.valueOf(yList[i].split(" ")[1]);
				map.put(y, count);
			}
			coocMap.put(x, map);
			
			if (0 == n % 1000) {
				System.out.printf("COOC %.1f", (float)100*n/lines.size());
				System.out.println("% completed.");
			}
		}
		return coocMap;
	}

	public static HashMap<Character, HashMap<Character, Integer>> loadCoocPara() 
			throws FileNotFoundException,IOException {
		HashMap<Character, HashMap<Character, Integer>> coocMap = new HashMap<>();
		
		File f = new File(Util.FL_COOC_PARA);
		List<String> lines = Files.readAllLines(f.toPath(), Charset.defaultCharset());
		
		for (int n=0; n<lines.size(); n++) { 
			String[] yList = lines.get(n).split("#");
			Character x = yList[0].charAt(0);
			HashMap<Character, Integer> map = new HashMap<>();
			
			for (int i=1; i<yList.length; i++) {
				Character y = yList[i].charAt(0);
				Integer count = Integer.valueOf(yList[i].split(" ")[1]);
				map.put(y, count);
			}
			coocMap.put(x, map);
			if (0 == n % 1000) {
				System.out.printf("COOC_PARA %.1f", (float)100*n/lines.size());
				System.out.println("% completed.");
			}
		}
		
		return coocMap;
	}
	
	public static HashMap<Character, Integer> loadLexicon() 
		throws FileNotFoundException,IOException {
		HashMap<Character, Integer> lexicon = new HashMap<>();
		
		List<String> lines = Files.readAllLines(
				new File(Util.FL_LEXICON).toPath(), Charset.defaultCharset());
		
		for (int n=0; n<lines.size(); n++) {
			Character key = lines.get(n).trim().split(" ")[0].charAt(0);
			Integer value = Integer.valueOf(lines.get(n).trim().split(" ")[1]);
			lexicon.put(key, value);
			if (0 == n % 1000) {
				System.out.printf("LEX %.1f", (float)100*n/lines.size());
				System.out.println("% completed.");
			}
		}
		
		return lexicon;
	}
	
	public static List<String> loadCorpus() throws IOException {
		File file = new File(Preprocess.FL_CORPUS);
		return Files.readAllLines(file.toPath(), Charset.defaultCharset());
	}
}



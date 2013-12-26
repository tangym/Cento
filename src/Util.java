import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;

/**
 * @author TYM
 * @version 1.0.0
 * @date 2013-12-22
 */
public class Util {
	public static final String FL_OUTPUT_PATH = "output/";
	public static final String FL_LEXICON = FL_OUTPUT_PATH + "lexicon.txt";
	public static final String FL_COOC_PARA = FL_OUTPUT_PATH + "cooc.para.txt";
	public static final String FL_COOC = FL_OUTPUT_PATH + "cooc.txt";
	public static final String FL_CORPUS = Preprocess.FL_CORPUS;
	
	public static int CORPUS_SIZE=0;
	public static int N_BEST = 5;
	public static HashMap<Character, Integer> LEXICON = null;
	public static HashMap<Character, HashMap<Character, Integer>> COOC = null;
	public static HashMap<Character, HashMap<Character, Integer>> COOC_PARA = null;
	public static List<String> lines = null;
	
	public static void countCorpusCharNum() 
			throws FileNotFoundException,IOException {		
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		CORPUS_SIZE=0;
		String line = reader.readLine();
		while (null != line) {
			CORPUS_SIZE += line.length();
			line = reader.readLine();
		}
	}
	
	public static float calMI(Character x, Character y) 
			throws FileNotFoundException,IOException {
		if (null == LEXICON) {
			LEXICON = Load.loadLexicon();
		}
		if (null == COOC) {
			COOC = Load.loadCooc();
		}
		
		int pxy = 0;
		int px = 0;
		int py = 0;
		
		if (LEXICON.containsKey(x)) {
			px = LEXICON.get(x);
		}
		
		if (LEXICON.containsKey(y)) {
			py = LEXICON.get(y);
		}
		
		if (COOC.containsKey(x)) {
			if (COOC.get(x).containsKey(y)) {
				pxy = COOC.get(x).get(y);
			}
		}
		else if (COOC.containsKey(y)) {
			if (COOC.containsKey(x)) {
				pxy = COOC.get(y).get(x);
			}
		}
		
	
		//简单add 1平滑一下
		if (0 == pxy) {
			pxy = 1;
		}
		if (0 == px) {
			px = 1;
		}
		if (0 == py) {
			py = 1;
		}
		
		return (float) ((pxy)*Math.log(pxy*(double)CORPUS_SIZE/(px*(double)py))/Math.log(2)/CORPUS_SIZE);
	}

	public static float calMIPara(Character x, Character y) 
			throws FileNotFoundException,IOException {
		if (null == LEXICON) {
			LEXICON = Load.loadLexicon();
		}
		if (null == COOC) {
			COOC = Load.loadCooc();
		}
		if (null == COOC_PARA) {
			COOC_PARA = Load.loadCoocPara();
		}
		
		int pxy = 0;
		int px = 0;
		int py = 0;
		
		if (LEXICON.containsKey(x)) {
			px = LEXICON.get(x);
		}
		
		if (LEXICON.containsKey(y)) {
			py = LEXICON.get(y);
		}
		
		if (COOC_PARA.containsKey(x)) {
			if (COOC_PARA.get(x).containsKey(y)) {
				pxy = COOC_PARA.get(x).get(y);
			}
		}
		else if (COOC_PARA.containsKey(y)) {
			if (COOC_PARA.containsKey(x)) {
				pxy = COOC_PARA.get(y).get(x);
			}
		}
		
	
		//简单add 1平滑一下
		if (0 == pxy) {
			pxy = 1;
		}
		if (0 == px) {
			px = 1;
		}
		if (0 == py) {
			py = 1;
		}
		
		return (float) ((pxy)*Math.log(pxy*(double)CORPUS_SIZE/(px*(double)py))/Math.log(2)/CORPUS_SIZE);
	}
	
	public static int countCooc(String x, String y) 
			throws FileNotFoundException,IOException {
		int pxy = 0;
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		
		//判断连续的两句诗
		String line = reader.readLine();
		String nextLine = "";
		
		while (null != line) {
			nextLine = reader.readLine();
			
			if (line.contains(x)) {
				if (line.contains(y)) {
					pxy++;
				}
				else {
					if (null != nextLine) {
						if (nextLine.contains(y)) {
							pxy++;
						}
					}
				}
			}			
			line = nextLine;
		}		

		return pxy;
	}
	
	public static HashMap<Character, HashMap<Character, Integer>> countCoocPara() 
		throws FileNotFoundException,IOException {
		HashMap<Character, HashMap<Character, Integer>> coocMap = 
				new HashMap<Character, HashMap<Character, Integer>>();
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		String line = reader.readLine();
		
		int l=0;
		while (null != line) {
			System.out.println(++l);
			line = line.replace("）", "").replace("（", "");
			String[] pair = line.split("，");
			//判断只有当对仗字数相等时才统计
			if (2 != pair.length) {}
			else if (pair[0].length() != pair[1].length()) {}
			else {
				for (int i=0; i<pair[0].length(); i++) {
					Character x = pair[0].charAt(i);
					Character y = pair[1].charAt(i);
			
					if (Character.isWhitespace(x.charValue()) 
							|| Character.isWhitespace(y.charValue())) {
						continue;
					}
					
					if(x.equals(y)) {
						continue;
					}
					else {
						boolean containsX = coocMap.containsKey(x);
						boolean containsY = coocMap.containsKey(y);
						boolean counted = false;
																
						if (containsX) {
							HashMap<Character, Integer> tempMap = coocMap.get(x);
							if (tempMap.containsKey(y)) {
								tempMap.put(y, tempMap.get(y)+1);
								counted = true;
							}
						}
						if (!counted && containsY) {
							HashMap<Character, Integer> tempMap = coocMap.get(y);
							if (tempMap.containsKey(x)) {
								tempMap.put(x, tempMap.get(x)+1);
								counted = true;
							}
						}
						if (!counted) {
							if (containsX) {
								HashMap<Character, Integer> tempMap = coocMap.get(x);
								tempMap.put(y, 1);
							}
							else if (containsY) {
								HashMap<Character, Integer> tempMap = coocMap.get(y);
								tempMap.put(x, 1);
							}
							else {
								HashMap<Character, Integer> map = new HashMap<>();
								map.put(y, 1);
								coocMap.put(x, map);	
							}
						}

					}
				}
			}
			line = reader.readLine();
		}
		
		return coocMap;
	} 
	
	public static HashMap<Character, HashMap<Character, Integer>> countCooc() 
			throws FileNotFoundException,IOException {
			HashMap<Character, HashMap<Character, Integer>> coocMap = 
					new HashMap<Character, HashMap<Character, Integer>>();
			BufferedReader reader = new BufferedReader(
					new FileReader(new File(Preprocess.FL_CORPUS)));
			String line = reader.readLine();
					
			int l = 0;
			while (null != line) {
				System.out.println(++l);
				line = line.replace("）", "").replace("（", "").replace("，", "");
				//判断只有当对仗字数相等时才统计
				for (int i=0; i<line.length(); i++) {
					for (int j=i+1; j<line.length(); j++) { 
						Character x = line.charAt(i);
						Character y = line.charAt(j);
						
						if (Character.isWhitespace(x.charValue()) 
								|| Character.isWhitespace(y.charValue())) {
							continue;
						}
							
						if(x.equals(y)) {
							continue;
						}
						else {
							boolean containsX = coocMap.containsKey(x);
							boolean containsY = coocMap.containsKey(y);
							boolean counted = false;
																	
							if (containsX) {
								HashMap<Character, Integer> tempMap = coocMap.get(x);
								if (tempMap.containsKey(y)) {
									tempMap.put(y, tempMap.get(y)+1);
									counted = true;
								}
							}
							if (!counted && containsY) {
								HashMap<Character, Integer> tempMap = coocMap.get(y);
								if (tempMap.containsKey(x)) {
									tempMap.put(x, tempMap.get(x)+1);
									counted = true;
								}
							}
							if (!counted) {
								if (containsX) {
									HashMap<Character, Integer> tempMap = coocMap.get(x);
									tempMap.put(y, 1);
								}
								else if (containsY) {
									HashMap<Character, Integer> tempMap = coocMap.get(y);
									tempMap.put(x, 1);
								}
								else {
									HashMap<Character, Integer> map = new HashMap<>();
									map.put(y, 1);
									coocMap.put(x, map);	
								}
							}

						}
					}
				}
				line = reader.readLine();
			}
			
			return coocMap;
		} 
		
	
	public static float calSentenceMI(String sentenceX, String sentenceY)
				throws FileNotFoundException,IOException {
		float mi = 0;
		if (sentenceX.length() != sentenceY.length()) {
			return mi;
		}
		else {
			//一些硬性条件
			//上下句有相同的字则返回0
			for (int i=0; i<sentenceX.length(); i++) {
				if (sentenceY.contains(sentenceX.subSequence(i, i+1))) {
					return mi;
				}
			}
			//TODO 对应位置字不相同，返回0，不好，应减少mi
			for (int i=0; i<sentenceX.length(); i++) {
				for (int j=i+1; j<sentenceY.length(); j++) {
					if (sentenceX.charAt(i) == sentenceX.charAt(j)) {
						if (sentenceY.charAt(i) != sentenceY.charAt(j)) {
							return mi;
						}
					}
					if (sentenceY.charAt(i) == sentenceY.charAt(j)) {
						if (sentenceX.charAt(i) != sentenceX.charAt(j)) {
							return mi;
						}
					}
				}
			}
			
			for (int i=0; i<sentenceX.length(); i++) {
				mi += calMIPara(sentenceX.charAt(i), sentenceY.charAt(i));
				for (int j=0; j<sentenceY.length(); j++) {
					mi += (0.001 * calMI(sentenceX.charAt(i), sentenceY.charAt(j)));
				}
			}
			return mi;
		}
	}
 
 	public static String[] cento(String sentence) 
				throws FileNotFoundException,IOException,InterruptedException,ExecutionException {
		String[] best = new String[N_BEST];
		float[] bestMI = new float[N_BEST];
		for (int i=0; i<N_BEST; i++) {
			best[i] = "";
			bestMI[i] = 0;
		}
		
		for (int numLine=0; numLine<lines.size(); numLine++) {
			String[] tokens = lines.get(numLine).trim().split("，");
			
			for (int i=0; i<tokens.length; i++) {
				float mi = calSentenceMI(sentence, tokens[i].replace("（", "").replace("）", ""));
				//将该句的互信息依次与最高的若干项比较，如果更高，则记录
				for (int j=0; j<best.length; j++) {					
					if (mi > bestMI[j]) {
						for (int k=j; k<best.length-1; k++) {
							best[k+1] = best[k];
							bestMI[k+1] = bestMI[k];
						}
						best[j] = tokens[i];
						bestMI[j] = mi;
						break;
					}
					else {
						continue;
					}
				}
			}
		}
		return best;
	}
}

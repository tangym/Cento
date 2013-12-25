import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sun.misc.Cache;
import sun.org.mozilla.javascript.internal.ast.ThrowStatement;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
//	public static final String FL_LEXICON_SORTED = FL_OUTPUT_PATH + "lexicon.sorted.txt";
	public static final String FL_COOC = FL_OUTPUT_PATH + "cooc.txt";
	public static final String FL_CORPUS = Preprocess.FL_CORPUS;
	
	public static int CORPUS_SIZE=0;
	public static int N_BEST = 1;
	public static HashMap<Character, Integer> LEXICON = null;
	public static HashMap<Character, HashMap<Character, Integer>> COOC = null;

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
		
		
		//TODO pxy,px和py暂时只用频数代替，频率不知道具体怎么求(写成了出现次数/语料库大小)
		return (float) ((pxy)*Math.log((float)pxy*CORPUS_SIZE/(px*py))/Math.log(2));
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
	
	public static HashMap<Character, HashMap<Character, Integer>> countCooc() 
		throws FileNotFoundException,IOException {
		HashMap<Character, HashMap<Character, Integer>> coocMap = 
				new HashMap<Character, HashMap<Character, Integer>>();
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		String line = reader.readLine();
		
		while (null != line) {
			//TODO 读每一行并填coocMap
			for (int i=0; i<line.length(); i++) {
				for (int j=i+1; j<line.length(); j++) {
					Character x = line.charAt(i);
					Character y = line.charAt(j);
					
					if (coocMap.containsKey(x)) {
						HashMap<Character, Integer> tempMap = coocMap.get(x);
						if (tempMap.containsKey(y)) {
							tempMap.put(y, tempMap.get(y)+1);
						}
						else {
							tempMap.put(y, 1);
						}
					}
					else if (coocMap.containsKey(y)) {
						HashMap<Character, Integer> tempMap = coocMap.get(y);
						if (tempMap.containsKey(x)) {
							tempMap.put(x, tempMap.get(x)+1);
						}
						else {
							tempMap.put(y, 1);
						}
					}
					else {
						HashMap<Character, Integer> map = new HashMap<>();
						map.put(y, 1);
						coocMap.put(x, map);
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
			for (int i=0; i<sentenceX.length(); i++) {
				mi += calMI(sentenceX.charAt(i), sentenceY.charAt(i)); 
			}
			return mi;
		}
	}
 
 	public static String[] cento(String sentence) 
				throws FileNotFoundException,IOException {
		String[] best = new String[N_BEST];
		float[] bestMI = new float[N_BEST];
		for (int i=0; i<N_BEST; i++) {
			best[i] = "";
			bestMI[i] = 0;
		}
		
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		String line = reader.readLine();

		while (null != line) {
			String[] tokens = line.split("，");
			for (int i=0; i<tokens.length; i++) {
				float mi = calSentenceMI(sentence, tokens[i]);
				//将该句的互信息依次与最高的若干项比较，如果更高，则记录
				//TODO 修改为从后向前比较
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

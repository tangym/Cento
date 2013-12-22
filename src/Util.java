import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
	public static final String FL_LEXICON_SORTED = FL_OUTPUT_PATH + "lexicon.sorted.txt"; 
	public static int CORPUS_SIZE=0;

	
	//从corpus生成lexicon
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

	public static void countCorpusCharNum() 
			throws FileNotFoundException,IOException {		
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_OUT)));
		CORPUS_SIZE=0;
		String line = reader.readLine();
		while (null != line) {
			CORPUS_SIZE += line.length();
			line = reader.readLine();
		}
	}
	
	public static float calMI(String x, String y) 
			throws FileNotFoundException,IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(new File(Preprocess.FL_OUT)));
		
		//判断连续的两句诗
		String line = reader.readLine();
		String nextLine = "";
		int pxy = 0;
		int px = 0;
		int py = 0;
		
		while (null != line) {
			nextLine = reader.readLine();
			
			if (line.contains(x)) {
				px++;
				
				//计算xy的共现概率
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
			
			if (line.contains(y)) {
				py++;
			}
			
			line = nextLine;
		}
		
		//TODO pxy,px和py暂时只用频数代替，频率不知道具体怎么求(写成了出现次数/语料库大小)
		return (float) (Math.log((float)pxy*CORPUS_SIZE/(px*py))/Math.log(2));
	}

	public static float calSentenceMI(String sentenceX, String sentenceY)
				throws FileNotFoundException,IOException {
		float mi = 0;
		if (sentenceX.length() != sentenceY.length()) {
			return mi;
		}
		else {
			for (int i=0; i<sentenceX.length(); i++) {
				mi += calMI(sentenceX.substring(i, i+1), 
						sentenceY.substring(i, i+1)); 
			}
			return mi;
		}
	}
}

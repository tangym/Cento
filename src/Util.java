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
//	public static final String FL_LEXICON_SORTED = FL_OUTPUT_PATH + "lexicon.sorted.txt";
	public static final String FL_COOC = FL_OUTPUT_PATH + "cooc.txt";
	public static int CORPUS_SIZE=0;
	public static int N_BEST = 2;

	
	//从corpus生成lexicon
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
				new FileReader(new File(Preprocess.FL_CORPUS)));
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
				new FileReader(new File(Preprocess.FL_CORPUS)));
		
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
 
	public static void genCooc() throws FileNotFoundException,IOException {
		BufferedReader readerLex = new BufferedReader(
				new FileReader(new File(FL_LEXICON)));
		BufferedReader readerCor = new BufferedReader(
				new FileReader(new File(Preprocess.FL_CORPUS)));
		FileWriter writer = new FileWriter(new File(FL_COOC));
		
		ArrayList<String> lexicon = new ArrayList<>();
		
		String x = readerLex.readLine();
		while (null != x) {
			x = x.split(" ")[0];
			lexicon.add(x);
			x = readerLex.readLine();
		}
		
		for (int i=0; i<lexicon.size(); i++) {
			for (int j=i+1; j<lexicon.size(); j++) {
				int pxy = countCooc(lexicon.get(i), lexicon.get(j));
				if (0 != pxy) {
					writer.append(lexicon.get(i) + " " + lexicon.get(j) 
							+ " " +  pxy + System.getProperty("line.separator"));
				}
				
				if ((i+1)*j%50 == 0) {
					System.out.println((float) ((2*lexicon.size()-i-1)*i+2*j)/((1+lexicon.size())*lexicon.size()) + "%");
				}
			}	
		}
		
		readerCor.close();
		readerLex.close();
		writer.close();
	}
	
	//TODO 修改
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * @author TYM
 * @version 1.0.0
 * @date 2013-12-22
 */
public class MainClass {
	public static final int EXIT_FILE_NOT_FOUND = 1;
	public static final int EXIT_IOEXCEPTION = 2;
	
	public static void main(String[] args) throws Exception {
		try {
			//将语料库统一格式写入corpus.txt文件
//			Preprocess.qtsFormat();
//			Preprocess.alldataFormat();
			//生成lexicon.txt文件
//			Generate.genLexicon();
			//生成共现文件
//			Generate.genCoocPara();
//			Generate.genCooc();
			
			//通过修改N_BEST的值来改变取最优的多少项
//			Util.N_BEST=5;
			
			//将训练好的模型载入程序
			Util.countCorpusCharNum();
			Util.COOC = Load.loadCooc();
			Util.LEXICON = Load.loadLexicon();
			Util.COOC_PARA = Load.loadCoocPara();
			Util.lines = Load.loadCorpus();
			System.out.println("File loaded.");

			BufferedReader in = new BufferedReader(
					new InputStreamReader(System.in));
			
			String input = "";
			System.out.println("Input sentence (press ENTER to exit):");
			input = in.readLine();
			while (!input.equals("")) {
				//可以通过以下语句大致计时
//				long begin = System.currentTimeMillis();
				String[] centos = Util.cento(input);
				long end = System.currentTimeMillis();
				
				//随机选前若干个中的一个作为结果
				Random random = new Random(end);
				System.out.println(centos[random.nextInt(centos.length)]);
//				System.out.println(end-begin);
				System.out.println("Input sentence (press ENTER to exit):");
				input = in.readLine();
			}
		}
		catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
			System.exit(EXIT_FILE_NOT_FOUND);
		}
		catch (IOException ioe) {
			System.out.println(ioe);
			System.exit(EXIT_IOEXCEPTION);
		}
	}
}

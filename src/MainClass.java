import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 */

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
//			Preprocess.qtsFormat();
//			Preprocess.alldataFormat();
//			Generate.genLexicon();
//			Generate.genCoocPara();
//			Generate.genCooc();
			
			Util.countCorpusCharNum();
			Util.COOC = Load.loadCooc();
			Util.LEXICON = Load.loadLexicon();
			Util.COOC_PARA = Load.loadCoocPara();
			System.out.println("载入完成。");
			
			long begin = System.currentTimeMillis();
			String[] centos = Util.cento("两个黄鹂鸣翠柳");
			long end = System.currentTimeMillis();
			for (int i=0; i<centos.length; i++) {
				System.out.println(centos[i]);	
			}
			System.out.println(end-begin);
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

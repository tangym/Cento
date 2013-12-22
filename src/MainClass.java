import java.io.FileNotFoundException;
import java.io.IOException;

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
	
	public static void main(String[] args) {
		try {
//			Preprocess.qtsFormat();
//			Util.genLexicon();
			Util.countCorpusCharNum();
			System.out.println(Util.CORPUS_SIZE);
			System.out.println(Util.calSentenceMI("海上生明月", "海上生明月"));
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

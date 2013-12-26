import java.util.concurrent.Callable;
/**
 * 
 */

/**
 * @author TYM
 *
 */
public class CallableCento implements Callable {
	private String sentenceX = "";
	private String sentenceY = "";
	
	public CallableCento(String x, String y) {
		sentenceX = x;
		sentenceY = y;
	}
	public Float call() throws Exception {
		return Util.calSentenceMI(sentenceX, sentenceY);
	}
}

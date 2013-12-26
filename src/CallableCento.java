import java.util.concurrent.Callable;

/**
 * 无关文件
 * 本想通过多线程加速，但因为瓶颈是IO，因此加速不明显
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

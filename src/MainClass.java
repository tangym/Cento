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
			//�����Ͽ�ͳһ��ʽд��corpus.txt�ļ�
//			Preprocess.qtsFormat();
//			Preprocess.alldataFormat();
			//����lexicon.txt�ļ�
//			Generate.genLexicon();
			//���ɹ����ļ�
//			Generate.genCoocPara();
//			Generate.genCooc();
			
			//ͨ���޸�N_BEST��ֵ���ı�ȡ���ŵĶ�����
//			Util.N_BEST=5;
			
			//��ѵ���õ�ģ���������
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
				//����ͨ�����������¼�ʱ
//				long begin = System.currentTimeMillis();
				String[] centos = Util.cento(input);
				long end = System.currentTimeMillis();
				
				//���ѡǰ���ɸ��е�һ����Ϊ���
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

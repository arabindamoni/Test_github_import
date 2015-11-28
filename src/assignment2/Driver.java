package assignment2;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Driver {

	public static void main(String[] args) throws Exception {

		// String params[] = {"/home/arabinda/Documents/NLP assignment
		// 2/stanford-postagger-2015-04-20/models/english-left3words-distsim.tagger","/home/arabinda/Documents/NLP
		// assignment 2/ner.txt"};
		// FeatureGenerator.generate(params[0],params[1],"/home/arabinda/Documents/NLP
		// assignment 2/training");
		String dir = "Resources/";
		CVDataCreator dc = new CVDataCreator();
		String params[];
		Process process;
		for (int i = 0; i < 10; i++) {
			System.out.println("\n\n::::::::::::::::Cross Validation: iteration "+i+"::::::::::::::::\n");
			dc.generateDataSet(dir + "NLP_assignment_2/tagged", dir + "NLP_assignment_2/cross_validation/");
			
			params = new String("java -cp " + dir + "NLP_assignment_2/mallet-2.0.8RC3/class:" + dir
					+ "NLP_assignment_2/mallet-2.0.8RC3/lib/mallet-deps.jar"
					+ " cc.mallet.fst.SimpleTagger --iterations 50 --train true " + "--threads 8 --model-file " + dir
					+ "NLP_assignment_2/model2_pos " + dir + "NLP_assignment_2/cross_validation/train").split(" ");
			process = Runtime.getRuntime().exec(params);
			/*BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = null;
			String previous = null;
		   while ((line = br.readLine()) != null)
				if (!line.equals(previous)) {
					previous = line;
					System.out.println(line);
				}
			 */
			if (process.waitFor() == 0) {
				//System.out.println("Training Complete");				
			}
			
			//test
			params = new String("java -cp " + dir + "NLP_assignment_2/mallet-2.0.8RC3/class:" + dir
					+ "NLP_assignment_2/mallet-2.0.8RC3/lib/mallet-deps.jar"
					+ " cc.mallet.fst.SimpleTagger " + "--threads 8 --model-file " + dir
					+ "NLP_assignment_2/model2_pos " + dir + "NLP_assignment_2/cross_validation/testNotag").split(" ");
			process = Runtime.getRuntime().exec(params);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
						
			FileWriter fw = new FileWriter(dir+"NLP_assignment_2/cross_validation/out");
			while ((line = br.readLine()) != null){				
				//System.out.println(line);		
				fw.write(line+"\n");
			}		
			fw.close();
			if (process.waitFor() == 0) {
				//System.out.println("Testing Complete");				
			}
			
			//get scores
			Evaluation ev = new Evaluation();
			System.out.println(ev.getF1Score(dir+"NLP_assignment_2/cross_validation/out", dir+"/NLP_assignment_2/cross_validation/test"));
		}
	}

}

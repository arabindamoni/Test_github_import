package assignment2;

import java.io.IOException;

public class Train {
		
	public static void main(String[] args) throws IOException, InterruptedException {
		// String params[] = {"/home/arabinda/Documents/NLP assignment
		// 2/stanford-postagger-2015-04-20/models/english-left3words-distsim.tagger","/home/arabinda/Documents/NLP
		// assignment 2/ner.txt"};
		// FeatureGenerator.generate(params[0],params[1],"/home/arabinda/Documents/NLP
		// assignment 2/training");
		
		String dir = "Resources/";
		CVDataCreator dc = new CVDataCreator();
		String params[];
		Process process;					
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

	}	
}

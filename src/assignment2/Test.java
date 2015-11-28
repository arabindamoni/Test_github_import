package assignment2;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Test {
	public static void test(String infile, String outfile) throws Exception{
		String dir = "Resources/";		
		String params[];
		Process process;
		TestFeatureGenerator.generate(dir+"NLP_assignment_2/stanford-postagger-2015-04-20/models/english-left3words-distsim.tagger",infile,dir+"NLP_assignment_2/cross_validation/testNotag");
		
		params = new String("java -cp " + dir + "NLP_assignment_2/mallet-2.0.8RC3/class:" + dir
				+ "NLP_assignment_2/mallet-2.0.8RC3/lib/mallet-deps.jar"
				+ " cc.mallet.fst.SimpleTagger " + "--threads 8 --model-file " + dir
				+ "NLP_assignment_2/model2_pos " + dir + "NLP_assignment_2/cross_validation/testNotag").split(" ");
		process = Runtime.getRuntime().exec(params);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = null;
					
		FileWriter fw = new FileWriter(outfile);
		while ((line = br.readLine()) != null){				
			//System.out.println(line);		
			fw.write(line+"\n");
		}		
		fw.close();
		if (process.waitFor() == 0) {
			//System.out.println("Testing Complete");				
		}
	}

	public static void main(String[] args) throws Exception {
		//test(args[0],args[1]);
		test("Resources/NLP_assignment_2/test1","Resources/NLP_assignment_2/out");
	}

}

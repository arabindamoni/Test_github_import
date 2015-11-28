package assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CVDataCreator {
	/*
	 * @param file: file containing sentences	 
	 * */
	public void generateDataSet(String file, String outdir) throws IOException{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		//list of sentences
		ArrayList<String> sList = new ArrayList<>();
		ArrayList<String> sListNotag = new ArrayList<>();
		String sentence="";
		String sentenceNotag = "";
		while ((line = br.readLine()) != null) {			
			if(line.isEmpty()){
				sList.add(sentence);
				sentence = "";
				sListNotag.add(sentenceNotag);
				sentenceNotag = "";
			}
			else{
				sentence += line+"\n";
				sentenceNotag += line.substring(0, line.lastIndexOf(" "))+"\n";
			}
		}
		br.close();
		fr.close();
		
		int N = sList.size();
		boolean marked[] = new boolean[N];
		//generate training set
		FileWriter fw1 = new FileWriter(outdir+"train");
		Random r = new Random(System.currentTimeMillis());
		int i=0;
		while(i<N*0.8){
			int k = r.nextInt(N);			
			if(!marked[k]){
				marked[k]=true;
				fw1.write(sList.get(k)+"\n");
				i++;
			}
		}
		fw1.close();

		// generate development set
		FileWriter fw2 = new FileWriter(outdir + "development");
		i = 0;
		while (i < N * 0.1) {
			int k = r.nextInt(N);
			if (!marked[k]) {
				marked[k] = true;
				fw2.write(sList.get(k) + "\n");
				i++;
			}
		}
		fw2.close();
		
		//generate test dataset with labels
		FileWriter fw3 = new FileWriter(outdir+"test");		
		FileWriter fw4 = new FileWriter(outdir+"testNotag");			
		for(int j=0;j<N;j++){
			if(!marked[j]){
				fw3.write(sList.get(j)+"\n");
				fw4.write(sListNotag.get(j)+"\n");
			}
		}
		fw3.close();						
		fw4.close();
	}

	public static void main(String[] args) throws IOException {
		CVDataCreator dc = new CVDataCreator();
		dc.generateDataSet("/home/arabinda/Documents/NLP assignment 2/tagged", "/home/arabinda/Documents/NLP assignment 2/cross_validation/");
	}

}

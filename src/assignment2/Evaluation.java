package assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Evaluation {
	public class score{
		int tp = 0;
		int fp = 0;
		int tn = 0;
		int fn = 0;
	}
	public double getF1Score(String evaluated, String actual) throws IOException{		
		FileReader fr1 = new FileReader(evaluated);
		FileReader fr2 = new FileReader(actual);
		BufferedReader eval = new BufferedReader(fr1);
		BufferedReader act = new BufferedReader(fr2);
		String line = "";
		score sc[] = new score[3];
		for(int i=0;i<3;i++) {sc[i] = new score();}
		while ((line = eval.readLine()) != null) {
			
			if(line.isEmpty()) {
				act.readLine();
				continue;
			}
			
			String toks[] = line.split(" ");			
			String evallabel = toks[toks.length-1]; 
			
			line = act.readLine();
			toks = line.split(" ");			
			String truelabel =  toks[toks.length-1];			
				
			// for label T
			if(truelabel.equals("T") && evallabel.equals("T")){
				sc[0].tp++;
			}
			else if(truelabel.equals("T") && !evallabel.equals("T")){
				sc[0].fn++;
			}
			else if(!truelabel.equals("T") && evallabel.equals("T")){
				sc[0].fp++;
			}
			else{
				sc[0].tn++;
			}
			
			//for label O
			if(truelabel.equals("O") && evallabel.equals("O")){
				sc[1].tp++;
			}
			else if(truelabel.equals("O") && !evallabel.equals("O")){
				sc[1].fn++;
			}
			else if(!truelabel.equals("O") && evallabel.equals("O")){
				sc[1].fp++;
			}
			else{
				sc[1].tn++;
			}
			
			//for label D
			if(truelabel.equals("D") && evallabel.equals("D")){
				sc[2].tp++;
			}
			else if(truelabel.equals("D") && !evallabel.equals("D")){
				sc[2].fn++;
			}
			else if(!truelabel.equals("D") && evallabel.equals("D")){
				sc[2].fp++;
			}
			else{
				sc[2].tn++;
			}
			
		}
		eval.close();
		act.close();
		fr1.close();
		fr2.close();
		double glob_f1 = 0;
		for(int i=0;i<3;i++){
			switch(i){
			case 0: System.out.println("Score for class T"); break;
			case 1: System.out.println("Score for class O"); break;
			case 2: System.out.println("Score for class D"); break;
			}
			System.out.printf("tp: %d \t tn: %d \t fp: %d \t fn: %d\n", sc[i].tp,sc[i].tn,sc[i].fp,sc[i].fn);
			System.out.println("accuracy: "+(sc[i].tp+sc[i].tn)/(double)(sc[i].tp+sc[i].tn+sc[i].fp+sc[i].fn));
			System.out.println("precision :"+sc[i].tp/(double)(sc[i].tp+sc[i].fp));
			System.out.println("recall: "+sc[i].tp/(double)(sc[i].tp+sc[i].fn));
			double f1 =  2*sc[i].tp/(double)(2*sc[i].tp+sc[i].fp+sc[i].fn);
			System.out.println("F1: "+ f1+"\n");
		}
		return glob_f1/3;
	}

	public static void main(String args[]) throws IOException{
		String dir="/home/arabinda/Documents/";
		Evaluation ev = new Evaluation();
		System.out.println(ev.getF1Score(dir+"NLP assignment 2/o", dir+"/NLP assignment 2/test"));
	}
}

package assignment1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Problem1 {
	
	private HashMap<Integer,String> vocabulary;	
	private HashMap<String,HashMap<Integer,Double>> ngram;  // key string is formed as ""+x_(t-n)+"_"+x_(t-n+1) .... +"_"+x_(t-1) 
	
	private void loadVocabulary(String voc_file) throws IOException{
		long st = System.currentTimeMillis();
		vocabulary = new HashMap<>();
		Scanner sc = new Scanner(new File(voc_file));
		while(sc.hasNext()){
			vocabulary.put(sc.nextInt(), sc.next());
		}
		sc.close();
		System.out.println("Vocabulary loaded in "+(double)(System.currentTimeMillis()-st)/1000+" secs");
	}
	
	private void loadUnigram(String file) throws IOException{
		long st = System.currentTimeMillis();
		ngram = new HashMap<>(150000);
		Scanner sc = new Scanner(new File(file));
		while(sc.hasNext()){
			ngram.put(""+sc.nextInt(), Math.pow(10,sc.nextDouble()));
		}
		sc.close();
		System.out.println("Unigram loaded in "+(double)(System.currentTimeMillis()-st)/1000+" secs");
	}
	
	private void loadBigram(String file) throws IOException {
		long st = System.currentTimeMillis();		
		Scanner sc = new Scanner(new File(file));
		while (sc.hasNext()) {
			int xt_1=sc.nextInt();  // x_(t-1)
			int xt=sc.nextInt();  // x_t 
			if(!bigram.containsKey(xt_1)){
				bigram.put(xt_1, new HashMap<Integer,Double>());
			}
			bigram.get(xt_1).put(xt, Math.pow(10,sc.nextDouble()));
		}
		sc.close();
		System.out.println("Bigram loaded in "+(double)(System.currentTimeMillis()-st)/1000+" secs");
	}
	
	private void loadTrigram(String file) throws IOException {
		long st = System.currentTimeMillis();
		trigram = new HashMap<>();
		Scanner sc = new Scanner(new File(file));
		while (sc.hasNext()) {
			int xt_2 = sc.nextInt();  //x_(t-2)
			int xt_1 = sc.nextInt();  //x_(t-1)
			int xt = sc.nextInt();  //x
			String key = ""+xt_2+"_"+xt_1;
			if(!trigram.containsKey(key)){
				trigram.put(key, new HashMap<Integer,Double>());
			}
			trigram.get(key).put(xt, Math.pow(10,sc.nextDouble()));			
		}
		sc.close();
		System.out.println("Trigram loaded in "+(double)(System.currentTimeMillis()-st)/1000+" secs");
	}
	
	// returns -1 if the pdf is invalid 
	private int getRandomFromDist(HashMap<Integer,Double> pdf){		
		if(pdf == null || pdf.isEmpty()) return -1;
		double sum = 0;
		for(double d: pdf.values()) sum += d;		
		Random r = new Random();
		double rand = r.nextDouble()*sum;
		double t = 0;
		for(int i: pdf.keySet()){
			t+=pdf.get(i);
			if(t  >= rand) return i;  
		}
		return -1;
	}
	
	private String generateSentence(){
		StringBuilder sen = new StringBuilder();
		int xt,xt_1,xt_2;
		xt = -1;
		xt_1 = 153; 
		xt_2= 153;
		int c =0;
		sen.append(vocabulary.get(xt_2));
		while(xt !=152 && c++< 100){
			xt = getRandomFromDist(trigram.get(""+xt_2+"_"+xt_1));
			//System.out.println("tri "+xt+" "+vocabulary.get(xt));
			if(xt!=-1){
				sen.append(vocabulary.get(xt)+" ");				
			}
			else{
				xt = getRandomFromDist(bigram.get(xt_1));
			//	System.out.println("bi "+xt+" "+vocabulary.get(xt));
				if(xt!=-1){
					sen.append(vocabulary.get(xt)+" ");
				}
				else{
					xt = getRandomFromDist(unigram);
				//	System.out.println("uni "+xt+" "+vocabulary.get(xt));
					sen.append(vocabulary.get(xt)+" ");
				}
			}
			xt_2 = xt_1;
			xt_1 = xt;
		}
		return sen.toString();
	}

	public static void main(String[] args) throws IOException {
		String dir = args[0];
		String voc_file = dir+"vocab.txt";
		String uni_file = dir+"unigram_counts.txt";
		String bi_file = dir+"bigram_counts.txt";
		String tri_file = dir+"trigram_counts.txt";
		
		Problem1 pb1= new Problem1();
		pb1.loadVocabulary(voc_file);
		pb1.loadUnigram(uni_file);
		pb1.loadBigram(bi_file);
		pb1.loadTrigram(tri_file);
		System.out.println("Generating "+args[1]+" sentences of length at most 100 words\n");
		for(int i=0;i<Integer.parseInt(args[1]);i++){
			System.out.println(pb1.generateSentence());
		}				
	}

}

package assignment1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Problem2 {
	final Double MIN = -Double.MAX_VALUE;
	private HashMap<Integer, String> vocabulary;
	//key string is formed as ""+x_(t-n)+"#"+x_(t-n+1) ....+"#"+x_(t)
	private HashMap<String, Double> ngram = new HashMap<>(400000);
	private void loadVocabulary(String voc_file) throws IOException {
		long st = System.currentTimeMillis();
		vocabulary = new HashMap<>();
		Scanner sc = new Scanner(new File(voc_file));
		while (sc.hasNext()) {
			int id = sc.nextInt();
			String token = sc.next();
			vocabulary.put(id, token);
		}
		sc.close();
		System.out.println("Vocabulary loaded in " + (double) (System.currentTimeMillis() - st) / 1000 + " secs");
	}

	private void loadBigram(String file) throws IOException {
		long st = System.currentTimeMillis();
		Scanner sc = new Scanner(new File(file));
		while (sc.hasNext()) {
			int xt_1 = sc.nextInt(); // x_(t-1)
			int xt = sc.nextInt(); // x
			String key = "" + xt_1 + "#" + xt;
			ngram.put(key, sc.nextDouble());
		}
		sc.close();
		System.out.println("Bigram loaded in " + (double) (System.currentTimeMillis() - st) / 1000 + " secs");
	}

	public int editDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();		
		int[][] mat = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			mat[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			mat[0][j] = j;
		}		
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);				
				if (c1 == c2) {					
					mat[i + 1][j + 1] = mat[i][j];
				} else {
					int replace = mat[i][j] + 1;
					int insert = mat[i][j + 1] + 1;
					int delete = mat[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					mat[i + 1][j + 1] = min;
				}
			}
		}
		return mat[len1][len2];
	}

	public int factorial(int n) {
		int fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	private double emissionProb(String word1, String word2) {
		int k = editDistance(word1, word2);
		double log_lambda = -2;
		return k * log_lambda - Math.log10(factorial(k));
	}

	private double transProb(int i, int j) {
		if (!ngram.containsKey("" + i + "#" + j))
			return MIN;
		return ngram.get("" + i + "#" + j);
	}

	private String viterbi(String seq) {
		String toks[] = seq.split(" ");
		int n = vocabulary.size();
		
		//notations are consistent with those used in class
		double V[][] = new double[toks.length + 1][n + 1];
		int B[][] = new int[toks.length + 1][n + 1];

		//initialization 
		for (int i = 1; i <= n; i++) {
			V[0][i] = transProb(153, i) + emissionProb(vocabulary.get(i), toks[0]);
			B[0][i] = 153;
		}		
		
		//viterbi  
		for (int t = 1; t < toks.length; t++) {
			for (int i = 1; i <= n; i++) {
				V[t][i] = MIN;
				for (int j = 1; j <= n; j++) {
					double tmp = V[t - 1][j] + transProb(j, i);
					if (tmp > V[t][i]) {
						V[t][i] = tmp;
						B[t][i] = j;
					}
				}
				//added outside loop to reduce calculation overhead
				V[t][i] += emissionProb(vocabulary.get(i), toks[t]); 
			}
		}

		//calculate the last backpointer
		int last = 0;
		double max = MIN;
		for (int j = 1; j <= n; j++) {
			if (max < V[toks.length - 1][j]) {
				max = V[toks.length - 1][j];
				last = j;
			}
		}

		//trace most probable sequence using backpointers
		String result = "";
		result += vocabulary.get(last) + " ";
		for (int t = toks.length - 1; t > 0; t--) {			
			last = B[t][last];
			result = vocabulary.get(last) + " " + result;
		}

		return result;
	}

	public static void main(String args[]) throws IOException {
		String dir = args[0];
		String voc_file = dir + "vocab.txt";
		String bi_file = dir + "bigram_counts.txt";

		Problem2 pb2 = new Problem2();
		pb2.loadVocabulary(voc_file);
		pb2.loadBigram(bi_file);

		for (int i = 1; i < args.length; i++) {
			long st = System.currentTimeMillis();
			System.out.println(args[i] + "   :    " + pb2.viterbi(args[i]));
			System.out.println("Time taken: " + (System.currentTimeMillis() - st) / 1000 + " secs");
		}
	}

}

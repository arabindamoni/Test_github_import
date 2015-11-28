package assignment2;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

class FeatureGenerator {

  private FeatureGenerator() {}

  public static void generate(String model, String fileToTag, String outfile) throws Exception {
   
    MaxentTagger tagger = new MaxentTagger(model);       
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outfile), "utf-8"));
    
    BufferedReader br = new BufferedReader(new FileReader(fileToTag));
    String line = "";
    ArrayList<String> toks = new ArrayList<>();
    ArrayList<String> vals = new ArrayList<>();
    while((line = br.readLine())!=null){
    	if(line.length()==0){
    		String params[] = new String[toks.size()];
    		toks.toArray(params);
    		List<HasWord> sent = Sentence.toWordList(params);
    		List<TaggedWord> taggedSent = tagger.tagSentence(sent);
    		int i=0;
    	    for (TaggedWord tw : taggedSent) {    	     
    	        pw.println(tw.word()+" "+tw.tag()+" "+vals.get(i));
    	        i++;
    	    }    
    	    pw.println();
    	    toks = new ArrayList<>();
    	    vals = new ArrayList<>();
    	}    	
    	else{
    		String tok[]= line.split(" ");
    		toks.add(tok[0]);
    		vals.add(tok[1]);
    	}
    }
    br.close();    
    pw.close();
  }

}

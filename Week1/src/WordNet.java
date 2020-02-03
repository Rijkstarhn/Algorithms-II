import java.io.File;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class WordNet {
	
	private final int root;
	private SeparateChainingHashST<String, Integer> synsets;
	private Digraph hypernyms;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		this.synsets = new SeparateChainingHashST<String, Integer>();
		File synsetsFile = new File(synsets);
		In synsetsIn = new In(synsetsFile);
		int countOfSyn = 0;
		while(!synsetsIn.isEmpty()) {
			String[] line = synsetsIn.readLine().split(",");
			String[] words = line[1].split(" ");
			for (int i = 0; i < words.length; i++) {
				this.synsets.put(words[i], Integer.parseInt(line[0]));
			}
			countOfSyn ++;
		}
		File hypernymsFile = new File(hypernyms);
		In hypernymsIn = new In(hypernymsFile);
		this.hypernyms = new Digraph(countOfSyn);
		int num = 0;
		int numOfRoot = 0;
		while(!hypernymsIn.isEmpty()) {
			String[] line = hypernymsIn.readLine().split(",");
			if (line.length == 1) numOfRoot = num;
			for (int i = 1; i < line.length; i ++) {
				this.hypernyms.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
			}
			num ++;
		}
		root = numOfRoot;
	}
	
	// returns all WordNet nouns
	public Iterable<String> nouns() {
		ArrayList<String> nouns = new ArrayList<String>();
		for(String key : synsets.keys()) {
			nouns.add(key);
		}
		return nouns;
	}
	
	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return synsets.get(word) != null;
	}
	
	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		return 0;
	}
	
	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		return null;
	}
	
	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet(args[0], args[1]);
		System.out.println("1");
//		System.out.println(wn.nouns());
		System.out.println(wn.isNoun("401-k_plan"));
	}
}

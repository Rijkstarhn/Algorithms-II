import java.io.File;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class WordNet {
	
//	private final int root;
	private SeparateChainingHashST<String, ArrayList<Integer>> synsets;
	private Digraph hypernyms;
	private ArrayList<String> words;// for the convinience of finding the word when knowing the id in sap()
	private int distance;
	private String ancestor;
	private SAP sap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
		this.words = new ArrayList<String>();
		this.synsets = new SeparateChainingHashST<String, ArrayList<Integer>>();
		File synsetsFile = new File(synsets);
		In synsetsIn = new In(synsetsFile);
		int countOfSyn = 0;
		while(!synsetsIn.isEmpty()) {
			String[] line = synsetsIn.readLine().split(",");
			String[] words = line[1].split(" ");
			this.words.add(line[1]);
			for (int i = 0; i < words.length; i++) {
				if (!this.synsets.contains(words[i])) {
					 this.synsets.put(words[i], new ArrayList<Integer>());
				}
				ArrayList<Integer> idPool = this.synsets.get(words[i]);
				idPool.add(Integer.parseInt(line[0]));
				this.synsets.put(words[i], idPool);
			}
			countOfSyn ++;
		}
		File hypernymsFile = new File(hypernyms);
		In hypernymsIn = new In(hypernymsFile);
		this.hypernyms = new Digraph(countOfSyn);
		while(!hypernymsIn.isEmpty()) {
			String[] line = hypernymsIn.readLine().split(",");
			for (int i = 1; i < line.length; i ++) {
				this.hypernyms.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
			}
		}
		sap = new SAP(this.hypernyms);
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
		if (word == null) throw new IllegalArgumentException();
		return synsets.get(word) != null;
	}
	
	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) throw new IllegalArgumentException();
		if (this.synsets.get(nounA) == null || this.synsets.get(nounB) == null) throw new IllegalArgumentException("Not an exsit pair");
		calculate(nounA, nounB);
		return this.distance;
	}
	
	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) throw new IllegalArgumentException();
		if (this.synsets.get(nounA) == null || this.synsets.get(nounB) == null) throw new IllegalArgumentException("Not an exsit pair");
		calculate(nounA, nounB);
		return this.ancestor;
	}
	
	private void calculate(String nounA, String nounB) {
		if (nounA == null || nounB == null) throw new IllegalArgumentException();
		ArrayList<Integer> wordsA = synsets.get(nounA);
		ArrayList<Integer> wordsB = synsets.get(nounB);
		int ancestorID;
		distance = sap.length(wordsA, wordsB);
		ancestorID = sap.ancestor(wordsA, wordsB);
		ancestor = this.words.get(ancestorID);
	}
	
	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet(args[0], args[1]);
		System.out.println("1");
//		System.out.println(wn.nouns());
		System.out.println(wn.isNoun("fuck"));
		int count = 0;
//		for(String xString : wn.nouns()) {
//			count++;
//		}
		System.out.println(count);
		System.out.println(wn.synsets.get("word"));
		System.out.println(wn.distance("401-K", "money"));
	}
}

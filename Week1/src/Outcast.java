import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	
	private WordNet wn;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		wn = wordnet;
	}
	
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int distanceMax = 0;
		int distance;
		String outcast = null;
		for (int i = 0; i < nouns.length; i ++) {
			distance = 0;
			for (int j = 0; j < nouns.length; j ++) {
				if (i == j) continue;
				distance += wn.distance(nouns[i], nouns[j]);
			}
			if (distance > distanceMax) {
				distanceMax = distance;
				outcast = nouns[i];
			}
		}
		return outcast;
	}
	
	// see test client below
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
}

package StringSearch;

import edu.princeton.cs.algs4.Queue;

public class TriesST<Value> {
    
    private static int R = 256;
    private Node root = new Node();
    
    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }
    
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }
    
    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.value = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }
    
    public boolean contains(String key) {
        return get(key) != null;
    }
    
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        else return (Value)x.value; 
    }
    
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }
    
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, "", queue);
        return queue;
    }
    
    private void collect(Node x, String prefix, Queue<String> queue) {
        if (x == null) return;
        if (x.value != null) queue.enqueue(prefix);
        for (char c = 0; c < R; c++) {
            collect(x.next[c], prefix+c, queue);
        }
    }
    
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, prefix, queue);
        return queue;
    }
    
    public String longestPrefixOf(String query) {
        int length = search(root, query, 0, 0);
        return query.substring(0, length);
    }
    
    private int search(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.value != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return search(x.next[c], query, d+1, length);
    }
    
    public static void main(String[] args) {
        String[] input = {"sea", "shell", "sells", "the", "shore"};
        TriesST<Integer> tst = new TriesST<Integer>();
        int i = 0;
        for (String s : input) {
            tst.put(s, i);
            i++;
        }
        System.out.println(tst.contains("sells"));
        System.out.println(tst.get("sea"));
        for (String string : tst.keys()) {
            System.out.println(string);
        }
        for (String string : tst.keysWithPrefix("sh")) {
            System.out.println(string);
        }
        System.out.println(tst.longestPrefixOf("sellsman"));
    }
}

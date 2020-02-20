package StringSearch;

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
    }
}

public class CircularSuffixArray {
    
    private String s;
    private CircularSuffix[] circularSuffixArray;
    
    private class CircularSuffix {
        private String original;
        private int pointer;
        public CircularSuffix(int i) {
            original = s;
            pointer = i;
        }
    }
    
    private class QuickThree {
        
        public void sort(CircularSuffix[] a) {
//            StdRandom.shuffle(a);
            sort(a, 0, a.length-1, 0);
        }
        
        // 3-way string quicksort a[lo..hi] starting at dth character
        private void sort(CircularSuffix[] a, int lo, int hi, int d) { 

            // cutoff to insertion sort for small subarrays
            if (hi <= lo) return;
            int lt = lo, gt = hi;
            int v = charAt(a[lo], d);
            int i = lo + 1;
            while (i <= gt) {
                int t = charAt(a[i], d);
                if (t < v) exch(a, lt++, i++);
                else if (t > v) exch(a, i, gt--);
                else i++;
            }

            // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
            sort(a, lo, lt-1, d);
            if (v >= 0) sort(a, lt, gt, d+1);
            sort(a, gt+1, hi, d);
        }
        
        // exchange a[i] and a[j]
        private void exch(CircularSuffix[] a, int i, int j) {
            CircularSuffix temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        
        // return the dth character of s, -1 if d = length of s
        private int charAt(CircularSuffix cs, int d) { 
//            assert d >= 0 && d <= s.length();
            int length = cs.original.length();
            if (d == length) return -1;
            return cs.original.charAt((d+cs.pointer)%length);
        }
    }
//    private class ManberMayer {
//        
//        public void manberMayer(String[] a) {
//            // Phase 0: Key-indexed Sort
//            a = keyIndexedSort(a);
//            // Phase i: Index Sort
//            
//        }
//        
//        private String[] keyIndexedSort(String[] a) {
//            int n = a.length;
//            int R = 256;   // extend ASCII alphabet size
//            String[] aux = new String[n];
////            int[] index = new int[n];
//            for (int i = 0; i < n; i++) {
//                index[i] = i;
//            }
//            // compute frequency counts
//            int[] count = new int[R+1];
//            for (int i = 0; i < n; i++)
//                count[a[i].charAt(0) + 1]++;
//            // compute cumulates
//            for (int r = 0; r < R; r++)
//                count[r+1] += count[r];
//            // move data
//            for (int i = 0; i < n; i++) {
//                index[count[a[i].charAt(0)]] = i;
//                aux[count[a[i].charAt(0)]++] = a[i];
//            }
//            return aux;
//        }
//    }
    
    
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Input should not be null!");
        this.s = s;
        circularSuffixArray = new CircularSuffix[s.length()];
        // create the suffix arrays
        for (int i = 0; i < s.length(); i++) {
            circularSuffixArray[i] = new CircularSuffix(i);
        }
        // sort the array
//        ManberMayer mm = new ManberMayer();
//        mm.manberMayer(suffixArray);// How about a Manber-Mayer Sort?
        QuickThree qt = new QuickThree();
        qt.sort(circularSuffixArray);
    }
    
    private String circularSuffix(String s, int index) {
        StringBuilder sb = new StringBuilder();
        String begin = s.substring(index);
        String end = s.substring(0, index);
        sb.append(begin);
        sb.append(end);
        return sb.toString();
    }
    
    // length of s
    public int length() {
        return s.length();
    }
    
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > s.length()-1) throw new IllegalArgumentException("Illegeal input!");
        return circularSuffixArray[i].pointer;
    }
 
    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        System.out.println(csa.index(6));
        System.out.println(csa.length());
    }
}

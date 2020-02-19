
public class MSD {
    public static void sort(String[] a) {
        String aux[] = new String[a.length];
        sort(a, aux, 0, a.length-1, 0);
    }
    
    private static void sort(String[] a, String[] aux, int lo, int hi, int d) {
        if (hi < lo) return;
        int R = 256;
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            count[charAt(a[i], d)+2]++;
        }
        for (int r = 1; r < count.length; r++) {
            count[r] += count[r-1];
        }
        for (int i = lo; i <= hi; i++) {
            aux[count[charAt(a[i], d)+1]++] = a[i];// R+2 to avoid charAt() returns -1
        }
        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i-lo];// i-lo instead of i
        }
        for (int r = 97; r < R; r++) {
            sort(a, aux, lo + count[r], lo + count[r+1]-1, d+1);
        }
    }
    
    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }
    
    public static void main(String[] args) {
        String[] a = {"she","sells","seashells","by","the","sea","shore","the","shells","she","sells",
                "are","surely","seashells"};
        String[] aux = new String[a.length];
        sort(a, aux, 0, a.length-1, 0);
        System.out.println(aux);
    }
}

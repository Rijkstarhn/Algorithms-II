
public class KeyIndexed {
    
    public KeyIndexed() {
        // TODO Auto-generated constructor stub
        char[] ao = {'d','a','c','f','f','b','d','b','f','b','e','a'};
        int[] a = new int[ao.length];
        int R = 0;
        for (int i = 0; i < ao.length; i++) {
            a[i] = ao[i] - 'a';
            R = Math.max(R, a[i]);
        }
        R ++;
        int N = a.length;
        int[] count = new int[R+1];
        
        for (int i = 0; i < N; i++) {
            count[a[i]+1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r+1] += count[r];
        }
        int[] aux = new int[N];
        for (int i = 0; i < N; i++) {
            aux[count[a[i]]++] = a[i];
        }
        
        for (int i = 0; i < N; i++) {
            a[i] = aux[i] + 'a';
        }
        char[] aoux = new char[N];
        for (int i = 0; i < N; i++) {
            aoux[i] = (char)a[i];
            System.out.println(aoux[i]);
        }
    }
    
    public static void main(String[] args) {
        KeyIndexed k = new KeyIndexed();
    }
}

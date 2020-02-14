import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
 
    private static final double BORDER = 1000.0;
    private Picture pic;
    private int[][] RGB;
    private int width, height;
    // record the last calculation direction, can save many transposes
    // false is vertical, true is horizontal
    private boolean horizontal;
    //record the state that the removeHorizontal() is called, for knowing the purpose of calling energy(x, y)
    private boolean verticalFindCalled;
    private boolean horizontalFindCalled;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Input shouldn't be null");
        pic = picture;
        width = picture.width();
        height = picture.height();
        horizontal = false;
        verticalFindCalled = false;
        horizontalFindCalled = false;
        RGB = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                RGB[i][j] = picture.getRGB(j, i);// pixel (x, y) refers to the pixel in column x and row y,
            }
        }
    }
 
    // current picture
    public Picture picture() {
        if (horizontal) {
            transpose();
            horizontal = false;
        }
        Picture p = new Picture(width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                p.setRGB(col, row, RGB[row][col]);
            }
        }
        pic = p;
        return pic;
    }
    
    // width of current picture
    public int width() {
        if (horizontal) {
            transpose();
            horizontal = false;
        }
        return width;
    }
    
    // height of current picture
    public int height() {
        if (horizontal) {
            transpose();
            horizontal = false;
        }
        return height;
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!verticalFindCalled && !horizontalFindCalled) {
            if (horizontal) {
                transpose();
                horizontal = false;
            }
        }
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException("position out of range");
        if (x == 0 || x == width-1 || y == 0 || y == height-1) return BORDER;
        double Rx, Bx, Gx, Ry, By, Gy;
        int left = RGB[y][x-1], right = RGB[y][x+1], Up = RGB[y+1][x], Down = RGB[y-1][x];
        Rx = getRed(right) - getRed(left); Bx = getBlue(right) - getBlue(left); Gx = getGreen(right) - getGreen(left);
        Ry = getRed(Up) - getRed(Down); By = getBlue(Up) - getBlue(Down); Gy = getGreen(Up) - getGreen(Down);
        double deltaXSquare = Math.pow(Rx, 2) + Math.pow(Bx, 2) + Math.pow(Gx, 2);
        double deltaYSquare = Math.pow(Ry, 2) + Math.pow(By, 2) + Math.pow(Gy, 2);
        return Math.sqrt(deltaXSquare + deltaYSquare);
    }
    
    private int getRed(int RGB) {
        return (RGB >> 16) & 0xFF;
    }
    
    private int getGreen(int RGB) {
        return (RGB >> 8) & 0xFF;
    }
    
    private int getBlue(int RGB) {
        return (RGB >> 0) & 0xFF;
    }
    
    // Calculate the energy of every pixels in the picture
    private double[][] energy() {
        double[][] energy = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++)
                energy[row][col] = energy(col, row);
        }
        return energy;
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        verticalFindCalled = true;
        if (horizontal) {
            transpose();
            horizontal = false;
        }
        int[] seam = findSeam();
        verticalFindCalled = false;
        return seam;
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        horizontalFindCalled = true;
        if(!horizontal) {
            transpose();
            horizontal = true;
        }
        int[] seam = findSeam();
        horizontalFindCalled = false;
        return seam;
    }
    
    // the find seam method
    private int[] findSeam() {
        int[] minSeam = new int[height];
        double[][] energy = energy();
        double[] distTo = new double[height*width];
        int[] edgeTo = new int[height*width];
        for (int j = 0; j < width; j++) {
            distTo[j] = BORDER;
        }
        for (int j = width; j < height * width; j++) {
            distTo[j] = Double.POSITIVE_INFINITY;
        }
        for (int j = 0; j < height * width; j++) {
            edgeTo[j] = -1;
        }
        // topological relax
        for (int row = 1; row < height; row++) {
            for (int col = 0; col < width; col++) {
                for (int preCol = col - 1; preCol <= col + 1; preCol++) {
                    if (preCol < 0) preCol = 0;
                    if (preCol >= width) break;
                    if (distTo[pos(row, col)] > distTo[pos(row-1, preCol)] + energy[row][col]) {
                        distTo[pos(row, col)] = distTo[pos(row-1, preCol)] + energy[row][col];
                        edgeTo[pos(row, col)] = pos(row-1, preCol);
                    }
                }
            }
        }
        // get the minSeam
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width; col++) {
            if (distTo[pos(height-1, col)] < minEnergy) {
                minEnergy = distTo[pos(height-1, col)];
                minSeam = pathTo(pos(height-1, col), edgeTo);
            }
            
        }
        return minSeam;
    }
    
    private int pos(int i, int j) {
        return width * i + j;
    }
    
    private int[] pathTo(int pos, int[] edgeTo) {
        int[] minSeam = new int[height];
        int seamVertex = pos;
        for (int i = height-1; i >= 0; i--) {
            minSeam[i] = seamVertex;
            seamVertex = edgeTo[seamVertex];
        }
        // mod to get the col of each row
        for (int i = 0; i < minSeam.length; i++) {
            minSeam[i] = minSeam[i] % width; 
        }
        return minSeam;
    }
    
    private void transpose() {
        int[][] trans = new int[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                trans[col][row] = RGB[row][col];
            }
        }
        RGB = trans;
        height = RGB.length;
        width = RGB[0].length;
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Input shouldn't be null");
        if (horizontal) {
            transpose();
            horizontal = false;
        }
        if (seam.length != height) throw new IllegalArgumentException("Seam's length is incorrect");
        if (invalidSeam(seam)) throw new IllegalArgumentException("Invalid seam");
        removeSeam(seam);
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Input shouldn't be null");
        if (!horizontal) {
            transpose();
            horizontal = true;
        }
        if (seam.length != height) throw new IllegalArgumentException("Seam's length is incorrect");
        if (invalidSeam(seam)) throw new IllegalArgumentException("Invalid seam");
        removeSeam(seam);
    }
    
    // remove seam operation
    private void removeSeam(int[] seam) {
        int[][] removeRGB = new int[height][width-1];
        for(int i = 0; i < seam.length; i++) {
             System.arraycopy(RGB[i], 0, removeRGB[i], 0, seam[i]);
             System.arraycopy(RGB[i], seam[i]+1, removeRGB[i], seam[i], removeRGB[0].length-seam[i]);
        }
        RGB = removeRGB;
        height = RGB.length;
        width = RGB[0].length;
        if (height <= 0 || width <=0) throw new IllegalArgumentException(" ");
    }
    
    // vertical is false, horizontal is true
    private boolean invalidSeam(int[] seam) {
        if (seam[0] < 0 || seam[0] >= width) return true;
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) return true;
            if (Math.abs(seam[i-1] - seam[i]) > 1) return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        double[][] energy = sc.energy();
        sc.height();
        sc.height();
        sc.energy();
        System.out.println(sc.energy(1, 2));
        int[] seam;
//        int[] seam = {-1, 0, 0, 1, 1, 2, 3, 4, 5, 5, 6, 6};
//        int[] seam = {2,3,2,3,2};
//        int[] seam = {1,2,1,2,1,0};
//        seam = sc.findVerticalSeam();
        seam = sc.findHorizontalSeam();
        sc.width();
        sc.findHorizontalSeam();
//        sc.removeVerticalSeam(seam);
//        seam = sc.findHorizontalSeam();
//        sc.removeHorizontalSeam(seam);
//        Picture p = sc.picture();
//        sc.removeVerticalSeam(seam);
        System.out.println(sc.energy(1, 2));
//        sc.transpose();
        
//        System.out.println(sc.findHorizontalSeam());
//        ArrayList<Double>[] t = new ArrayList[2];
//        for (int i = 0; i < t.length; i++) {
//            t[i] = new ArrayList<Double>();
//        }
//        t[0].add(1.0);t[0].add(2.0);t[0].add(3.0);
//        t[1].add(4.0);t[1].add(5.0);t[1].add(6.0);
//        ArrayList<Double>[] tt = sc.transpose(t);
        
        
//        int[] seam = (sc.findHorizontalSeam());
//        sc.removeHorizontalSeam(seam);
//        sc.energy();
//        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        System.out.println();
    }
}

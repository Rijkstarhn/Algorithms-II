import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
 
    private Picture pic;
    private Color[][] color;
    private int width, height;
//    private double[][] energy;
//    private ArrayList<Double>[] energy;
//    private Queue<int[]> seam;// store the trace of the removal seams
//    private Queue<Boolean> vh;// store the trace of the removal direction; vertical is false 
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
//        pic = picture;
        width = picture.width();
        height = picture.height();
        color = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color[i][j] = picture.get(j, i);// pixel (x, y) refers to the pixel in column x and row y,
            }
        }
//        energy = new double[height][width];
//        energy = new ArrayList[height];
//        for (int i = 0; i < energy.length; i++) {
//            energy[i] = new ArrayList<Double>();
//        }
        
    }
 
    // current picture
    public Picture picture() {
        Picture p = new Picture(width, height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                p.set(col, row, color[row][col]);
            }
        }
        pic = p;
        return pic;
    }
    
//    private void trimPic() {
//        while(!vh.isEmpty()) {
//            
//        }
//    }
    
    // width of current picture
    public int width() {
        return width;
    }
    
    // height of current picture
    public int height() {
        return height;
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException();
        if (x == 0 || x == width-1 || y == 0 || y == height-1) return 1000.0;
        double Rx, Bx, Gx, Ry, By, Gy;
        Color left = color[y][x-1], right = color[y][x+1], Up = color[y+1][x], Down = color[y-1][x];
        Rx = right.getRed() - left.getRed(); Bx = right.getBlue() - left.getBlue(); Gx = right.getGreen() - left.getGreen();
        Ry = Up.getRed() - Down.getRed(); By = Up.getBlue() - Down.getBlue(); Gy = Up.getGreen() - Down.getGreen();
        double deltaXSquare = Math.pow(Rx, 2) + Math.pow(Bx, 2) + Math.pow(Gx, 2);
        double deltaYSquare = Math.pow(Ry, 2) + Math.pow(By, 2) + Math.pow(Gy, 2);
        return Math.sqrt(deltaXSquare + deltaYSquare);
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
        int[] minSeam = new int[height];
        int[] seam = new int[height];
        double minEnergy = Double.POSITIVE_INFINITY;
        double[][] energy = energy();
        for (int i = 0; i < width; i ++) {
            double[] distTo = new double[height];
            int[][] edgeTo = new int[height][2];
            int[] pos = {0, i};// record the position of the vertex, pos[0] is row and pos[1] is col
            double sumEnergy = 0.0;
            while (pos[0] < height - 1) {
                double min = min(energy, pos);
                seam[pos[0]] = pos[1];
                sumEnergy += min;
            }
            sumEnergy += 1000.0;// add the energy of the last line
            seam[0] = seam[1]; seam[height-1] = seam[height-2];
            if (sumEnergy < minEnergy) {
                minEnergy = sumEnergy;
                minSeam = Arrays.copyOf(seam, seam.length);// don't use shallow copy!!
            }
        }
//        for (int col = 0; col < width - 1; col++) {
//            if (energy[1][col] < energy[1][vSeam[1]]) vSeam[1] = col;
////            if (energy[1].get(col) < energy[1].get(vSeam[1])) vSeam[1] = col;
//        }
//        for (int row = 2; row < height; row++) {
//            for (int col = vSeam[row-1] - 1; col <= vSeam[row-1] + 1; col++)
//                if (energy[row][col] < energy[row][vSeam[row]]) vSeam[row] = col;
////            if (energy[row].get(col) < energy[row].get(vSeam[row])) vSeam[row] = col;
//        }
//        vSeam[0] = vSeam[1]; vSeam[height-1] = vSeam[height-2];
        return minSeam;
        
    }
    
    // get the least energy in the next level
    private double min(double[][] energy, int[] pos) {
        pos[0] += 1;
        int row = pos[0];//get to the next level
        int col = pos[1];
        double minEnergy = 1000.0;
//        row += 1; 
        for (int i = col - 1; i <= col + 1; i++) {
            if (i < 0 || i >= width) continue;
            if (energy[row][i] < minEnergy) {
                minEnergy = energy[row][i];
                pos[1] = i;
            }
        }
        return minEnergy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }
//    public int[] findHorizontalSeam() {
////        int[] hSeam = new int[width];
////        for (int row = 0; row < height - 1; row++) {
////            if (energy[row][1] < energy[hSeam[1]][1]) hSeam[1] = row;
////        }
////        for (int col = 2; col < width; col++) {
////            for (int row = hSeam[col-1] - 1; row <= hSeam[col-1] + 1; row++)
////                if (energy[row][col] < energy[row][hSeam[col]]) hSeam[col] = row;
////        }
//        transpose(energy);
//        int[] hSeam = findVerticalSeam();
//        hSeam[0] = hSeam[1]; hSeam[hSeam.length-1] = hSeam[hSeam.length-2];
//        transpose(energy);
//        return hSeam;
//    }
    
    private void transpose() {
        Color[][] trans = new Color[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                trans[col][row] = color[row][col];
            }
        }
        color = trans;
        height = color.length;
        width = color[0].length;
//        height = energy[0].size();
//        width = energy.length;
//        energy = new ArrayList[height];
//        for (int i = 0; i < energy.length; i++) {
//            energy[i] = new ArrayList<Double>();
//        }
//        for (int i = 0; i < height; i ++) {
//            for (int j = 0; j < width; j ++) {
//                energy[i].add(elderEnergy[j].get(i));
//            }
//        }
    }
 
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height) throw new IllegalArgumentException();
        if (invalidSeam(seam, false)) throw new IllegalArgumentException();
        if (width <= 1) throw new IllegalArgumentException();
        Color[][] removeColor = new Color[height][width-1];
        for(int i = 0; i < seam.length; i++) {
             System.arraycopy(color[i], 0, removeColor[i], 0, seam[i]);
             System.arraycopy(color[i], seam[i]+1, removeColor[i], seam[i], removeColor[0].length-seam[i]);
        }
        color = removeColor;
        width -= 1;
//        for (int i = 0; i < seam.length; i++) {
//            energy[i].remove(seam[i]);
//        }
//        width -= 1;
//        this.seam.enqueue(seam);
//        vh.enqueue(false);
    }
//    
//    private void removeSeam(double[] energy, int i) {
//        System.arraycopy()
//    }
//    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width) throw new IllegalArgumentException();
        if (invalidSeam(seam, true)) throw new IllegalArgumentException();
        if (height <= 1) throw new IllegalArgumentException();
        transpose();
        removeVerticalSeam(seam);
        transpose();
//        transpose(energy);
//        removeVerticalSeam(seam);
//        transpose(energy);
//        this.seam.enqueue(seam);
//        vh.enqueue(true);
    }
    
    // vertical is false, horizontal is true
    private boolean invalidSeam(int[] seam, boolean vh) {
        if (vh) {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] >= height) return true;
                if (i < seam.length-1 && Math.abs(seam[i+1] - seam[i]) > 1) return true;
            }
        }
        else {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] >= width) return true;
                if (i < seam.length-1 && Math.abs(seam[i+1] - seam[i]) > 1) return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        double[][] energy = sc.energy();
        int[] seam = (sc.findVerticalSeam());
//        sc.transpose();
        
//        System.out.println(sc.findHorizontalSeam());
//        ArrayList<Double>[] t = new ArrayList[2];
//        for (int i = 0; i < t.length; i++) {
//            t[i] = new ArrayList<Double>();
//        }
//        t[0].add(1.0);t[0].add(2.0);t[0].add(3.0);
//        t[1].add(4.0);t[1].add(5.0);t[1].add(6.0);
//        ArrayList<Double>[] tt = sc.transpose(t);
        
//        sc.removeVerticalSeam(seam);
//        int[] seam = (sc.findHorizontalSeam());
//        sc.removeHorizontalSeam(seam);
//        sc.energy();
//        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        System.out.println();
    }
}

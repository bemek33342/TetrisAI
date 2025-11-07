package atlet;

import java.awt.*;
import java.util.Random;

public class Piece {
    private int[][] matrix;
    public static final Color[] COLORS = {
        null,
        new Color(0xFF0D72),
        new Color(0x0DC2FF),
        new Color(0x0DFF72),
        new Color(0xF538FF),
        new Color(0xFF8E0D),
        new Color(0xFFE138),
        new Color(0x3877FF)
    };

    public Piece(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getMatrix() { return matrix; }

    public static Piece create(char type) {
        switch (type) {
            case 'T': return new Piece(new int[][]{{0,1,0},{1,1,1},{0,0,0}});
            case 'O': return new Piece(new int[][]{{2,2},{2,2}});
            case 'L': return new Piece(new int[][]{{0,0,3},{3,3,3},{0,0,0}});
            case 'J': return new Piece(new int[][]{{4,0,0},{4,4,4},{0,0,0}});
            case 'I': return new Piece(new int[][]{{0,5,0,0},{0,5,0,0},{0,5,0,0},{0,5,0,0}});
            case 'S': return new Piece(new int[][]{{0,6,6},{6,6,0},{0,0,0}});
            case 'Z': return new Piece(new int[][]{{7,7,0},{0,7,7},{0,0,0}});
        }
        return null;
    }

    public static Piece randomPiece() {
        String pieces = "TJLOSZI";
        char c = pieces.charAt(new Random().nextInt(pieces.length()));
        return create(c);
    }

    public static int[][] rotate(int[][] m, int dir) {
        int size = m.length;
        int[][] res = new int[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (dir > 0) res[x][size - 1 - y] = m[y][x];
                else res[size - 1 - x][y] = m[y][x];
            }
        }
        return res;
    }
}

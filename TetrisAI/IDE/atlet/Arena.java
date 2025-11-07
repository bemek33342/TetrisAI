package atlet;

import java.awt.*;

public class Arena {
    private int[][] grid;
    private int cols, rows;

    public Arena(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        grid = new int[rows][cols];
    }

    public int[][] getGrid() { return grid; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }

    public void merge(Piece piece, int px, int py) {
        int[][] shape = piece.getMatrix();
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    grid[py + y][px + x] = shape[y][x];
                }
            }
        }
    }

    public void sweep(Player player) {
        int rowCount = 1;
        for (int y = rows - 1; y >= 0; y--) {
            boolean full = true;
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                // usuń linię
                for (int yy = y; yy > 0; yy--) {
                    grid[yy] = grid[yy - 1].clone();
                }
                grid[0] = new int[cols];
                player.addScore(rowCount * 10);
                rowCount *= 2;
                y++; // sprawdź ponownie ten sam wiersz
            }
        }
    }

    public void draw(Graphics g, int tile) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] != 0) {
                    g.setColor(Piece.COLORS[grid[y][x]]);
                    g.fillRect(x * tile, y * tile, tile, tile);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * tile, y * tile, tile, tile);
                }
            }
        }
    }
}
